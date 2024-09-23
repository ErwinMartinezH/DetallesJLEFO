/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import funciones.ctrlZ_Y.Control;
import funciones.orden.Ordenador;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.*;
import java.util.*;
import modelo.M_estado;
import modelo.M_transicion;
import vista.V_lienzo;
import vista.V_popupmenu;
import static javax.swing.JOptionPane.*;
import static java.awt.event.MouseEvent.*;
import static funciones.NmComponentes.*;
import funciones.archivo.Archivo;
import funciones.backtracking.Rastreador;
import funciones.er_afd.Inicio;
import java.awt.Component;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import modelo.M_arco;
import modelo.M_nodo;
import vista.V_interfaz;
import vista.V_rastreo;
import vista.V_tabs;

/**
 *
 * @author herma
 */
public class C_automata extends MouseAdapter implements ActionListener {

    private boolean rastreoActivo = false;

    private V_lienzo lienzo;
    private V_rastreo rastreo;
    private List<M_estado> estados;
    private List<M_transicion> transiciones;
    private V_popupmenu menu;
    private Control ctrlVersion;

    private List estadosElim;
    private int idEstado = 0;
    private int idEstadoElim = 0;
    private Point clic = null, origen = null, destino = null;
    private int idOrigen = 0;
    private String alfabeto[] = {"0", "1", "0,1"};
    private int version = 0;

    //auxiliares
    private M_estado nodo;
    private int indiceNodo = 0;
    private boolean cambios = false;

    //constantes
    private static final int CUADRADO = 8;
    private final String ACEPTACION = "Edo-Aceptacion";
    private final String TRANSICION = "Edo-Transicion";
    private final String INICIAL = "Edo-Inicial";
    private final String SIMPLE = "simple";
    private final String ARCO = "arco";

    public C_automata() {
    }

    public C_automata(V_lienzo dibujar, V_rastreo rastreo) {
        this.lienzo = dibujar;
        this.rastreo = rastreo;
        estados = new ArrayList();
        estadosElim = new ArrayList();
        transiciones = new ArrayList();
        menu = new V_popupmenu(dibujar, this);
        ctrlVersion = new Control();

    }

    public boolean getRastreoActivo() {
        return rastreoActivo;
    }

    public void setRastreoActivo(boolean a) {
        this.rastreoActivo = a;
    }

    /**
     * Indica que se han producido cambios en el lienzo
     *
     * @return TRUE si hay cambios, FALSE en caso contrario
     */
    public boolean isCambios() {
        return cambios;
    }

    /**
     * Indicar cuando se han realizado cambios en el lienzo
     *
     * @param cambios TRUE si hay cambios, FALSE en caso contrario
     */
    public void setCambios(boolean cambios) {
        this.cambios = cambios;
    }

    /**
     * Estados eliminados por el usuario
     *
     * @return List"<<dd>> de estados eliminados
     */
    public List getEstadosElim() {
        return estadosElim;
    }

    /**
     * Modifica los estados eliminados por el usuario
     *
     * @param estadosElim List de estados
     */
    public void setEstadosElim(List estadosElim) {
        this.estadosElim = estadosElim;
    }

    /**
     *
     * @return version del objeto (ctrl z)
     */
    public int getVersion() {
        return this.version;
    }

    /**
     * Modifica la versión del objeto (ctrl z)
     *
     * @param version version del objeto
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     *
     * @return el id del estado actual
     */
    public int getIdEstado() {
        return idEstado;
    }

    /**
     * Modifica el id del estado actual
     *
     * @param idEstado nuevo id
     */
    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    /**
     * Obtiene todos los estados
     *
     * @return List estados
     */
    public List<M_estado> getEstados() {
        return estados;
    }

    /**
     * Obtiene todos las transiciones
     *
     * @return List transiciones
     */
    public List<M_transicion> getTransiciones() {
        return transiciones;
    }

    /**
     * Modifica el List de transiciones
     *
     * @param t nuevo List
     */
    public void setTransiciones(List<M_transicion> t) {
        transiciones = t;
    }

    /**
     * Modifica el List de estados
     *
     * @param e nuevo List
     */
    public void setEstados(List<M_estado> e) {
        estados = e;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        switch (e.getButton()) {
            case BUTTON1:
                if (C_slideMenu.estados) {
                    if (!rastreoActivo) {
                        ObjetoDeshacer();
                        //Incluye lógica de recuperacion para nodos eliminados
                        if (idEstado == 0 && estados.size() > 0) {
                            List temp = new ArrayList();
                            for (M_estado edo : estados) {
                                temp.add(edo.getIdEstado());
                            }
                            new Ordenador().quicksort(temp);
                            int[] array = new int[(int) temp.get(temp.size() - 1) + 1];
                            for (Object obj : temp) {
                                array[(int) obj] = (int) obj;
                            }

                            for (int i = 1; i < array.length; i++) {
                                if (array[i] == 0) {
                                    estadosElim.add(i);
                                }
                            }

                            idEstado = (int) temp.get(temp.size() - 1) + 1;
                        }

                        if (idEstado == 0) {
                            estados.add(new M_estado(e.getX(), e.getY(),
                                    idEstado, INICIAL));
                            lienzo.repaint();
                            setCambios(true);
                            idEstado++;
                        } else if (estadosElim.size() > 0) {
                            if (idEstadoElim == 0) {
                                idEstadoElim = idEstado;
                            }
                            idEstado = (int) estadosElim.get(0);
                            estados.add(new M_estado(e.getX(), e.getY(),
                                    idEstado, TRANSICION));
                            estadosElim.remove(0);
                        } else {
                            if (idEstadoElim != 0) {
                                idEstado = idEstadoElim;
                                idEstadoElim = 0;
                            }
                            estados.add(new M_estado(e.getX(), e.getY(),
                                    idEstado, TRANSICION));
                            idEstado++;
                        }
                        lienzo.repaint();
                        setCambios(true);
                    } else {
                        JOptionPane.showMessageDialog(null, RASTREANDO,
                                "Editar Diagrama", INFORMATION_MESSAGE);
                    }
                } else if (C_slideMenu.transicion) {
                    //iniciar una transicion
                    clic = new Point(e.getX(), e.getY());
                    for (M_estado edo : estados) {
                        if (estadoSeleccionado(edo, clic)) {
                            if (!rastreoActivo) {
                                origen = new Point(edo.getX(), edo.getY());
                                idOrigen = edo.getIdEstado();
                                lienzo.setInicioLinea(origen);
                                break;

                            } else {
                                JOptionPane.showMessageDialog(null, RASTREANDO,
                                        "Editar Diagrama", INFORMATION_MESSAGE);
                            }
                        }
                    }
                } else if (C_slideMenu.seleccionar) {
                    //seleccionar un nodo
                    clic = new Point(e.getX(), e.getY());
                    int indice = 0;
                    for (M_estado estado : estados) {
                        if (estadoSeleccionado(estado, clic)) {
                            ObjetoDeshacer();
                            nodo = estado;
                            indiceNodo = indice;
                            break;
                        }
                        indice++;
                    }
                }
                break;
            case BUTTON3:
                if (!rastreoActivo) {
                    //desplegar popMenu
                    menu.setVisible(true);
                    menu.setLocation(e.getXOnScreen(), e.getYOnScreen());
                    clic = new Point(e.getX(), e.getY());
                    for (M_estado edo : estados) {
                        if (estadoSeleccionado(edo, clic)) {
                            if (edo.getTipo().equals(ACEPTACION)) {
                                menu.setState(true);
                                break;
                            } else {
                                menu.setState(false);
                                break;
                            }
                        } else {
                            menu.setState(false);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        if (C_slideMenu.transicion) {
            //continua la trayectoria de la transicion iniciada
            if (origen != null) {
                destino = new Point(e.getX(), e.getY());
                lienzo.setFinLinea(destino);
                lienzo.repaint();
                setCambios(true);
            }
        } else if (C_slideMenu.seleccionar) {
            if (nodo != null) {
                int x = e.getX();
                int y = e.getY();
                if (x <= 25) {
                    x = 25;
                } else if (x >= lienzo.getWidth() - 25) {
                    x = lienzo.getWidth() - 25;
                }
                if (y <= 25) {
                    y = 25;
                } else if (y >= lienzo.getHeight() - 25) {
                    y = lienzo.getHeight() - 25;
                }

                //mover transiciones
                int indice = 0;
                int tope = transiciones.size() - 1;
                for (;;) {
                    if (indice > tope) {
                        break;
                    }
                    M_transicion temp = transiciones.get(indice);
                    if (nodo.getIdEstado() == temp.getOrigen()) {
                        temp.setXa(x);
                        temp.setYa(y);
                        transiciones.set(indice, temp);
                    }
                    if (nodo.getIdEstado() == temp.getDestino()) {
                        temp.setXb(x);
                        temp.setYb(y);
                        transiciones.set(indice, temp);
                    }
                    if ((nodo.getIdEstado() == temp.getDestino())
                            && (nodo.getIdEstado() == temp.getOrigen())) {
                        temp.setXa(x);
                        temp.setYa(y);
                        temp.setXb(x);
                        temp.setYb(y);
                        transiciones.set(indice, temp);
                    }
                    indice++;
                    lienzo.repaint();
                    setCambios(true);
                }

                //nodo en nueva posicion
                estados.set(indiceNodo, new M_estado(x, y, nodo.getIdEstado(),
                        nodo.getTipo()));
            }
            lienzo.repaint();
            setCambios(true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        if (BUTTON1 == e.getButton()) {
            if (!rastreo.isVisible()) {
                if (C_slideMenu.transicion) {
                    //finalizar y dibujar la transicion
                    if (origen != null && destino != null) {

                        for (M_estado edo : estados) {
                            if (estadoSeleccionado(edo, destino)
                                    && idOrigen != edo.getIdEstado()) {
                                crearTransicion(origen.x, origen.y, edo.getX(),
                                        edo.getY(), SIMPLE, idOrigen, edo.getIdEstado());
                                origen = null;
                                destino = null;
                                break;
                            } else if (estadoSeleccionado(edo, destino)
                                    && idOrigen == edo.getIdEstado()) {
                                crearTransicion(origen.x, origen.y, edo.getX(),
                                        edo.getY(), ARCO, idOrigen, edo.getIdEstado());
                                origen = null;
                                destino = null;
                                break;
                            }
                        }
                        lienzo.setInicioLinea(null);
                        lienzo.setFinLinea(null);
                        lienzo.repaint();
                        setCambios(true);
                    }

                } else if (C_slideMenu.seleccionar) {
                    //reinicio de auxiliares
                    nodo = null;
                    indiceNodo = -1;
                }
            }

            Component c = (Component) e.getSource();
            switch (c.getName()) {
                case TABLACADENAS:
                    cadena = rastreo.getCadena();
                    estatusCadena = rastreo.getEstatus();
                    rastreo.setCadena(cadena);
                    rastreo.setFin(false);
                    rastreo.getPanel().repaint();
                    break;
                case SLIDER:
                    switch (rastreo.getVelocidad()) {
                        case 0://lento
                            intervalo = 3200;
                            break;
                        case 1://normal
                            intervalo = 1600;
                            break;
                        case 2://rapido
                            intervalo = 800;
                            break;
                    }
                    break;
            }

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Component componente = (Component) e.getSource();
        int indice;
        int id;
        int coordX, coordY, ancho, alto;
        if (!rastreoActivo) {
            switch (componente.getName()) {
                case ESTADO_ACEP:
                    indice = 0;
                    id = 0;
                    for (M_estado estado : estados) {
                        if (estadoSeleccionado(estado, clic)) {
                            ObjetoDeshacer();
                            nodo = estado;
                            if (nodo.getTipo().contentEquals(TRANSICION)
                                    || nodo.getTipo().contentEquals(INICIAL)) {
                                nodo.setTipo(ACEPTACION);
                                estados.set(indice, nodo);
                                nodo = null;
                            } else {
                                nodo.setTipo(TRANSICION);
                                estados.set(indice, nodo);
                                nodo = null;
                            }
                            lienzo.repaint();
                            setCambios(true);
                            break;
                        }
                        indice++;
                    }
                    menu.setVisible(false);

                    break;
                case EDITAR_TRANS:
                    coordX = clic.x - CUADRADO / 2;
                    coordY = clic.y - CUADRADO / 2;
                    ancho = CUADRADO;
                    alto = CUADRADO;
                    for (M_transicion trans : transiciones) {
                        if (trans.getMascara().intersects(coordX, coordY, ancho, alto)) {
                            String alfa = (String) JOptionPane.showInputDialog(lienzo,
                                    "Seleccionar", "Alfabeto",
                                    JOptionPane.QUESTION_MESSAGE, null,
                                    alfabeto, alfabeto[0]);
                            if (alfa != null) {
                                ObjetoDeshacer();
                                trans.setAlfabeto(alfa);
                            }
                            lienzo.repaint();
                            setCambios(true);
                            break;
                        }
                    }
                    menu.setVisible(false);

                    break;
                case ELIM_ESTADO:
                    indice = 0;
                    id = 0;
                    for (M_estado estado : estados) {
                        id = estado.getIdEstado();
                        if (estadoSeleccionado(estado, clic) && id != 0) {
                            ObjetoDeshacer();
                            estadosElim.add(id);
                            new Ordenador().quicksort(estadosElim);
                            estados.remove(indice);

                            ///agregado
                            if (idEstado == 0 && estados.size() > 0) {
                                List temp = new ArrayList();
                                for (M_estado edo : estados) {
                                    temp.add(edo.getIdEstado());
                                }
                                new Ordenador().quicksort(temp);
                                int[] array = new int[(int) temp.get(temp.size() - 1) + 1];
                                for (Object obj : temp) {
                                    array[(int) obj] = (int) obj;
                                }

                                for (int i = 1; i < array.length; i++) {
                                    if (array[i] == 0) {
                                        estadosElim.add(i);
                                    }
                                }

                                idEstado = (int) temp.get(temp.size() - 1) + 1;
                            }

                            /////
                            indice = 0;
                            for (;;) {
                                int tope = transiciones.size() - 1;
                                if (indice > tope) {
                                    break;
                                }
                                M_transicion temp = transiciones.get(indice);
                                if (id == temp.getOrigen()) {
                                    transiciones.remove(indice);
                                    indice = 0;
                                } else if (id == temp.getDestino()) {
                                    transiciones.remove(indice);
                                    indice = 0;
                                } else {
                                    indice++;
                                }
                            }
                            lienzo.repaint();

                            break;
                        }
                        indice++;
                    }
                    menu.setVisible(false);
                    break;
                case ELIM_TRANS:
                    coordX = clic.x - CUADRADO / 2;
                    coordY = clic.y - CUADRADO / 2;
                    ancho = CUADRADO;
                    alto = CUADRADO;
                    for (M_transicion trans : transiciones) {
                        if (trans.getMascara().intersects(coordX, coordY, ancho, alto)) {
                            ObjetoDeshacer();
                            transiciones.remove(trans);
                            lienzo.repaint();
                            setCambios(true);
                            break;
                        }
                    }
                    menu.setVisible(false);
                    break;
            }
        } else {
            switch (componente.getName()) {
                case AFD:
                    guardarAFNDtoAFD();
                    break;
                case ORDENAR:
                    ordenarCadenas();
                    break;
                case PASO_A_PASO:
                    ExecutorService exe = Executors.newSingleThreadExecutor();
                    exe.submit(new Rastreador(cadena, lienzo, rastreo,
                            model_TransicionGeneral, estatusCadena, intervalo, alfabetoFinal));
                    break;
                default:
                    JOptionPane.showMessageDialog(null, RASTREANDO, "Editar Diagrama",
                            JOptionPane.INFORMATION_MESSAGE);
                    break;
            }

        }

    }

    /**
     * Verificar que se haya seleccionado un estado
     *
     * @param estado objeto de tipo M_estado
     * @param cursor punto donde se genero el clic
     * @return True si seleccionamos un estado, False en caso contrario
     */
    private boolean estadoSeleccionado(M_estado estado, Point cursor) {
        return new Rectangle(estado.getX() - lienzo.getDiametro() / 2,
                estado.getY() - lienzo.getDiametro() / 2, lienzo.getDiametro(),
                lienzo.getDiametro()).contains(cursor);
    }

    /**
     * Crea la transicion entre un nodo y otro
     *
     * @param x1 coordenada "x" del nodo origen
     * @param y1 coordenada "y" del nodo origen
     * @param x2 coordenada "x" del nodo destino
     * @param y2 coordenada "y" del nodo destino
     * @param tipo SIMPLE o ARCO
     * @param origen id del nodo origen
     * @param destino id del nodo destino
     */
    private void crearTransicion(int x1, int y1, int x2, int y2, String tipo,
            int origen, int destino) {
        String alfa = (String) JOptionPane.showInputDialog(null,
                "Seleccionar", "Alfabeto",
                JOptionPane.QUESTION_MESSAGE, null,
                alfabeto, alfabeto[0]);

        boolean existe = false;

        if (transiciones.isEmpty()) {
            if (alfa != null) {
                ObjetoDeshacer();
                transiciones.add(new M_transicion(x1, y1, x2, y2, tipo,
                        origen, destino, alfa));
            }
        } else {
            if (alfa != null) {
                for (M_transicion t : transiciones) {
                    if (origen == t.getOrigen() && destino == t.getDestino()) {
                        ObjetoDeshacer();
                        t.setAlfabeto(alfa);
                        existe = true;
                        break;
                    }
                }
            }

            if (alfa != null) {
                if (!existe) {
                    ObjetoDeshacer();
                    transiciones.add(new M_transicion(x1, y1, x2, y2, tipo,
                            origen, destino, alfa));
                }
            }
        }

        lienzo.setInicioLinea(null);
        lienzo.setFinLinea(null);
        lienzo.repaint();
        setCambios(true);
    }

    /**
     * Crea la version anterior del estado actual del lienzo
     */
    private void ObjetoDeshacer() {
        String vs;
        ctrlVersion.crearObjetoUndo(lienzo.getName(),
                getTransiciones(), getEstados(), getEstadosElim(), version);
        version++;
        vs = ctrlVersion.getVersionRedo(lienzo.getName());
        if (vs != null && vs.endsWith("bin")) {
            ctrlVersion.clearDirRedo(lienzo.getName());
            V_interfaz.b_rehacer.setEnabled(false);
            V_interfaz.rehacer.setEnabled(false);
        }
        V_interfaz.b_guardar.setEnabled(true);
        V_interfaz.guardar.setEnabled(true);
        V_interfaz.b_deshacer.setEnabled(true);
        V_interfaz.deshacer.setEnabled(true);
        vs = "";
    }

    /*
    COMIENZA LOGICA DEL CONTROL DE RASTREO
     */
    private final String RUTA = System.getProperty("user.dir") + "/archivos/cadenasRastreo.txt";
    private final String RUTA0 = System.getProperty("user.dir") + "/archivos/cero.txt";
    private final String RUTA1 = System.getProperty("user.dir") + "/archivos/uno.txt";
    private V_tabs tabs;
    private DefaultTableModel model_TransicionGeneral, model_CADENAS, model_AFND;
    private final int EDOACEP = 1;
    private final int EDONOACEP = 0;
    private ArrayList<String> valido;
    private ArrayList<String> nvalido;
    private String cadena = "";
    private String estatusCadena = "";
    private boolean btn_order = true;
    private String alfabetoFinal = "";
    long intervalo = 1600;
    Random aleatorio = new Random(System.currentTimeMillis());

    public String getEstatusCadena(){
        return estatusCadena;
    }
    
    public void rastrear() {
        btn_order = true;
        valido = new ArrayList();
        nvalido = new ArrayList();

        String tipoAutomata = diagramaValido();

        //Eliminar "," alfabeto
        if (alfabetoFinal.length() > 1) {
            alfabetoFinal = alfabetoFinal.replace(",", "");
        }

        switch (tipoAutomata) {
            case AFD:
                model_TransicionGeneral = tipoTableModel();
                tablaTransiciones();
                try {
                    llenarTablaTransiciones(model_TransicionGeneral);
                } catch (IOException e) {
                }
                rastreoActivo = true;
                rastreo.setModelTC(model_CADENAS);
                rastreo.setModelTT(model_TransicionGeneral);
                rastreo.setVisible(true);
                rastreo.getB_Afd().setVisible(false);
                break;
            case AFND:
                model_TransicionGeneral = tipoTableModel();
                model_AFND = tipoTableModel();
                tablaTransiciones();

                if (alfabetoFinal.equals("01")) {
                    tablaSubConjuntos2();
                    try {
                        llenarTablaTransiciones(model_AFND);
                    } catch (IOException e) {
                    }
                    rastreoActivo = true;
                    rastreo.setModelTC(model_CADENAS);
                    rastreo.setModelTT(model_TransicionGeneral);
                    rastreo.setVisible(true);
                    rastreo.getB_Afd().setVisible(true);
                } else {
                    tablaSubConjuntos1();
                    try {
                        llenarTablaTransiciones(model_AFND);
                    } catch (IOException e) {
                    }
                    rastreoActivo = true;
                    rastreo.setModelTC(model_CADENAS);
                    rastreo.setModelTT(model_TransicionGeneral);
                    rastreo.setVisible(true);
                    rastreo.getB_Afd().setVisible(true);
                }
                break;
            case RETORNAR:
                JOptionPane.showMessageDialog(null, SIN_TRANS,
                        "Error en el diagramado", JOptionPane.ERROR_MESSAGE);
                break;
            case DESCONECTADOS:
                JOptionPane.showMessageDialog(null, SIN_CONEXION,
                        "Error en el diagramado", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }

    /**
     * Construye el modelo de la tabla de transiciones segun el alfabeto
     *
     * @return modelo de la tabla de transiciones
     */
    private DefaultTableModel tipoTableModel() {
        DefaultTableModel tabla_AF = null;

        switch (alfabetoFinal) {
            case "0":
                tabla_AF = new DefaultTableModel(new Object[][]{},
                        new String[]{
                            "ESTADO", "Σ={0}", "FINAL"
                        }) {
                    boolean[] canEdit = new boolean[]{
                        false, false, false
                    };

                    @Override
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return canEdit[columnIndex];
                    }
                };

                break;
            case "1":
                tabla_AF = new DefaultTableModel(new Object[][]{},
                        new String[]{
                            "ESTADO", "Σ={1}", "FINAL"
                        }) {
                    boolean[] canEdit = new boolean[]{
                        false, false, false
                    };

                    @Override
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return canEdit[columnIndex];
                    }
                };

                break;
            case "01":
                tabla_AF = new DefaultTableModel(new Object[][]{},
                        new String[]{
                            "ESTADO", "Σ={0}", "Σ={1}", "FINAL"
                        }) {
                    boolean[] canEdit = new boolean[]{
                        false, false, false, false
                    };

                    @Override
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return canEdit[columnIndex];
                    }
                };
                break;
        }
        return tabla_AF;
    }

    /*
            __col0__col1_col2_col3__
            ESTADO |  0 |  1 | FINAl
            ------------------------
       fil0 |  q0  | qn | q0 |   1
            ------------------------
       filn |  qn  | qn | qn |   0
     */
    /**
     *
     */
    private void tablaTransiciones() {
        List<M_estado> estados = getEstados();
        List<M_transicion> transiciones = getTransiciones();
        Object[] object;
        ///Adecuando segun el alfabeto
        if (alfabetoFinal.equals("01")) {
            object = new Object[4];
        } else {
            object = new Object[3];
        }

        int fil, col;

        //crear tabla vacia
        for (int i = 0; i < getEstados().size(); i++) {
            model_TransicionGeneral.addRow(object);
        }

        fil = 0;
        col = 0;
        //obtener id de estados y agregar en la tabla
        for (M_estado edo : estados) {
            model_TransicionGeneral.setValueAt(edo.getIdEstado(), fil, col);
            fil++;
        }

        fil = 0;
        ///Adecuando segun el alfabeto
        if (alfabetoFinal.equals("01")) {
            col = 3;
        } else {
            col = 2;
        }

        //obtener estados de aceptacion y agregar a la tabla
        for (M_estado edo : estados) {
            if (edo.getTipo().contentEquals(ACEPTACION)) {
                model_TransicionGeneral.setValueAt(EDOACEP, fil, col);
                fil++;
            } else {
                model_TransicionGeneral.setValueAt(EDONOACEP, fil, col);
                fil++;
            }
        }

        //obtener transiciones y agregar a la tabla
        String aux;
        for (int i = 0; i < getEstados().size(); i++) {
            int id = (int) model_TransicionGeneral.getValueAt(i, 0);

            for (M_transicion temp : transiciones) {
                if (id == temp.getOrigen()) {
                    if (temp.getAlfabeto().equals("0")) {

                        if (model_TransicionGeneral.getValueAt(i, 1) == null) {
                            model_TransicionGeneral.setValueAt(temp.getDestino(), i, 1);
                        } else {
                            aux = model_TransicionGeneral.getValueAt(i, 1) + "," + temp.getDestino();
                            model_TransicionGeneral.setValueAt(aux, i, 1);
                        }

                    } else if (temp.getAlfabeto().equals("1")) {

                        //Adecuación por alfabeto 
                        if (alfabetoFinal.equals("01")) {
                            if (model_TransicionGeneral.getValueAt(i, 2) == null) {
                                model_TransicionGeneral.setValueAt(temp.getDestino(), i, 2);
                            } else {
                                aux = model_TransicionGeneral.getValueAt(i, 2) + "," + temp.getDestino();
                                model_TransicionGeneral.setValueAt(aux, i, 2);
                            }
                        } else {
                            if (model_TransicionGeneral.getValueAt(i, 1) == null) {
                                model_TransicionGeneral.setValueAt(temp.getDestino(), i, 1);
                            } else {
                                aux = model_TransicionGeneral.getValueAt(i, 1) + "," + temp.getDestino();
                                model_TransicionGeneral.setValueAt(aux, i, 1);
                            }
                        }

                    } else if (temp.getAlfabeto().equals("0,1")) {

                        if (model_TransicionGeneral.getValueAt(i, 1) == null) {
                            model_TransicionGeneral.setValueAt(temp.getDestino(), i, 1);
                        } else {
                            aux = model_TransicionGeneral.getValueAt(i, 1) + "," + temp.getDestino();
                            model_TransicionGeneral.setValueAt(aux, i, 1);
                        }

                        if (model_TransicionGeneral.getValueAt(i, 2) == null) {
                            model_TransicionGeneral.setValueAt(temp.getDestino(), i, 2);
                        } else {
                            aux = model_TransicionGeneral.getValueAt(i, 2) + "," + temp.getDestino();
                            model_TransicionGeneral.setValueAt(aux, i, 2);
                        }
                    }

                }//FIN IF ID
            }//FIN FOR TRANSICION
        }//FIN FOR ESTADOS

        for (int i = 0; i < model_TransicionGeneral.getRowCount(); i++) {
            if (model_TransicionGeneral.getValueAt(i, 1) == null) {
                model_TransicionGeneral.setValueAt("Ø", i, 1);
            }
            if (alfabetoFinal.equals("01")) {
                if (model_TransicionGeneral.getValueAt(i, 2) == null) {
                    model_TransicionGeneral.setValueAt("Ø", i, 2);
                }
            }

        }
    }

    private void llenarTablaTransiciones(DefaultTableModel model) throws FileNotFoundException, IOException {
        model_CADENAS = new DefaultTableModel(new Object[][]{},
                new String[]{
                    "ACEPTA", "NO ACEPTA"
                }) {
            boolean[] canEdit = new boolean[]{
                false, false
            };

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };

        Object[] object = new Object[2];
        File file;
        if (alfabetoFinal.equals("1")) {
            file = new File(RUTA1);
        } else if (alfabetoFinal.equals("0")) {
            file = new File(RUTA0);
        } else {
            file = new File(RUTA);
        }
        FileReader fileR;
        BufferedReader file2;
        int row03;

        if (alfabetoFinal.equals("01")) {
            row03 = (int) model.getValueAt(0, 3);
        } else {
            row03 = (int) model.getValueAt(0, 2);
        }

        for (int i = 0; i < 25; i++) {
            model_CADENAS.addRow(object);
        }

        try {
            fileR = new FileReader(file);
            file2 = new BufferedReader(fileR);

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "No encontramos el archivo de lectura"
                    + file.getName(), "Atención!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {

            //Condicion para validar epsilon
            if (row03 == 1) {
                model_CADENAS.setValueAt("Épsilon", 0, 0);
            } else {
                model_CADENAS.setValueAt("Épsilon", 0, 1);
            }
            //Fin de condición

            String lineas;
            while ((lineas = file2.readLine()) != null) {
                analizarCadenas(lineas, model);
            }
            if (!valido.isEmpty()) {
                if (valido.size() < 24) {
                    ArrayList<String> aux = new ArrayList();
                    aux = desordenar(valido);
                    int h = 1;
                    for (int i = 0; i < valido.size(); i++) {
                        model_CADENAS.setValueAt(aux.get(i), h, 0);
                        h++;
                    }
                } else {
                    ArrayList<String> aux = new ArrayList();
                    aux = desordenar(valido);
                    int h = 1;
                    for (int i = 0; i < 24; i++) {
                        model_CADENAS.setValueAt(aux.get(i), h, 0);
                        h++;
                    }
                }

            }

            if (!nvalido.isEmpty()) {
                if (nvalido.size() < 24) {
                    ArrayList<String> aux = new ArrayList();
                    aux = desordenar(nvalido);
                    int j = 1;
                    for (int i = 0; i < aux.size(); i++) {
                        model_CADENAS.setValueAt(aux.get(i), j, 1);
                        j++;
                    }
                } else {
                    ArrayList<String> aux = new ArrayList();
                    aux = desordenar(nvalido);
                    int j = 1;
                    for (int i = 0; i < 24; i++) {
                        model_CADENAS.setValueAt(aux.get(i), j, 1);
                        j++;
                    }
                }
            }
        } catch (IOException e) {
        }

    }

    public void analizarCadenas(String cad, DefaultTableModel model) throws IOException {

        cad = cad + "B";
        int edo = 0;

        switch (alfabetoFinal) {
            case "01":
                for (int n = 0; n < cad.length(); n++) {
                    char c = cad.charAt(n);
                    if (c == '0') {
                        edo = (int) model.getValueAt(edo, 1);
                    } else if (c == '1') {
                        edo = (int) model.getValueAt(edo, 2);
                    }
                    if (c == 'B') {
                        int eval = (int) model.getValueAt(edo, 3);
                        if (eval == 1) {
                            cad = cad.substring(0, cad.length() - 1);
                            valido.add(cad);
                        } else {
                            cad = cad.substring(0, cad.length() - 1);
                            nvalido.add(cad);
                        }
                        break;
                    }
                }
                break;

            case "0":
                for (int n = 0; n < cad.length(); n++) {
                    char c = cad.charAt(n);
                    if (c == '0') {
                        edo = (int) model.getValueAt(edo, 1);
                    } else if (c == '1') {
                        cad = cad.substring(0, cad.length() - 1);
                        nvalido.add(cad);
                        break;
                    }
                    if (c == 'B') {
                        int eval = (int) model.getValueAt(edo, 2);
                        if (eval == 1) {
                            cad = cad.substring(0, cad.length() - 1);
                            valido.add(cad);
                        } else {
                            cad = cad.substring(0, cad.length() - 1);
                            nvalido.add(cad);
                        }
                        break;
                    }
                }
                break;

            case "1":
                for (int n = 0; n < cad.length(); n++) {
                    char c = cad.charAt(n);
                    if (c == '1') {
                        edo = (int) model.getValueAt(edo, 1);
                    } else if (c == '0') {
                        cad = cad.substring(0, cad.length() - 1);
                        nvalido.add(cad);
                        break;
                    }
                    if (c == 'B') {
                        int eval = (int) model.getValueAt(edo, 2);
                        if (eval == 1) {
                            cad = cad.substring(0, cad.length() - 1);
                            valido.add(cad);
                        } else {
                            cad = cad.substring(0, cad.length() - 1);
                            nvalido.add(cad);
                        }
                        break;
                    }
                }
                break;
        }

    }

    private ArrayList desordenar(ArrayList<String> arreglo) {
        ArrayList<String> datos = new ArrayList<>();
        datos.addAll(arreglo);
        //Crea un ArrayList a partir del que conteniendo los valores originales.
        ArrayList<String> va = new ArrayList<>(datos);
        //Permuta aleatoriamente la lista.
        Collections.shuffle(va);
        //Limpiar para almacenar los datos ordenados aleatoriamente.
        datos.clear();
        //Agrega los valores ordenados alatoriamente.
        datos.addAll(va);
        return datos;
    }

    private void guardarAFNDtoAFD() {
        File file = null;
        JFileChooser seleccionar = new JFileChooser();
        Inicio toAFD;

        seleccionar.addChoosableFileFilter(new FileNameExtensionFilter("JLEFO (*.jlefo)", "jlefo"));
        seleccionar.setAcceptAllFileFilterUsed(false);

        ///Obner nombre de la pestaÃ±a para colocarlo en: Nombre de Mod_archivo
        seleccionar = Archivo.carpetaJLEFOConversion(seleccionar);
        seleccionar.setSelectedFile(
                new File("AFND-TO-AFD.jlefo"));

        if (seleccionar.showSaveDialog(
                null) == JFileChooser.APPROVE_OPTION) {
            file = seleccionar.getSelectedFile();

            //Comprobando ExtensiÃ³n .jlefo
            if (file.toString().endsWith(".jlefo") == false) {
                file = new File(file + ".jlefo");
            }

            //Validar si el archivo existe
            if (file.exists() == true) {
                //Validando cuando le da en la X de la pestaÃ±a
                int resp = JOptionPane.showConfirmDialog(seleccionar, "El archivo " + file.getName() + " ya existe.\n" + "Desea remplazarlo",
                        "Guardar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (JOptionPane.OK_OPTION == resp) {
                    if (alfabetoFinal.equals("01")) {
                        Archivo.guardarConversiontoAFD(model_AFND, file.toString());
                    } else {
                        Archivo.guardarConversiontoAFD1(model_AFND, file.toString(), alfabetoFinal);
                    }

                } else {
                    guardarAFNDtoAFD();
                }

            } else {
                //Si el archivo no existe se manda a Guardar
                if (alfabetoFinal.equals("01")) {
                    Archivo.guardarConversiontoAFD(model_AFND, file.toString());
                } else {
                    Archivo.guardarConversiontoAFD1(model_AFND, file.toString(), alfabetoFinal);
                }
            }
        }
    }

    private void ordenarCadenas() {
        if (btn_order) {
            //ordena cadenas validas

            if (!valido.isEmpty()) {
                if (valido.size() > 25) {
                    int j = 1;
                    for (int i = 0; i < 24; i++) {
                        model_CADENAS.setValueAt(valido.get(i), j, 0);
                        j++;
                    }
                } else {
                    int j = 1;
                    for (int i = 0; i < valido.size(); i++) {
                        model_CADENAS.setValueAt(valido.get(i), j, 0);
                        j++;
                    }
                }
                rastreo.setModelTC(model_CADENAS);
            }

            //ordena cadenas no validas 
            if (!nvalido.isEmpty()) {
                if (nvalido.size() > 25) {
                    int k = 1;
                    for (int i = 0; i < 24; i++) {
                        model_CADENAS.setValueAt(nvalido.get(i), k, 1);
                        k++;
                    }
                } else {
                    int k = 1;
                    for (int i = 0; i < nvalido.size(); i++) {
                        model_CADENAS.setValueAt(nvalido.get(i), k, 1);
                        k++;
                    }
                }
                rastreo.setModelTC(model_CADENAS);
            }

            btn_order = false;
            rastreo.getB_Ordenar_cadenas().setText("Desordenar");
            rastreo.getB_Ordenar_cadenas().setIcon(new ImageIcon(getClass()
                    .getResource("/img_icon/aleatorio-16.png")));
        } else {
            //Desordena cadenas validas
            if (!valido.isEmpty()) {
                if (valido.size() < 24) {

                    ArrayList<String> aux = new ArrayList();
                    aux = desordenar(valido);
                    int h = 1;
                    for (int i = 0; i < valido.size(); i++) {
                        model_CADENAS.setValueAt(aux.get(i), h, 0);
                        h++;
                    }
                    rastreo.setModelTC(model_CADENAS);
                } else {

                    ArrayList<String> aux = new ArrayList();
                    aux = desordenar(valido);
                    int h = 1;
                    for (int i = 0; i < 24; i++) {
                        model_CADENAS.setValueAt(aux.get(i), h, 0);
                        h++;
                    }
                    rastreo.setModelTC(model_CADENAS);
                }
            }
            //Desordena cadenas no validas 
            if (!nvalido.isEmpty()) {
                if (nvalido.size() < 24) {

                    ArrayList<String> aux = new ArrayList();
                    aux = desordenar(nvalido);
                    int h = 1;
                    for (int i = 0; i < aux.size(); i++) {
                        model_CADENAS.setValueAt(aux.get(i), h, 1);
                        h++;
                    }
                    rastreo.setModelTC(model_CADENAS);
                } else {
                    ArrayList<String> aux = new ArrayList();
                    aux = desordenar(nvalido);
                    int h = 1;
                    for (int i = 0; i < 24; i++) {
                        model_CADENAS.setValueAt(aux.get(i), h, 1);
                        h++;
                    }
                    rastreo.setModelTC(model_CADENAS);
                }
            }

            btn_order = true;
            rastreo.getB_Ordenar_cadenas().setText("Ordenar");
            rastreo.getB_Ordenar_cadenas().setIcon(new ImageIcon(getClass()
                    .getResource("/img_icon/ordenar-16.png")));
        }
    }

    //Tabla que almacena los Subconjuntos de un AFN
    private void tablaSubConjuntos2() {
        List<M_estado> estados = getEstados();
        List<M_transicion> transiciones = getTransiciones();
        Object[] object = new Object[4];
        List edoactual = new ArrayList<>();//Almacenamos los estados actuales a verificar

        int fil = 0;//NÃºmero de fila creadas: de acuerdo a los nuevos subconjuntos 
        int corfila = 0;//NÃºmero de la fila  en la que se debe comprobar los subconjuntos
        boolean col0 = false, col1 = false; //Banderas para conocer si los subconjuntos ya existen Columna1 -- Columna2
        boolean fin = false; //Validamos el final
        boolean bucleExiste = false;
        Object edobucle = "";

        //Inicializamos el arraylist con el estado inicial ---> q0
        edoactual.add(0);

        while (fin != true) {

            //Agregamos una fila
            if (fil == 0) {
                model_AFND.addRow(object);
            }

            //LLenar la nueva fila en la columna de estados, con el nuevo subconjunto
            for (int i = 0; i < edoactual.size(); i++) {
                if (model_AFND.getValueAt(corfila, 0) == null) {
                    model_AFND.setValueAt(edoactual.get(i), corfila, 0);
                } else {
                    model_AFND.setValueAt(model_AFND.getValueAt(corfila, 0) + "," + edoactual.get(i), corfila, 0);
                }
                model_AFND = llenarColTabSub((int) edoactual.get(i), corfila, transiciones, model_AFND);
                aceptacion(estados, (int) edoactual.get(i), corfila);
            }

            //Ordenamos los datos en las columnas
            model_AFND = order(model_AFND, corfila, 0, true);
            model_AFND = order(model_AFND, corfila, 1, true);
            model_AFND = order(model_AFND, corfila, 2, true);

            //Comprueba el valor en la tabla  de la columna del Σ={0}
            for (int i = 0; i < model_AFND.getRowCount(); i++) {
                if (model_AFND.getValueAt(corfila, 1) == null) {
                    col0 = false;
                    break;
                }

                String var1 = "" + model_AFND.getValueAt(corfila, 1);
                String var2 = "" + model_AFND.getValueAt(i, 0);
                if (var1.equals(var2)) {
                    col0 = true;
                    break;
                } else {
                    col0 = false;
                }
            }

            //Comprueba el valor en la tabla  de la columna del Σ={1}
            for (int i = 0; i < model_AFND.getRowCount(); i++) {
                if (model_AFND.getValueAt(corfila, 2) == null) {
                    col1 = false;
                    break;
                }
                String var1 = model_AFND.getValueAt(corfila, 2).toString();
                String var2 = model_AFND.getValueAt(i, 0).toString();
                if (var1.equals(var2)) {
                    col1 = true;
                    break;
                } else {
                    col1 = false;
                }
            }

            int qn = 0;
            //Caso 1
            if (col0 == false && col1 == false) {
                if (model_AFND.getValueAt(corfila, 1) == null && model_AFND.getValueAt(corfila, 2) == null) {
                    if (bucleExiste == true) {
//                        System.out.println("NULL OK:");
                        model_AFND.setValueAt(edobucle, corfila, 1);
                        model_AFND.setValueAt(edobucle, corfila, 2);
                        corfila++;
                    } else {
//                        System.out.println("NULL NO:");
                        qn = fil + 1;
                        edobucle = "q" + qn;
                        model_AFND.setValueAt(edobucle, corfila, 1);
                        model_AFND.setValueAt(edobucle, corfila, 2);
                        corfila++;
                        model_AFND.addRow(object);
                        fil++;
                        model_AFND.setValueAt(edobucle, fil, 0);
                        model_AFND.setValueAt(edobucle, fil, 1);
                        model_AFND.setValueAt(edobucle, fil, 2);
                        model_AFND.setValueAt(0, fil, 3);
                        bucleExiste = true;
                    }

                } else if (model_AFND.getValueAt(corfila, 1) == null) {

                    if (bucleExiste == true) {
//                        System.out.println("NULL 1 OK:");
                        model_AFND.setValueAt(edobucle, corfila, 1);
                        model_AFND.addRow(object);
                        fil++;
                        model_AFND.setValueAt(model_AFND.getValueAt(corfila, 2), fil, 0);
                        corfila++;
                    } else {
//                        System.out.println("NULL 1 NO:");
                        qn = fil + 1;
                        edobucle = "q" + qn;
                        model_AFND.setValueAt(edobucle, corfila, 1);
                        model_AFND.addRow(object);
                        fil++;
                        model_AFND.setValueAt(edobucle, fil, 0);
                        model_AFND.setValueAt(edobucle, fil, 1);
                        model_AFND.setValueAt(edobucle, fil, 2);
                        model_AFND.setValueAt(0, fil, 3);
                        bucleExiste = true;
                        model_AFND.addRow(object);
                        fil++;
                        model_AFND.setValueAt(model_AFND.getValueAt(corfila, 2), fil, 0);
                        corfila++;
                    }
                } else if (model_AFND.getValueAt(corfila, 2) == null) {
                    if (bucleExiste == true) {
//                        System.out.println("NULL 2 OK:");
                        model_AFND.setValueAt(edobucle, corfila, 2);
                        model_AFND.addRow(object);
                        fil++;
                        model_AFND.setValueAt(model_AFND.getValueAt(corfila, 1), fil, 0);
                        corfila++;
                    } else {
//                        System.out.println("NULL 2 NO:");
                        qn = fil + 1;
                        edobucle = "q" + qn;
                        model_AFND.setValueAt(edobucle, corfila, 2);
                        model_AFND.addRow(object);
                        fil++;
                        model_AFND.setValueAt(edobucle, fil, 0);
                        model_AFND.setValueAt(edobucle, fil, 1);
                        model_AFND.setValueAt(edobucle, fil, 2);
                        model_AFND.setValueAt(0, fil, 3);
                        bucleExiste = true;
                        model_AFND.addRow(object);
                        fil++;
                        model_AFND.setValueAt(model_AFND.getValueAt(corfila, 1), fil, 0);
                        corfila++;
                    }

                } else {

                    String var1 = model_AFND.getValueAt(corfila, 1).toString();
                    String var2 = model_AFND.getValueAt(corfila, 2).toString();

                    if (var1.equals(var2)) {
//                        System.out.println("NORMAL FALSE ==:");
                        model_AFND.addRow(object);
                        fil++;
                        model_AFND.setValueAt(model_AFND.getValueAt(corfila, 1), fil, 0);
                        corfila++;
                    } else {
//                        System.out.println("NORMAL FALSE !=:");
                        model_AFND.addRow(object);
                        fil++;
                        model_AFND.setValueAt(model_AFND.getValueAt(corfila, 1), fil, 0);
                        model_AFND.addRow(object);
                        fil++;
                        model_AFND.setValueAt(model_AFND.getValueAt(corfila, 2), fil, 0);
                        corfila++;
                    }
                }
            }
            //Caso 2
            if (col0 == false && col1 == true) {
                if (model_AFND.getValueAt(corfila, 1) == null) {
                    if (bucleExiste == true) {
                        //System.out.println("Caso2:  NULL OK:");
                        model_AFND.setValueAt(edobucle, corfila, 1);
                        corfila++;
                    } else {
                        ///System.out.println("Caso2:  NULL NO:");
                        qn = fil + 1;
                        edobucle = "q" + qn;
                        model_AFND.setValueAt(edobucle, corfila, 1);
                        corfila++;
                        model_AFND.addRow(object);
                        fil++;
                        model_AFND.setValueAt(edobucle, fil, 0);
                        model_AFND.setValueAt(edobucle, fil, 1);
                        model_AFND.setValueAt(edobucle, fil, 2);
                        model_AFND.setValueAt(0, fil, 3);
                        bucleExiste = true;
                    }
                } else {
                    //  System.out.println("Caso2:  NORMAL OK:");
                    model_AFND.addRow(object);
                    fil++;
                    model_AFND.setValueAt(model_AFND.getValueAt(corfila, 1), fil, 0);
                    corfila++;
                }
            }

            //Caso 3
            if (col0 == true && col1 == false) {
                if (model_AFND.getValueAt(corfila, 2) == null) {
                    if (bucleExiste == true) {
                        //  System.out.println("Caso3:  NULL OK:");
                        model_AFND.setValueAt(edobucle, corfila, 2);
                        corfila++;
                    } else {
                        //   System.out.println("Caso3:  NULL NO:");
                        qn = fil + 1;
                        edobucle = "q" + qn;
                        model_AFND.setValueAt(edobucle, corfila, 2);
                        corfila++;
                        model_AFND.addRow(object);
                        fil++;
                        model_AFND.setValueAt(edobucle, fil, 0);
                        model_AFND.setValueAt(edobucle, fil, 1);
                        model_AFND.setValueAt(edobucle, fil, 2);
                        model_AFND.setValueAt(0, fil, 3);
                        bucleExiste = true;
                    }
                } else {
                    ///  System.out.println("Caso3:  NORMAL OK:");
                    model_AFND.addRow(object);
                    fil++;
                    model_AFND.setValueAt(model_AFND.getValueAt(corfila, 2), fil, 0);
                    corfila++;
                }
            }
            //Caso 4
            if (col0 == true && col1 == true) {
                corfila++;
            }

            /*Condición de paro*/
            if (fil >= corfila) {
                if (model_AFND.getValueAt(corfila, 0).equals(edobucle)) {
                    //System.out.println("Estado BUCLE:");
                    corfila++;
                    if (corfila <= fil) {
                        edoactual = nuevoEstado(model_AFND, corfila, 0);
                    } else {
                        //   System.out.println("FIN");
                        fin = true;
                    }
                } else {
                    //  System.out.println("Estado NORMAL OK");
                    edoactual = nuevoEstado(model_AFND, corfila, 0);
                }

            } else {
                // System.out.println("FIN");
                fin = true;
            }

        }
        //Mandamos a convertir
        convertAFNtoAFD();

    }

    private void tablaSubConjuntos1() {
        List<M_estado> estados = getEstados();
        List<M_transicion> transiciones = getTransiciones();
        Object[] object = new Object[4];
        List edoactual = new ArrayList<>();//Almacenamos los estados actuales a verificar

        int fil = 0;//NÃºmero de fila creadas: de acuerdo a los nuevos subconjuntos 
        int corfila = 0;//NÃºmero de la fila  en la que se debe comprobar los subconjuntos
        boolean colAlfabeto = false; // Comprobar si  el nuevo subconjuntos ya existen columna Alfabeto (0 |1)
        boolean fin = false; //Validamos el final
        boolean bucleExiste = false;
        Object edobucle = "";

        //Inicializamos el arraylist con el estado inicial ---> q0
        edoactual.add(0);

        while (fin != true) {

            //Agregamos una fila
            if (fil == 0) {
                model_AFND.addRow(object);
            }

            //LLenar la nueva fila en la columna de estados, con el nuevo subconjunto
            for (int i = 0; i < edoactual.size(); i++) {
                if (model_AFND.getValueAt(corfila, 0) == null) {
                    model_AFND.setValueAt(edoactual.get(i), corfila, 0);
                } else {
                    model_AFND.setValueAt(model_AFND.getValueAt(corfila, 0) + "," + edoactual.get(i), corfila, 0);
                }
                model_AFND = llenarColTabSub((int) edoactual.get(i), corfila, transiciones, model_AFND);
                aceptacion(estados, (int) edoactual.get(i), corfila);
            }

            //Ordenamos los datos en las columnas
            model_AFND = order(model_AFND, corfila, 0, true);
            model_AFND = order(model_AFND, corfila, 1, true);

            //Comprueba el valor en la tabla  de la columna del Σ={0 | 1}
            for (int i = 0; i < model_AFND.getRowCount(); i++) {
                if (model_AFND.getValueAt(corfila, 1) == null) {
                    colAlfabeto = false;
                    break;
                }
                String var1 = "" + model_AFND.getValueAt(corfila, 1);
                String var2 = "" + model_AFND.getValueAt(i, 0);
                if (var1.equals(var2)) {
                    colAlfabeto = true;
                    break;
                } else {
                    colAlfabeto = false;
                }
            }

            int qn = 0;
            //Caso 1
            if (colAlfabeto == false) {
                if (model_AFND.getValueAt(corfila, 1) == null) {
                    if (bucleExiste == true) {
//                        System.out.println("NULL OK:");
                        model_AFND.setValueAt(edobucle, corfila, 1);
                        corfila++;
                    } else {
//                        System.out.println("NULL NO:");
                        qn = fil + 1;
                        edobucle = "q" + qn;
                        model_AFND.setValueAt(edobucle, corfila, 1);
                        corfila++;
                        model_AFND.addRow(object);
                        fil++;
                        model_AFND.setValueAt(edobucle, fil, 0);
                        model_AFND.setValueAt(edobucle, fil, 1);
                        model_AFND.setValueAt(0, fil, 2);
                        bucleExiste = true;
                    }

                } else {
//                    System.out.println("NORMAL FALSE !=:");
                    model_AFND.addRow(object);
                    fil++;
                    model_AFND.setValueAt(model_AFND.getValueAt(corfila, 1), fil, 0);
                    corfila++;
                }
            }

            //Caso 2
            if (colAlfabeto == true) {
                corfila++;
            }


            /*Condición de paro*/
            if (fil >= corfila) {
                if (model_AFND.getValueAt(corfila, 0).equals(edobucle)) {
//                    System.out.println("Estado BUCLE:");
                    corfila++;
                    if (corfila <= fil) {
                        edoactual = nuevoEstado(model_AFND, corfila, 0);
//                        System.out.println("Saltando bucle");
                    } else {
//                        System.out.println("FIN BUCLE");
                        fin = true;
                    }
                } else {
//                    System.out.println("Estado NORMAL OK");
                    edoactual = nuevoEstado(model_AFND, corfila, 0);
                }

            } else {
//                System.out.println("FIN");
                fin = true;
            }

        }
        //Mandamos a convertir
        convertAFNtoAFD();

    }

    public DefaultTableModel order(DefaultTableModel afnd, int corfil, int corcolum, boolean ordenar) {
        HashSet hs = new HashSet();
        ArrayList subestados = new ArrayList<>();
        String aux;
        int x;

        if (afnd.getValueAt(corfil, corcolum) == null) {
            return afnd;
        } else {
            aux = afnd.getValueAt(corfil, corcolum).toString();
        }
        Object[] subconjunto = aux.split(",");
        for (int i = 0; i < subconjunto.length; i++) {
            x = Integer.valueOf((String) subconjunto[i]);
            subestados.add(x);
        }
        if (ordenar == true) {
            new Ordenador().quicksort(subestados);
            hs.addAll(subestados);
            subestados.clear();
            subestados.addAll(hs);

            afnd.setValueAt(null, corfil, corcolum);
            for (int i = 0; i < subestados.size(); i++) {
                if (afnd.getValueAt(corfil, corcolum) == null) {
                    afnd.setValueAt(subestados.get(i), corfil, corcolum);
                } else {
                    afnd.setValueAt(afnd.getValueAt(corfil, corcolum) + "," + subestados.get(i), corfil, corcolum);
                }
            }

        }

        return afnd;
    }

    public ArrayList nuevoEstado(DefaultTableModel afnd, int corfila, int corcolum) {
        ArrayList subestados = new ArrayList<>();
        String aux;
        int x;
        aux = afnd.getValueAt(corfila, corcolum).toString();
        Object[] subconjunto = aux.split(",");
        for (int i = 0; i < subconjunto.length; i++) {
            x = Integer.valueOf((String) subconjunto[i]);
            subestados.add(x);
        }
        return subestados;
    }

    private void aceptacion(List<M_estado> estados, int id, int fil) {

        for (M_estado edo : estados) {
            if (id == edo.getIdEstado()) {
                if (edo.getTipo().contentEquals(ACEPTACION)) {
                    if (alfabetoFinal.equals("01")) {
                        model_AFND.setValueAt(EDOACEP, fil, 3);
                    } else {
                        model_AFND.setValueAt(EDOACEP, fil, 2);
                    }
                }
            }
        }

    }

    private DefaultTableModel llenarColTabSub(int estado, int fil, List<M_transicion> transiciones,
            DefaultTableModel mod_tabSub) {

        String aux;
        for (M_transicion temp : transiciones) {
            if (estado == temp.getOrigen()) {
                if (temp.getAlfabeto().equals("0")) {

                    if (mod_tabSub.getValueAt(fil, 1) == null) {
                        mod_tabSub.setValueAt(temp.getDestino(), fil, 1);
                    } else {
                        aux = mod_tabSub.getValueAt(fil, 1) + "," + temp.getDestino();
                        mod_tabSub.setValueAt(aux, fil, 1);
                    }

                } else if (temp.getAlfabeto().equals("1")) {
                    if (alfabetoFinal.equals("01")) {
                        if (mod_tabSub.getValueAt(fil, 2) == null) {
                            mod_tabSub.setValueAt(temp.getDestino(), fil, 2);
                        } else {
                            aux = mod_tabSub.getValueAt(fil, 2) + "," + temp.getDestino();
                            mod_tabSub.setValueAt(aux, fil, 2);
                        }
                    } else {

                        if (mod_tabSub.getValueAt(fil, 1) == null) {
                            mod_tabSub.setValueAt(temp.getDestino(), fil, 1);
                        } else {
                            aux = mod_tabSub.getValueAt(fil, 1) + "," + temp.getDestino();
                            mod_tabSub.setValueAt(aux, fil, 1);
                        }

                    }

                } else if (temp.getAlfabeto().equals("0,1")) {

                    if (mod_tabSub.getValueAt(fil, 1) == null) {
                        mod_tabSub.setValueAt(temp.getDestino(), fil, 1);
                    } else {
                        aux = mod_tabSub.getValueAt(fil, 1) + "," + temp.getDestino();
                        mod_tabSub.setValueAt(aux, fil, 1);
                    }

                    if (mod_tabSub.getValueAt(fil, 2) == null) {
                        mod_tabSub.setValueAt(temp.getDestino(), fil, 2);
                    } else {
                        aux = mod_tabSub.getValueAt(fil, 2) + "," + temp.getDestino();
                        mod_tabSub.setValueAt(aux, fil, 2);
                    }

                }
            }
        }
        return mod_tabSub;

    }

    private void convertAFNtoAFD() {
        for (int i = 0; i < model_AFND.getRowCount(); i++) {
            String aux = model_AFND.getValueAt(i, 0).toString(); //col  0 en i
            for (int j = 0; j < model_AFND.getRowCount(); j++) {
                if (model_AFND.getValueAt(j, 1).toString().equals(aux)) {
                    model_AFND.setValueAt(i + "-", j, 1);
                }
                if (alfabetoFinal.equals("01")) {
                    if (model_AFND.getValueAt(j, 2).toString().equals(aux)) {
                        model_AFND.setValueAt(i + "-", j, 2);
                    }
                }
            }

            if (alfabetoFinal.equals("01")) {
                if (model_AFND.getValueAt(i, 3) == null) {
                    model_AFND.setValueAt(0, i, 3);
                }
            } else {
                if (model_AFND.getValueAt(i, 2) == null) {
                    model_AFND.setValueAt(0, i, 2);
                }
            }
            model_AFND.setValueAt(i, i, 0);
        }

        clear();

    }

    private void clear() {
        for (int i = 0; i < model_AFND.getRowCount(); i++) {
            Object limpiar;
            int x;
            limpiar = model_AFND.getValueAt(i, 1).toString().replace("-", "");
            x = Integer.valueOf((String) limpiar);
            model_AFND.setValueAt(x, i, 1);
            if (alfabetoFinal.equals("01")) {
                limpiar = model_AFND.getValueAt(i, 2).toString().replace("-", "");
                x = Integer.valueOf((String) limpiar);
                model_AFND.setValueAt(x, i, 2);
            }
        }

    }

    /**
     *
     * @return tipo de automata AFD o AFND, RETORNAR o DESCONECTADOS indica
     * inconvenientes con el diagrama
     */
    private String diagramaValido() {
        List<M_estado> estados = getEstados();
        List<M_transicion> transiciones = getTransiciones();
        List<M_nodo> nodos = new ArrayList();
        for (M_estado e : estados) {
            nodos.add(new M_nodo(String.valueOf(e.getIdEstado()), ""));
        }
        for (M_nodo n : nodos) {
            for (M_transicion t : transiciones) {
                if (Integer.parseInt(n.getIdNodo()) == t.getOrigen()) {
                    if (t.getAlfabeto().contains(",")) {
                        String[] split = t.getAlfabeto().split(",");
                        for (String it : split) {
                            n.addSucesor(new M_arco(String.valueOf(t.getOrigen()),
                                    String.valueOf(t.getDestino()), it));
                        }
                    } else {
                        n.addSucesor(new M_arco(String.valueOf(t.getOrigen()),
                                String.valueOf(t.getDestino()), t.getAlfabeto()));
                    }
                }
            }
        }
        ArrayList<M_arco> arc = null;

        /*En el extraño caso que q0 no tenga transiciones, no podemos realizar nada*/
        if (nodos.get(0).getSucesores().isEmpty()) {
            return RETORNAR;
        }

        boolean id[] = new boolean[nodos.size()];
        id[0] = true;
        /*la idea principal es que si ninguna transicion hacia otro estado logra 
        marcar uno o mas estados, entonces estos estados no tienen union con el
        diagrama
         */
        for (M_nodo n : nodos) {
            ArrayList<M_arco> arco = n.getSucesores();
            if (!arco.isEmpty()) {
                for (M_arco a : arco) {
                    if (!a.getDestino().equals(n.getIdNodo())) {
                        id[Integer.parseInt(a.getDestino())] = true;
                    }
                }
            }
        }

        for (int i = 0; i < id.length; i++) {
            if (!id[i]) {
                return DESCONECTADOS;
            }
        }

        alfabetoFinal = "";
        int num_alfa = 1;
        boolean salir = false;
        for (M_nodo n : nodos) {
            if (!salir) {
                arc = n.getSucesores();
                /*se recoge un elemento del alfabeto de la transicion, si en dado caso
                ese elemento guardado es diferente al de otras transiciones
                entonces se da por hecho que el alfabeto es de 0 y 1, por el contrario 
                el alfabeto es 0 o 1.Si la variable alfabeto es siempre la misma contra 
                los demas elementos de las transiciones la variable no se modifica
                entonces tendria guardado un 0 o un 1, por el contrario la variable cambia a
                0,1.
                num alfa es la representacion numerica del alfabeto
                 */
                for (M_arco a : arc) {
                    if (alfabetoFinal.equals("")) {
                        alfabetoFinal = a.getAlfabeto();
                    } else {
                        if (!alfabetoFinal.equals(a.getAlfabeto())) {
                            alfabetoFinal = "0,1";
                            num_alfa = 2;
                            salir = true;
                            break;
                        }
                    }
                }
            }
        }

        /*
        si es solo un elemento del alfabeto 0 o 1, entonces para ser AFD todos
        los estados deberian tener 1 transicion, de no ser asi en dado caso de 
        mas o menos transiciones por estado, se considera AFND.
         */
        if (num_alfa == 1) {
            int numTrans = 0;
            for (M_nodo n : nodos) {
                arc = n.getSucesores();
                for (M_arco a : arc) {
                    //cuenta el numero de transiciones que tiene el nodo
                    numTrans++;
                }
                if (numTrans < num_alfa || numTrans > num_alfa) {
                    return AFND;
                }
                numTrans = 0;
            }
            return AFD;
        } else {
            /*
            en caso de ser dos elementos del alfabeto para AFD se consideran
            dos transiciones por estado, si tiene mas o menos transiciones 
            se considera AFND, la variable concat resuelve un caso de confusion
             */
            int numTrans = 0;
            String concat = "";
            for (M_nodo n : nodos) {
                arc = n.getSucesores();
                for (M_arco a : arc) {
                    numTrans++;
                    concat += a.getAlfabeto();
                }
                if ((numTrans < num_alfa || numTrans > num_alfa)
                        || (concat.contains("00") || concat.contains("11"))) {
                    return AFND;
                }
                numTrans = 0;
                concat = "";
            }
            return AFD;
        }

    }

}
