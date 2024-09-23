/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import control.C_automata;
import control.C_automataP;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.StringCharacterIterator;
import static javax.swing.BorderFactory.createLineBorder;
import static javax.swing.BorderFactory.createTitledBorder;
import static funciones.NmComponentes.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import modelo.M_colores;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

/**
 *
 * @author herma
 */
public class V_rastreo extends JInternalFrame {

    private C_automata ctrl;
    private C_automataP ctrlP;
    private StringCharacterIterator cadena = new StringCharacterIterator("");
    private short posicion = -1;
    private boolean min = true;
    private boolean max = true;
    private final String rutaIcono = "/img_icon/";

    public V_rastreo() {
        componentes();
    }

    void cargarControlador(C_automata ctrl) {
        this.ctrl = ctrl;
        b_Afd.addActionListener(ctrl);
        b_Ordenar_cadenas.addActionListener(ctrl);
        b_rastreo_paso.addActionListener(ctrl);
        slider.addMouseListener(ctrl);
        tablaCadenas.addMouseListener(ctrl);
    }

    void cargarControlador(C_automataP ctrlP) {
        this.ctrlP = ctrlP;
//        b_Afd.addActionListener(ctrl);
//        b_Ordenar_cadenas.addActionListener(ctrl);
//        b_rastreo_paso.addActionListener(ctrl);
        slider.addMouseListener(ctrlP);
        tablaCadenas.addMouseListener(ctrlP);
    }

    public String getEstatus() {
        return tablaCadenas.getColumnName(tablaCadenas.getSelectedColumn());
    }

    public int getVelocidad() {
        return slider.getValue();
    }

    public void setCadena(String cad) {
        cadena.setText(cad);
    }

    public JPanel getPanel() {
        return panelCadena;
    }

    public void setPosicion(short pos) {
        posicion = pos;
    }

    public short getPosicion() {
        return posicion;
    }

    public JButton getB_Afd() {
        return b_Afd;
    }

    public JButton getB_Ordenar_cadenas() {
        return b_Ordenar_cadenas;
    }

    public void setModelTC(DefaultTableModel t) {
        tablaCadenas.setModel(t);
    }

    public void setModelTT(DefaultTableModel t) {
        tablaTransiciones.setModel(t);
    }

    private void componentes() {

        b_Afd.setName(AFD);
        b_Afd.setToolTipText("Convertir AFND a AFD");
        b_Afd.addActionListener(ctrl);

        b_Ordenar_cadenas.setName(ORDENAR);
        b_Ordenar_cadenas.setToolTipText("Ordenar cadenas");
        b_Ordenar_cadenas.setIcon(new ImageIcon(getClass()
                .getResource(rutaIcono + "ordenar-16.png")));
        b_Ordenar_cadenas.addActionListener(ctrl);

        b_rastreo_paso.setName(PASO_A_PASO);
        b_rastreo_paso.setToolTipText("Rastrear Paso a Paso");
        b_rastreo_paso.addActionListener(ctrl);

        b_Cerrar.setName(CERRAR);
        b_Cerrar.setToolTipText(CERRAR);
        b_Cerrar.setFocusable(false);
        b_Cerrar.setOpaque(false);

        b_Minimizar.setName(MINIMIZAR);
        b_Minimizar.setToolTipText(MINIMIZAR);
        b_Minimizar.setFocusable(false);
        b_Minimizar.setOpaque(false);

        b_Maximizar.setName(MAXIMIZAR);
        b_Maximizar.setToolTipText(MAXIMIZAR);
        b_Maximizar.setFocusable(false);
        b_Maximizar.setOpaque(false);

        tablaTransiciones.setToolTipText("Tabla de Transiciones");
        tablaCadenas.setToolTipText("Selecciona una cadena");
        tablaCadenas.setCellSelectionEnabled(true);

        slider.setToolTipText(SLIDER);
        slider.addMouseListener(ctrl);
        slider.setName(SLIDER);

        setResizable(true);
        setTitle("Rastreo");
        setMaximumSize(new Dimension(530, 600));
        setMinimumSize(new Dimension(530, 360));
        setFrameIcon(new ImageIcon(getClass().getResource(rutaIcono + "icono-32.png")));
        setPreferredSize(new Dimension(530, 360));
        setAutoscrolls(true);
        setBorder(null);

        p_Scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        p_Scroll.setAutoscrolls(true);
        p_Scroll.setPreferredSize(new Dimension(520, 350));
        p_Scroll.getVerticalScrollBar().setUnitIncrement(15);

        p_Contenedor.setAutoscrolls(true);
        p_Contenedor.setBorder(createLineBorder(new Color(204, 204, 204)));
        p_Contenedor.setPreferredSize(new Dimension(510, 568));

        tablaTransiciones.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ESTADO", " Σ={0}", "Σ={1}", "FINAL"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                false, false, false, false
            };

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        tablaTransiciones.setGridColor(new java.awt.Color(235, 234, 234));
        tablaTransiciones.getTableHeader().setReorderingAllowed(false);
        contTrans.setViewportView(tablaTransiciones);

        tablaCadenas.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ACEPTA", "NO ACEPTA"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                false, false
            };

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        tablaCadenas.setGridColor(new Color(235, 234, 234));
        tablaCadenas.addMouseListener(ctrl);
        tablaCadenas.setName(TABLACADENAS);
        tablaCadenas.setSelectionBackground(Color.lightGray);
        tablaCadenas.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contCad.setViewportView(tablaCadenas);

        etq_Trans.setFont(new Font("Dialog", 0, 18));
        etq_Cad.setFont(new Font("Dialog", 0, 18));
        etq1.setFont(new Font("Dialog", 1, 14));

        panelCadena.setBackground(new Color(239, 239, 239));
        panelCadena.setToolTipText("Cadena a rastrear");
        panelCadena.setBorder(createLineBorder(new Color(204, 0, 0)));

        panel_rastreo.setBorder(createTitledBorder(null, "Rastreo",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("Ebrima", 1, 12)));
        panel_rastreo.setLayout(new AbsoluteLayout());

        panel_rastreo.add(b_rastreo_paso, new AbsoluteConstraints(300, 50, 120, -1));
        panel_rastreo.add(etq1, new AbsoluteConstraints(90, 20, -1, -1));
        panel_rastreo.add(etq4, new AbsoluteConstraints(210, 80, -1, -1));
        panel_rastreo.add(etq3, new AbsoluteConstraints(20, 80, -1, -1));
        panel_rastreo.add(etq2, new AbsoluteConstraints(120, 80, -1, -1));
        panel_rastreo.add(slider, new AbsoluteConstraints(30, 50, -1, -1));
        panel_rastreo.add(panelCadena, new AbsoluteConstraints(30, 110, 420, -1));

        p_Colores.setBorder(createTitledBorder(null, "Simbología",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI Black", 0, 12)));

        L_Ed_Inicio.setBackground(new Color(220, 202, 82));
        L_Ed_Inicio.setFont(new Font("Ebrima", 1, 10));
        L_Ed_Inicio.setForeground(new Color(255, 255, 255));
        L_Ed_Inicio.setHorizontalAlignment(SwingConstants.CENTER);
        L_Ed_Inicio.setOpaque(true);
        L_Ed_Inicio.setPreferredSize(new Dimension(67, 14));

        L_Ed_Aceptacion.setBackground(new Color(0, 251, 80));
        L_Ed_Aceptacion.setFont(new Font("Ebrima", 1, 10));
        L_Ed_Aceptacion.setForeground(new Color(255, 255, 255));
        L_Ed_Aceptacion.setHorizontalAlignment(SwingConstants.CENTER);
        L_Ed_Aceptacion.setOpaque(true);
        L_Ed_Aceptacion.setPreferredSize(new Dimension(67, 14));

        L_Ed_NoAceptacion.setBackground(new Color(190, 60, 78));
        L_Ed_NoAceptacion.setFont(new Font("Ebrima", 1, 10));
        L_Ed_NoAceptacion.setForeground(new Color(255, 255, 255));
        L_Ed_NoAceptacion.setHorizontalAlignment(SwingConstants.CENTER);
        L_Ed_NoAceptacion.setOpaque(true);
        L_Ed_NoAceptacion.setPreferredSize(new Dimension(67, 14));

        L_Transicion.setBackground(new Color(240, 163, 31));
        L_Transicion.setFont(new java.awt.Font("Ebrima", 1, 10));
        L_Transicion.setForeground(new Color(255, 255, 255));
        L_Transicion.setHorizontalAlignment(SwingConstants.CENTER);
        L_Transicion.setOpaque(true);
        L_Transicion.setPreferredSize(new Dimension(67, 14));

        b_Cerrar.setIcon(new ImageIcon(getClass().getResource(rutaIcono + "cerrar.png")));
        b_Cerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                acciones(evt);
            }
        });

        b_Minimizar.setIcon(new ImageIcon(getClass().getResource(rutaIcono + "minimizar.png")));
        b_Minimizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                acciones(evt);
            }
        });

        b_Maximizar.setIcon(new ImageIcon(getClass().getResource(rutaIcono + "maximizar.png")));
        b_Maximizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                acciones(evt);
            }
        });

        layouts();
    }

    private void acciones(ActionEvent e) {
        Component cmp = (Component) e.getSource();
        switch (cmp.getName()) {
            case MAXIMIZAR:
                if (max) {
                    setSize(new Dimension(530, 600));
                    max = false;
                    min = false;
                } else {
                    setSize(new Dimension(530, 360));
                    max = true;
                    min = true;
                }
                break;
            case MINIMIZAR:
                if (min) {
                    setSize(new Dimension(530, 65));
                    min = false;
                    max = false;
                } else {
                    setSize(new Dimension(530, 360));
                    min = true;
                    max = true;
                }
                break;
            case CERRAR:
                min = true;
                max = true;
                this.setVisible(false);
                ctrl.setRastreoActivo(false);
                break;
        }
    }

    private boolean fin = false;
    
    public boolean getFin(){
        return fin;
    }
    
    public void setFin(boolean fin){
        this.fin = fin;
    }

    private void repintar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(M_colores.NORM_TRANSICION);
        short x = 10;
        if (!cadena.toString().isEmpty()) {
            g2.setFont(new Font("Arial", Font.BOLD, 22));
            for (int i = 0; i < cadena.getEndIndex(); i++) {
                cadena.setIndex(i);
                if (cadena.getIndex() == posicion) {
                    g2.setColor(M_colores.SEARCH_TRANSICION);
                    g2.drawString(String.valueOf(cadena.current()), x,
                            panelCadena.getHeight() - 10);
                    g2.setColor(M_colores.NORM_TRANSICION);
                } else {
                    g2.drawString(String.valueOf(cadena.current()), x,
                            panelCadena.getHeight() - 10);
                }
                x += 14;
            }
            posicion = -1;
            if (fin) {
                switch (ctrl.getEstatusCadena()) {
                    case "ACEPTA":
                        g2.setColor(M_colores.FINAL_ESTADO);
                        x = 10;
                        for (int i = 0; i < cadena.getEndIndex(); i++) {
                            cadena.setIndex(i);
                            g2.drawString(String.valueOf(cadena.current()), x,
                                    panelCadena.getHeight() - 10);
                            x += 14;
                        }
                        break;
                    case "NO ACEPTA":
                        g2.setColor(M_colores.NOFINAL_ESTADO);
                        x = 10;
                        for (int i = 0; i < cadena.getEndIndex(); i++) {
                            cadena.setIndex(i);
                            g2.drawString(String.valueOf(cadena.current()), x,
                                    panelCadena.getHeight() - 10);
                            x += 14;
                        }
                        break;
                }

            }
        }
    }

    public String getCadena() {
        if (tablaCadenas.getValueAt(tablaCadenas.getSelectedRow(),
                tablaCadenas.getSelectedColumn()) != null) {
            return tablaCadenas.getValueAt(tablaCadenas.getSelectedRow(),
                    tablaCadenas.getSelectedColumn()).toString();
        } else {
            return "";
        }
    }

    //cuerpo
    private final JPanel p_Contenedor = new JPanel();
    private final JScrollPane p_Scroll = new JScrollPane();

    //opciones de la ventana
    private final JButton b_Cerrar = new JButton();
    private final JButton b_Minimizar = new JButton();
    private final JButton b_Maximizar = new JButton();

    //tabla transiciones
    private final JLabel etq_Trans = new JLabel("Transiciones");
    private final JScrollPane contTrans = new JScrollPane();
    private final JTable tablaTransiciones = new JTable();

    //tabla cadenas
    private final JLabel etq_Cad = new JLabel("Cadenas");
    private final JScrollPane contCad = new JScrollPane();
    private final JTable tablaCadenas = new JTable();

    //conversion y ordenamiento
    private final JButton b_Afd = new JButton(AFD);
    private final JButton b_Ordenar_cadenas = new JButton(ORDENAR);

    //Rastreo paso a paso
    private final JButton b_rastreo_paso = new JButton(PASO_A_PASO);
    private final JLabel etq1 = new JLabel("Velocidad");
    private final JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 2, 1);
    private final JLabel etq2 = new JLabel("Lento");
    private final JLabel etq3 = new JLabel("Normal");
    private final JLabel etq4 = new JLabel("Rápido");
    private final JLabel L_Ed_Aceptacion = new JLabel("Aceptación");
    private final JLabel L_Ed_Inicio = new JLabel("Estado de Transición");
    private final JLabel L_Ed_NoAceptacion = new JLabel("No aceptación");
    private final JLabel L_Transicion = new JLabel("Transición");
    private final JPanel panel_rastreo = new JPanel();
    private final JPanel p_Colores = new JPanel();
    private final JPanel panelCadena = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            repintar(g);
        }
    };

    private void layouts() {

        GroupLayout panelCadenaLayout = new GroupLayout(panelCadena);
        panelCadena.setLayout(panelCadenaLayout);
        panelCadenaLayout.setHorizontalGroup(
                panelCadenaLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGap(0, 436, Short.MAX_VALUE)
        );
        panelCadenaLayout.setVerticalGroup(
                panelCadenaLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 42, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout p_ColoresLayout = new GroupLayout(p_Colores);
        p_Colores.setLayout(p_ColoresLayout);
        p_ColoresLayout.setHorizontalGroup(
                p_ColoresLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(p_ColoresLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(L_Ed_Inicio, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(L_Transicion, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(L_Ed_Aceptacion, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(L_Ed_NoAceptacion, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        p_ColoresLayout.setVerticalGroup(
                p_ColoresLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, p_ColoresLayout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(p_ColoresLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(L_Transicion)
                                        .addComponent(L_Ed_Inicio, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(L_Ed_Aceptacion, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(L_Ed_NoAceptacion))
                                .addContainerGap())
        );

        GroupLayout panel_rastreoLayout = new GroupLayout(panel_rastreo);
        panel_rastreo.setLayout(panel_rastreoLayout);
        panel_rastreoLayout.setHorizontalGroup(
                panel_rastreoLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(panel_rastreoLayout.createSequentialGroup()
                                .addGroup(panel_rastreoLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(panel_rastreoLayout.createSequentialGroup()
                                                .addGap(25, 25, 25)
                                                .addComponent(slider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(panel_rastreoLayout.createSequentialGroup()
                                                .addGap(92, 92, 92)
                                                .addComponent(etq1)))
                                .addGap(80, 80, 80)
                                .addComponent(b_rastreo_paso, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, panel_rastreoLayout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(p_Colores, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36))
                        .addGroup(panel_rastreoLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panel_rastreoLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(panel_rastreoLayout.createSequentialGroup()
                                                .addComponent(panelCadena, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(12, 12, 12))
                                        .addGroup(panel_rastreoLayout.createSequentialGroup()
                                                .addComponent(etq2)
                                                .addGap(66, 66, 66)
                                                .addComponent(etq3)
                                                .addGap(66, 66, 66)
                                                .addComponent(etq4)
                                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        panel_rastreoLayout.setVerticalGroup(
                panel_rastreoLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(panel_rastreoLayout.createSequentialGroup()
                                .addGroup(panel_rastreoLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(panel_rastreoLayout.createSequentialGroup()
                                                .addComponent(etq1)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(slider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(panel_rastreoLayout.createSequentialGroup()
                                                .addGap(11, 11, 11)
                                                .addComponent(b_rastreo_paso)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panel_rastreoLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(etq2)
                                        .addComponent(etq3)
                                        .addComponent(etq4))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(p_Colores, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panelCadena, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout p_CotainerLayout = new GroupLayout(p_Contenedor);
        p_Contenedor.setLayout(p_CotainerLayout);
        p_CotainerLayout.setHorizontalGroup(
                p_CotainerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, p_CotainerLayout.createSequentialGroup()
                                .addGroup(p_CotainerLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(GroupLayout.Alignment.LEADING, p_CotainerLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(panel_rastreo, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(p_CotainerLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(p_CotainerLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                        .addGroup(p_CotainerLayout.createSequentialGroup()
                                                                .addGap(54, 54, 54)
                                                                .addComponent(b_Afd, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(b_Ordenar_cadenas, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(51, 51, 51))
                                                        .addGroup(p_CotainerLayout.createSequentialGroup()
                                                                .addComponent(contTrans, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(contCad, GroupLayout.PREFERRED_SIZE, 232, GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(p_CotainerLayout.createSequentialGroup()
                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                .addComponent(b_Minimizar, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(b_Maximizar, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(b_Cerrar, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))))
                                        .addGroup(p_CotainerLayout.createSequentialGroup()
                                                .addGap(74, 74, 74)
                                                .addComponent(etq_Trans)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(etq_Cad)
                                                .addGap(78, 78, 78)))
                                .addGap(24, 24, 24))
        );
        p_CotainerLayout.setVerticalGroup(
                p_CotainerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(p_CotainerLayout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addGroup(p_CotainerLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(b_Minimizar)
                                        .addComponent(b_Maximizar)
                                        .addComponent(b_Cerrar))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(p_CotainerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(etq_Trans)
                                        .addComponent(etq_Cad, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(p_CotainerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(contTrans, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(contCad, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(p_CotainerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(b_Ordenar_cadenas)
                                        .addComponent(b_Afd))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panel_rastreo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33))
        );

        p_Scroll.setViewportView(p_Contenedor);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(p_Scroll, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(p_Scroll, GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
        );

        pack();
    }

}
