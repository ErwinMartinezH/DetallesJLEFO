/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package funciones;

import javax.swing.JScrollPane;
import javax.swing.JViewport;
import vista.V_lienzo;
import vista.V_tabs;

/**
 *
 * @author herma
 */
public class LienzoFromScroll {

    public LienzoFromScroll() {
    }

    public V_lienzo obtener(V_tabs tabs) {
        JScrollPane scroll = (JScrollPane) tabs.getSelectedComponent();
        if (scroll != null) {
            JViewport view = scroll.getViewport();
            V_lienzo lienzo = (V_lienzo) view.getView();
            return lienzo;
        }
        return null;
    }

    public V_lienzo obtenerEn(int i, V_tabs tabs) {
        JScrollPane scroll = (JScrollPane) tabs.getComponent(i);
        JViewport view = scroll.getViewport();
        V_lienzo lienzo = (V_lienzo) view.getView();
        return lienzo;
    }

}
