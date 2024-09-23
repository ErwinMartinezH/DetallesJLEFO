/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import control.C_interfaz;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import javax.swing.*;
import static funciones.NmComponentes.*;
import java.awt.Color;
import static java.awt.event.KeyEvent.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author herma
 */
public class V_interfaz extends JFrame {

    public V_interfaz(String[] file) {
        funcion(file);
    }

    private void funcion(String[] file) {
        //interfaz
        setTitle("JLEFO");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(683, 384));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setIconImage(new ImageIcon(getClass().getResource(rutaIconos
                + "icono-32.png")).getImage());
        tabs = new V_tabs();
        tabs.setVisible(false);
        slideMenu = new V_slideMenu(tabs);
        slideMenu.setVisible(false);

        ctrl = new C_interfaz(this, tabs, slideMenu, contenedor, tabs, file);

        componentes();

        addWindowListener(ctrl);
        addWindowStateListener(ctrl);
    }

    private void componentes() {
        //barra de menu
        nuevo.setIcon(new ImageIcon(getClass()
                .getResource(rutaIconos + "nuevo-16.png")));

        af.setAccelerator(KeyStroke.getKeyStroke(VK_A, SHIFT_MASK | CTRL_MASK));
        af.setIcon(new ImageIcon(getClass()
                .getResource(rutaIconos + "automata-16.png")));
        af.setName(AF);
        af.addActionListener(ctrl);
        af.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                controlVisual(true, false, false, false);
            }
        });
        nuevo.add(af);

        er.setAccelerator(KeyStroke.getKeyStroke(VK_E, SHIFT_MASK | CTRL_MASK));
        er.setText(ER);
        er.setName(ER);
        er.setIcon(new ImageIcon(getClass().getResource(rutaIconos + "er-16.png")));
        er.addActionListener(ctrl);
        nuevo.add(er);

        glc.setAccelerator(KeyStroke.getKeyStroke(VK_G, SHIFT_MASK | CTRL_MASK));
        glc.setText(GLC);
        glc.setName(GLC);
        glc.setIcon(new ImageIcon(getClass().getResource(rutaIconos
                + "glc-16.png")));
        glc.addActionListener(ctrl);
        nuevo.add(glc);
        
        adp.setAccelerator(KeyStroke.getKeyStroke(VK_P, SHIFT_MASK | CTRL_MASK));
        adp.setIcon(new ImageIcon(getClass()
                .getResource(rutaIconos + "automata-16.png")));
        adp.setName(ADP);
        adp.addActionListener(ctrl);
        adp.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                controlVisual(true, false, false, false);
            }
        });
        nuevo.add(adp);

        mt.setAccelerator(KeyStroke.getKeyStroke(VK_M, SHIFT_MASK | CTRL_MASK));
        mt.setIcon(new ImageIcon(getClass()
                        .getResource(rutaIconos + "mt-16.png")));
        mt.setName(MT);
        mt.addActionListener(ctrl);
        nuevo.add(mt);

        mtbd.setAccelerator(KeyStroke.getKeyStroke(VK_B, SHIFT_MASK | CTRL_MASK));
        mtbd.setIcon(new ImageIcon(getClass()
                        .getResource(rutaIconos + "mt-16.png")));
        mtbd.setName(MTBD);
        mtbd.addActionListener(ctrl);
        nuevo.add(mtbd);
        
        mtmc.setAccelerator(KeyStroke.getKeyStroke(VK_C, SHIFT_MASK | CTRL_MASK));
        mtmc.setIcon(new ImageIcon(getClass()
                        .getResource(rutaIconos + "mt-16.png")));
        mtmc.setName(MTMC);
        mtmc.addActionListener(ctrl);
        nuevo.add(mtmc);

        mtb.setAccelerator(KeyStroke.getKeyStroke(VK_T, SHIFT_MASK | CTRL_MASK));
        mtb.setIcon(new ImageIcon(getClass()
                        .getResource(rutaIconos + "mt-16.png")));
        mtb.setName(MTB);
        mtb.addActionListener(ctrl);
        nuevo.add(mtb);

        archivo.add(nuevo);
        archivo.add(separador1);

        abrir.setAccelerator(KeyStroke.getKeyStroke(VK_A, CTRL_MASK));
        abrir.setIcon(new ImageIcon(getClass()
                .getResource(rutaIconos + "abrir-16.png")));
        abrir.setName(ABRIR);
        abrir.setText(ABRIR);
        abrir.addActionListener(ctrl);
        archivo.add(abrir);

        guardar.setAccelerator(KeyStroke.getKeyStroke(VK_G, CTRL_MASK));
        guardar.setIcon(new ImageIcon(getClass()
                .getResource(rutaIconos + "guardar-16.png")));
        guardar.setName(GUARDAR);
        guardar.setText(GUARDAR);
        guardar.addActionListener(ctrl);
        archivo.add(guardar);

        guardarComo.setText(GUARDARCOMO);
        guardarComo.setName(GUARDARCOMO);
        guardarComo.addActionListener(ctrl);
        archivo.add(guardarComo);

        deshacer.setAccelerator(KeyStroke.getKeyStroke(VK_Z, CTRL_MASK));
        deshacer.setIcon(new ImageIcon(getClass()
                .getResource(rutaIconos + "deshacer-16.png")));
        deshacer.setName("DeshacerI");
        deshacer.setText("Deshacer");
        deshacer.addActionListener(ctrl);
        editar.add(deshacer);

        rehacer.setAccelerator(KeyStroke.getKeyStroke(VK_Y, CTRL_MASK));
        rehacer.setIcon(new ImageIcon(getClass()
                .getResource(rutaIconos + "rehacer-16.png")));
        rehacer.setName("RehacerI");
        rehacer.setText("Rehacer");
        rehacer.addActionListener(ctrl);
        editar.add(rehacer);

        acercar.setAccelerator(KeyStroke.getKeyStroke(VK_PLUS, CTRL_MASK));
        acercar.setIcon(new ImageIcon(getClass()
                .getResource(rutaIconos + "acercar-16.png")));
        acercar.setName("zoomIn");
        acercar.setText("Acercar");
        acercar.addActionListener(ctrl);
        editar.add(acercar);

        alejar.setAccelerator(KeyStroke.getKeyStroke(VK_MINUS, CTRL_MASK));
        alejar.setIcon(new ImageIcon(getClass()
                .getResource(rutaIconos + "alejar-16.png")));
        alejar.setName("zoomOut");
        alejar.setText("Alejar");
        alejar.addActionListener(ctrl);
        editar.add(alejar);

        tutoriales.setName(TUTORIALES);
        tutoriales.addActionListener(ctrl);
        ayuda.add(tutoriales);

        manual.setName(MANUAL);
        manual.addActionListener(ctrl);
        ayuda.add(manual);

        ejercicios.setName(EJERCICIOS);
        ejercicios.addActionListener(ctrl);
        ayuda.add(ejercicios);

        acercaDe.setName(ACERCADE);
        acercaDe.addActionListener(ctrl);
        ayuda.add(acercaDe);

        barraMenu.add(archivo);
        barraMenu.add(editar);
        barraMenu.add(ayuda);

        setJMenuBar(barraMenu);
        //termina barra de menu

        //barra de herramientas
        p_barraHerr.setBorder(BorderFactory.createEmptyBorder());
        barraHerr.setFloatable(false);
        barraHerr.setRollover(true);
        barraHerr.setBackground(new Color(240, 240, 240));

        menu.setIcon(new ImageIcon(getClass()
                .getResource(rutaIconos + "menu-24.png")));
        menu.setFocusable(false);
        menu.setOpaque(false);
        menu.setHorizontalTextPosition(SwingConstants.CENTER);
        menu.setVerticalTextPosition(SwingConstants.BOTTOM);
        menu.setName(MENU);
        menu.setToolTipText(MENU);
        menu.addActionListener(ctrl);
        barraHerr.add(menu);
        barraHerr.add(separador2);

        b_guardar.setIcon(new ImageIcon(getClass()
                .getResource(rutaIconos + "guardar-24.png")));
        b_guardar.setFocusable(false);
        b_guardar.setOpaque(false);
        b_guardar.setHorizontalTextPosition(SwingConstants.CENTER);
        b_guardar.setVerticalTextPosition(SwingConstants.BOTTOM);
        b_guardar.setName(GUARDAR);
        b_guardar.setToolTipText(GUARDAR);
        b_guardar.addActionListener(ctrl);
        barraHerr.add(b_guardar);

        imagen.setIcon(new ImageIcon(getClass()
                .getResource(rutaIconos + "imagen-24.png")));
        imagen.setFocusable(false);
        imagen.setOpaque(false);
        imagen.setHorizontalTextPosition(SwingConstants.CENTER);
        imagen.setVerticalTextPosition(SwingConstants.BOTTOM);
        imagen.setName(IMAGEN);
        imagen.setToolTipText(IMAGEN);
        imagen.addActionListener(ctrl);
        barraHerr.add(imagen);

        imprimir.setIcon(new ImageIcon(getClass()
                .getResource(rutaIconos + "impresora-24.png")));
        imprimir.setFocusable(false);
        imprimir.setOpaque(false);
        imprimir.setHorizontalTextPosition(SwingConstants.CENTER);
        imprimir.setVerticalTextPosition(SwingConstants.BOTTOM);
        imprimir.setName("Imprimir");
        imprimir.addActionListener(ctrl);
        barraHerr.add(imprimir);

        b_deshacer.setIcon(new ImageIcon(getClass()
                .getResource(rutaIconos + "deshacer-24.png")));
        b_deshacer.setFocusable(false);
        b_deshacer.setOpaque(false);
        b_deshacer.setHorizontalTextPosition(SwingConstants.CENTER);
        b_deshacer.setVerticalTextPosition(SwingConstants.BOTTOM);
        b_deshacer.setName(DESHACER);
        b_deshacer.setToolTipText(DESHACER);
        b_deshacer.addActionListener(ctrl);
        barraHerr.add(b_deshacer);

        b_rehacer.setIcon(new ImageIcon(getClass()
                .getResource(rutaIconos + "rehacer-24.png")));
        b_rehacer.setFocusable(false);
        b_rehacer.setOpaque(false);
        b_rehacer.setHorizontalTextPosition(SwingConstants.CENTER);
        b_rehacer.setVerticalTextPosition(SwingConstants.BOTTOM);
        b_rehacer.setName(REHACER);
        b_rehacer.setToolTipText(REHACER);
        b_rehacer.addActionListener(ctrl);
        barraHerr.add(b_rehacer);

        barraHerr.add(separador3);

        b_acercar.setIcon(new ImageIcon(getClass()
                .getResource(rutaIconos + "acercar-24.png")));
        b_acercar.setFocusable(false);
        b_acercar.setOpaque(false);
        b_acercar.setHorizontalTextPosition(SwingConstants.CENTER);
        b_acercar.setVerticalTextPosition(SwingConstants.BOTTOM);
        b_acercar.setName(ACERCAR);
        b_acercar.setToolTipText(ACERCAR);
        b_acercar.addActionListener(ctrl);
        barraHerr.add(b_acercar);

        b_alejar.setIcon(new ImageIcon(getClass()
                .getResource(rutaIconos + "alejar-24.png")));
        b_alejar.setFocusable(false);
        b_alejar.setOpaque(false);
        b_alejar.setHorizontalTextPosition(SwingConstants.CENTER);
        b_alejar.setVerticalTextPosition(SwingConstants.BOTTOM);
        b_alejar.setName(ALEJAR);
        b_alejar.setToolTipText(ALEJAR);
        b_alejar.addActionListener(ctrl);
        barraHerr.add(b_alejar);

        layoutPanelHerramientas();
        //termina barra de herramientas

        //contenedor - cuerpo
        contenedor.setBackground(Color.white);
        layoutContenedor();

        layouts();

        pack();
    }

    public void controlVisual(boolean b1, boolean b2, boolean b3, boolean b4) {
        menu.setEnabled(b1);
        b_guardar.setEnabled(b2);
        guardar.setEnabled(b2);
        b_deshacer.setEnabled(b3);
        deshacer.setEnabled(b3);
        b_rehacer.setEnabled(b4);
        rehacer.setEnabled(b4);
    }

    //controlador
    private C_interfaz ctrl;

    private final String rutaIconos = "/img_icon/";

    /*Barra de menu*/
    private final JMenuBar barraMenu = new JMenuBar();
    private final JMenu archivo = new JMenu(ARCHIVO);
    private final JMenu editar = new JMenu(EDITAR);
    private final JMenu nuevo = new JMenu(NUEVO);
    private final JMenu ayuda = new JMenu(AYUDA);
    private final JMenuItem af = new JMenuItem(AF);
    private final JMenuItem er = new JMenuItem(ER);
    private final JMenuItem glc = new JMenuItem(GLC);
    private final JMenuItem adp = new JMenuItem(ADP);
    private final JMenuItem mt = new JMenuItem(MT);
    private final JMenuItem mtbd = new JMenuItem(MTBD);
    private final JMenuItem mtb = new JMenuItem(MTB);
    private final JMenuItem mtmc = new JMenuItem(MTMC);
    private final JMenuItem abrir = new JMenuItem(ABRIR);
    public static final JMenuItem guardar = new JMenuItem(GUARDAR);
    private final JMenuItem guardarComo = new JMenuItem(GUARDARCOMO);
    public static final JMenuItem deshacer = new JMenuItem(DESHACER);
    public static final JMenuItem rehacer = new JMenuItem(REHACER);
    private final JMenuItem acercar = new JMenuItem(ACERCAR);
    private final JMenuItem alejar = new JMenuItem(ALEJAR);
    private final JMenuItem tutoriales = new JMenuItem(TUTORIALES);
    private final JMenuItem manual = new JMenuItem(MANUAL);
    private final JMenuItem ejercicios = new JMenuItem(EJERCICIOS);
    private final JMenuItem acercaDe = new JMenuItem(ACERCADE);

    /*Barra de herramientas*/
    private final JToolBar barraHerr = new JToolBar();
    private final JPanel p_barraHerr = new JPanel();
    public static final JButton menu = new JButton();
    public static final JButton b_deshacer = new JButton();
    private final JButton imagen = new JButton();
    private final JButton imprimir = new JButton();
    public static final JButton b_rehacer = new JButton();
    public static final JButton b_guardar = new JButton();
    private final JButton b_acercar = new JButton();
    private final JButton b_alejar = new JButton();
    private final JPopupMenu.Separator separador1 = new JPopupMenu.Separator();
    private final JToolBar.Separator separador2 = new JToolBar.Separator();
    private final JToolBar.Separator separador3 = new JToolBar.Separator();

    /*Cuerpo*/
    private final JPanel contenedor = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            //Acciones iniciales de atajos de teclado
            super.paintComponent(g);
            ImageIcon img_pane;
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            if (screenSize.getWidth() < 1920) {
                img_pane = new ImageIcon(getClass()
                        .getResource(rutaIconos + "panel_Inicio2.png"));
            } else {
                img_pane = new ImageIcon(getClass()
                        .getResource(rutaIconos + "panel_Inicio1.png"));
            }
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.drawImage(img_pane.getImage(), 0, 0,
                    img_pane.getIconWidth(),
                    img_pane.getIconHeight(), this);
        }

    };
    private V_slideMenu slideMenu;
    private V_tabs tabs;

    private void layoutContenedor() {
        GroupLayout panel_contenedorLayout = new GroupLayout(contenedor);
        contenedor.setLayout(panel_contenedorLayout);
        panel_contenedorLayout.setHorizontalGroup(panel_contenedorLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(panel_contenedorLayout.createSequentialGroup()
                        .addComponent(tabs)
                        .addGap(0, 0, 0))
        );
        panel_contenedorLayout.setVerticalGroup(panel_contenedorLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(tabs)
        );
    }

    private void layoutPanelHerramientas() {
        GroupLayout jPanel1Layout = new GroupLayout(p_barraHerr);
        p_barraHerr.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(barraHerr, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(barraHerr, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );
    }

    private void layouts() {
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(p_barraHerr, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(slideMenu, GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(contenedor, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(p_barraHerr, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(slideMenu, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(contenedor, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
    }

}
