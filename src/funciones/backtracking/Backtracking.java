/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package funciones.backtracking;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import modelo.M_arco;
import modelo.M_nodo;

/**
 *
 * @author herma
 */
public class Backtracking {

    private String[] split = {};
    private Pila nodosConsum;
    private Pila alfaConsum;
    private Pila cadena;
    private Pila ruta;
    private DefaultTableModel model;
    private final String ESTATUS;
    private String alfabetoFinal;

    public Backtracking(DefaultTableModel model, String cadena, String estatus, String alfabetoFinal) {
        this.ESTATUS = estatus;
        this.model = model;
        this.cadena = new Pila();
        //llenar pila con la cadena
        for (int i = cadena.length() - 1; i >= 0; i--) {
            this.cadena.insertarAlf(String.valueOf(cadena.charAt(i)));
        }
        this.nodosConsum = new Pila();
        this.alfaConsum = new Pila();
        this.ruta = new Pila();
        this.alfabetoFinal = alfabetoFinal;
    }

    /**
     * Ø
     *
     * @return la ruta para determinar el camino que se debe trazar gráficamente
     */
    public ArrayList rastrear() {
        int colum1 = 1;
        int colum2 = 2;
        if (alfabetoFinal.length() == 1) {
            colum1 = 1;
            colum2 = 1;
        }
        String uno = "1";
        String cero = "0";
        String sucesor = "";
        //crear ruta
        while (!cadena.isEmpty()) {
            //obtener elemento de la cadena para avanzar a un nodo
            String c = cadena.sacarAlf();
            switch (c) {
                case "0":
                    if (sucesor.isEmpty()) { //comenzar por el nodo cero
                        sucesor = "0";
                        sucesor = crearNodo(sucesor, colum1, colum2, cero);
                        //en caso de ser una cadena rechazada
                        if (sucesor.equals("reject")) {
                            //retornar ruta
                            return ruta;
                        }
                    } else {
                        sucesor = crearNodo(sucesor, colum1, colum2, cero);
                        if (sucesor.equals("reject")) {
                            //retornar ruta
                            return ruta;
                        }
                    }
                    break;
                case "1":
                    if (sucesor.isEmpty()) { //comenzar por el nodo cero
                        sucesor = "0";
                        sucesor = crearNodo(sucesor, colum1, colum2, uno);
                        if (sucesor.equals("reject")) {
                            //retornar ruta
                            return ruta;
                        }
                    } else {
                        sucesor = crearNodo(sucesor, colum1, colum2, uno);
                        if (sucesor.equals("reject")) {
                            //retornar ruta
                            return ruta;
                        }
                    }
                    break;
            }

            //comprobar si el ultimo nodo es un nodo de aceptacion y mientras 
            //sea una cadena aceptada, en caso contrario no es necesario saber
            //si el nodo es de aceptacion
            if (cadena.isEmpty() && ESTATUS.equals("ACEPTA")) {
                M_nodo n = (M_nodo) ruta.get(ruta.size() - 1);
                M_arco arc = n.getSucesores().get(0);
                int col = 3;
                if (alfabetoFinal.length() == 1) {
                    col = 2;
                }
                for (int i = 0; i < model.getRowCount(); i++) {

                    if (arc.getDestino().equals(model.getValueAt(i, 0).toString())
                            && "1".equals(model.getValueAt(i, col).toString())) {
                        return ruta;
                    }
                }
                M_nodo n2 = nodosConsum.sacarNod();
                String nAlfabeto = alfaConsum.sacarAlf();
                boolean noSucesores = true;
                //comprobar si con el mismo elemento del alfabeto puedo avanzar 
                //a un estado de aceptacion
                if (!n2.getSucesores().isEmpty()) {
                    for (M_arco a : n2.getSucesores()) {
                        if (a.getAlfabeto().equals(nAlfabeto)) {
                            n.getSucesores().remove(0);
                            n.addSucesor(a);
                            arc = n.getSucesores().get(0);
                            for (int i = 0; i < model.getRowCount(); i++) {
                                if (arc.getDestino().equals(model.getValueAt(i, 0).toString())
                                        && "1".equals(model.getValueAt(i, col).toString())) {
                                    return ruta;
                                }
                            }
                        }
                    }
                } else { // si no existen mas sucesores, retroceder
                    noSucesores = false;
                    sucesor = retroceder(nAlfabeto);
                }

                if (noSucesores) { //si no se encontraron sucesores para el alfabeto
                    sucesor = retroceder(nAlfabeto);
                }
            }

        }
        return ruta;
    }

    /**
     *
     * @param sucesor el nodo al que se avanza
     * @param col1 columna para el alfabeto 0
     * @param col2 columna para el alfabeto 1
     * @param alfabeto alfabeto con el que avanza al siguiente nodo
     * @return id del siguiente nodo
     */
    private String crearNodo(String sucesor, int col1, int col2, String alfabeto) {
        int row = Integer.parseInt(sucesor);
        int colFinal = 3;
        if (col2 == 1) {
            colFinal = 2;
        }
        M_nodo n = new M_nodo(
                sucesor,
                model.getValueAt(row, colFinal).toString());
        //crear transiciones a nodos sucesores
        if (alfabetoFinal.length() == 1) {
            System.out.println("ESTE ALFAFINAL" + alfabetoFinal);
            n = sucesoresParaN(n, row, col1, alfabetoFinal);
        } else {
            n = sucesoresParaN(n, row, col1, "0");
            n = sucesoresParaN(n, row, col2, "1");
        }
        int index = 0;
        boolean noSucesores = true;
        //obtener siguiente nodo
        if (!n.getSucesores().isEmpty()) { //si almenos hay un sucesor
            for (M_arco arc : n.getSucesores()) {
                if (arc.getAlfabeto().equals(alfabeto)) {
                    sucesor = arc.getDestino();
                    index = n.getSucesores().indexOf(arc);
                    noSucesores = false;
                    break;
                }
            }
        } else { //cuando hay cero sucesores
            //retrocedes mientras la cadenas sea de aceptacion
            if (!ESTATUS.equals("NO ACEPTA") && alfabetoFinal.length() > 1) {
                return sucesor = retroceder(alfabeto);
            } else {
                return "reject";
            }
        }
        //si no se encontraron sucesores para el alfabeto
        if (noSucesores) {
            //retroceder mientras la cadena sea de aceptacion
            if (!ESTATUS.equals("NO ACEPTA") && alfabetoFinal.length() > 1) {
                return sucesor = retroceder(alfabeto);
            } else {
                return "reject";
            }
        }
        //ingresar nodo a la ruta con su unico sucesor
        ruta.insertarNod(new M_nodo(
                n.getIdNodo(), n.getTipo(),
                n.getSucesores().get(index)));
        n.removerSucesor(index); //remover sucesor de la ruta
        nodosConsum.insertarNod(n); //almacenarlo en la pila
        alfaConsum.insertarAlf(alfabeto); //almacenar alfabeto en la pila
        return sucesor;
    }

    /**
     *
     * @param alfabeto alfabeto que fallo para ir a un nodo siguiente
     * @return el sucesor del nodo anterior
     */
    private String retroceder(String alfabeto) {
        cadena.insertarAlf(alfabeto); //regresar alfabeto a la cadena 
        String nAlfabeto = "";
        String sucesor = "";
        int index = 0;
        boolean solucion = false;
        boolean noSucesores = true;
        M_nodo n2 = null;
        while (!solucion) { //hasta encontrar un sucesor
            if (!nodosConsum.isEmpty()) {
                //sacar nodo de la pila y remover de la ruta
                n2 = nodosConsum.sacarNod();
                ruta.removerNodo();
            }
            nAlfabeto = alfaConsum.sacarAlf(); //sacar alfabeto anterior
            if (!n2.getSucesores().isEmpty()) { //obtener nuevo sucesor
                for (M_arco arc : n2.getSucesores()) {
                    if (arc.getAlfabeto().equals(nAlfabeto)) {
                        sucesor = arc.getDestino();
                        index = n2.getSucesores().indexOf(arc);
                        solucion = true;
                        noSucesores = false;
                        break;
                    }
                }
                if (noSucesores) { //seguir retrocediendo
                    cadena.insertarAlf(nAlfabeto);
                }
            } else { //retroceder de inmediato
                cadena.insertarAlf(nAlfabeto);
            }
        }
        //añadir el nuevo nodo a la ruta 
        ruta.insertarNod(new M_nodo(
                n2.getIdNodo(), n2.getTipo(),
                n2.getSucesores().get(index)));
        n2.removerSucesor(index); //remover sucesor del nodo
        nodosConsum.insertarNod(n2); //almacenar en la pila
        alfaConsum.insertarAlf(nAlfabeto); //almacenar alfabeto
        return sucesor;
    }

    /**
     *
     * @param n nodo
     * @param row fila de la tabla que pertenece el nodo
     * @param col columna a la que pertenece el alfabeto
     * @param alfabeto alfabeto para crear arco
     * @return nodo
     */
    private M_nodo sucesoresParaN(M_nodo n, int row, int col, String alfabeto) {
        //si exiten transiciones a mas de un nodo
        if (model.getValueAt(row, col).toString().contains(",")) {
            split = model.getValueAt(row, col).toString().split(",");
            for (String split1 : split) {
                n.addSucesor(new M_arco(n.getIdNodo(),
                        split1, alfabeto));
            }
        } else if (!model.getValueAt(row, col).toString().contains("Ø")) {
            //existe una transicion a un solo nodo
            n.addSucesor(new M_arco(n.getIdNodo(),
                    model.getValueAt(row, col).toString(),
                    alfabeto));
        }
        return n;
    }

}
