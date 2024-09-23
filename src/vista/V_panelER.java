package vista;

import control.C_panelER;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import static funciones.NmComponentes.*;
import funciones.botonesLib.RSButtonMetro;

public class V_panelER extends JPanel {

    private ArrayList<String> valido;
    private ArrayList<String> nvalido;
    private C_panelER ctrlER;

    public V_panelER() {
        componentes();
    }

    public JTable getTablaCadenas() {
        return tablaCadenas;
    }

    public void setTablaCadenas(JTable tablaCadenas) {
        this.tablaCadenas = tablaCadenas;
    }

    public JLabel getEtqColumna() {
        return etqColumna;
    }

    public JLabel getEtqError() {
        return etqError;
    }

    public void setVisibleEtqError(boolean b) {
        etqColumna.setVisible(b);
    }

    public JTextField getEntrada() {
        return entrada;
    }

    public void setjTextER(String c) {
        entrada.setText(c);
    }

    public RSButtonMetro getrSButAFD() {
        return b_afd;
    }

    public void setrSButAFD(boolean c) {
        b_afd.setEnabled(c);
    }

    public RSButtonMetro getrSButC() {
        return b_borrar;
    }

    public void setrSButC(String c) {
        entrada.setText(c);
    }

    public RSButtonMetro getrSButCE() {
        return b_eliminar;
    }

    public void setrSButCE(RSButtonMetro rSButCE) {
        this.b_eliminar = rSButCE;
    }

    public RSButtonMetro getrSButCero() {
        return b_cero;
    }

    public void setrSButCero(RSButtonMetro rSButCero) {
        this.b_cero = rSButCero;
    }

    public RSButtonMetro getrSButEstrPositiva() {
        return b_mas;
    }

    public void setrSButEstrPositiva(RSButtonMetro rSButEstrPositiva) {
        this.b_mas = rSButEstrPositiva;
    }

    public RSButtonMetro getrSButEstrella() {
        return b_asterisco;
    }

    public void setrSButEstrella(RSButtonMetro rSButEstrella) {
        this.b_asterisco = rSButEstrella;
    }

    public RSButtonMetro getrSButGO() {
        return b_ejecutar;
    }

    public void setrSButGO(RSButtonMetro rSButGO) {
        this.b_ejecutar = rSButGO;
    }

    public RSButtonMetro getrSButIntero() {
        return b_orden;
    }

    public void setrSButIntero(boolean b) {
        b_orden.setEnabled(b);
    }

    public RSButtonMetro getrSButParenDer() {
        return b_parend;
    }

    public void setrSButParenDer(RSButtonMetro rSButParenDer) {
        this.b_parend = rSButParenDer;
    }

    public RSButtonMetro getrSButParenIzq() {
        return b_pareni;
    }

    public void setrSButParenIzq(RSButtonMetro rSButParenIzq) {
        this.b_pareni = rSButParenIzq;
    }

    public RSButtonMetro getrSButPipe() {
        return b_o;
    }

    public void setrSButPipe(RSButtonMetro rSButPipe) {
        this.b_o = rSButPipe;
    }

    public RSButtonMetro getrSButUno() {
        return b_uno;
    }

    public void setrSButUno(RSButtonMetro rSButUno) {
        this.b_uno = rSButUno;
    }

    public ArrayList<String> getValido() {
        return valido;
    }

    public void setValido(String cadena) {
        valido.add(cadena);
    }

    public ArrayList<String> getNvalido() {
        return nvalido;
    }

    public void setNvalido(String cadenan) {
        nvalido.add(cadenan);
    }

    public JTextPane getP_TextInfo() {
        return txt_informacion;
    }

    private void componentes() {

        valido = new ArrayList<>();
        nvalido = new ArrayList<>();

        ctrlER = new C_panelER(this);

        setBackground(new Color(53, 60, 81));
        setPreferredSize(new Dimension(1180, 595));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        etq.setFont(new Font("Arial", Font.BOLD, 14));
        etq.setForeground(new Color(255, 255, 255));
        etq.setHorizontalAlignment(SwingConstants.CENTER);
        add(etq, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 19, 260, 49));

        entrada.setToolTipText("Ingresar Expresión Regular");
        entrada.requestFocus();
        entrada.setBackground(new java.awt.Color(255, 255, 255));
        entrada.setFont(new Font("Arial", Font.BOLD, 18));
        entrada.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        entrada.setBorder(null);
        entrada.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        entrada.setDisabledTextColor(new Color(255, 255, 255));
        entrada.addCaretListener(ctrlER);
        entrada.setFocusable(true);
        entrada.setName("texer");
        add(entrada, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 74, 260, 34));

        tablaCadenas.setToolTipText("Tabla de Cadenas");
        tablaCadenas.setAutoCreateRowSorter(true);
        tablaCadenas.setBackground(new Color(159, 176, 220));
        tablaCadenas.setFont(new Font("Arial", Font.BOLD, 12));
        tablaCadenas.setForeground(new Color(0, 0, 0));
        tablaCadenas.setFocusable(false);

        tablaCadenas.setModel(new DefaultTableModel(new Object[][]{},
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

        tablaCadenas.setCellSelectionEnabled(true);
        tablaCadenas.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        tablaCadenas.setDragEnabled(true);
        tablaCadenas.setGridColor(new Color(255, 255, 255));
        tablaCadenas.setRowHeight(20);
        panelScroll1.setViewportView(tablaCadenas);
        panelScroll1.setFocusable(false);
        add(panelScroll1, new org.netbeans.lib.awtextra.AbsoluteConstraints(101, 177, 380, 360));

        b_cero.setToolTipText("Alfabeto");
        b_cero.setFont(new Font("Arial", Font.BOLD, 24));
        b_cero.setFocusable(false);
        b_cero.addActionListener(ctrlER);
        b_cero.setName(CERO);
        add(b_cero, new org.netbeans.lib.awtextra.AbsoluteConstraints(583, 324, 67, -1));

        b_uno.setToolTipText("aLfabeto");
        b_uno.setFont(new Font("Arial", Font.BOLD, 24));
        b_uno.setFocusable(false);
        b_uno.addActionListener(ctrlER);
        b_uno.setName(UNO);
        add(b_uno, new org.netbeans.lib.awtextra.AbsoluteConstraints(662, 324, 67, -1));

        b_pareni.setToolTipText("Operador");
        b_pareni.setFont(new Font("Arial", Font.BOLD, 24));
        b_pareni.setFocusable(false);
        b_pareni.addActionListener(ctrlER);
        b_pareni.setName(PARENI);
        add(b_pareni, new org.netbeans.lib.awtextra.AbsoluteConstraints(583, 393, 67, -1));

        b_parend.setToolTipText("Operador");
        b_parend.setFont(new Font("Arial", Font.BOLD, 24));
        b_parend.setFocusable(false);
        b_parend.addActionListener(ctrlER);
        b_parend.setName(PAREND);
        add(b_parend, new org.netbeans.lib.awtextra.AbsoluteConstraints(741, 393, 67, -1));

        b_mas.setToolTipText("Operador");
        b_mas.setFont(new Font("Arial", Font.BOLD, 24));
        b_mas.setFocusable(false);
        b_mas.addActionListener(ctrlER);
        b_mas.setName(MAS);
        add(b_mas, new org.netbeans.lib.awtextra.AbsoluteConstraints(662, 454, 67, -1));

        b_orden.setToolTipText("Odenar/Desordenar");
        b_orden.setIcon(new ImageIcon(getClass()
                .getResource("/img_icon/ordenar-24.png")));
        b_orden.setFont(new Font("Arial", Font.BOLD, 12));
        b_orden.setOpaque(false);
        b_orden.setEnabled(false);
        b_orden.setFocusable(false);
        b_orden.addActionListener(ctrlER);
        b_orden.setName(ORDEN);
        add(b_orden, new org.netbeans.lib.awtextra.AbsoluteConstraints(371, 543, 110, -1));

        b_o.setToolTipText("Operador");
        b_o.setFont(new Font("Arial", Font.BOLD, 24));
        b_o.setFocusable(false);
        b_o.addActionListener(ctrlER);
        b_o.setName(O);
        add(b_o, new org.netbeans.lib.awtextra.AbsoluteConstraints(662, 393, 67, -1));

        b_asterisco.setToolTipText("Operador");
        b_asterisco.setFont(new Font("Arial", Font.BOLD, 24));
        b_asterisco.setFocusable(false);
        b_asterisco.addActionListener(ctrlER);
        b_asterisco.setName(ASTERISCO);
        add(b_asterisco, new org.netbeans.lib.awtextra.AbsoluteConstraints(583, 454, 67, -1));

        b_borrar.setToolTipText("Borrar carácter");
        b_borrar.setColorNormal(new Color(42, 173, 212));
        b_borrar.setFont(new Font("Arial", Font.BOLD, 12));
        b_borrar.setFocusable(false);
        b_borrar.addActionListener(ctrlER);
        b_borrar.setName(BORRAR);
        add(b_borrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(741, 324, 67, -1));

        b_eliminar.setToolTipText("Eliminar cadena");
        b_eliminar.setColorNormal(new Color(42, 173, 212));
        b_eliminar.setFont(new Font("Arial", Font.BOLD, 14));
        b_eliminar.setFocusable(false);
        b_eliminar.addActionListener(ctrlER);
        b_eliminar.setName(ELIMINAR);
        add(b_eliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(583, 260, 110, -1));

        b_ejecutar.setToolTipText("Realizar análisis");
        b_ejecutar.setColorNormal(new Color(42, 173, 212));
        b_ejecutar.setFont(new Font("Arial", Font.BOLD, 14));
        b_ejecutar.setFocusable(false);
        b_ejecutar.addActionListener(ctrlER);
        b_ejecutar.setName(EJECUTAR);
        add(b_ejecutar, new org.netbeans.lib.awtextra.AbsoluteConstraints(699, 260, 110, -1));

        b_afd.setToolTipText("Convertir e.r. a AFD");
        b_afd.setEnabled(true);
        b_afd.setColorNormal(new Color(42, 173, 212));
        b_afd.setFont(new Font("Arial", Font.BOLD, 24));
        b_afd.setFocusable(false);
        b_afd.addActionListener(ctrlER);
        b_afd.setName(AFD);
        add(b_afd, new org.netbeans.lib.awtextra.AbsoluteConstraints(741, 454, 67, -1));

        etqColumna.setFont(new Font("Arial", Font.BOLD, 13));
        etqColumna.setForeground(new Color(244, 170, 35));
        etqColumna.setHorizontalAlignment(SwingConstants.CENTER);
        etqColumna.setFocusable(false);
        add(etqColumna, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 120, 140, 30));

        etqError.setBackground(new Color(255, 148, 148));
        etqError.setFont(new Font("Dialog", 1, 11));
        etqError.setForeground(new Color(255, 255, 255));
        etqError.setHorizontalAlignment(SwingConstants.CENTER);
        etqError.setBorder(new LineBorder(new Color(204, 204, 204), 1, true));
        etqError.setOpaque(true);
        etqError.setVisible(false);
        etqError.setFocusable(false);
        add(etqError, new org.netbeans.lib.awtextra.AbsoluteConstraints(478, 74, 620, 34));

        panelScroll2.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panelScroll2.setFocusable(false);

        txt_informacion.setBorder(BorderFactory.createTitledBorder(null,
                "INFORMACIÓN", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14), Color.black));
        txt_informacion.setBackground(new Color(159, 176, 220));
        txt_informacion.setFont(new Font("Arial", Font.BOLD, 12));
        txt_informacion.setText(ER_INFO);
        txt_informacion.setFocusable(false);
        panelScroll2.setViewportView(txt_informacion);
        add(panelScroll2, new org.netbeans.lib.awtextra.AbsoluteConstraints(884, 260, 226, 277));
    }

    private final JLabel etq = new JLabel("Ingrese una Expresión Regular");
    private JLabel etqColumna = new JLabel();
    private JLabel etqError = new JLabel();
    private final JScrollPane panelScroll1 = new JScrollPane();
    private JScrollPane panelScroll2 = new JScrollPane();
    private JTable tablaCadenas = new JTable();
    private JTextField entrada = new JTextField();
    private RSButtonMetro b_afd = new RSButtonMetro(AFD_FUNCION);
    private RSButtonMetro b_borrar = new RSButtonMetro(BORRAR);
    private RSButtonMetro b_eliminar = new RSButtonMetro(ELIMINAR);
    private RSButtonMetro b_cero = new RSButtonMetro(CERO);
    private RSButtonMetro b_mas = new RSButtonMetro(MAS);
    private RSButtonMetro b_asterisco = new RSButtonMetro(ASTERISCO);
    private RSButtonMetro b_ejecutar = new RSButtonMetro(EJECUTAR);
    private RSButtonMetro b_orden = new RSButtonMetro(ORDEN);
    private RSButtonMetro b_parend = new RSButtonMetro(PAREND);
    private RSButtonMetro b_pareni = new RSButtonMetro(PARENI);
    private RSButtonMetro b_o = new RSButtonMetro(O);
    private RSButtonMetro b_uno = new RSButtonMetro(UNO);
    private JTextPane txt_informacion = new JTextPane();

}
