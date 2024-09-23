/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import control.C_automata;
import control.C_automataP;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import static funciones.NmComponentes.*;

/**
 *
 * @author herma
 */
public class V_popupmenu extends JPopupMenu {

    private JCheckBoxMenuItem estadoAcep;

    public V_popupmenu(V_lienzo dibujar, C_automata ctrl) {
        super.setVisible(false);
        super.setInvoker(dibujar);
        JMenu eliminar = new JMenu("Eliminar");
        JMenuItem editarTrans = new JMenuItem(EDITAR_TRANS);
        JMenuItem eliminarEdo = new JMenuItem(ELIM_ESTADO);
        JMenuItem eliminarTrans = new JMenuItem(ELIM_TRANS);
        estadoAcep = new JCheckBoxMenuItem(ESTADO_ACEP);
        estadoAcep.setName(ESTADO_ACEP);
        estadoAcep.addActionListener(ctrl);
        editarTrans.setName(EDITAR_TRANS);
        editarTrans.addActionListener(ctrl);
        eliminarEdo.setName(ELIM_ESTADO);
        eliminarEdo.addActionListener(ctrl);
        eliminarTrans.setName(ELIM_TRANS);
        eliminarTrans.addActionListener(ctrl);
        super.add(estadoAcep);
        super.add(editarTrans);
        eliminar.add(eliminarEdo);
        eliminar.add(eliminarTrans);
        super.add(eliminar);
    }
    
    public V_popupmenu(V_lienzo dibujar, C_automataP ctrl) {
        super.setVisible(false);
        super.setInvoker(dibujar);
        JMenu eliminar = new JMenu("Eliminar");
        JMenuItem editarTrans = new JMenuItem(EDITAR_TRANS);
        JMenuItem eliminarEdo = new JMenuItem(ELIM_ESTADO);
        JMenuItem eliminarTrans = new JMenuItem(ELIM_TRANS);
        estadoAcep = new JCheckBoxMenuItem(ESTADO_ACEP);
        estadoAcep.setName(ESTADO_ACEP);
        estadoAcep.addActionListener(ctrl);
        editarTrans.setName(EDITAR_TRANS);
        editarTrans.addActionListener(ctrl);
        eliminarEdo.setName(ELIM_ESTADO);
        eliminarEdo.addActionListener(ctrl);
        eliminarTrans.setName(ELIM_TRANS);
        eliminarTrans.addActionListener(ctrl);
        super.add(estadoAcep);
        super.add(editarTrans);
        eliminar.add(eliminarEdo);
        eliminar.add(eliminarTrans);
        super.add(eliminar);
    }

    /**
     * Obtener estado del checkbox
     *
     * @return true si esta activo - false en caso contrario
     */
    public boolean getState() {
        return estadoAcep.getState();
    }

    /**
     * Cambiar el estado del checkbox
     *
     * @param estado true si es un estado de aceptacion - false en caso
     * contrario
     */
    public void setState(boolean estado) {
        estadoAcep.setState(estado);
    }

}
