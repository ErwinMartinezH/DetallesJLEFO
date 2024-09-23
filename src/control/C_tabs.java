/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import static funciones.NmComponentes.*;
import funciones.LienzoFromScroll;
import funciones.archivo.Archivo;
import funciones.ctrlZ_Y.Control;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import vista.V_lienzo;
import vista.V_tabs;

/**
 *
 * @author herma
 */
public class C_tabs extends JPanel {

    private V_tabs tabs;
    private V_lienzo lienzo;
    private C_automata controlador;

    public C_tabs(V_tabs tabs, V_lienzo lienzo) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        if (tabs == null) {
            throw new NullPointerException("TabbedPane is null");
        }
        this.tabs = tabs;
        this.lienzo = lienzo;
        if (lienzo.getTipoPanel().equals(AF)) {
            MouseListener[] ml = this.lienzo.getMouseListeners();
            controlador = (C_automata) ml[0];
        }
        setOpaque(false);

        //make JLabel read titles from JTabbedPane
        JLabel label = new JLabel() {
            @Override
            public String getText() {
                int i = tabs.indexOfTabComponent(C_tabs.this);
                if (i != -1) {
                    return tabs.getTitleAt(i);
                }
                return null;
            }
        };

        add(label);
        //add more space between the label and the button
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        //tab button
        JButton button = new TabButton();
        add(button);
        //add more space to the top of the component
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
    }

    private class TabButton extends JButton implements ActionListener {

        public TabButton() {
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("Cerrar pestaña");
            //Make the button looks the same for all Laf's
            setUI(new BasicButtonUI());
            //Make it transparent
            setContentAreaFilled(false);
            //No need to be focusable
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            //Making nice rollover effect
            //we use the same listener for all buttons
            addMouseListener(buttonMouseListener);
            setRolloverEnabled(true);
            //Close the proper tab by clicking the button
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            int i = tabs.indexOfTabComponent(C_tabs.this);
            if (i != -1) {

                lienzo = new LienzoFromScroll().obtenerEn(i, tabs);
                Control m = new Control();
                if (controlador != null && controlador.isCambios() == true) {
                    //Validando cuando le da en la X de la pestaña
                    int resp = JOptionPane.showConfirmDialog(
                            tabs, "¿Desea guardar los cambios en " + tabs.getTitleAt(i) + "?",
                            "Guardar", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (JOptionPane.OK_OPTION == resp) {
                        if (tabs.getTitleAt(i).endsWith(".jlefo") == false) {
                            Archivo.guardarJLEFO_Cerrar(lienzo, tabs, i, m);
                        } else {
                            lienzo.setMonitor(true);
                            lienzo.setAnalizar(false);
                            Archivo.grabarArchivo(lienzo.getRutaArchivo(), Archivo.saveAutomata(lienzo), lienzo);  //falta validar correctamente 
                            //eliminar directorios y archivos de los tab existente
                            if (lienzo.getTipoPanel().equals(AF)) {
                                //eliminar directorios y archivos del tab existentes
                                m.deleteRootFolder(lienzo.getName());
                            }

                            tabs.remove(i);
                            indicePestaña.minIndice();

                        }
                    }

                    if (JOptionPane.CANCEL_OPTION == resp) {
                        return;
                    }
                    if (JOptionPane.NO_OPTION == resp) {
                        if (lienzo.getTipoPanel().equals(AF)) {
                            //eliminar directorios y archivos de los tabs existentes
                            m.deleteRootFolder(lienzo.getName());
                        }
                        if (lienzo.getIdNombre() != 0) {
                            indicePestaña.getPesEliminada().add(lienzo.getIdNombre());
                            indicePestaña.setPesEliminada(indicePestaña.getPesEliminada());
                        }
                        lienzo.setAnalizar(false);
                        lienzo.setMonitor(true);  //dejar de monitorear archivo

                        tabs.remove(i);
                        indicePestaña.minIndice();
                    }

                } else {
                    if (lienzo.getIdNombre() != 0) {
                        indicePestaña.getPesEliminada().add(lienzo.getIdNombre());
                        indicePestaña.setPesEliminada(indicePestaña.getPesEliminada());
                    }

                    if (tabs.getTitleAt(i).endsWith(".jlefo")) {
                        lienzo.setAnalizar(false);
                        lienzo.setMonitor(true);  //ojo  
                        System.out.println("Cerrar");
                    }

                    if (lienzo.getTipoPanel().equals(AF)) {

                        //eliminar directorios y archivos del tab existente
                        m.deleteRootFolder(lienzo.getName());
                    }
                    tabs.remove(i);
                    indicePestaña.minIndice();
                }
                if (indicePestaña.getIndice() == 0) {
                    tabs.setVisible(false);
                }
            }
        }

        //we don't want to update UI for this button
        @Override
        public void updateUI() {
        }

        //paint the cross
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            //shift the image for pressed buttons
            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            if (getModel().isRollover()) {
                g2.setColor(Color.RED);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(Color.WHITE);
            }
            int delta = 5;
            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
            g2.dispose();
        }
    }

    private final static MouseListener buttonMouseListener = new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(true);
                button.setBackground(Color.red);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(false);
            }
        }
    };

}
