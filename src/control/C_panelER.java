package control;

import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import static java.lang.String.valueOf;
import static funciones.NmComponentes.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import funciones.lexer.ER;
import funciones.lexer.ParseException;
import funciones.archivo.Archivo;
import funciones.er_afd.Inicio;
import vista.V_panelER;

public class C_panelER implements ActionListener, CaretListener {

    public V_panelER panelER;
    private String alfabetoER = "";
    boolean b = true;
    private ER parser = null;
    ArrayList<String> valido;
    ArrayList<String> nvalido;

    public C_panelER(V_panelER panelER) {
        this.panelER = panelER;
        valido = new ArrayList();
        nvalido = new ArrayList();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Component cmp = (Component) e.getSource();
        switch (cmp.getName()) {
            case BORRAR:
                panelER.getP_TextInfo().setText(CE_INFO);

                panelER.getEtqError().setVisible(false);
                String cadena,
                 aux1,
                 aux2;
                int posicion = panelER.getEntrada().getCaretPosition();
                cadena = panelER.getEntrada().getText();

                if (cadena.length() > 0 && posicion > 0) {
                    //cadena = cadena.substring(posicion, cadena.length() - 1);
                    aux1 = cadena.substring(0, posicion - 1);
                    aux2 = cadena.substring(posicion, cadena.length());
                    cadena = aux1 + aux2;
                    panelER.getEntrada().setText(cadena);
                    panelER.getEntrada().setCaretPosition(posicion - 1);
                }
                break;
            case ELIMINAR:
                panelER.getP_TextInfo().setText(C_INFO);
                panelER.getEtqError().setVisible(false);
                panelER.setjTextER("");
                panelER.setrSButIntero(false);
                break;
            case EJECUTAR:
                panelER.getP_TextInfo().setText(GO_INFO);
                limpiarTabla(panelER.getTablaCadenas());
                limpiarArrays(panelER.getValido(), panelER.getNvalido());
                b = true;
                if (panelER.getEntrada().getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Campo Vacío, Introduce una Expresión Regular", "Analizar Expresión Regular",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String texto = panelER.getEntrada().getText();
                    texto = texto.replace(" ", "");
                    validarAlfabeto(texto);
                    try {
                        evaluar(texto);
                    } catch (ParseException | Error ex) {
                        panelER.getEtqError().setText("ERROR: " + ex.getMessage());
                        panelER.getEtqError().setVisible(true);
                        return;
                    }

                    panelER.getTablaCadenas().setModel(new Inicio(texto, "", alfabetoER).tabla());
                    alfabetoER = "";

                    valido = Inicio.valido;
                    nvalido = Inicio.nvalido;

                    panelER.setrSButIntero(true);
                    //panelER.setrSButAFD(true);
                }
                break;
            case AFD_FUNCION:
                panelER.getP_TextInfo().setText(AFD_INFO);
                if (panelER.getEntrada().getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Campo Vacío, Introduce una Expresión Regular", "Convertir Expresión Regular",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String texto = panelER.getEntrada().getText();
                    texto = texto.replace(" ", "");
                    validarAlfabeto(texto);
                    try {
                        evaluar(texto);
                    } catch (ParseException | Error ex) {
                        panelER.getEtqError().setText("ERROR: " + ex.getMessage());
                        panelER.getEtqError().setVisible(true);
                        return;
                    }
                    guardarERtoAFD(texto);
                    alfabetoER = "";
                }
                break;
            case ORDEN:
                panelER.getP_TextInfo().setText(ORDENAR_INFO);
                if (b) {

                    //ordena cadenas validas
                    if (!valido.isEmpty()) {
                        if (valido.size() > 25) {
                            int va  = 1;
                            for (int i = 0; i < 24; i++) {
                                panelER.getTablaCadenas().setValueAt(valido.get(i), va, 0);
                                va++;
                            }
                        } else {
                            int va  = 1;
                            for (int i = 0; i < valido.size(); i++) {
                                panelER.getTablaCadenas().setValueAt(valido.get(i), va, 0);
                                va++;
                            }
                        }
                    }

                    //ordena cadenas no validas 
                    if (!nvalido.isEmpty()) {
                        if (nvalido.size() > 25) {
                            int nva = 1;
                            for (int i = 0; i < 24; i++) {
                                panelER.getTablaCadenas().setValueAt(nvalido.get(i), nva, 1);
                                nva++;
                            }
                        } else {
                            int nva = 1;
                            for (int i = 0; i < nvalido.size(); i++) {
                                panelER.getTablaCadenas().setValueAt(nvalido.get(i), nva, 1);
                                nva++;
                            }
                        }
                    }
                    b = false;
                    panelER.getrSButIntero().setIcon(new ImageIcon(getClass().getResource("/img_icon/aleatorio-24.png")));
                    panelER.getrSButIntero().setText("Desordenar");

                } else {
                    //Desordena cadenas validas
                    if (!valido.isEmpty()) {
                        if (valido.size() < 24) {

                            ArrayList<String> aux = new ArrayList();
                            aux = desordenar(valido);
                            int h = 1;
                            for (int i = 0; i < valido.size(); i++) {
                                panelER.getTablaCadenas().setValueAt(aux.get(i), h, 0);
                                h++;
                            }
                        } else {

                            ArrayList<String> aux = new ArrayList();
                            aux = desordenar(valido);
                            int h = 1;
                            for (int i = 0; i < 24; i++) {
                                panelER.getTablaCadenas().setValueAt(aux.get(i), h, 0);
                                h++;
                            }
                        }
                    }
                    //Desordena cadenas no validas 
                    if (!nvalido.isEmpty()) {
                        if (nvalido.size() < 24) {

                            ArrayList<String> aux = new ArrayList();
                            aux = desordenar(nvalido);
                            int h = 1;
                            for (int i = 0; i < aux.size(); i++) {
                                panelER.getTablaCadenas().setValueAt(aux.get(i), h, 1);
                                h++;
                            }
                        } else {
                            ArrayList<String> aux = new ArrayList();
                            aux = desordenar(nvalido);
                            int h = 1;
                            for (int i = 0; i < 24; i++) {
                                panelER.getTablaCadenas().setValueAt(aux.get(i), h, 1);
                                h++;
                            }
                        }
                    }

                    b = true;
                    panelER.getrSButIntero().setIcon(new ImageIcon(getClass().getResource("/img_icon/ordenar-24.png")));
                    panelER.getrSButIntero().setText("Ordenar");
                }
                break;
            case CERO:
                panelER.getP_TextInfo().setText(CERO_INFO);
                panelER.getEtqError().setVisible(false);
                if (panelER.getEntrada().getText() == null) {
                    panelER.setjTextER("0");
                } else {
                    panelER.setjTextER(panelER.getEntrada().getText() + "0");
                }
                break;
            case UNO:
                panelER.getP_TextInfo().setText(UNO_INFO);
                panelER.getEtqError().setVisible(false);
                if (panelER.getEntrada().getText() == null) {
                    panelER.setjTextER("1");
                } else {
                    panelER.setjTextER(panelER.getEntrada().getText() + "1");
                }
                break;
            case PARENI:
                panelER.getP_TextInfo().setText(PARENIZQ_INFO);
                panelER.getEtqError().setVisible(false);
                if (panelER.getEntrada().getText() == null) {
                    panelER.setjTextER("(");
                } else {
                    panelER.setjTextER(panelER.getEntrada().getText() + "(");
                }
                break;
            case PAREND:
                panelER.getP_TextInfo().setText(PARENDER_INFO);
                panelER.getEtqError().setVisible(false);
                if (panelER.getEntrada().getText() == null) {
                    panelER.setjTextER(")");
                } else {
                    panelER.setjTextER(panelER.getEntrada().getText() + ")");
                }
                break;
            case MAS:
                panelER.getP_TextInfo().setText(ESTRELLAMAS_INFO);
                panelER.getEtqError().setVisible(false);
                if (panelER.getEntrada().getText() == null) {
                    panelER.setjTextER("+");
                } else {
                    panelER.setjTextER(panelER.getEntrada().getText() + "+");
                }
                break;
            case ASTERISCO:
                panelER.getP_TextInfo().setText(ESTRELLA_INFO);
                panelER.getEtqError().setVisible(false);
                if (panelER.getEntrada().getText() == null) {
                    panelER.setjTextER("*");
                } else {
                    panelER.setjTextER(panelER.getEntrada().getText() + "*");
                }
                break;
            case O:
                panelER.getP_TextInfo().setText(PIPE_INFO);
                panelER.getEtqError().setVisible(false);
                if (panelER.getEntrada().getText() == null) {
                    panelER.setjTextER("|");
                } else {
                    panelER.setjTextER(panelER.getEntrada().getText() + "|");
                }
                break;

        }
    }

    private void limpiarTabla(JTable Table) {
        Table.setModel(new DefaultTableModel(new Object[][]{},
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
    }

    private void limpiarArrays(ArrayList<String> valido, ArrayList<String> nvalido) {
        valido.clear();
        nvalido.clear();
    }

    private void guardarERtoAFD(String texto) {
       

        File archivo;
        JFileChooser seleccionar = new JFileChooser();
        validarAlfabeto(panelER.getEntrada().getText());

        ///Obner nombre de la pestaña para colocarlo en: Nombre de Mod_archivo
        seleccionar.addChoosableFileFilter(new FileNameExtensionFilter("JLEFO (*.jlefo)", "jlefo"));
        seleccionar.setAcceptAllFileFilterUsed(false);
        seleccionar = Archivo.carpetaJLEFOConversion(seleccionar);
        seleccionar.setSelectedFile(
                new File("er-TO-afd.jlefo"));

        if (seleccionar.showSaveDialog(
                null) == JFileChooser.APPROVE_OPTION) {
            archivo = seleccionar.getSelectedFile();

            //Comprobando Extensión .jlefo
            if (!archivo.toString().endsWith(".jlefo")) {
                archivo = new File(archivo + ".jlefo");
            }

            //Validar si el archivo existe
            if (archivo.exists()) {
                //Validando cuando le da en la X de la pestaña
                int resp = JOptionPane.showConfirmDialog(
                        seleccionar, "El archivo " + archivo.getName() + " ya existe.\n" + "¿Desea remplazarlo?",
                        "Guardar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (JOptionPane.OK_OPTION == resp) {
                    if (alfabetoER.equals("01")) {

                        Archivo.guardarConversiontoAFD(new Inicio(texto, archivo.toString(), alfabetoER).tabla(), archivo.toString());
                        alfabetoER = "";
                    } else {
                        Archivo.guardarConversiontoAFD1(new Inicio(texto, archivo.toString(), alfabetoER).tabla(), archivo.toString(), alfabetoER);
                        alfabetoER = "";
                    }

                } else {
                    guardarERtoAFD(texto);
                }

            } else {
                //Si el archivo no existe se manda a Guardar
                if (alfabetoER.equals("01")) {
                    Archivo.guardarConversiontoAFD(new Inicio(texto, archivo.toString(), alfabetoER).tabla(), archivo.toString());
                    alfabetoER = "";
                } else {
                    Archivo.guardarConversiontoAFD1(new Inicio(texto, archivo.toString(), alfabetoER).tabla(), archivo.toString(), alfabetoER);
                    alfabetoER = "";
                }
            }
        }
    }

    private ArrayList<String> ordenar(ArrayList<String> cadenas) {
        if (!cadenas.isEmpty()) {
            Collections.sort(cadenas, new Comparator<String>() {
                @Override
                public int compare(String t, String t1) {
                    int larg0 = t.length();
                    int larg1 = t1.length();
                    return larg0 < larg1 ? -1 : larg0 > larg1 ? 1 : 0;
                }
            });
        }
        return cadenas;
    }

    private ArrayList desordenar(ArrayList<String> arreglo) {
        ArrayList<String> datos = new ArrayList<>();
        datos.addAll(arreglo);
        //Crea un ArrayList a partir del que conteniendo los valores originales.
        ArrayList<String> va  = new ArrayList<>(datos);
        //Permuta aleatoriamente la lista.
        Collections.shuffle(va);
        //Limpiar para almacenar los datos ordenados aleatoriamente.
        datos.clear();
        //Agrega los valores ordenados alatoriamente.
        datos.addAll(va);
        return datos;
    }

    private void validarAlfabeto(String texto) {
        String alfabetoCero = "", alfabetoUno = "";
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c == '0') {
                alfabetoCero = "" + c;
            }
            if (c == '1') {
                alfabetoUno = "" + c;
            }
        }

        alfabetoER = alfabetoCero + alfabetoUno;

    }

    private void evaluar(String texto) throws ParseException {
        InputStream er = new ByteArrayInputStream(texto.getBytes());

        parser = new ER(er);

        parser.expresion();
        panelER.getEtqError().setVisible(false);

    }

    private ArrayList<String> buscarAlfabeto(char[] er) {
        ArrayList<String> alfabeto;
        alfabeto = new ArrayList();

        for (int i = 0; i < er.length; i++) {
            String elemento = valueOf(er[i]);

            if (elemento.equals("0")) {
                alfabeto.add(elemento);
            }

            if (elemento.equals("1")) {
                alfabeto.add(elemento);
            }
        }

        Set<String> hs = new HashSet<>();
        hs.addAll(alfabeto);
        alfabeto.clear();
        alfabeto.addAll(hs);

        return alfabeto;
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        int row = panelER.getEntrada().getCaretPosition();
        panelER.getEtqColumna().setText("Columna:" + row);
    }

}
