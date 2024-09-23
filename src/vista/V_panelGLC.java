package vista;

import funciones.glc.*;
import funciones.adp.*;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import funciones.conv_sint.Con_GRtoADP2;

public class V_panelGLC extends JPanel {// panel grafico de el lenguaje de gramatica libre de contexto

    /*
     * Se establecera en el panel un cuadro con 2 columnas, la primer columna
     * es para los no terminales y la segunda columna son para los terminales
     */

    private JPanel panel;// panel principal de la tabla
    private JPanel panel2;// Panel para los botones
    private static JPanel panel3;// Panel para el arbol de derivacion
    public static JFrame frame;// Panel para la ventana del arbol de derivacion
    public static JFrame frameConversionGLCAunADP;// Panel para la ventana de la conversion de una GLC a un ADP
    private JTextField cadena;// Cadena a evaluar
    private static JTable tabla;
    private JScrollPane scroll;
    private JButton b_probar;
    private JButton b_eliminartb;
    private JButton b_añadir;
    private JButton b_eliminarF;
    private JButton b_crearArbol;
    private JButton b_nuevo;
    private JButton b_conversionADO;

    private String sResultadoConversion;

    public V_panelGLC() {
        componentes();
        accionRaton();
        accionBotones();
        dibujarArbol();

    }



    private void componentes() {
        panel = new JPanel();
        panel.setBorder(new TitledBorder("Lenguaje de Gramática Libre de Contexto"));
        panel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.setBackground(new Color(255, 255, 255, 255));
        panel.setPreferredSize(new Dimension(1200, 500));
        panel.setLayout(new BorderLayout());
        // Posicionar la tabla en el centro del panel
        tabla = new JTable();
        tabla.setFont(new Font("Arial", Font.BOLD, 14));
        tabla.setBackground(new Color(255, 255, 255));
        tabla.setForeground(new Color(0, 0, 0));
        tabla.setShowGrid(true);
        tabla.setGridColor(new Color(0, 0, 0));
        tabla.setShowHorizontalLines(true);
        tabla.setShowVerticalLines(true);
        tabla.setPreferredScrollableViewportSize(new Dimension(650, 400));
        tabla.setFillsViewportHeight(true);
        scroll = new JScrollPane(tabla);
        panel.add(scroll);
        this.add(panel);
        // Agregar botones abajo de la tabla: agregar, eliminar, editar, guardar, abrir,
        // nuevo
        panel2 = new JPanel();
        panel2.setLayout(new FlowLayout());
        cadena = new JTextField(20);
        b_probar = new JButton("Ejecutar");
        b_crearArbol = new JButton("Mostrar árbol de derivación");
        b_eliminartb = new JButton("Eliminar datos en tabla");
        b_añadir = new JButton("Añadir filas");
        b_eliminarF = new JButton("Eliminar fila");
        b_nuevo = new JButton("Nuevo");
        b_conversionADO = new JButton("Convertir a ADP");
        panel2.add(new JLabel("Cadena a evaluar: "));
        panel2.add(cadena);
        panel2.add(b_probar);
        panel2.add(b_crearArbol);
        panel2.add(b_eliminartb);
        panel2.add(b_añadir);
        panel2.add(b_eliminarF);
        //panel2.add(b_nuevo);
        panel2.add(b_conversionADO);
        panel.add(panel2, BorderLayout.SOUTH);

        // Añadimos un ejemplo de gramatica libre de contexto a la tabla
        String[] columnas = { "No terminales", "Terminales" };
        String[][] datos = { { "S", "ab" }, { "S", "cba" }, { "S", "acb" } };
        tabla.setModel(new DefaultTableModel(datos, columnas));
        // tabla.setColumnModel(setFont(new Font("Arial",Font.BOLD,14)));
        // Se busca dar color y tamaño a las letras de las columnas y ademas una
        // posicion central

        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.getColumnModel().getColumn(0).setCellRenderer(tcr);
        tabla.getColumnModel().getColumn(1).setCellRenderer(tcr);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getTableHeader().setResizingAllowed(false);
        tabla.setEnabled(true);

    }

    public void accionRaton() {
        // Se crea accion de deseleccionar la fila de la tabla al hacer click en
        // cualquier parte del panel1 o panel2
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabla.clearSelection();
            }
        });
        //con dos click se selecciona el campo de la tabla, pasa a ser editable

    }

    public void accionBotones() {
        // usaremos el b_probar para ejecutar la gramatica libre de contexto
        b_probar.addActionListener(e -> {
            /* toma los datos de la tabla y se guardan en una matriz */
            Gramatica2 gr = new Gramatica2();
            for (int i = 0; i < tabla.getRowCount(); i++) {
                gr.addProduccion((char) tabla.getValueAt(i, 0), (char[]) tabla.getValueAt(i, 1));
            }
            /* se evalua las cadenas */
            gr.ValidarCad(cadena.getText());
        });
        // usaremos el b_eliminartb para eliminar todos los datos de la tabla
        b_eliminartb.addActionListener(e -> {
            DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
            int filas = tabla.getRowCount();
            for (int i = 0; filas > i; i++) {
                modelo.removeRow(0);
            }
        });
        // usaremos el b_añadir para añadir filas a la tabla
        b_añadir.addActionListener(e -> {
            DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
            modelo.addRow(new Object[] { "", "" });
        });
        // usaremos el b_eliminarF para eliminar filas de la tabla de abajo hacia arriba
        b_eliminarF.addActionListener(e -> {
            DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
            int filas = tabla.getRowCount();
            //Si no esta seleccionada una fila eliminas de abajo hacia arriba, pero si no se elimina la fila seleccionada
            if (filas > 0) {
                if (tabla.getSelectedRow() == -1) {
                    modelo.removeRow(filas - 1);
                } else {
                    modelo.removeRow(tabla.getSelectedRow());
                }
            }

        });
        // usaremos el b_crearArbol para crear un arbol de derivacion
        b_crearArbol.addActionListener(e -> {
            frame.setVisible(true);
        });

        b_conversionADO.addActionListener(e -> {

            ArrayList<Regla2> producciones = new ArrayList();
            DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
            if (modelo.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Ingresa alguna producción");

                return;
            }
            for (int row = 0; row < modelo.getRowCount(); row++) {
                for (int column = 0; column < modelo.getColumnCount(); column++) {
                    // Obtener el valor de la celda en la posición (row, column)
                    Object value = modelo.getValueAt(row, column);
                    String sValor = value.toString().trim();
                    System.out.println("Fila: " + row + ", Columna: " + column + ", Valor: " + sValor);
                    if (column == 0) {
                        if (sValor.length() != 1) {
                            JOptionPane.showMessageDialog(null,
                                    "El No Terminal debe tener una longitud de 1 en la fila: " + row);
                            return;
                        }

                        char cNoTerminal = sValor.charAt(0);
                        if (!(cNoTerminal >= 'A' && cNoTerminal <= 'Z')) {
                            JOptionPane.showMessageDialog(null,
                                    "El No Terminal debe ser una letra mayuscula en la fila: " + row);
                            return;
                        }
                    } else if (column == 1) {
                        if (sValor.length() == 0) {
                            JOptionPane.showMessageDialog(null,
                                    "La Regla debe tener al menos un caracter en la fila: " + row);
                            return;
                        }
                    }
                }

                producciones.add(new Regla2(modelo.getValueAt(row, 0).toString().trim().charAt(0),
                        modelo.getValueAt(row, 1).toString().trim().toCharArray()));

            }

            Con_GRtoADP2 cv = new Con_GRtoADP2(producciones);
            ArrayList<Estado> ess = cv.estados;
            // ArrayList<Transicion> t;
            // for (Estado est : ess) {
            // System.out.println(est.getNombre());
            // sResultadoConversion += est.getNombre() + "\n";
            // t = est.getTransiciones();
            // for (Transicion f : t) {
            // System.out.println(f);
            // sResultadoConversion += f + "\n";
            // }
            // }
            mostrarConversionGLCAunAUP(ess);
            // System.out.println(sResultadoConversion);
            frameConversionGLCAunADP.setVisible(true);
            // s
        });
    }

    public static void dibujarArbol() {// Crearemos arboles de derivacion en el frame y dentro el panel3
        frame = new JFrame("Árbol de derivación");
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        // Agregar icono a la ventana
        ImageIcon icono = new ImageIcon("src/img_icon/arbol.png");
        frame.setIconImage(icono.getImage());
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setVisible(false);

        panel3 = new JPanel();
        panel3.setLayout(new BorderLayout());
        panel3.setBackground(new Color(255, 255, 255));
        frame.add(panel3);
        // Crea el árbol de derivación y lo agregar al panel3
        Nodo raiz = construirArbolDeDerivacion(); // Debes implementar esta función para obtener la raíz del árbol
        Arbol arbol = new Arbol(raiz);
        panel3.add(arbol, BorderLayout.CENTER);

    }

    public static void mostrarConversionGLCAunAUP(ArrayList<Estado> ess) {// Se crea el frame que muestra la
        // conversion de GLC a ADO
        frameConversionGLCAunADP = new JFrame("Conversión GLC a un ADP");
        frameConversionGLCAunADP.setSize(500, 500);
        frameConversionGLCAunADP.setLocationRelativeTo(null);
        // frameConversionGLCAunADP.setLayout(new BorderLayout());
        // Agregar icono a la ventana
        ImageIcon icono = new ImageIcon("src/img_icon/arbol.png");
        frameConversionGLCAunADP.setIconImage(icono.getImage());
        frameConversionGLCAunADP.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frameConversionGLCAunADP.setVisible(false);

        // frameConversionGLCAunADP.setLayout(new FlowLayout());

        // JTextArea miText = new JTextArea(5, 30);
        // JScrollPane jscroll2 = new JScrollPane(miText);
        // frameConversionGLCAunADP.add(jscroll2);

        V_panelConversionGLCAunADO vConversion = new V_panelConversionGLCAunADO(ess);
        JScrollPane jscroll = new JScrollPane(vConversion);
        // jscroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        // jscroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        frameConversionGLCAunADP.add(jscroll);

    }

    public static void mostrarGLC(TreeNodeDerivation raiz) {// Se crea el frame que muestra la
        // conversion de GLC a ADO
        frame = new JFrame("GLC Arbol de drivacion");
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        // frameConversionGLCAunADP.setLayout(new BorderLayout());
        // Agregar icono a la ventana
        ImageIcon icono = new ImageIcon("src/img_icon/arbol.png");
        frame.setIconImage(icono.getImage());
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setVisible(false);

        // frameConversionGLCAunADP.setLayout(new FlowLayout());

        // JTextArea miText = new JTextArea(5, 30);
        // JScrollPane jscroll2 = new JScrollPane(miText);
        // frameConversionGLCAunADP.add(jscroll2);

        TreePanel panel = new TreePanel(raiz);
        JScrollPane jscroll = new JScrollPane(panel);
        // jscroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        // jscroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        frame.add(jscroll);

    }

    /* Esta clase dibuja el arbol visual */
    public static Nodo construirArbolDeDerivacion() {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        int rowCount = modelo.getRowCount();
        if (rowCount == 0) {
            return null;
        }

        // Construir el árbol de derivación recursivamente
        return construirArbol(modelo, 0, rowCount - 1);
    }

    private static Nodo construirArbol(DefaultTableModel modelo, int start, int end) {
        return null;
    }

}
