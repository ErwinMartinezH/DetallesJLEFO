/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package funciones.backtracking;

import control.C_automata;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.M_arco;
import modelo.M_colores;
import modelo.M_estado;
import modelo.M_nodo;
import modelo.M_transicion;
import vista.V_lienzo;
import vista.V_rastreo;

/**
 *
 * @author herma
 */
public class Rastreador implements Runnable {

    String cadena;
    V_lienzo lienzo;
    V_rastreo interfaz;
    DefaultTableModel mod_tabtrans;
    String estatusCadena;
    long intervalo;
    String alfabetoFinal;

    public Rastreador() {
    }

    public Rastreador(String cadena, V_lienzo lienzo, V_rastreo interfaz,
            DefaultTableModel mod_tabtrans, String estatusCadena, long intervalo, String alfabetoFinal) {
        this.cadena = cadena;
        this.lienzo = lienzo;
        this.interfaz = interfaz;
        this.mod_tabtrans = mod_tabtrans;
        this.estatusCadena = estatusCadena;
        this.intervalo = intervalo;
        this.alfabetoFinal = alfabetoFinal;
    }

    @Override
    public void run() {
        //prevenir que no se haya sellecionado nada
        if (cadena.equals("")) {
            JOptionPane.showMessageDialog(interfaz, "Elige una cadena de la tabla",
                    "Rastreo Paso a Paso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        MouseListener[] ml = lienzo.getMouseListeners();
        C_automata cl = (C_automata) ml[0];
        List<M_estado> estados = cl.getEstados();
        List<M_transicion> transiciones = cl.getTransiciones();
        lienzo.setAnalizar(true);

        ArrayList<M_nodo> ruta = new Backtracking(mod_tabtrans, cadena, estatusCadena, alfabetoFinal).rastrear();
        M_nodo n = null;
        M_arco arc = null;

        int index = 0;

        //solucion a falla en ruta
        if (ruta.size() > cadena.length()) {
            ruta.remove(0);
        }

        if (alfabetoFinal.length() == 1 && ruta.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Imposible realizar análisis", "Advertencia!", JOptionPane.WARNING_MESSAGE);
        }

        //construir la ruta graficamente
        while (!ruta.isEmpty() && lienzo.isAnalizar()) {

            if (!ruta.isEmpty()) {
                n = ruta.get(0);
                arc = n.getSucesores().get(0);
                ruta.remove(0);
            }
            //coincidencias con el estado
            for (M_estado e : estados) {
                if (e.getIdEstado() == Integer.parseInt(n.getIdNodo())) {
                    colorearEstado(e, lienzo, M_colores.SEARCH_ESTADO,
                            M_colores.SEARCH_CONTORNO,
                            M_colores.SEARCH_IDESTADO);
                    break;
                }
            }

            //coincidencias en transiciones al siguiente estado
            for (M_transicion t : transiciones) {
                if (t.getOrigen() == Integer.parseInt(arc.getOrigen())
                        && t.getDestino() == Integer.parseInt(arc.getDestino())) {
                    interfaz.setPosicion((short) index);
                    interfaz.getPanel().repaint();
                    t.setColores(M_colores.SEARCH_TRANSICION, M_colores.ETIQUETA);
                    lienzo.repaint();
                    try {
                        Thread.sleep(intervalo);
                    } catch (Exception e) {
                    }
                    t.resetColores();
                    lienzo.repaint();;
                    break;
                }
            }

            index++;

            //al finalizar marcar si el nodo es de aceptacion o rechazo
            if (ruta.isEmpty()) {
                switch (estatusCadena) {
                    case "ACEPTA":
                        for (M_estado e : estados) {
                            if (Integer.parseInt(arc.getDestino()) == e.getIdEstado()) {
                                colorearEstado(e, lienzo, M_colores.FINAL_ESTADO,
                                        M_colores.FINAL_CONTORNO,
                                        M_colores.FINAL_IDESTADO);
                                interfaz.setFin(true);
                                break;
                            }
                        }
                        break;
                    case "NO ACEPTA":
                        for (M_estado e : estados) {
                            if (Integer.parseInt(arc.getDestino()) == e.getIdEstado()) {
                                colorearEstado(e, lienzo, M_colores.NOFINAL_ESTADO,
                                        M_colores.NOFINAL_CONTORNO,
                                        M_colores.NOFINAL_IDESTADO);
                                interfaz.setFin(true);
                                break;
                            }
                        }
                        break;
                }
            }
        }
    }

    private void colorearEstado(M_estado e, V_lienzo lienzo,
            Color c1, Color c2, Color c3) {
        e.setColores(c1, c2, c3);
        lienzo.repaint();
        try {
            Thread.sleep(intervalo);
        } catch (InterruptedException ex) {
        }
        e.resetColores();
        lienzo.repaint();
    }
}
