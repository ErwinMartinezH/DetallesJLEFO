/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import static funciones.NmComponentes.ADP;
import static funciones.NmComponentes.AF;
import static funciones.NmComponentes.EDITAR_TRANS;
import static funciones.NmComponentes.ELIM_ESTADO;
import static funciones.NmComponentes.ELIM_TRANS;
import static funciones.NmComponentes.ESTADO_ACEP;
import static funciones.NmComponentes.RASTREANDO;
import static funciones.NmComponentes.SLIDER;
import funciones.ctrlZ_Y.Control;
import funciones.orden.Ordenador;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static java.awt.event.MouseEvent.BUTTON1;
import static java.awt.event.MouseEvent.BUTTON3;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import javax.swing.JPanel;
import javax.swing.JTextField;
import modelo.M_estado;
import modelo.M_transicion;
import vista.V_interfaz;
import vista.V_lienzo;
import vista.V_popupmenu;
import vista.V_rastreo;
//import vista.V_rastreoP;

/**
 *
 * @author MSIGF63
 */
public class C_automataP extends MouseAdapter implements ActionListener {

    private boolean rastreoActivo = false;
    public V_rastreo rastreo;
    private V_lienzo lienzo;
    private List<M_estado> estados;
    private List<M_transicion> transiciones;
    private V_popupmenu menu;
    private Control ctrlVersion;

    private List estadosElim;
    private int idEstado = 0;
    private int idEstadoElim = 0;
    private Point clic = null, origen = null, destino = null;
    private int idOrigen = 0;
    private String camino = "";
    private String alfabeto[] = {camino};
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

    public void automataP() {
    }

    public C_automataP(V_lienzo dibujar, V_rastreo rastreo) {
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
//                case TABLACADENAS:
//                    cadena = rastreo.getCadena();
//                    estatusCadena = rastreo.getEstatus();
//                    rastreo.setCadena(cadena);
//                    rastreo.setFin(false);
//                    rastreo.getPanel().repaint();
//                    break;
//                case SLIDER:
//                    switch (rastreo.getVelocidad()) {
//                        case 0://lento
//                            intervalo = 3200;
//                            break;
//                        case 1://normal
//                            intervalo = 1600;
//                            break;
//                        case 2://rapido
//                            intervalo = 800;
//                            break;
//                    }
//                    break;
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
                        if (lienzo.getTipoPanel().equals(AF)) {
                            String alfabeto[] = {"0", "1", "0,1"};
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
                        } else if (lienzo.getTipoPanel().equals(ADP)) {
                            if (trans.getMascara().intersects(coordX, coordY, ancho, alto)) {

                                JPanel panel = new JPanel();
                                panel.setLayout(new GridLayout(3, 2)); // Establecer un diseño de cuadrícula para organizar los campos

// Agregar campos para Lectura, Pop y Push
                                panel.add(new JLabel("Lectura:"));
                                JTextField lecturaField = new JTextField();
                                panel.add(lecturaField);

                                panel.add(new JLabel("Pop:"));
                                JTextField popField = new JTextField();
                                panel.add(popField);

                                panel.add(new JLabel("Push:"));
                                JTextField pushField = new JTextField();
                                panel.add(pushField);

// Mostrar el cuadro de diálogo y capturar los valores ingresados
                                int result = JOptionPane.showConfirmDialog(lienzo, panel, "Ingrese los datos de la transición", JOptionPane.OK_CANCEL_OPTION);
                                String alfa = null;

// Verificar si el usuario hizo clic en OK
                                if (result == JOptionPane.OK_OPTION) {
                                    String lectura = lecturaField.getText();
                                    String pop = popField.getText();
                                    String push = pushField.getText();

                                    //si alguno de los campos esta vacío se coloca un "?"
                                    // Verificar si alguno de los campos está vacío
                                    if (lectura.isEmpty() && pop.isEmpty() && push.isEmpty()) {
                                        alfa = "(" + "?" + ", " + "?" + ", " + "?" + ")";
                                    }else if (lectura.isEmpty()&&pop.isEmpty()){
                                        alfa = "(" + "?" + ", " + "?" + ", " + push + ")";
                                    }else if(lectura.isEmpty()&&push.isEmpty()){
                                        alfa = "(" + "?" + ", " + pop + ", " + "?" + ")";
                                    }else if(pop.isEmpty() && push.isEmpty()){
                                        alfa = "(" + lectura + ", " + "?" + ", " + "?" + ")";
                                    }else if(lectura.isEmpty()){
                                        alfa = "(" + "?" + ", " + pop + ", " + push + ")";
                                    }else if(pop.isEmpty()){
                                        alfa = "(" + lectura + ", " + "?" + ", " + push + ")";
                                    }else if(push.isEmpty()){
                                        alfa = "(" + lectura + ", " + pop + ", " + "?" + ")";
                                    } else{
                                        // Construir la cadena alfa con los valores ingresados
                                        alfa = "(" + lectura + ", " + pop + ", " + push + ")";
                                    }
                                }

                                if (alfa != null) {
                                    ObjetoDeshacer();
                                    trans.setAlfabeto(alfa);
                                }
                                lienzo.repaint();
                                setCambios(true);
                                break;
                            }
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
//            switch (componente.getName()) {
//                case AFD:
//                    guardarAFNDtoAFD();
//                    break;
//                case ORDENAR:
//                    ordenarCadenas();
//                    break;
//                case PASO_A_PASO:
//                    ExecutorService exe = Executors.newSingleThreadExecutor();
//                    exe.submit(new Rastreador(cadena, lienzo, rastreo,
//                            model_TransicionGeneral, estatusCadena, intervalo, alfabetoFinal));
//                    break;
//                default:
//                    JOptionPane.showMessageDialog(null, RASTREANDO, "Editar Diagrama",
//                            JOptionPane.INFORMATION_MESSAGE);
//                    break;
//            }

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
        //String[] alfa = null;
        if (lienzo.getTipoPanel().equals(AF)) {
            System.out.println("Soy AF");
            String alfabeto[] = {"0", "1", "0,1"};

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
        if (lienzo.getTipoPanel().equals(ADP)) {
            System.out.println("Soy ADP");
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(3, 2)); // Establecer un diseño de cuadrícula para organizar los campos

// Agregar campos para Lectura, Pop y Push
            panel.add(new JLabel("Lectura:"));
            JTextField lecturaField = new JTextField();
            panel.add(lecturaField);

            panel.add(new JLabel("Pop:"));
            JTextField popField = new JTextField();
            panel.add(popField);

            panel.add(new JLabel("Push:"));
            JTextField pushField = new JTextField();
            panel.add(pushField);

// Mostrar el cuadro de diálogo y capturar los valores ingresados
            int result = JOptionPane.showConfirmDialog(lienzo, panel, "Ingrese los datos de la transición", JOptionPane.OK_CANCEL_OPTION);
            String alfa = null;

// Verificar si el usuario hizo clic en OK
            if (result == JOptionPane.OK_OPTION) {
                String lectura = lecturaField.getText();
                String pop = popField.getText();
                String push = pushField.getText();


                //si alguno de los campos esta vacío se asigna un valor "?"
                // Verificar si alguno de los campos está vacío
                if (lectura.isEmpty() && pop.isEmpty() && push.isEmpty()) {
                    alfa = "(" + "?" + ", " + "?" + ", " + "?" + ")";
                }else if (lectura.isEmpty()&&pop.isEmpty()){
                    alfa = "(" + "?" + ", " + "?" + ", " + push + ")";
                }else if(lectura.isEmpty()&&push.isEmpty()){
                    alfa = "(" + "?" + ", " + pop + ", " + "?" + ")";
                }else if(pop.isEmpty() && push.isEmpty()){
                    alfa = "(" + lectura + ", " + "?" + ", " + "?" + ")";
                }else if(lectura.isEmpty()){
                    alfa = "(" + "?" + ", " + pop + ", " + push + ")";
                }else if(pop.isEmpty()){
                    alfa = "(" + lectura + ", " + "?" + ", " + push + ")";
                }else if(push.isEmpty()){
                    alfa = "(" + lectura + ", " + pop + ", " + "?" + ")";
                } else{
                // Construir la cadena alfa con los valores ingresados
                alfa = "(" + lectura + ", " + pop + ", " + push + ")";
                }
            }

            /*JPanel panel = new JPanel();
            panel.add(new JLabel("Formato de escritura: (in, pop, push)"));

            //Lanzamos la ventana
            String alfa = (String) JOptionPane.showInputDialog(lienzo, panel, "Ingrese los datos de la transición", INFORMATION_MESSAGE, null, null, camino);
*/
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

}
