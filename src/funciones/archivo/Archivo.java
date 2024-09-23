/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package funciones.archivo;

import control.C_automata;
import control.indicePestaña;
import funciones.LienzoFromScroll;
import funciones.ctrlZ_Y.Control;
import funciones.monitor_archivo.Vigilante;
import java.awt.Graphics2D;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import modelo.M_estado;
import modelo.M_imagen;
import modelo.M_transicion;
import vista.V_lienzo;
import vista.V_tabs;
import static funciones.NmComponentes.*;

/**
 *
 * @author herma
 */
public class Archivo {

    private static File archivo = null;
    private static JFileChooser seleccionar = new JFileChooser();
    private static int idPestaña;
    private static V_lienzo save;

    private static final ArrayList<String> guardar = new ArrayList<>();
    private static List<M_transicion> transiciones;
    private static List<M_estado> estados;

    public static final void guardarJLEFO(boolean activo, V_tabs tabs) {

        idPestaña = tabs.getSelectedIndex();
        save = new LienzoFromScroll().obtener(tabs);
        MouseListener[] ml = save.getMouseListeners();
        C_automata ctrl = (C_automata) ml[0];
        //Si el archivo esta guardado: guardar cambios solamente
        if (activo == true) {
            System.out.println("Archivo: " + save.getRutaArchivo());
            grabarArchivo(save.getRutaArchivo(), saveAutomata(save), save); //Obtener la ruta  y mandar el arraylist
            ctrl.setCambios(false);
            save.setGuardado(true);
        }
        //Si es un archivo nuevo: Se hace un Guardar Como, Para guardar  and Guardar Como
        if (activo == false) {
            ///Obner nombre de la pestaña para colocarlo en: Nombre de Mod_archivo

            seleccionar = new JFileChooser();
            seleccionar.addChoosableFileFilter(new FileNameExtensionFilter("JLEFO (*.jlefo)", "jlefo"));
            seleccionar.setAcceptAllFileFilterUsed(false);
            carpetaJLEFO();
            seleccionar.setSelectedFile(new File(tabs.getTitleAt(idPestaña)));

            if (seleccionar.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                archivo = seleccionar.getSelectedFile();

                //Comprobando Extensión .jlefo
                if (archivo.toString().endsWith(".jlefo") == false) {
                    archivo = new File(archivo + ".jlefo");
                }

                //Validar si el archivo existe
                if (archivo.exists() == true) {
                    //Validando cuando le da en la X de la pestaña
                    int resp = JOptionPane.showConfirmDialog(
                            seleccionar, "El archivo " + archivo.getName() + " ya existe.\n" + "¿Desea remplazarlo?",
                            "Guardar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (JOptionPane.OK_OPTION == resp) {

                        for (int i = 0; i < indicePestaña.getIndice(); i++) {
                            if (tabs.getTitleAt(i).equals(archivo.getName())) {
                                System.out.println("Archivo igual abierto--------------------");
                                ctrl.setCambios(false);
                                save.setGuardado(true);
                                grabarArchivo(archivo.toString(), saveAutomata(save), save);
                                break;
                            } else {
                                System.out.println("Archivo igual a iniciar--------------------");
                                grabarArchivo(archivo.toString(), saveAutomata(save), save);
                                WatchService(save, tabs, archivo);
                            }

                        }

                        tabs.setTitleAt(tabs.getSelectedIndex(), archivo.getName());
                        if (save.getIdNombre() != 0) {
                            indicePestaña.getPesEliminada().add(save.getIdNombre());
                            indicePestaña.setPesEliminada(indicePestaña.getPesEliminada());
                            save.setIdNombre(0);
                        }
                        save.setRutaArchivo(archivo.toString());
                        ctrl.setCambios(false);
                    } else {
                        guardarJLEFO(activo, tabs);

                    }

                } else {
                    //Si el archivo no existe se manda a Guardar
                    System.out.println("No existe archivo: " + archivo.toString());
                    grabarArchivo(archivo.toString(), saveAutomata(save), save);
                    WatchService(save, tabs, archivo);
                    tabs.setTitleAt(tabs.getSelectedIndex(), archivo.getName());
                    if (save.getIdNombre() != 0) {
                        indicePestaña.getPesEliminada().add(save.getIdNombre());
                        indicePestaña.setPesEliminada(indicePestaña.getPesEliminada());
                        save.setIdNombre(0);
                    }
                    save.setRutaArchivo(archivo.toString());
                    ctrl.setCambios(false);
                }
            }

        }
    }

    public static ArrayList<String> saveAutomata(V_lienzo save) {
        MouseListener[] ml = save.getMouseListeners();
        C_automata ctrl = (C_automata) ml[0];
        transiciones = ctrl.getTransiciones();
        estados = ctrl.getEstados();
        String datos = null;
        guardar.clear();
        guardar.add("//ESTADOS");
        for (M_estado edo : estados) {
            datos = edo.getX() + ", " + edo.getY() + ", " + edo.getIdEstado() + ", " + edo.getTipo();
            guardar.add(datos);
        }
        guardar.add("//TRANSICIONES");
        for (M_transicion trans : transiciones) {
            datos = trans.getXa() + ", " + trans.getYa() + ", " + trans.getXb() + ", "
                    + trans.getYb() + ", " + trans.getTipo() + ", " + trans.getOrigen()
                    + ", " + trans.getDestino() + ", " + cambiarIda(trans.getAlfabeto());
            guardar.add(datos);
        }
        guardar.add("//FIN");

        return guardar;

    }

    public static void guardarImagen(V_tabs tabs) {
        idPestaña = tabs.getSelectedIndex();
        seleccionar = new JFileChooser();
        seleccionar.addChoosableFileFilter(new FileNameExtensionFilter("PNG (*.png)", "png"));
        seleccionar.setAcceptAllFileFilterUsed(false);
        carpetaJLEFO();
        seleccionar.setSelectedFile(new File(tabs.getTitleAt(idPestaña)));
        if (seleccionar.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            archivo = seleccionar.getSelectedFile();

            //Comprobando Extensión .png
            if (archivo.toString().endsWith(".png") == false) {
                archivo = new File(archivo + ".png");
            }
            try {
                if (archivo.exists() == true) {

                    int resp = JOptionPane.showConfirmDialog(
                            seleccionar, "El archivo " + archivo.getName() + " ya existe.\n" + "¿Desea remplazarlo?",
                            "Guardar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (JOptionPane.OK_OPTION == resp) {
                        ImageIO.write(Archivo.crearImagen(tabs), "png", archivo);
                    } else {
                        guardarImagen(tabs);
                    }
                } else {
                    ImageIO.write(Archivo.crearImagen(tabs), "png", archivo);
                }

            } catch (IOException ex) {
                Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void abrirJLEFO(V_tabs tabs) {
        boolean activo = false; //Si ya se creo el la pestaña nueva
        idPestaña = 0; //indice de pestaña
        C_automata ctrl = new C_automata();
        if (tabs.getComponentCount() != 0) {
            save = new LienzoFromScroll().obtener(tabs);
            if (save.getTipoPanel().equals(AF)) {
                MouseListener[] ml = save.getMouseListeners();
                ctrl = (C_automata) ml[0];
            }
        }
        seleccionar = new JFileChooser();
        seleccionar.addChoosableFileFilter(new FileNameExtensionFilter("JLEFO (*.jlefo)", "jlefo"));
        seleccionar.setAcceptAllFileFilterUsed(false);
        carpetaJLEFO();
        if (seleccionar.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            tabs.setVisible(true);
            archivo = seleccionar.getSelectedFile();

            //Primero verificamos que sea un archivo valido
            if (archivo.toString().endsWith(".jlefo") == true) {
                //archivo = new File(path); // lo utilizamos para sacar el nombre del archivo y no toda la ruta
                if (tabs.getSelectedComponent() != null) {

                    idPestaña = tabs.getSelectedIndex();
                    //Si quieres abrir el mismo archivo Activo **Necesitamos recorrer el array de Vs_tabs
                    //Si eliges un archivo abierto, para ello el nombre y ruta debe ser el mismo;

                    if (!save.getTipoPanel().equals(ER)) {

                        for (int i = 0; i < indicePestaña.getIndice(); i++) {
                            if (archivo.getName().equals(tabs.getTitleAt(i))) {
                                V_lienzo comprobar = new LienzoFromScroll().obtenerEn(i, tabs);
                                if (archivo.toString().equals(comprobar.getRutaArchivo())) {
                                    tabs.setSelectedIndex(i);
                                    return;
                                }
                            }
                        }

                        //Si es una pestaña totalmente en blanco, que los array estan vacios, se sobreescribe en ella
                        if (tabs.getTitleAt(idPestaña).endsWith(".jlefo") == false
                                && ctrl.getTransiciones().isEmpty()
                                && ctrl.getEstados().isEmpty()) {
                            tabs.setTitleAt(tabs.getSelectedIndex(), archivo.getName());
                            save.setRutaArchivo(archivo.toString());
                            /*Posiblemente instanciar el escuchador*/

                            indicePestaña.getPesEliminada().add(save.getIdNombre());
                            indicePestaña.setPesEliminada(indicePestaña.getPesEliminada());
                            save.setIdNombre(0);

                            //Archivo.abrirArchivo(path, tabs, idPestaña);
                            activo = true;//Activar el Pintado de los elementos
                        } else {
                            //si es una pestaña no en blanco, puede ser Nuevo Autómata con algun estado creado o x.jlefo
                            tabs.añadirTab(archivo.getName(), indicePestaña.getIndice(), archivo, 0, AF);
                            idPestaña = indicePestaña.getIndice();
                            tabs.setSelectedIndex(idPestaña);
                            indicePestaña.maxIndice();
                            // Mod_archivo.abrirArchivo(archivo.toString(), tabs, idPestaña);
                            activo = true;//Activar el Pintado de los elementos

                        }

                        //
                    } else {
                        ///si es una pestaña tipo e.r
                        tabs.añadirTab(archivo.getName(), indicePestaña.getIndice(), archivo, 0, AF);
                        idPestaña = indicePestaña.getIndice();
                        tabs.setSelectedIndex(idPestaña);
                        indicePestaña.maxIndice();
                        activo = true;//Activar el Pintado de los elementos

                    }

                } else {
                    //Cuando no hay pestañas abiertas
                    tabs.añadirTab(archivo.getName(), indicePestaña.getIndice(), archivo, 0, AF);
                    idPestaña = indicePestaña.getIndice();
                    indicePestaña.maxIndice();
                    activo = true;//Activar el Pintado de los elementos
                }
            } else {
                int resp = JOptionPane.showConfirmDialog(
                        seleccionar, "El archivo " + archivo.getName() + " no es un formato válido.\n" + "¿Desea seleccionar otro?",
                        "Guardar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (JOptionPane.OK_OPTION == resp) {
                    abrirJLEFO(tabs);
                } else {
                    return;
                }

            }

            //Mandar a Pintar 
            if (activo == true) {
                tabs.setVisible(true);
                abrirArchivo(archivo, tabs, idPestaña, false);
                activo = false;
            }
        }
    }

    public static void abrirArchivo(File archivo, V_tabs tabs, int index, boolean watch) {
        V_lienzo save;
        save = new LienzoFromScroll().obtenerEn(index, tabs);
        MouseListener[] ml = save.getMouseListeners();
        C_automata ctrl = (C_automata) ml[0];
        ArrayList<String> lineas = leerArchivo(archivo.toString(), save);

        ctrl.getTransiciones().clear();
        ctrl.getEstados().clear();
        Iterator<String> lista = lineas.iterator();
        //Declarando variables para estados y transiciones
        if (lineas.size() == 3) {
            if (watch == false) {
                WatchService(save, tabs, archivo);
            }
            return;
        }

        int x1 = 0, y1 = 0, x2 = 0, y2 = 0, idEstado = 0, idOrigen = 0, idDestino = 0;
        String tipo = null, alfabeto = null;

        while (lista.hasNext()) {
            if (lista.next().equals("//ESTADOS")) {
                String linea = lista.next().trim();
                do {
                    if (!linea.equals("")) {
                        String[] datos = linea.split(",");
                        if (datos.length > 1) {
                            x1 = Integer.parseInt(datos[0].trim());
                            y1 = Integer.parseInt(datos[1].trim());
                            idEstado = Integer.parseInt(datos[2].trim());
                            tipo = datos[3].trim();
                            ctrl.getEstados().add(new M_estado(x1, y1, idEstado, tipo));
                        }
                    }
                    linea = lista.next().trim();
                } while (!linea.equals("//TRANSICIONES"));
                linea = lista.next().trim();
                do {
                    if (!linea.equals("")) {
                        String[] datos = linea.split(",");
                        if (datos.length > 1) {
                            x1 = Integer.parseInt(datos[0].trim());
                            y1 = Integer.parseInt(datos[1].trim());
                            x2 = Integer.parseInt(datos[2].trim());
                            y2 = Integer.parseInt(datos[3].trim());
                            tipo = datos[4].trim();
                            idOrigen = Integer.parseInt(datos[5].trim());
                            idDestino = Integer.parseInt(datos[6].trim());
                            alfabeto = datos[7].trim();
                            ctrl.getTransiciones().add(new M_transicion(x1, y1, x2, y2, tipo, idOrigen, idDestino, cambiarVuelta(alfabeto)));
                            save.repaint();
                            linea = lista.next().trim();
                        }
                    }
                } while (!linea.equals("//FIN"));
            }
        }
        save.repaint();
        if (watch == false) {
            WatchService(save, tabs, archivo);
        }

    }

    public static String cambiarIda(String alfabeto) {
        if (alfabeto.length() > 1) {
            alfabeto = "0-1";
        }
        return alfabeto;
    }

    public static String cambiarVuelta(String alfabeto) {
        if (alfabeto.length() > 1) {
            alfabeto = "0,1";
        }
        return alfabeto;
    }

////Correcto    
    public static BufferedImage crearImagen(V_tabs tab) {
        save = new LienzoFromScroll().obtener(tab);
        MouseListener[] ml = save.getMouseListeners();
        C_automata ctrl = (C_automata) ml[0];
        M_imagen panel = new M_imagen(ctrl.getEstados(), ctrl.getTransiciones());
        panel.repaint();
        panel.area();

        int w = panel.getWidth();
        int h = panel.getHeight();

        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        bi = bi.getSubimage(panel.area().x, panel.area().y, panel.area().width, panel.area().height);
        panel.paint(g);

        return bi;
    }

    public static ArrayList<String> leerArchivo(String archivo, V_lienzo save) {
        ArrayList<String> lineas = new ArrayList();

        try {
            FileReader flujo = new FileReader(archivo);
            BufferedReader buffer = new BufferedReader(flujo);
            String linea = buffer.readLine();
            while (linea != null) {
                lineas.add(linea);
                linea = buffer.readLine();
            }
            buffer.close();
            flujo.close();
        } catch (IOException ex) {
            System.out.println("Error de archivo" + ex);
            System.exit(-1);
        }

        return lineas;
    }

    public static void grabarArchivo(String archivo, ArrayList<String> lineas, V_lienzo save) {

        try {
            FileWriter flujo = new FileWriter(archivo);
            BufferedWriter buffer = new BufferedWriter(flujo);
            for (String linea : lineas) {
                buffer.write(linea);
                buffer.newLine();
            }
            buffer.close();
            flujo.close();
        } catch (IOException error) {
            System.out.println("Error de archivo" + error);
            System.exit(-1);
        }
    }

    public static void grabarArchivoConvertidoAFD(String archivo, ArrayList<String> lineas) {
        try {
            FileWriter flujo = new FileWriter(archivo);
            BufferedWriter buffer = new BufferedWriter(flujo);
            for (String linea : lineas) {
                buffer.write(linea);
                buffer.newLine();
            }

            buffer.close();
            flujo.close();
        } catch (IOException error) {
            System.out.println("Error de archivo" + error);
            System.exit(-1);
        }
    }

    public static void guardarConversiontoAFD(DefaultTableModel mod_tabSub, String ruta) {

        System.out.println("\n\n\t FINAL CAFD");
        System.out.println("Q--Σ={0}--Σ={1}--FINAL");
        for (int i = 0; i < mod_tabSub.getRowCount(); i++) {
            System.out.println(mod_tabSub.getValueAt(i, 0) + "  " + mod_tabSub.getValueAt(i, 1) + "      " + mod_tabSub.getValueAt(i, 2) + "      " + mod_tabSub.getValueAt(i, 3));
        }

        System.out.println("Conversion:" + mod_tabSub.getRowCount());
        List<M_estado> estadosAFN = new ArrayList<>();
        List<M_transicion> transicionesAFN = new ArrayList<>();
        String datos = null;
        String tipoA = "Edo-Aceptacion";
        String tipoT = "Edo-Transicion";
        int x = 50, y = 200;
        //ruta = "src/archivos/AFN.jlefo";

        //Recorrer la tabla para obtener los estados (q0...qn)
        for (int i = 0; i < mod_tabSub.getRowCount(); i++) {
            if (mod_tabSub.getValueAt(i, 3).toString().equals("1")) {
                estadosAFN.add(new M_estado(x, y, i, tipoA));
            } else {
                estadosAFN.add(new M_estado(x, y, i, tipoT));
            }
            x = x + 50;
        }

        //Recorrer tabla para obtener Transiciones 
        for (int i = 0; i < mod_tabSub.getRowCount(); i++) {
            int coorx = 0, coory = 0; // Guarda las coordenadas de el estado i-->(idEstado--q0...qn)
            int column1 = (int) mod_tabSub.getValueAt(i, 1);//Obtener valor de la columna de alfabeto 0
            int column2 = (int) mod_tabSub.getValueAt(i, 2);//Obtener valor de la Columna de alfabeto 1
            //Coordenadas de la transiciones segun su columna (0 | 1)
            int column1X = 0, column1Y = 0, column2X = 0, column2Y = 0;
            //Obtener las coordenadas del estado de origen 
            for (M_estado estado : estadosAFN) {
                if (estado.getIdEstado() == i) {
                    coorx = estado.getX();
                    coory = estado.getY();
                    break;
                }
            }

            //Validar si el estado de destino es el mismo (0 | 1)--> (0 | 0)
            if (column1 == column2) {
                if (i == column1) {
                    //Si el estado de destino es el mismo que el de origen (Transición de tipo)
                    transicionesAFN.add(new M_transicion(coorx, coory, coorx, coory, "arco", i, i, "0,1"));
                } else {
                    for (M_estado estado : estadosAFN) {
                        //Si el estado de destino es diferente que el de origen (Transición de tipo simple)
                        if (estado.getIdEstado() == column1) {
                            transicionesAFN.add(new M_transicion(coorx, coory, estado.getX(), estado.getY(), "simple", i, estado.getIdEstado(), "0,1"));
                            break;
                        }
                    }

                }
            }
            //Validar si el estado de destino es diferente (0 | 1)--> (2 | 3)
            if (column1 != column2) {
                //Obtenemos coordenadas para estado de la columna del alfabeto 0
                for (M_estado estado : estadosAFN) {
                    if (estado.getIdEstado() == column1) {
                        column1X = estado.getX();
                        column1Y = estado.getY();
                        break;
                    }
                }
                //Obtenemos coordenadas para estado de la columna del alfabeto 1
                for (M_estado estado : estadosAFN) {
                    if (estado.getIdEstado() == column2) {
                        column2X = estado.getX();
                        column2Y = estado.getY();
                        break;
                    }
                }

                //Validamos los tres casos que se pueden presentar cuando las columnas son direntes, con el fin 
                // de determinar que tipo de transición se creará, sus coordenadas, alfabeto, Id origen y destino.
                if (column1 == i) {
                    transicionesAFN.add(new M_transicion(coorx, coory, column1X, column1Y, "arco", i, column1, "0"));
                    transicionesAFN.add(new M_transicion(coorx, coory, column2X, column2Y, "simple", i, column2, "1"));
                } else if (column2 == i) {
                    transicionesAFN.add(new M_transicion(coorx, coory, column2X, column2Y, "arco", i, column2, "1"));
                    transicionesAFN.add(new M_transicion(coorx, coory, column1X, column1Y, "simple", i, column1, "0"));
                } else {
                    transicionesAFN.add(new M_transicion(coorx, coory, column1X, column1Y, "simple", i, column1, "0"));
                    transicionesAFN.add(new M_transicion(coorx, coory, column2X, column2Y, "simple", i, column2, "1"));
                }
            }
        }

        String datosAFN = null;
        guardar.clear();
        guardar.add("//ESTADOS");
        for (M_estado edo : estadosAFN) {
            datosAFN = edo.getX() + ", " + edo.getY() + ", " + edo.getIdEstado() + ", " + edo.getTipo();
            guardar.add(datosAFN);
        }
        guardar.add("//TRANSICIONES");
        for (M_transicion trans : transicionesAFN) {
            datosAFN = trans.getXa() + ", " + trans.getYa() + ", " + trans.getXb() + ", "
                    + trans.getYb() + ", " + trans.getTipo() + ", " + trans.getOrigen()
                    + ", " + trans.getDestino() + ", " + cambiarIda(trans.getAlfabeto());
            guardar.add(datosAFN);
        }
        guardar.add("//FIN");
        grabarArchivoConvertidoAFD(ruta, guardar);

    }

    public static void guardarConversiontoAFD1(DefaultTableModel mod_tabSub, String ruta, String alfabeto) {

        System.out.println("Conversion:" + mod_tabSub.getRowCount());
        List<M_estado> estadosAFN = new ArrayList<>();
        List<M_transicion> transicionesAFN = new ArrayList<>();
        String tipoA = "Edo-Aceptacion";
        String tipoT = "Edo-Transicion";
        int x = 50, y = 200;
        //ruta = "src/archivos/AFN.jlefo";

        //Recorrer la tabla para obtener los estados (q0...qn)
        for (int i = 0; i < mod_tabSub.getRowCount(); i++) {
            if (mod_tabSub.getValueAt(i, 2).toString().equals("1")) {
                estadosAFN.add(new M_estado(x, y, i, tipoA));
            } else {
                estadosAFN.add(new M_estado(x, y, i, tipoT));
            }
            x = x + 50;
        }

        //Recorrer tabla para obtener Transiciones 
        for (int i = 0; i < mod_tabSub.getRowCount(); i++) {
            int coorx = 0, coory = 0; // Guarda las coordenadas de el estado i-->(idEstado--q0...qn)
            int column1 = (int) mod_tabSub.getValueAt(i, 1);//Obtener valor de la columna de alfabeto 0
            //Obtener las coordenadas del estado de origen 
            for (M_estado estado : estadosAFN) {
                if (estado.getIdEstado() == i) {
                    coorx = estado.getX();
                    coory = estado.getY();
                    break;
                }
            }

            //Validar si el estado de destino es el mismo que el id del estado
            if (i == column1) {
                //Si el estado de destino es el mismo que el de origen (Transición de tipo)
                transicionesAFN.add(new M_transicion(coorx, coory, coorx, coory, "arco", i, i, alfabeto));
            } else {
                for (M_estado estado : estadosAFN) {
                    //Si el estado de destino es diferente que el de origen (Transición de tipo simple)
                    if (estado.getIdEstado() == column1) {
                        transicionesAFN.add(new M_transicion(coorx, coory, estado.getX(), estado.getY(), "simple", i, estado.getIdEstado(), alfabeto));
                        break;
                    }
                }
            }

        }
        String datosAFN = null;
        guardar.clear();
        guardar.add("//ESTADOS");
        for (M_estado edo : estadosAFN) {
            datosAFN = edo.getX() + ", " + edo.getY() + ", " + edo.getIdEstado() + ", " + edo.getTipo();
            guardar.add(datosAFN);
        }
        guardar.add("//TRANSICIONES");
        for (M_transicion trans : transicionesAFN) {
            datosAFN = trans.getXa() + ", " + trans.getYa() + ", " + trans.getXb() + ", "
                    + trans.getYb() + ", " + trans.getTipo() + ", " + trans.getOrigen()
                    + ", " + trans.getDestino() + ", " + cambiarIda(trans.getAlfabeto());
            guardar.add(datosAFN);
        }
        guardar.add("//FIN");
        grabarArchivoConvertidoAFD(ruta, guardar);

    }

    public static void WatchService(V_lienzo lienzo, V_tabs pestaña, File ruta) {
        Vigilante jws = new Vigilante(lienzo, pestaña, ruta);
    }

    public static void guardarJLEFO_Cerrar(V_lienzo dibujo, V_tabs tab, int i, Control m) {
        //JFileChooser fileChooser = new JFileChooser();
        //File archivo;
        seleccionar = new JFileChooser();
        seleccionar.addChoosableFileFilter(new FileNameExtensionFilter("JLEFO (*.jlefo)", "jlefo"));
        seleccionar.setAcceptAllFileFilterUsed(false);
        carpetaJLEFO();
        seleccionar.setSelectedFile(new File(tab.getTitleAt(i)));

        int result = seleccionar.showSaveDialog(null);

        if (result != seleccionar.CANCEL_OPTION) {

            archivo = seleccionar.getSelectedFile();

            if (archivo.toString().endsWith(".jlefo") == false) {
                archivo = new File(archivo + ".jlefo");
            }
            if (archivo.exists() == true) {
                //Validando cuando le da en la X de la pestaña
                int resp = JOptionPane.showConfirmDialog(
                        seleccionar, "El archivo " + archivo.getName() + " ya existe.\n" + "¿Desea remplazarlo?",
                        "Guardar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (JOptionPane.OK_OPTION == resp) {

                    grabarArchivo(archivo.toString(), saveAutomata(dibujo), dibujo);
                    indicePestaña.getPesEliminada().add(dibujo.getIdNombre());
                    indicePestaña.setPesEliminada(indicePestaña.getPesEliminada());
                    if (dibujo.getTipoPanel().equals("af")) {
                        //eliminar directorios y archivos de los tabs existentes
                        m.deleteRootFolder(dibujo.getName());
                        dibujo.setAnalizar(false);
                    }

                    tab.remove(i);
                    indicePestaña.minIndice();
                } else {
                    guardarJLEFO_Cerrar(dibujo, tab, i, m);

                }

            } else {

                //Si el archivo no existe se manda a Guardar
                grabarArchivo(archivo.toString(), saveAutomata(dibujo), dibujo);
                indicePestaña.getPesEliminada().add(dibujo.getIdNombre());
                indicePestaña.setPesEliminada(indicePestaña.getPesEliminada());
                if (dibujo.getTipoPanel().equals("af")) {
                    //eliminar directorios y archivos de los tabs existentes
                    m.deleteRootFolder(dibujo.getName());
                    dibujo.setAnalizar(false);

                }

                tab.remove(i);
                indicePestaña.minIndice();
            }

        }

    }

    public static void carpetaJLEFO() {
        String root = System.getProperty("user.home") + "/Documents/JLEFO";
        File f = new File(root);

        if (f.exists()) {
            seleccionar.setCurrentDirectory(new File(root + "/"));
        } else {
            f.mkdir();
            seleccionar.setCurrentDirectory(new File(root + "/"));
        }
    }

    public static JFileChooser carpetaJLEFOConversion(JFileChooser seleccionar) {
        String root = System.getProperty("user.home") + "/Documents/JLEFO";
        File f = new File(root);

        if (f.exists()) {
            seleccionar.setCurrentDirectory(new File(root + "/"));
        } else {
            f.mkdir();
            seleccionar.setCurrentDirectory(new File(root + "/"));
        }
        return seleccionar;
    }
}
