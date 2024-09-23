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
import java.awt.Cursor;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import vista.V_lienzo;

/**
 *
 * @author herma
 */
public class C_slideMenu implements ActionListener {

    static boolean estados = false;
    static boolean seleccionar = false;
    static boolean transicion = false;
    private boolean analizar = false;
    V_tabs tabs;

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
                            && !c.getTransiciones().isEmpty()) {
                        check.rastreo();
                    } else {
                        JOptionPane.showMessageDialog(tabs, "No hay nada que analizar",
                                "Analizar Autómata Finito", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }

                } else {
                    JOptionPane.showMessageDialog(tabs, "No hay nada que analizar",
                            "Analizar Autómata Finito", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }

                break;
        }
    }

}
