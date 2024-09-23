/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package funciones.monitor_archivo;

import control.C_automata;
import control.indicePestaña;
import funciones.LienzoFromScroll;
import funciones.archivo.Archivo;
import funciones.ctrlZ_Y.Control;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import javax.swing.JOptionPane;
import vista.V_lienzo;
import vista.V_tabs;

/**
 *
 * @author jcarl
 */
public class Vigilante implements Runnable {

    private File configDirectoryToWatch;
    private static Object cerrojo = new Object();
    private int modify = 0;
    private V_lienzo lienzo;
    private V_tabs tab;
    private Thread th;
    private int resp;
    WatchService watchService;

    public Vigilante(V_lienzo lienzo, V_tabs tab, File ruta) {
        this.lienzo = lienzo;
        this.tab = tab;
        this.configDirectoryToWatch = ruta;
        th = new Thread(this, "Hilo " + ruta.getName());
        th.start();
    }

    @Override
    public synchronized void run() {
        try {

            Path toWatch = Paths.get(configDirectoryToWatch.getAbsoluteFile().getParentFile().toString());
            watchService = toWatch.getFileSystem().newWatchService();
            toWatch.register(watchService, new WatchEvent.Kind[]{ENTRY_MODIFY, ENTRY_DELETE});

            WatchKey key = watchService.take();
            while (key != null && !lienzo.isMonitor()) {

                for (WatchEvent watchEvent : key.pollEvents()) {
                    Kind<?> kind = watchEvent.kind();
                    tipoEvento(kind.toString(), watchEvent.context().toString());
                }

                //reiniciar evento ocurrido
                key.reset();
                key = watchService.take();

                System.out.println("DeleteThread: " + lienzo.isMonitor());

            }

        } catch (IOException | InterruptedException ex) {
            System.out.println("Exception:" + ex.toString());
        }
    }

    public void tipoEvento(String kind, String context) {

        switch (kind) {
            case "ENTRY_MODIFY":
                synchronized (cerrojo) {
                    if (modify == 0) {
                        modify++;
                    } else if (modify == 1) {
                        if (context.equals(configDirectoryToWatch.getName())) {

                            if (lienzo.isGuardado() != true) {
                                //   System.out.println("Modificar: " + configDirectoryToWatch.getName());
                                //   System.out.println("Hay un cambio en la ruta del archivo: " + configDirectoryToWatch.getName());
                                reload();
                            } else {
                                // System.out.println("Se ha guardado cambios: " + configDirectoryToWatch.getName());
                                lienzo.setGuardado(false);
                            }
                        }
                        modify--;
                    }
                }
                break;

            case "ENTRY_DELETE":
                delete();
                break;
        }
    }

    public void reload() {

        int index = tab.getSelectedIndex();

        for (int i = 0; i < indicePestaña.getIndice(); i++) {

            if (configDirectoryToWatch.getName().equals(tab.getTitleAt(i)) && i != index) {
                tab.setSelectedIndex(i);
                resp = JOptionPane.showConfirmDialog(
                        null, "" + configDirectoryToWatch.toString() + "\n" + "Este archivo ha sido modificado" + "\n ¿Deseas recargarlo?",
                        "Recargar archivo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (JOptionPane.OK_OPTION == resp) {
                    Archivo.abrirArchivo(configDirectoryToWatch, tab, i, true);
                    MouseListener[] ml = lienzo.getMouseListeners();
                    C_automata cl = (C_automata) ml[0];
                    cl.setIdEstado(0);
                    cl = null;
                }
            }
        }
    }

    public void delete() {

        Control m = new Control();
        int total = 0;
        for (int i = 0; i < indicePestaña.getIndice(); i++) {

            if (configDirectoryToWatch.getName().equals(tab.getTitleAt(i))) {
                tab.setSelectedIndex(i);
                resp = JOptionPane.showConfirmDialog(
                        null, "" + configDirectoryToWatch.toString() + "\n" + "Este archivo ha sido eliminado" + "\n ¿Deseas conservar el archivo?",
                        "Conservar archivo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (JOptionPane.OK_OPTION == resp) {
                    lienzo.setRutaArchivo(configDirectoryToWatch.toString());
                }

                if (JOptionPane.NO_OPTION == resp) {
                    V_lienzo dibujo = new LienzoFromScroll().obtenerEn(i, tab);

                    //eliminar directorios y archivos de los tabs existentes
                    m.deleteRootFolder(dibujo.getName());
                    if (lienzo.getIdNombre() != 0) {
                        indicePestaña.getPesEliminada().add(lienzo.getIdNombre());
                        indicePestaña.setPesEliminada(indicePestaña.getPesEliminada());
                    }
                    lienzo.setAnalizar(false);
                    tab.remove(i);
                    indicePestaña.minIndice();
                    i--;
                    total--;
                }
                 total++;

            }
        }

        if (total <= 1) {
            lienzo.setMonitor(true);  //ojo  
            System.out.println("Murio");
        }

    }

}
