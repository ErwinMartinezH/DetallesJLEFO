/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import control.C_automata;
import control.C_automataP;
import control.C_interfaz;
import control.C_tabs;
import control.indicePestaña;
import funciones.LienzoFromScroll;
import java.io.File;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import static funciones.NmComponentes.*;
import funciones.ctrlZ_Y.Control;
import java.awt.event.MouseListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author herma
 */
public class V_tabs extends JTabbedPane {

    public V_tabs() {
        addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    V_lienzo lienzo = new LienzoFromScroll().obtener(V_tabs.this);
                    if (lienzo != null) {
                        if (lienzo.getTipoPanel().equals(AF)) {
                            MouseListener[] ml = lienzo.getMouseListeners();
                            C_automata cl = (C_automata) ml[0];
                            if (indicePestaña.getIndice() > 1) {
                                if (lienzo.getTipoPanel().equals(AF)) {
                                    C_interfaz.deslizarMenu(AF);
                                    Control tmp = new Control();
                                    String undo;
                                    String redo;
                                    undo = tmp.getPunteroUndo(lienzo.getName());
                                    redo = tmp.getPunteroRedo(lienzo.getName());
                                    V_interfaz.menu.setEnabled(true);
                                    if (undo != null) {
                                        V_interfaz.deshacer.setEnabled(true);
                                        V_interfaz.b_deshacer.setEnabled(true);
                                    } else {
                                        V_interfaz.deshacer.setEnabled(false);
                                        V_interfaz.b_deshacer.setEnabled(false);
                                    }
                                    if (redo != null) {
                                        V_interfaz.rehacer.setEnabled(true);
                                        V_interfaz.b_rehacer.setEnabled(true);
                                    } else {
                                        V_interfaz.rehacer.setEnabled(false);
                                        V_interfaz.b_rehacer.setEnabled(false);
                                    }
                                    if (cl.isCambios()) {
                                        V_interfaz.guardar.setEnabled(true);
                                        V_interfaz.b_guardar.setEnabled(true);
                                    } else {
                                        V_interfaz.guardar.setEnabled(false);
                                        V_interfaz.b_guardar.setEnabled(false);
                                    }
                                }
                            }
                        }
                        if (lienzo.getTipoPanel().equals(ADP)) {
                            MouseListener[] ml = lienzo.getMouseListeners();
                            C_automataP cl = (C_automataP) ml[0];
                            if (indicePestaña.getIndice() > 1) {
                                if (lienzo.getTipoPanel().equals(ADP)) {
                                    C_interfaz.deslizarMenu(ADP);
                                    Control tmp = new Control();
                                    String undo;
                                    String redo;
                                    undo = tmp.getPunteroUndo(lienzo.getName());
                                    redo = tmp.getPunteroRedo(lienzo.getName());
                                    V_interfaz.menu.setEnabled(true);
                                    if (undo != null) {
                                        V_interfaz.deshacer.setEnabled(true);
                                        V_interfaz.b_deshacer.setEnabled(true);
                                    } else {
                                        V_interfaz.deshacer.setEnabled(false);
                                        V_interfaz.b_deshacer.setEnabled(false);
                                    }
                                    if (redo != null) {
                                        V_interfaz.rehacer.setEnabled(true);
                                        V_interfaz.b_rehacer.setEnabled(true);
                                    } else {
                                        V_interfaz.rehacer.setEnabled(false);
                                        V_interfaz.b_rehacer.setEnabled(false);
                                    }
                                    if (cl.isCambios()) {
                                        V_interfaz.guardar.setEnabled(true);
                                        V_interfaz.b_guardar.setEnabled(true);
                                    } else {
                                        V_interfaz.guardar.setEnabled(false);
                                        V_interfaz.b_guardar.setEnabled(false);
                                    }
                                }
                            }
                        }

                        if (indicePestaña.getIndice() > 1) {
                            if (lienzo.getTipoPanel().equals(ER)) {
                                C_interfaz.deslizarMenu(ER);
                                V_interfaz.menu.setEnabled(false);
                            }
                        }
                    } else {

                    }
                } catch (NullPointerException ex) {
                    // Manejo de la excepción
                    ex.printStackTrace();
                }
            }
        });
    }

    public void añadirTab(String nombreTab, int indice, File ruta,
            int idNombre, String tipoPanel) {
        V_lienzo areaDibujo;
        JScrollPane scroll = new JScrollPane();
        scroll.getVerticalScrollBar().setUnitIncrement(15);
        scroll.getHorizontalScrollBar().setUnitIncrement(15);
        if (tipoPanel.equals(ER)) {
            areaDibujo = new V_lienzo(tipoPanel);
        } else {
            areaDibujo = new V_lienzo(tipoPanel);
            long time = System.currentTimeMillis();
            areaDibujo.setName(Long.toString(time));
            new Control(Long.toString(time));
            areaDibujo.setRutaArchivo(ruta.toString());
        }
        areaDibujo.setIdNombre(idNombre);
        scroll.setViewportView(areaDibujo);
        // añadir
        addTab(nombreTab, null, scroll, ruta.toString());

        C_tabs ctrl = new C_tabs(this, areaDibujo);

        setTabComponentAt(indice, ctrl);

        update(areaDibujo.getGraphics());
        updateUI();

        if (tipoPanel.equals(ER)) {
            C_interfaz.deslizarMenu(ER);
        } else if (tipoPanel.equals(AF) || tipoPanel.equals(ADP)) {
            C_interfaz.deslizarMenu(AF);
            C_interfaz.deslizarMenu(ADP);
        } else if (tipoPanel.equals(MT) || tipoPanel.equals(MTBD) || tipoPanel.equals(MTB) || tipoPanel.equals(MTMC)) {
            C_interfaz.deslizarMenu(MT);
            C_interfaz.deslizarMenu(MTBD);
            C_interfaz.deslizarMenu(MTB);
            C_interfaz.deslizarMenu(MTMC);
        } else if (tipoPanel.equals(GLC)) {
            C_interfaz.deslizarMenu(GLC);
        }
    }

}
