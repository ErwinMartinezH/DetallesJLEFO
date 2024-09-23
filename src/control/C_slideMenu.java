/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import funciones.LienzoFromScroll;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import vista.V_tabs;
import static funciones.NmComponentes.*;
import funciones.adp.Automata;
import funciones.adp.Estado;
import funciones.adp.Estados_Validos;
import funciones.adp.Transicion;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.M_estado;
import modelo.M_transicion;
import vista.Paso_paso;
import vista.V_interfaz;
import vista.V_lienzo;
import vista.V_popupmenu;

/**
 *
 * @author herma
 */
public class C_slideMenu implements ActionListener {

    static boolean estados = false;
    private V_popupmenu menu;
    private V_interfaz interfaz;
    private List<M_estado> activo;
    private final String ACEPTACION = "Edo-Aceptacion";
    private final String NO_ACEPTACION = "Edo-No-Aceptacion";
    private final boolean EDOACEP = true;
    private final boolean EDONOACEP = false;
    static boolean seleccionar = false;
    public Estados_Validos validos;
    private int estadoAct = 0;
    private int estadoAnt = 0;
    static boolean transicion = false;
    private boolean analizar = false;
    private boolean evaluar = false;
    V_tabs tabs;

    public V_lienzo lienzo;
    public V_popupmenu acept;
    //public pruebas p;
    public Estados_Validos Evalido;
    ArrayList<Estado> estado = new ArrayList();

    ArrayList<Integer> listSeguimientoEstados;
    ArrayList<Stack<Character>> listSeguimientoPila;
    int iContadoPosicionSeguimiento = -1;

    public C_slideMenu(V_tabs tabs) {
        this.tabs = tabs;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Component cmp = (Component) e.getSource();
        switch (cmp.getName()) {
            case ESTADO:
                if (!estados) {
                    estados = true;
                    seleccionar = false;
                    transicion = false;
                    V_lienzo cursor = new LienzoFromScroll().obtener(tabs);
                    cursor.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
                break;
            case TRANSICION:
                if (!transicion) {
                    transicion = true;
                    estados = false;
                    seleccionar = false;
                    V_lienzo cursor = new LienzoFromScroll().obtener(tabs);
                    cursor.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
                break;
            case SELECCIONAR:
                if (!seleccionar) {
                    seleccionar = true;
                    estados = false;
                    transicion = false;
                    V_lienzo cursor = new LienzoFromScroll().obtener(tabs);
                    cursor.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
                break;
            case ANALIZAR:
                if (!analizar) {
                    analizar = true;
                    seleccionar = false;
                    estados = false;
                    transicion = false;

                }
                analizar = false;
                if (tabs.getSelectedComponent() != null) {
                    V_lienzo check = new LienzoFromScroll().obtener(tabs);
                    check.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    MouseListener[] ml = check.getMouseListeners();
                    C_automata c = (C_automata) ml[0];
                    if (!check.getTipoPanel().equals(ER) && !c.getEstados().isEmpty()
                            && !c.getTransiciones().isEmpty() && !check.getTipoPanel().equals(GLC) && !check.getTipoPanel().equals(ADP)
                            && !check.getTipoPanel().equals(MT) && !check.getTipoPanel().equals(MTBD) &&
                            !check.getTipoPanel().equals(MTB)) {
                        check.rastreo();

                    } else if (check.getTipoPanel().equals(ADP)) {
                        System.out.println("Soy ADP");
                        if (tabs.getSelectedComponent() != null) {
                            Paso_paso ps = new Paso_paso();
                            if (!check.getTipoPanel().equals(ER) && !c.getEstados().isEmpty()
                                    && !c.getTransiciones().isEmpty() && !check.getTipoPanel().equals(GLC) && !check.getTipoPanel().equals(AF)) {//analiza el automata correctamente
                                //check.paso_paso(); 
                                Paso_paso p = new Paso_paso();
                                p.getjButton2().addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {//accion de boton de retroceso
                                        System.out.println("Soy anterior");

                                        iContadoPosicionSeguimiento--;

                                        if (iContadoPosicionSeguimiento <= -1) {
                                            iContadoPosicionSeguimiento = 0;
                                        }

                                        if (iContadoPosicionSeguimiento < listSeguimientoPila.size() && iContadoPosicionSeguimiento < listSeguimientoEstados.size()) {
                                            Object[] nuevaFila = listSeguimientoPila.get(iContadoPosicionSeguimiento).toArray();
                                            Collections.reverse(Arrays.asList(nuevaFila));

                                            DefaultTableModel model = (DefaultTableModel) p.getjTable1().getModel();
                                            model.setRowCount(0);

                                            for (Object object : nuevaFila) {
                                                model.addRow(new Object[]{object});
                                            }

                                            int iNodoID = listSeguimientoEstados.get(iContadoPosicionSeguimiento);

                                            for (M_estado estado1 : c.getEstados()) {
                                                if (estado1.getIdEstado() == iNodoID) {
                                                    estado1.setColores(Color.orange, Color.black, Color.WHITE);
                                                } else {
                                                    estado1.setColores(Color.blue, Color.yellow, Color.WHITE);
                                                }
                                            }

                                            p.getjTable1().setModel(model);
                                            p.repaint();
                                            p.getjTable1().repaint();

                                            check.repaint();
                                        }

                                    }
                                });
                                p.getjButton3().addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {//Accion de boton de avance

                                        System.out.println("Soy siguiente");
                                        iContadoPosicionSeguimiento++;

                                        if (!(iContadoPosicionSeguimiento < listSeguimientoPila.size())) {
                                            iContadoPosicionSeguimiento = 0;
                                        }
                                        System.out.println("---------------Ver cantidades seguimientio-----------");
                                        System.out.println(iContadoPosicionSeguimiento);
                                        System.out.println(listSeguimientoPila.size());
                                        System.out.println(listSeguimientoEstados.size());
                                        if (iContadoPosicionSeguimiento < listSeguimientoPila.size() && iContadoPosicionSeguimiento < listSeguimientoEstados.size()) {
                                            Object[] nuevaFila = listSeguimientoPila.get(iContadoPosicionSeguimiento).toArray();
                                            Collections.reverse(Arrays.asList(nuevaFila));

                                            DefaultTableModel model = (DefaultTableModel) p.getjTable1().getModel();
                                            model.setRowCount(0);

                                            for (Object object : nuevaFila) {
                                                model.addRow(new Object[]{object});
                                            }

                                            int iNodoID = listSeguimientoEstados.get(iContadoPosicionSeguimiento);

                                            for (M_estado estado1 : c.getEstados()) {
                                                System.out.println(estado1.getTipo());
                                                if (estado1.getIdEstado() == iNodoID && estado1.getTipo().equals("Edo-Aceptacion")) {
                                                    estado1.setColores(Color.GREEN, Color.yellow, Color.WHITE);
                                                } else if (estado1.getIdEstado() == iNodoID) {
                                                    estado1.setColores(Color.orange, Color.black, Color.WHITE);
//
                                                } else {
                                                    estado1.setColores(Color.blue, Color.yellow, Color.WHITE);
                                                }
                                            }

                                            p.getjTable1().setModel(model);
                                            p.repaint();
                                            p.getjTable1().repaint();

                                            check.repaint();
                                        }

                                    }
                                });
                                p.getjButton1().addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {//accion del boton que evalua la cadena
                                        System.out.println("--------------------------Evaluando cadena--------------------------");
//                                String cadena = p.getjTextField2().getText();
//                                System.out.println(cadena);
                                        List<M_transicion> transiciones = c.getTransiciones();
                                        List<M_estado> edos = c.getEstados();
                                        System.out.println("transiciones: " + transiciones);
                                        System.out.println("edos: " + edos);
                                        ArrayList<Estado> estados = new ArrayList();
                                        HashMap<Integer, ArrayList<Transicion>> mapEstadosAutomata = new HashMap<>();
                                        listSeguimientoEstados = new ArrayList<>();
                                        iContadoPosicionSeguimiento = -1;
                                        Automata au = new Automata();
                                        for (int i = 0; i < transiciones.size(); i++) {
                                            M_transicion mTransicion = transiciones.get(i);
                                            int iNodoOrigen = mTransicion.getOrigen();
                                            int iNodoDestino = mTransicion.getDestino();
                                            String[] sValores = mTransicion.getAlfabeto().split(",");
                                            String sLectura = sValores[0].substring(1);
                                            String sPop = sValores[1].substring(1);
                                            String sPush = sValores[2].substring(1, sValores[2].length() - 1);
                                            System.out.println("Origen: " + iNodoOrigen + " Destino: " + iNodoDestino + " sLectura: " + sLectura + " pop: " + sPop + " spush: " + sPush);
                                            au.addTransicion(iNodoOrigen, sLectura.charAt(0), sPop.charAt(0), sPush.charAt(0), iNodoDestino);
                                        }
                                        for (M_estado estado : edos) {
                                            int iNodoId = estado.getIdEstado();
                                            au.setAceptacion(iNodoId, estado.getTipo().equals("Edo-Aceptacion"));
                                        }
//
                                        System.out.println("----------------------Estados para evaluar-------------");
                                        for (Estado estado : estados) {
                                            System.out.println("estaod: " + estado.getNombre() + " " + estado.getAceptacion());
                                            System.out.println(estado.getTransiciones());
                                        }
//                                System.out.println(estados);
                                        if (p.getjTextField2().getText().isEmpty()) {
                                            p.getjTextField2().setText("?");
                                        }
                                        System.out.println(p.getjTextField2().getText());
                                        ;
                                        au.start(p.getjTextField2().getText().toCharArray());
                                        //aqui se añade una condicional donde se pregunta si el automata acepta la cadena o no
                                        if (au.startg(p.getjTextField2().getText().toCharArray())) {
                                            //JOptionPane.showMessageDialog(tabs, "Cadena aceptada");
                                            //muestra en verde(RGB) el backgroud del label
                                            p.setLabel("Cadena aceptada");
                                            p.getJPanel3().setBackground(new Color(0, 255, 0));
                                        } else {
                                            //JOptionPane.showMessageDialog(tabs, "Cadena rechazada");
                                            //muestra en rojo(RGB) el backgroud del label
                                            p.setLabel("Cadena rechazada");
                                            p.getJPanel3().setBackground(new Color(241, 61, 47, 255));

                                        }
                                        Estados_Validos[] esVa = au.getRecorrido();
                                        listSeguimientoEstados = new ArrayList<>();
                                        if (esVa != null && esVa.length >= 1) {
                                            //listSeguimientoEstados.add(esVa[0].origen);
                                        }
                                        for (Estados_Validos estados_Valido : esVa) {
                                            System.out.println(estados_Valido);
                                           // listSeguimientoEstados.add(estados_Valido.destino);
                                        }
//                                System.out.println("estado: " + au.getListSeguimientoEstados());
//                                System.out.println("pila: " + au.getListSeguimientoPila());
                                       // listSeguimientoPila = au.getListSeguimientoPila(esVa);

                                        for (Stack<Character> stack : listSeguimientoPila) {
                                            System.out.println(stack);
                                        }
//                                listSeguimientoEstados = au.getListSeguimientoEstados();
//                                DefaultTableModel model = new DefaultTableModel();
                                        // Crear una JTable con el modelo de datos


                                    }
                                });
                                p.setVisible(true);
                                p.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            } else {
                                JOptionPane.showMessageDialog(tabs, "No hay nada que analizar",
                                        "Analizar Autómata", JOptionPane.INFORMATION_MESSAGE);
                                break;
                            }
                        } else {
                            JOptionPane.showMessageDialog(tabs, "No hay nada que analizar",
                                    "Analizar Autómata", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        }
                        break;
                    }
                } else {
                    JOptionPane.showMessageDialog(tabs, "No hay nada que analizar",
                            "Analizar Autómata", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
                break;
        }
    }

}
