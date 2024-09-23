/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import funciones.LienzoFromScroll;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import vista.V_slideMenu;
import vista.V_tabs;
import vista.pruebasV;

import static funciones.NmComponentes.*;
import funciones.archivo.Archivo;
import funciones.ctrlZ_Y.Control;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.*;
import modelo.M_imagen;
import vista.V_infoJlefo;
import vista.V_interfaz;
import vista.V_lienzo;
import vista.V_panel;


/**
 *
 * @author herma
 */
public class C_interfaz extends WindowAdapter implements ActionListener, Printable {

    V_tabs tabs;
    static JPanel slideMenu;
    static JPanel contenedor;
    static JTable tabla;
    static JTabbedPane tabPane;
    V_lienzo lienzo;
    String[] file;
    private int idNombre = 1;
    private int idNombreElimado = 1;
    private int idPestaña;
    private int maxReduccion = 0;
    private V_infoJlefo w;
    V_interfaz interfaz;

    public C_interfaz(V_interfaz interfaz, V_tabs tabs, V_slideMenu slideMenu, JPanel contenedor,
            V_tabs tabPane, String[] file) {
        this.interfaz = interfaz;
        this.tabs = tabs;
        this.slideMenu = slideMenu;
        this.tabPane = tabPane;
        this.contenedor = contenedor;
        this.file = file;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Component componente = (Component) e.getSource();
        C_automata ctrl = null;
        if (tabs.getComponentCount() != 0) {
            lienzo = new LienzoFromScroll().obtener(tabs);
            if (lienzo != null && lienzo.getTipoPanel().equals(AF)) {
                MouseListener[] ml = lienzo.getMouseListeners();
                ctrl = (C_automata) ml[0];
            }
        }
        switch (componente.getName()) {
            case AF:
                tabs.setVisible(true);
                if (indicePestaña.getPesEliminada().size() > 0) {
                    idNombreElimado = idNombre;
                    idNombre = (int) indicePestaña.getPesEliminada().get(0);
                    indicePestaña.getPesEliminada().remove(0);
                    tabs.añadirTab("Nuevo Autómata " + idNombre,
                            indicePestaña.getIndice(), new File(""), idNombre, AF);
                    tabs.setSelectedIndex(indicePestaña.getIndice());
                    indicePestaña.maxIndice();
                    idNombre = idNombreElimado;
                    interfaz.controlVisual(true, false, false, false);
                } else {
                    tabs.añadirTab("Nuevo Autómata " + idNombre,
                            indicePestaña.getIndice(), new File(""), idNombre, AF);
                    tabs.setSelectedIndex(indicePestaña.getIndice());
                    indicePestaña.maxIndice();
                    idNombre++;
                    interfaz.controlVisual(true, false, false, false);
                }
                break;
            case ER:
                tabs.setVisible(true);
                if (indicePestaña.getPesEliminada().size() > 0) {
                    idNombreElimado = idNombre;
                    idNombre = (int) indicePestaña.getPesEliminada().get(0);
                    indicePestaña.getPesEliminada().remove(0);
                    tabs.añadirTab("Nuevo e.r.  " + idNombre,
                            indicePestaña.getIndice(), new File(""), idNombre, ER);
                    tabs.setSelectedIndex(indicePestaña.getIndice());
                    indicePestaña.maxIndice();
                    idNombre = idNombreElimado;
                    interfaz.controlVisual(false, false, false, false);
                } else {
                    tabs.añadirTab("Nuevo e.r.  " + idNombre,
                            indicePestaña.getIndice(), new File(""), idNombre, ER);
                    tabs.setSelectedIndex(indicePestaña.getIndice());
                    indicePestaña.maxIndice();
                    idNombre++;
                    interfaz.controlVisual(false, false, false, false);
                }
                break;
            case GLC:// Añadir GLC
            tabs.setVisible(true);
            if (indicePestaña.getPesEliminada().size() > 0) {
                idNombreElimado = idNombre;
                idNombre = (int) indicePestaña.getPesEliminada().get(0);
                indicePestaña.getPesEliminada().remove(0);
                tabs.añadirTab("Nueva GLC  " + idNombre,
                        indicePestaña.getIndice(), new File(""), idNombre, GLC);
                tabs.setSelectedIndex(indicePestaña.getIndice());
                indicePestaña.maxIndice();
                idNombre = idNombreElimado;
                interfaz.controlVisual(false, false, false, false);
                V_panel panelglc = new V_panel();
                tabla = new JTable();
                panelglc.add(new Container(), tabla=new JTable());
                String[] columnas = { "No terminales", "Terminales" };
                String[][] datos = { { "S", "aSb" }, { "S", "?" } };
                tabla.setModel(new DefaultTableModel(datos, columnas));
                JTextField cadena = new JTextField(20);
                cadena.setText("aaabbb");
            } else {
                tabs.añadirTab("Nueva GLC  " + idNombre,
                        indicePestaña.getIndice(), new File(""), idNombre, GLC);
                tabs.setSelectedIndex(indicePestaña.getIndice());
                indicePestaña.maxIndice();
                idNombre++;
                interfaz.controlVisual(false, false, false, false);
                V_panel panelglc = new V_panel();
                tabla = new JTable();
                panelglc.add(new Container(), tabla=new JTable());
                String[] columnas = { "No terminales", "Terminales" };
                String[][] datos = { { "S", "aSb" }, { "S", "?" } };
                tabla.setModel(new DefaultTableModel(datos, columnas));
                JTextField cadena = new JTextField(20);
                cadena.setText("aaabbb");

                }
                break;
            case ADP:
                tabs.setVisible(true);
                if (indicePestaña.getPesEliminada().size() > 0) {
                    idNombreElimado = idNombre;
                    idNombre = (int) indicePestaña.getPesEliminada().get(0);
                    indicePestaña.getPesEliminada().remove(0);
                    tabs.añadirTab("Nuevo Autómata de Pila" + idNombre,
                            indicePestaña.getIndice(), new File(""), idNombre, ADP);
                    tabs.setSelectedIndex(indicePestaña.getIndice());
                    indicePestaña.maxIndice();
                    idNombre = idNombreElimado;
                    interfaz.controlVisual(true, false, false, false);
                } else {
                    tabs.añadirTab("Nuevo Autómata de Pila" + idNombre,
                            indicePestaña.getIndice(), new File(""), idNombre, ADP);
                    tabs.setSelectedIndex(indicePestaña.getIndice());
                    indicePestaña.maxIndice();
                    idNombre++;
                    interfaz.controlVisual(true, false, false, false);
                }
                break;
            case MT:
                tabs.setVisible(true);
                if (indicePestaña.getPesEliminada().size() > 0) {
                    idNombreElimado = idNombre;
                    idNombre = (int) indicePestaña.getPesEliminada().get(0);
                    indicePestaña.getPesEliminada().remove(0);
                    tabs.añadirTab("Nuevo MT" + idNombre,
                            indicePestaña.getIndice(), new File(""), idNombre, MT);
                    tabs.setSelectedIndex(indicePestaña.getIndice());
                    indicePestaña.maxIndice();
                    idNombre = idNombreElimado;
                    interfaz.controlVisual(false, false, false, false);
                } else {
                    tabs.añadirTab("Nuevo MT" + idNombre,
                            indicePestaña.getIndice(), new File(""), idNombre, MT);
                    tabs.setSelectedIndex(indicePestaña.getIndice());
                    indicePestaña.maxIndice();
                    idNombre++;
                    interfaz.controlVisual(false, false, false, false);
                }
                break;
            case MTBD:
                tabs.setVisible(true);
                if (indicePestaña.getPesEliminada().size() > 0) {
                    idNombreElimado = idNombre;
                    idNombre = (int) indicePestaña.getPesEliminada().get(0);
                    indicePestaña.getPesEliminada().remove(0);
                    tabs.añadirTab("Nuevo MTBD" + idNombre,
                            indicePestaña.getIndice(), new File(""), idNombre, MTBD);
                    tabs.setSelectedIndex(indicePestaña.getIndice());
                    indicePestaña.maxIndice();
                    idNombre = idNombreElimado;
                    interfaz.controlVisual(false, false, false, false);
                } else {
                    tabs.añadirTab("Nuevo MTBD" + idNombre,
                            indicePestaña.getIndice(), new File(""), idNombre, MTBD);
                    tabs.setSelectedIndex(indicePestaña.getIndice());
                    indicePestaña.maxIndice();
                    idNombre++;
                    interfaz.controlVisual(false, false, false, false);
                }
                break;
             case MTMC:
                tabs.setVisible(true);
                if (indicePestaña.getPesEliminada().size() > 0) {
                    idNombreElimado = idNombre;
                    idNombre = (int) indicePestaña.getPesEliminada().get(0);
                    indicePestaña.getPesEliminada().remove(0);
                    tabs.añadirTab("Nuevo MTMC" + idNombre,
                            indicePestaña.getIndice(), new File(""), idNombre, MTMC);
                    tabs.setSelectedIndex(indicePestaña.getIndice());
                    indicePestaña.maxIndice();
                    idNombre = idNombreElimado;
                    interfaz.controlVisual(false, false, false, false);
                } else {
                    tabs.añadirTab("Nuevo MTMC" + idNombre,
                            indicePestaña.getIndice(), new File(""), idNombre, MTMC);
                    tabs.setSelectedIndex(indicePestaña.getIndice());
                    indicePestaña.maxIndice();
                    idNombre++;
                    interfaz.controlVisual(false, false, false, false);
                }
                break;
            case MTB:
                tabs.setVisible(true);
                if (indicePestaña.getPesEliminada().size() > 0) {
                    idNombreElimado = idNombre;
                    idNombre = (int) indicePestaña.getPesEliminada().get(0);
                    indicePestaña.getPesEliminada().remove(0);
                    tabs.añadirTab("Nuevo MTB" + idNombre,
                            indicePestaña.getIndice(), new File(""), idNombre, MTB);
                    tabs.setSelectedIndex(indicePestaña.getIndice());
                    indicePestaña.maxIndice();
                    idNombre = idNombreElimado;
                    interfaz.controlVisual(false, false, false, false);
                } else {
                    tabs.añadirTab("Nuevo MTB" + idNombre,
                            indicePestaña.getIndice(), new File(""), idNombre, MTB);
                    tabs.setSelectedIndex(indicePestaña.getIndice());
                    indicePestaña.maxIndice();
                    idNombre++;
                    interfaz.controlVisual(false, false, false, false);
                }
                break;
            case ABRIR:
                Archivo.abrirJLEFO(tabs);
                break;
            case GUARDAR:
                if (tabs.getComponentCount() != 0) {
                    if (!lienzo.getTipoPanel().equals(ER)) {
                        idPestaña = tabs.getSelectedIndex();
                        if (tabs.getSelectedComponent() != null) {
                            if (tabs.getTitleAt(idPestaña).endsWith(".jlefo")) {
                                Archivo.guardarJLEFO(true, tabs);
                            } else {
                                Archivo.guardarJLEFO(false, tabs);
                            }
                            V_interfaz.b_guardar.setEnabled(false);
                            V_interfaz.guardar.setEnabled(false);
                        }
                    }
                }
                break;
            case GUARDARCOMO:
                if (tabs.getComponentCount() != 0) {
                    if (!lienzo.getTipoPanel().equals(ER)) {
                        if (tabs.getSelectedComponent() != null) {
                            Archivo.guardarJLEFO(false, tabs);
                        }
                    }
                }
                break;
            case DESHACER:
                if (tabs.getComponentCount() != 0) {
                    if (lienzo.getTipoPanel().equals(AF)) {
                        /* Agregado para cuando se este analizando */
                        if (!ctrl.getRastreoActivo()) {
                            try {
                                Control m = new Control();
                                String version = m.getVersionUndo(lienzo.getName());
                                if (version != null) {
                                    m.crearObjetoRedo(lienzo.getName(), ctrl.getTransiciones(),
                                            ctrl.getEstados(), ctrl.getEstadosElim(), ctrl.getVersion());
                                    ctrl.setVersion(ctrl.getVersion() - 1);
                                    m.getObjetosUndo(lienzo.getName(), version, lienzo);
                                    lienzo.repaint();
                                    lienzo = null;
                                    V_interfaz.b_rehacer.setEnabled(true);
                                    V_interfaz.rehacer.setEnabled(true);
                                    Thread.sleep(100);
                                }
                            } catch (InterruptedException ex) {
                                System.err.println(ex.getMessage());
                            }
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "Para  editar  el  diagrama,\ncierre la ventana de Rastreo.", "Editar Diagrama",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
                break;
            case REHACER:
                if (tabs.getComponentCount() != 0) {
                    if (lienzo.getTipoPanel().equals(AF)) {
                        /* Agregado para cuando se este analizando */
                        if (!ctrl.getRastreoActivo()) {
                            try {
                                Control m2 = new Control();
                                String version2 = m2.getVersionRedo(lienzo.getName());
                                if (version2 != null) {
                                    m2.crearObjetoUndo(lienzo.getName(), ctrl.getTransiciones(),
                                            ctrl.getEstados(), ctrl.getEstadosElim(), ctrl.getVersion());
                                    ctrl.setVersion(ctrl.getVersion() + 1);
                                    m2.getObjetosRedo(lienzo.getName(), version2, lienzo);
                                    lienzo.repaint();
                                    lienzo = null;
                                    Thread.sleep(100);
                                } else {
                                    V_interfaz.b_rehacer.setEnabled(false);
                                    V_interfaz.rehacer.setEnabled(false);
                                }
                            } catch (InterruptedException ex) {
                                System.err.println(ex.getMessage());
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, RASTREANDO,
                                    "Editar Diagrama", INFORMATION_MESSAGE);
                        }
                    }
                }
                break;
            case ACERCAR:
                if (tabs.getComponentCount() != 0) {
                    if (!lienzo.getTipoPanel().equals(ER)) {
                        lienzo.setFactor(lienzo.getFactor() + 0.1);
                        lienzo.repaint();
                        maxReduccion++;
                    }
                }
                break;
            case ALEJAR:
                if (tabs.getComponentCount() != 0) {
                    if (!lienzo.getTipoPanel().equals(ER)) {
                        if (lienzo.getDiametro() >= 30 && maxReduccion > -2) {
                            lienzo.setFactor(lienzo.getFactor() - 0.1);
                            lienzo.repaint();
                            maxReduccion--;
                        } else {
                            lienzo.setDiametro(30);
                            lienzo.repaint();
                        }
                    }
                }
                break;
            case MENU:
                if (indicePestaña.getIndice() > 0) {
                    if (lienzo.getTipoPanel().equals(AF)) {
                        deslizarMenu(BOTON);
                    }
                }
                if (indicePestaña.getIndice() == 0) {
                    deslizarMenu(BOTON);
                }
                break;
            case IMAGEN:
                if (tabs.getComponentCount() != 0) {
                    if (!lienzo.getTipoPanel().equals(ER)) {
                        try {
                            // En caso de que no este una pestaña activada
                            // o no exista nada dibujado no se hará nada
                            if (tabs.getSelectedComponent() == null
                                    || ctrl.getTransiciones().isEmpty()
                                            && ctrl.getEstados().isEmpty()) {
                                break;
                            }
                            // En caso contrario
                            Archivo.guardarImagen(tabs);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(tabs, "ERROR DE EJECUCIÓN",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                break;
            case IMPRIMIR:
                if (tabs.getComponentCount() != 0) {
                    if (!lienzo.getTipoPanel().equals(ER) && !lienzo.getTipoPanel().equals(GLC)) {
                        try {
                            PrinterJob job = PrinterJob.getPrinterJob();
                            job.setPrintable((Printable) this);
                            job.printDialog();
                            job.print();
                        } catch (PrinterException ex) {
                            System.out.println("Error\n" + ex);
                        }
                    }
                }
                break;
            case TUTORIALES:
                try { // la direccion web la maneja el sistema operativo invocando
                      // el navegador predeterminado
                    Desktop.getDesktop().browse(new URI(URL));
                } catch (IOException | URISyntaxException ex) {
                    JOptionPane.showMessageDialog(null, NO_CONEXION,
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case MANUAL:
                try { // debe estar ubicado en la carpeta raiz del proyecto
                    Desktop.getDesktop().browse(new URI("Manual_de_usuario.pdf"));
                } catch (IOException | URISyntaxException ex) {
                    JOptionPane.showMessageDialog(null, NO_ENCONTRADO,
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case EJERCICIOS:
                try { // debe estar ubicado en la carpeta raiz del proyecto
                    Desktop.getDesktop().browse(new URI("Ejercicios.pdf"));
                } catch (IOException | URISyntaxException ex) {
                    JOptionPane.showMessageDialog(null, NO_ENCONTRADO,
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case ACERCADE:
                if (w != null) {
                    w.setVisible(true);
                } else {
                    w = new V_infoJlefo();
                }
                break;
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        close();
    }

    @Override
    public void windowStateChanged(WindowEvent e) {
        super.windowStateChanged(e);
        deslizarMenu(GENERAL);
    }

    private void close() {
        V_lienzo save = new LienzoFromScroll().obtener(tabs);
        Control m = new Control();
        if (tabs.getSelectedComponent() != null) {

            int close = 0;

            for (int i = 0; i < indicePestaña.getIndice(); i++) {
                V_lienzo comprobar = new LienzoFromScroll().obtenerEn(i, tabs);
                if (comprobar.getTipoPanel().equals(AF)) {
                    MouseListener[] ml = comprobar.getMouseListeners();
                    C_automata ctrl = (C_automata) ml[0];
                    if (ctrl.isCambios()) {
                        close++;
                    }
                }
            }

            if (close == 0) {
                // eliminar directorios y archivos de los tabs existentes
                int numTabs = tabs.getTabCount();
                ArrayList<V_lienzo> lienzos = new ArrayList<>();
                for (int i = 0; i < numTabs; i++) {
                    lienzos.add(new LienzoFromScroll().obtenerEn(i, tabs));
                }
                for (V_lienzo c : lienzos) {
                    V_lienzo v = (V_lienzo) c;
                    if (v.getTipoPanel() == AF) {
                        m.deleteRootFolder(v.getName());
                    }

                }
                System.exit(0);
            }

            int resp = JOptionPane.showConfirmDialog(
                    null, "Estás a punto de cerrar " + close
                            + " pestañas sin cambios guardados. \n ¿Quieres continuar?",
                    "Salir", YES_NO_OPTION, QUESTION_MESSAGE);
            if (OK_OPTION == resp) {
                // eliminar directorios y archivos de los tabs existentes
                int numTabs = tabs.getTabCount();
                ArrayList<V_lienzo> lienzos = new ArrayList<>();
                for (int i = 0; i < numTabs; i++) {
                    lienzos.add(new LienzoFromScroll().obtenerEn(i, tabs));
                }
                for (V_lienzo c : lienzos) {
                    V_lienzo v = (V_lienzo) c;
                    if (v.getTipoPanel() == AF) {
                        m.deleteRootFolder(v.getName());
                    }

                }
                System.exit(0);
            }
            if (JOptionPane.NO_OPTION == resp) {
                return;
            }
        }
        System.exit(0);

    }

    /* Esta clase imprime un AF por impresora */
    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            /* We have only one page, and 'page' is zero-based */
            return NO_SUCH_PAGE;
        }
        MouseListener[] ml = lienzo.getMouseListeners();
        C_automata cl = (C_automata) ml[0];
        M_imagen panel = new M_imagen(cl.getEstados(), cl.getTransiciones());
        panel.repaint();
        panel.area();
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        g2d.scale(pageFormat.getImageableWidth() / panel.getWidth(), 0.6);
        panel.printAll(graphics);

        return PAGE_EXISTS;
    }

    public static void deslizarMenu(String tipo) {
        int posicion = slideMenu.getX();
        int ancho = contenedor.getWidth();
        int alto = contenedor.getHeight();
        switch (tipo) {
            case ER:
                slideMenu.setOpaque(true);
                if (slideMenu.isVisible()) {
                    if (posicion > -1) {
                        slideMenu.setVisible(false);
                    }
                    if (posicion < -1) {
                        slideMenu.setVisible(false);
                    }
                }
                break;
            case AF:
                if (slideMenu.isVisible()) {
                    if (posicion < -1) {

                        Animacion.Animacion.mover_derecha(-160, 0, 2, 2, slideMenu);
                        contenedor.setSize(ancho - 166, alto);
                        tabPane.setSize(ancho - 166, alto);
                        Animacion.Animacion.mover_derecha(0, 166, 0, 2, contenedor);
                    }
                }

                if (!slideMenu.isVisible()) {
                    Animacion.Animacion.mover_izquierda(0, -160, 0, 2, slideMenu);
                    Animacion.Animacion.mover_izquierda(0, -1, 0, 2, contenedor);
                    contenedor.setSize(ancho + 166, alto);
                    tabPane.setSize(ancho + 166, alto);
                    slideMenu.setVisible(true);
                    Animacion.Animacion.mover_derecha(-160, 0, 2, 2, slideMenu);
                    contenedor.setSize(ancho - 166, alto);
                    tabPane.setSize(ancho - 166, alto);
                    Animacion.Animacion.mover_derecha(0, 166, 0, 2, contenedor);
                }

                break;

            case ADP:
                if (slideMenu.isVisible()) {
                    if (posicion < -1) {

                        Animacion.Animacion.mover_derecha(-160, 0, 2, 2, slideMenu);
                        contenedor.setSize(ancho - 166, alto);
                        tabPane.setSize(ancho - 166, alto);
                        Animacion.Animacion.mover_derecha(0, 166, 0, 2, contenedor);
                    }
                }

                if (!slideMenu.isVisible()) {
                    Animacion.Animacion.mover_izquierda(0, -160, 0, 2, slideMenu);
                    Animacion.Animacion.mover_izquierda(0, -1, 0, 2, contenedor);
                    contenedor.setSize(ancho + 166, alto);
                    tabPane.setSize(ancho + 166, alto);
                    slideMenu.setVisible(true);
                    Animacion.Animacion.mover_derecha(-160, 0, 2, 2, slideMenu);
                    contenedor.setSize(ancho - 166, alto);
                    tabPane.setSize(ancho - 166, alto);
                    Animacion.Animacion.mover_derecha(0, 166, 0, 2, contenedor);
                }

                break;
            case GLC:
                slideMenu.setOpaque(true);
                if (slideMenu.isVisible()) {
                    if (posicion > -1) {
                        slideMenu.setVisible(false);
                    }
                    if (posicion < -1) {
                        slideMenu.setVisible(false);
                    }
                }
                break;
            case MT:
                slideMenu.setOpaque(true);
                if (slideMenu.isVisible()) {
                    if (posicion > -1) {
                        slideMenu.setVisible(false);
                    }
                    if (posicion < -1) {
                        slideMenu.setVisible(false);
                    }
                }
                break;
            case MTBD:
                slideMenu.setOpaque(true);
                if (slideMenu.isVisible()) {
                    if (posicion > -1) {
                        slideMenu.setVisible(false);
                    }
                    if (posicion < -1) {
                        slideMenu.setVisible(false);
                    }
                }
                break;
            case BOTON:

                if (posicion > -1) {
                    if (slideMenu.isVisible()) {
                        System.out.println("Normal");
                        Animacion.Animacion.mover_izquierda(0, -160, 2, 2, slideMenu);
                        Animacion.Animacion.mover_izquierda(0, -1, 2, 2, contenedor);
                        contenedor.setSize(ancho + 166, alto);
                        tabPane.setSize(ancho + 166, alto);
                    } else {
                        System.out.println("Modificado");
                        Animacion.Animacion.mover_izquierda(0, -160, 0, 2, slideMenu);
                        Animacion.Animacion.mover_izquierda(0, -1, 0, 2, contenedor);
                        contenedor.setSize(ancho + 166, alto);
                        tabPane.setSize(ancho + 166, alto);
                        slideMenu.setVisible(true);
                        Animacion.Animacion.mover_derecha(-160, 0, 2, 2, slideMenu);
                        contenedor.setSize(ancho - 166, alto);
                        System.out.println("En curso");
                        tabPane.setSize(ancho - 166, alto);
                        Animacion.Animacion.mover_derecha(0, 166, 0, 2, contenedor);
                    }

                }

                if (posicion < -1) {
                    Animacion.Animacion.mover_derecha(-160, 0, 2, 2, slideMenu);
                    contenedor.setSize(ancho - 166, alto);
                    System.out.println("En curso");
                    tabPane.setSize(ancho - 166, alto);
                    Animacion.Animacion.mover_derecha(0, 166, 0, 2, contenedor);
                }

                break;

            case GENERAL:
                if (posicion < -1) {
                    slideMenu.setVisible(false);
                }
                break;
        }
    }

}
