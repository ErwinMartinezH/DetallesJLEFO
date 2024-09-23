package funciones.er_afd;

import static funciones.er_afd.Inicio.EPSILON;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import funciones.orden.Ordenador;

/**
 * Clase que construye un AFD a partir de un AFN
 *
 * @author Pablo
 */
public class AFDConstructor {

    private final HashMap resultadoFollowPos;
    private final String RUTA = System.getProperty("user.dir") + "/archivos/cadenasRastreo.txt";
    private final String RUTA1 = System.getProperty("user.dir") + "/archivos/cero.txt";
    private final String RUTA2 = System.getProperty("user.dir") + "/archivos/uno.txt";
    public ArrayList<String> valido;
    public ArrayList<String> nvalido;
    DefaultTableModel mod_rastreo;
    private String alfabetoFinal = "";

    public AFDConstructor() {
        this.resultadoFollowPos = new HashMap();
        valido = new ArrayList();
        nvalido = new ArrayList();
    }

    /**
     * Método general que crea el AFD de forma directa
     *
     * @param arbolSintactico
     * @param archivo
     * @return
     */
    public DefaultTableModel creacionDirecta(SyntaxTree arbolSintactico, String archivo, String alfabetoER) {

        alfabetoFinal = alfabetoER;

        //colocar numeracion a los nodos hojas
        generarNumeracionNodos(arbolSintactico);

        ArrayList<Nodo> arrayNodos = arbolSintactico.getArrayNodos();
        //Se genera la tabla de siguinte pos
        for (int i = 0; i < arrayNodos.size(); i++) {
            if (arrayNodos.get(i).getId().equals("*") || arrayNodos.get(i).getId().equals(".")) {
                followPos(arrayNodos.get(i));
            }
        }

        toStringFollowPos();
        return tablaTransicionERtoAFD(arbolSintactico, archivo);

    }

    /**
     * Metodo que se utiliza para construir la tabla de transiciones
     *
     * @param arbolSintactico
     * @param archivo
     * @return
     */
    public DefaultTableModel tablaTransicionERtoAFD(SyntaxTree arbolSintactico, String archivo) {

        if (alfabetoFinal.equals("01")) {
            System.out.println("Dos elementos: " + alfabetoFinal);
            return dosElementosER(arbolSintactico, archivo);
        } else {
            System.out.println("Un elemento: " + alfabetoFinal);
            return unElementoER(arbolSintactico, archivo);
        }
    }

    private DefaultTableModel leeArchivo(DefaultTableModel model) throws FileNotFoundException, IOException {
        mod_rastreo = new DefaultTableModel(new Object[][]{},
                new String[]{
                    "ACEPTA", "NO ACEPTA"
                }) {
            boolean[] canEdit = new boolean[]{
                false, false
            };

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };

        Object[] object = new Object[2];
        File file = null;
        if (alfabetoFinal.equals("01")) {
            file = new File(RUTA);
        } else {
            if (alfabetoFinal.equals("0")) {
                file = new File(RUTA1);
            }
            if (alfabetoFinal.equals("1")) {
                file = new File(RUTA2);
            }
        }

        FileReader fileR = null;
        BufferedReader file2 = null;
        int row03;
        if (alfabetoFinal.equals("01")) {
            row03 = (int) model.getValueAt(0, 3);
        } else {
            row03 = (int) model.getValueAt(0, 2);
        }

        for (int i = 0; i < 25; i++) {
            mod_rastreo.addRow(object);
        }

        try {
            fileR = new FileReader(file);
            file2 = new BufferedReader(fileR);

        } catch (FileNotFoundException e) {
            System.out.println("No se encontro el archivo " + file.getName());
        }

        try {

            //Condición para validar epsilon
            if (row03 == 1) {
                mod_rastreo.setValueAt("Épsilon", 0, 0);
            } else {
                mod_rastreo.setValueAt("Épsilon", 0, 1);
            }
            //Fin de condición

            String lineas;
            while ((lineas = file2.readLine()) != null) {
                analizarCadenas(lineas, model);
            }

            if (!valido.isEmpty()) {
                if (valido.size() < 24) {
                    ArrayList<String> aux = new ArrayList();
                    aux = desordenar(valido);
                    int h = 1;
                    for (int i = 0; i < valido.size(); i++) {
                        mod_rastreo.setValueAt(aux.get(i), h, 0);
                        h++;
                    }
                } else {
                    ArrayList<String> aux = new ArrayList();
                    aux = desordenar(valido);
                    int h = 1;
                    for (int i = 0; i < 24; i++) {
                        mod_rastreo.setValueAt(aux.get(i), h, 0);
                        h++;
                    }
                }

            }

            if (!nvalido.isEmpty()) {
                if (nvalido.size() < 24) {
                    ArrayList<String> aux = new ArrayList();
                    aux = desordenar(nvalido);
                    int j = 1;
                    for (int i = 0; i < aux.size(); i++) {
                        mod_rastreo.setValueAt(aux.get(i), j, 1);
                        j++;
                    }
                } else {
                    ArrayList<String> aux = new ArrayList();
                    aux = desordenar(nvalido);
                    int j = 1;
                    for (int i = 0; i < 24; i++) {
                        mod_rastreo.setValueAt(aux.get(i), j, 1);
                        j++;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return mod_rastreo;

    }

    public void analizarCadenas(String cad, DefaultTableModel model) throws IOException {

        cad = cad + "B";
        int edo = 0;

        switch (alfabetoFinal) {
            case "01":
                for (int n = 0; n < cad.length(); n++) {
                    char c = cad.charAt(n);
                    if (c == '0') {
                        edo = (int) model.getValueAt(edo, 1);
                    } else if (c == '1') {
                        edo = (int) model.getValueAt(edo, 2);
                    }
                    if (c == 'B') {
                        int eval = (int) model.getValueAt(edo, 3);
                        if (eval == 1) {
                            cad = cad.substring(0, cad.length() - 1);
                            valido.add(cad);
                        } else {
                            cad = cad.substring(0, cad.length() - 1);
                            nvalido.add(cad);
                        }
                        break;
                    }
                }
                break;

            case "0":
                for (int n = 0; n < cad.length(); n++) {
                    char c = cad.charAt(n);
                    if (c == '0') {
                        edo = (int) model.getValueAt(edo, 1);
                    } else if (c == '1') {
                        cad = cad.substring(0, cad.length() - 1);
                        nvalido.add(cad);
                        break;
                    }
                    if (c == 'B') {
                        int eval = (int) model.getValueAt(edo, 2);
                        if (eval == 1) {
                            cad = cad.substring(0, cad.length() - 1);
                            valido.add(cad);
                        } else {
                            cad = cad.substring(0, cad.length() - 1);
                            nvalido.add(cad);
                        }
                        break;
                    }
                }
                break;

            case "1":
                for (int n = 0; n < cad.length(); n++) {
                    char c = cad.charAt(n);
                    if (c == '1') {
                        edo = (int) model.getValueAt(edo, 1);
                    } else if (c == '0') {
                        cad = cad.substring(0, cad.length() - 1);
                        nvalido.add(cad);
                        break;
                    }
                    if (c == 'B') {
                        int eval = (int) model.getValueAt(edo, 2);
                        if (eval == 1) {
                            cad = cad.substring(0, cad.length() - 1);
                            valido.add(cad);
                        } else {
                            cad = cad.substring(0, cad.length() - 1);
                            nvalido.add(cad);
                        }
                        break;
                    }
                }
                break;
        }

    }

    /**
     *
     * Metodo para convertir la tabla de subconjunto a tabla de transiciones
     *
     * @param afd
     * @return una tabla de transiciones
     */
    public DefaultTableModel convertERtoAFD(DefaultTableModel afd) {
        for (int i = 0; i < afd.getRowCount(); i++) {
            String aux = afd.getValueAt(i, 0).toString(); //col  0 en i
            for (int j = 0; j < afd.getRowCount(); j++) {
                if (afd.getValueAt(j, 1).toString().equals(aux)) {
                    afd.setValueAt(i + "*", j, 1);
                }
                if (alfabetoFinal.equals("01")) {
                    if (afd.getValueAt(j, 2).toString().equals(aux)) {
                        afd.setValueAt(i + "*", j, 2);
                    }
                }
            }
            if (alfabetoFinal.equals("01")) {
                if (afd.getValueAt(i, 3) == null) {
                    afd.setValueAt(0, i, 3);
                }
            } else {
                if (afd.getValueAt(i, 2) == null) {
                    afd.setValueAt(0, i, 2);
                }
            }
            afd.setValueAt(i, i, 0);

        }

        afd = clear(afd);
        return afd;

    }

    /**
     *
     * Metodo que elimina de las celdas de caracteres tipo
     *
     *
     * @param afd
     * @return una tabla sin caracteres *
     */
    public DefaultTableModel clear(DefaultTableModel afd) {
        for (int i = 0; i < afd.getRowCount(); i++) {
            String limpiar;
            int x;
            limpiar = afd.getValueAt(i, 1).toString().replace("*", "");
            x = Integer.valueOf((String) limpiar);
            afd.setValueAt(x, i, 1);
            if (alfabetoFinal.equals("01")) {
                limpiar = afd.getValueAt(i, 2).toString().replace("*", "");
                x = Integer.valueOf((String) limpiar);
                afd.setValueAt(x, i, 2);
            }
        }

        return afd;

    }

    /**
     *
     * Metodo que obtiene el nuevo subconjunto de estados
     *
     * @param afd
     * @param corfila
     * @param corcolum
     * @return un arraylist con el subconjunto de estados obtenidos de alguna
     * columna de la tabla afd
     */
    public ArrayList nuevoEstado(DefaultTableModel afd, int corfila, int corcolum) {
        ArrayList subestados = new ArrayList<>();
        String aux;
        int x;
        aux = afd.getValueAt(corfila, corcolum).toString();
        Object[] subconjunto = aux.split(",");
        for (int i = 0; i < subconjunto.length; i++) {
            x = Integer.valueOf((String) subconjunto[i]);
            subestados.add(x);
        }
        return subestados;
    }

    /**
     * Metodo para ordenar os valores que continen la celdas de la tabla
     *
     * @param afd
     * @param corfil
     * @param corcolum
     * @param ordenar
     * @return los valores de la celda indicada en orden y sin repeticiones
     */
    public DefaultTableModel order(DefaultTableModel afd, int corfil, int corcolum, boolean ordenar) {
        HashSet hs = new HashSet();
        ArrayList subestados = new ArrayList<>();
        String aux;
        int x;

        if (afd.getValueAt(corfil, corcolum) == null) {
            return afd;
        } else {
            aux = afd.getValueAt(corfil, corcolum).toString();
        }
        Object[] subconjunto = aux.split(",");
        for (int i = 0; i < subconjunto.length; i++) {
            x = Integer.valueOf((String) subconjunto[i]);
            subestados.add(x);
        }
        if (ordenar == true) {
            new Ordenador().quicksort(subestados);
            hs.addAll(subestados);
            subestados.clear();
            subestados.addAll(hs);

            afd.setValueAt(null, corfil, corcolum);
            for (int i = 0; i < subestados.size(); i++) {
                if (afd.getValueAt(corfil, corcolum) == null) {
                    afd.setValueAt(subestados.get(i), corfil, corcolum);
                } else {
                    afd.setValueAt(afd.getValueAt(corfil, corcolum) + "," + subestados.get(i), corfil, corcolum);
                }
            }

        }

        return afd;
    }

    /**
     *
     * Metodo para obtener los nuevos subconjunto de estados
     *
     * @param arrayNodosD
     * @param id
     * @param afd
     * @param fil
     * @return
     */
    public DefaultTableModel llenarColumnas(ArrayList<Nodo> arrayNodosD, Object id, DefaultTableModel afd, int fil) {
        ArrayList auxCero = new ArrayList();
        ArrayList auxUno = new ArrayList();
        Object cero = 0, uno = 1, aceptacion = "#";

        //recorremos para obtener  que estados corresponden ha cero y a uno
        for (int j = 0; j < arrayNodosD.size(); j++) {

            Object auxid = arrayNodosD.get(j).getNumeroNodo();
            //System.out.println("Entro id--- auxid:" + id + "--" + auxid);
            if (auxid.toString().equals(id.toString())) { //Localizar que nodo corresponde al id solicitado
                //  System.out.println("Entro al if principal");
                if (arrayNodosD.get(j).getId().toString().equals(cero.toString())) {
                    //    System.out.println("Entro al if cero");
                    auxCero.addAll((Collection) resultadoFollowPos.get(arrayNodosD.get(j).getNumeroNodo()));

                    ///Modificación por el tipo de alfabeto:
                    for (int i = 0; i < auxCero.size(); i++) {
                        if (afd.getValueAt(fil, 1) == null) {
                            afd.setValueAt(auxCero.get(i), fil, 1);
                        } else {
                            afd.setValueAt(afd.getValueAt(fil, 1) + "," + auxCero.get(i), fil, 1);
                        }
                    }
                    ///Fin de modificación
                }

                if (arrayNodosD.get(j).getId().toString().equals(uno.toString())) {
                    // System.out.println("Entro al if uno");
                    auxUno.addAll((Collection) resultadoFollowPos.get(arrayNodosD.get(j).getNumeroNodo()));

                    ///Modificación por el tipo de alfabeto:
                    if (alfabetoFinal.equals("01")) {
                        for (int i = 0; i < auxUno.size(); i++) {
                            if (afd.getValueAt(fil, 2) == null) {
                                afd.setValueAt(auxUno.get(i), fil, 2);
                            } else {
                                afd.setValueAt(afd.getValueAt(fil, 2) + "," + auxUno.get(i), fil, 2);
                            }
                        }
                    } else {
                        for (int i = 0; i < auxUno.size(); i++) {
                            if (afd.getValueAt(fil, 1) == null) {
                                afd.setValueAt(auxUno.get(i), fil, 1);
                            } else {
                                afd.setValueAt(afd.getValueAt(fil, 1) + "," + auxUno.get(i), fil, 1);
                            }
                        }

                    }

                    ///FIN de modificación
                }

                if (arrayNodosD.get(j).getId().equals(aceptacion)) {
                    if (alfabetoFinal.equals("01")) {
                        afd.setValueAt(uno, fil, 3);
                    } else {
                        afd.setValueAt(uno, fil, 2);
                    }

                }

            }

        }

        return afd;
    }

    /**
     * Método para verificar si el nodo puede generar epsilon
     *
     * @param expresion
     * @return true si lo puede generar, false si no
     */
    public boolean nullable(Nodo expresion) {
        //cerradura de kleene siempre retorna verdadero
        if (expresion.getId().equals(EPSILON)) {
            return true;
        } //si contiene epsilon, es true
        else if (expresion.getId().equals("*")) {
            return true;
        } //cuando es or, se verifica cada una las hojas del nodo
        else if (expresion.getId().equals("|")) {
            return (nullable(expresion.getIzquierda())) || (nullable(expresion.getDerecha()));
        } //cuando es concatenacion solo si los dos nodos son true, devuelve true
        else if (expresion.getId().equals(".")) {
            return (nullable(expresion.getIzquierda())) && (nullable(expresion.getDerecha()));
        } //verificar si es una hoja terminal
        else if (expresion.isIsLeaf() == true) {
            return false;
        }

        //valor por default a regresar
        return false;

    }

    /**
     * Devuelve una lista de elementos que contiene la primera posicion del nodo
     *
     * @param nodoEval
     * @return ArrayList con el resultado
     */
    public TreeSet firstPos(Nodo nodoEval) {
        TreeSet resultado = new TreeSet();
        //regresar i en caso de que sea epsilon, regresa vacio
        if (nodoEval.getId().equals(EPSILON)) {
            return resultado;
        } //en caso de sea una hoja regresa el nodo i en el arreglo
        else if (nodoEval.isIsLeaf()) {
            resultado.add(nodoEval);
            //return resultado;
        } //en caso del OR hace la union de firstPos de los nodos hijos
        else if (nodoEval.getId().equals("|")) {
            resultado.addAll(firstPos(nodoEval.getIzquierda()));
            resultado.addAll(firstPos(nodoEval.getDerecha()));
            return resultado;

        } /*en el caso de la concatenacion primero revisa el nullable y
        despues realiza la union */ else if (nodoEval.getId().equals(".")) {
            if (nullable(nodoEval.getIzquierda())) {
                resultado.addAll(firstPos(nodoEval.getIzquierda()));
                resultado.addAll(firstPos(nodoEval.getDerecha()));
            } else {
                resultado.addAll(firstPos(nodoEval.getIzquierda()));
            }
        } //en el caso de la cerradura de kleene regresa firstPos del nodo hijo izquierdo
        else if (nodoEval.getId().equals("*")) {
            resultado.addAll(firstPos(nodoEval.getIzquierda()));
        }

        return resultado;
    }

    /**
     * Metodo que retorna una lista con los elementos de la operacion last pos
     * del nodo especificado
     *
     * @param nodoEval
     * @return ArrayList con el resultado
     */
    public ArrayList lastPos(Nodo nodoEval) {
        ArrayList resultado = new ArrayList();

        if (nodoEval.getId().equals(EPSILON)) {
            return resultado;
        } else if (nodoEval.isIsLeaf()) {
            resultado.add(nodoEval);
            return resultado;
        } else if (nodoEval.getId().equals("*")) {
            resultado.addAll(lastPos(nodoEval.getIzquierda()));
        } else if (nodoEval.getId().equals("|")) {
            resultado.addAll(lastPos(nodoEval.getIzquierda()));
            resultado.addAll(lastPos(nodoEval.getDerecha()));
        } else if (nodoEval.getId().equals(".")) {
            if (nullable(nodoEval.getDerecha())) {

                resultado.addAll(lastPos(nodoEval.getIzquierda()));
                resultado.addAll(lastPos(nodoEval.getDerecha()));
            } else {
                resultado.addAll(lastPos(nodoEval.getDerecha()));
            }
        }

        return resultado;
    }

    /**
     * metodo para calcular el follow pos de cada hoja terminal del árbol
     *
     * @param nodoEval
     *
     */
    public void followPos(Nodo nodoEval) {
        //por definicion follow pos aplica para cerradura de kleene y concatenacion
        //System.out.println(nodoEval.getId());

        //si es cerradura de kleen
        if (nodoEval.getId().equals("*")) {

            //según el algoritmo primero verificamos el lastPos
            ArrayList<Nodo> lastPosition = lastPos(nodoEval);
            //el follow pos del lastPos incluye todo lo que este en el first pos
            //del kleen

            //por lo tanto se necesita el firstPos del kleen
            TreeSet<Nodo> firstPosition = firstPos(nodoEval);

            for (int j = 0; j < lastPosition.size(); j++) {
                int numero = lastPosition.get(j).getNumeroNodo();

                if (resultadoFollowPos.containsKey(numero)) {
                    //si ya la tiene, es agregar
                    firstPosition.addAll((Collection) resultadoFollowPos.get(numero));
                    //this.Sort_Set((LinkedList<Integer>)SiguientePos.get(numero));
                }

                //si no la tiene, es poner
                resultadoFollowPos.put(numero, firstPosition);

            }
        } //si es concatenación
        else if (nodoEval.getId().equals(".")) {
            /*según el algoritmo el follow pos del cada posicion del last pos
            del hijo izquierdo debe incluir el el first pos del hijo derecho*/

            //obtener el lastPos del hijo izquierdo
            ArrayList<Nodo> lastPosition = lastPos(nodoEval.getIzquierda());
            //obtener el fistPos del lado derecho
            TreeSet<Nodo> firstPosition = firstPos(nodoEval.getDerecha());

            //usamos el last pos del hijo izquierdo 
            for (int i = 0; i < lastPosition.size(); i++) {
                int numero = (int) lastPosition.get(i).getNumeroNodo();
                //le agregamos el first pos del hijo derecho [merge si ya existe]
                if (resultadoFollowPos.containsKey(numero)) {
                    //System.out.println(resultadoFollowPos);
                    //System.out.println(numero);
                    //System.out.println(firstPosition);
                    firstPosition.addAll((Collection) resultadoFollowPos.get(numero));//merge

                }

                resultadoFollowPos.put(numero, firstPosition);
                firstPosition = firstPos(nodoEval.getDerecha());
            }

        }

    }

    /**
     * Método para numerar los nodos hoja del árbol sintáctico
     *
     * @param arbol
     */
    private void generarNumeracionNodos(SyntaxTree arbol) {
        ArrayList<Nodo> arrayNodos = arbol.getArrayNodos();
        int index = 1;
        for (int i = 0; i < arrayNodos.size(); i++) {
            if (arrayNodos.get(i).isIsLeaf()) {
                arrayNodos.get(i).setNumeroNodo(index);
                index++;
            }
        }

        arbol.setArrayNodos(arrayNodos);

    }

    /**
     * Metodo para mostrar el hash map en forma de tabla
     */
    private void toStringFollowPos() {
        System.out.println("follow pos");

        Iterator it = resultadoFollowPos.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Nodo> e = (Map.Entry) it.next();
            System.out.println(e.getKey() + " " + e.getValue());
        }

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

    private DefaultTableModel tipoTableModel() {
        DefaultTableModel afd;

        switch (alfabetoFinal) {
            case "0":

                afd = new DefaultTableModel(new Object[][]{},
                        new String[]{
                            "ESTADO", "Σ={0}", "FINAL"
                        }) {
                    boolean[] canEdit = new boolean[]{
                        false, false, false
                    };

                    @Override
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return canEdit[columnIndex];
                    }
                };

                return afd;

            case "1":

                afd = new DefaultTableModel(new Object[][]{},
                        new String[]{
                            "ESTADO", "Σ={1}", "FINAL"
                        }) {
                    boolean[] canEdit = new boolean[]{
                        false, false, false
                    };

                    @Override
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return canEdit[columnIndex];
                    }
                };

                return afd;

            case "01":

                afd = new DefaultTableModel(new Object[][]{},
                        new String[]{
                            "ESTADO", "Σ={0}", "Σ={1}", "FINAL"
                        }) {
                    boolean[] canEdit = new boolean[]{
                        false, false, false, false
                    };

                    @Override
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return canEdit[columnIndex];
                    }
                };

                return afd;

        }
        return null;
    }

    private DefaultTableModel unElementoER(SyntaxTree arbolSintactico, String archivo) {

        Object[] object = new Object[3];

        DefaultTableModel afd = tipoTableModel();

        //Obtenemos el estado Inicial (primerapos del estado raiz)
        TreeSet<Nodo> estadoInicial = firstPos(arbolSintactico.getRoot());
        //Obtenemos todos los nodos del arbol sintactico
        ArrayList<Nodo> arrayNodosD = arbolSintactico.getArrayNodos();
        //Almacenamos el conjunto de  estados actuales a verificar en el arbol
        ArrayList edoactual = new ArrayList();
        //Almacenamos los estados siguintes en el caso de columna1 != columna2

        int fil = 0;//Número de fila creadas: de acuerdo a los nuevos subconjuntos 
        int corfila = 0;//Número Fila que se utiliza: En el caso de columna1 != columna2
        boolean colAlfabeto = false; // Comprobar si  el nuevo subconjuntos ya existen columna Alfabeto (0 |1)
        boolean terminar = false; //Indica el final del ciclo
        boolean bucleExiste = false;
        Object edobucle = "";

        //Inicializamos el arraylist con el estadoinicial --->primerapos de nodo raiz-->[1,4,6] 
        edoactual.addAll(estadoInicial);

        while (terminar != true) {
            if (fil == 0) {
                afd.addRow(object);
            }

            for (int i = 0; i < edoactual.size(); i++) {
                if (afd.getValueAt(corfila, 0) == null) {
                    afd.setValueAt(edoactual.get(i), corfila, 0);
                } else {
                    afd.setValueAt(afd.getValueAt(corfila, 0) + "," + edoactual.get(i), corfila, 0);
                }
                afd = llenarColumnas(arrayNodosD, edoactual.get(i), afd, corfila);
            }
            afd = order(afd, corfila, 0, true);
            afd = order(afd, corfila, 1, true);

            //Comprueba el valor en la tabla  de la columna del Σ={0 | 1}
            for (int i = 0; i < afd.getRowCount(); i++) {
                if (afd.getValueAt(corfila, 1) == null) {
                    colAlfabeto = false;
                    break;
                }
                String var1 = "" + afd.getValueAt(corfila, 1).toString();
                String var2 = "" + afd.getValueAt(i, 0).toString();
                if (var1.equals(var2)) {
                    colAlfabeto = true;
                    break;
                } else {
                    colAlfabeto = false;
                }
            }

            int qn = 0;
            //Caso 1
            if (colAlfabeto == false) {
                if (afd.getValueAt(corfila, 1) == null) {
                    if (bucleExiste == true) {
//                        System.out.println("NULL OK:");
                        afd.setValueAt(edobucle, corfila, 1);
                        corfila++;
                    } else {
//                        System.out.println("NULL NO:");
                        qn = fil + 1;
                        edobucle = "q" + qn;
                        afd.setValueAt(edobucle, corfila, 1);
                        corfila++;
                        afd.addRow(object);
                        fil++;
                        afd.setValueAt(edobucle, fil, 0);
                        afd.setValueAt(edobucle, fil, 1);
                        afd.setValueAt(0, fil, 2);
                        bucleExiste = true;
                    }

                } else {
//                    System.out.println("NORMAL FALSE !=:");
                    afd.addRow(object);
                    fil++;
                    afd.setValueAt(afd.getValueAt(corfila, 1), fil, 0);
                    corfila++;
                }
            }

            //Caso 2
            if (colAlfabeto == true) {
                corfila++;
            }

//            System.out.println("Q--Σ={0 | 1}--FINAL");
//            for (int i = 0; i < afd.getRowCount(); i++) {
//                System.out.println(afd.getValueAt(i, 0) + "  " + afd.getValueAt(i, 1) + "      " + afd.getValueAt(i, 2));
//            }
//            System.out.println("FILA:" + fil + "   COORFIL: " + corfila);
            if (fil >= corfila) {
                if (afd.getValueAt(corfila, 0).equals(edobucle)) {
//                    System.out.println("Estado BUCLE:");
                    corfila++;
                    if (corfila <= fil) {
                        edoactual = nuevoEstado(afd, corfila, 0);
//                        System.out.println("Saltando bucle");
                    } else {
//                        System.out.println("FIN BUCLE");
                        terminar = true;
                    }
                } else {
//                    System.out.println("Estado NORMAL OK");
                    edoactual = nuevoEstado(afd, corfila, 0);
                }

            } else {
//                System.out.println("FIN");
                terminar = true;
            }

        } //FIN DEL WHILE

//        System.out.println("\n\n\t FINAL");
//        System.out.println("Q-----Σ={0 | 1}---FINAL");
//        for (int i = 0; i < afd.getRowCount(); i++) {
//            System.out.println(afd.getValueAt(i, 0) + "  " + afd.getValueAt(i, 1) + "      " + afd.getValueAt(i, 2));
//        }
        afd = convertERtoAFD(afd);

        if (!archivo.equals("")) {
//            System.out.println("vacio");
            return afd;
        } else {
            try {
//                System.out.println("no vacio");
                return leeArchivo(afd);
            } catch (IOException ex) {
                Logger.getLogger(AFDConstructor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;

    }

    private DefaultTableModel dosElementosER(SyntaxTree arbolSintactico, String archivo) {

        Object[] object = new Object[4];

        DefaultTableModel afd = tipoTableModel();

        //Obtenemos el estado Inicial (primerapos del estado raiz)
        TreeSet<Nodo> estadoInicial = firstPos(arbolSintactico.getRoot());
        //Obtenemos todos los nodos del arbol sintactico
        ArrayList<Nodo> arrayNodosD = arbolSintactico.getArrayNodos();
        //Almacenamos el conjunto de  estados actuales a verificar en el arbol
        ArrayList edoactual = new ArrayList();
        //Almacenamos los estados siguintes en el caso de columna1 != columna2

        int fil = 0;//Número de fila creadas: de acuerdo a los nuevos subconjuntos 
        int corfila = 0;//Número Fila que se utiliza: En el caso de columna1 != columna2
        boolean col0 = false, col1 = false; // Comprobar si  el nuevo subconjuntos ya existen col1 && col2
        boolean terminar = false; //Indica el final del ciclo
        boolean bucleExiste = false;
        Object edobucle = "";

        //Inicializamos el arraylist con el estadoinicial --->primerapos de nodo raiz-->[1,4,6] 
        edoactual.addAll(estadoInicial);

        while (terminar != true) {
            if (fil == 0) {
                afd.addRow(object);
            }

            for (int i = 0; i < edoactual.size(); i++) {
                if (afd.getValueAt(corfila, 0) == null) {
                    afd.setValueAt(edoactual.get(i), corfila, 0);
                } else {
                    afd.setValueAt(afd.getValueAt(corfila, 0) + "," + edoactual.get(i), corfila, 0);
                }
                afd = llenarColumnas(arrayNodosD, edoactual.get(i), afd, corfila);
            }
            afd = order(afd, corfila, 0, true);
            afd = order(afd, corfila, 1, true);
            afd = order(afd, corfila, 2, true);

            //Comprueba el valor en la tabla  de la columna del Σ={0}
            for (int i = 0; i < afd.getRowCount(); i++) {
                if (afd.getValueAt(corfila, 1) == null) {
                    col0 = false;
                    break;
                }
                String var1 = "" + afd.getValueAt(corfila, 1).toString();
                String var2 = "" + afd.getValueAt(i, 0).toString();
                if (var1.equals(var2)) {
                    col0 = true;
                    break;
                } else {
                    col0 = false;
                }
            }

            //Comprueba el valor en la tabla  de la columna del Σ={1}
            for (int i = 0; i < afd.getRowCount(); i++) {
                if (afd.getValueAt(corfila, 2) == null) {
                    col1 = false;
                    break;
                }
                String var1 = afd.getValueAt(corfila, 2).toString();
                String var2 = afd.getValueAt(i, 0).toString();
                if (var1.equals(var2)) {
                    col1 = true;
                    break;
                } else {
                    col1 = false;
                }
            }

            int qn = 0;
            //Caso 1
            if (col0 == false && col1 == false) {
                if (afd.getValueAt(corfila, 1) == null && afd.getValueAt(corfila, 2) == null) {
                    if (bucleExiste == true) {
//                        System.out.println("NULL OK:");
                        afd.setValueAt(edobucle, corfila, 1);
                        afd.setValueAt(edobucle, corfila, 2);
                        corfila++;
                    } else {
//                        System.out.println("NULL NO:");
                        qn = fil + 1;
                        edobucle = "q" + qn;
                        afd.setValueAt(edobucle, corfila, 1);
                        afd.setValueAt(edobucle, corfila, 2);
                        corfila++;
                        afd.addRow(object);
                        fil++;
                        afd.setValueAt(edobucle, fil, 0);
                        afd.setValueAt(edobucle, fil, 1);
                        afd.setValueAt(edobucle, fil, 2);
                        afd.setValueAt(0, fil, 3);
                        bucleExiste = true;
                    }

                } else if (afd.getValueAt(corfila, 1) == null) {

                    if (bucleExiste == true) {
//                        System.out.println("NULL 1 OK:");
                        afd.setValueAt(edobucle, corfila, 1);
                        afd.addRow(object);
                        fil++;
                        afd.setValueAt(afd.getValueAt(corfila, 2), fil, 0);
                        corfila++;
                    } else {
//                        System.out.println("NULL 1 NO:");
                        qn = fil + 1;
                        edobucle = "q" + qn;
                        afd.setValueAt(edobucle, corfila, 1);
                        afd.addRow(object);
                        fil++;
                        afd.setValueAt(edobucle, fil, 0);
                        afd.setValueAt(edobucle, fil, 1);
                        afd.setValueAt(edobucle, fil, 2);
                        afd.setValueAt(0, fil, 3);
                        bucleExiste = true;
                        afd.addRow(object);
                        fil++;
                        afd.setValueAt(afd.getValueAt(corfila, 2), fil, 0);
                        corfila++;
                    }
                } else if (afd.getValueAt(corfila, 2) == null) {
                    if (bucleExiste == true) {
//                        System.out.println("NULL 2 OK:");
                        afd.setValueAt(edobucle, corfila, 2);
                        afd.addRow(object);
                        fil++;
                        afd.setValueAt(afd.getValueAt(corfila, 1), fil, 0);
                        corfila++;
                    } else {
//                        System.out.println("NULL 2 NO:");
                        qn = fil + 1;
                        edobucle = "q" + qn;
                        afd.setValueAt(edobucle, corfila, 2);
                        afd.addRow(object);
                        fil++;
                        afd.setValueAt(edobucle, fil, 0);
                        afd.setValueAt(edobucle, fil, 1);
                        afd.setValueAt(edobucle, fil, 2);
                        afd.setValueAt(0, fil, 3);
                        bucleExiste = true;
                        afd.addRow(object);
                        fil++;
                        afd.setValueAt(afd.getValueAt(corfila, 1), fil, 0);
                        corfila++;
                    }

                } else {

                    String var1 = afd.getValueAt(corfila, 1).toString();
                    String var2 = afd.getValueAt(corfila, 2).toString();

                    if (var1.equals(var2)) {
//                        System.out.println("NORMAL FALSE ==:");
                        afd.addRow(object);
                        fil++;
                        afd.setValueAt(afd.getValueAt(corfila, 1), fil, 0);
                        corfila++;
                    } else {
//                        System.out.println("NORMAL FALSE !=:");
                        afd.addRow(object);
                        fil++;
                        afd.setValueAt(afd.getValueAt(corfila, 1), fil, 0);
                        afd.addRow(object);
                        fil++;
                        afd.setValueAt(afd.getValueAt(corfila, 2), fil, 0);
                        corfila++;
                    }
                }
            }
            //Caso 2
            if (col0 == false && col1 == true) {
                if (afd.getValueAt(corfila, 1) == null) {
                    if (bucleExiste == true) {
                        //System.out.println("Caso2:  NULL OK:");
                        afd.setValueAt(edobucle, corfila, 1);
                        corfila++;
                    } else {
                        ///System.out.println("Caso2:  NULL NO:");
                        qn = fil + 1;
                        edobucle = "q" + qn;
                        afd.setValueAt(edobucle, corfila, 1);
                        corfila++;
                        afd.addRow(object);
                        fil++;
                        afd.setValueAt(edobucle, fil, 0);
                        afd.setValueAt(edobucle, fil, 1);
                        afd.setValueAt(edobucle, fil, 2);
                        afd.setValueAt(0, fil, 3);
                        bucleExiste = true;
                    }
                } else {
                    //  System.out.println("Caso2:  NORMAL OK:");
                    afd.addRow(object);
                    fil++;
                    afd.setValueAt(afd.getValueAt(corfila, 1), fil, 0);
                    corfila++;
                }
            }

            //Caso 3
            if (col0 == true && col1 == false) {
                if (afd.getValueAt(corfila, 2) == null) {
                    if (bucleExiste == true) {
                        //  System.out.println("Caso3:  NULL OK:");
                        afd.setValueAt(edobucle, corfila, 2);
                        corfila++;
                    } else {
                        //   System.out.println("Caso3:  NULL NO:");
                        qn = fil + 1;
                        edobucle = "q" + qn;
                        afd.setValueAt(edobucle, corfila, 2);
                        corfila++;
                        afd.addRow(object);
                        fil++;
                        afd.setValueAt(edobucle, fil, 0);
                        afd.setValueAt(edobucle, fil, 1);
                        afd.setValueAt(edobucle, fil, 2);
                        afd.setValueAt(0, fil, 3);
                        bucleExiste = true;
                    }
                } else {
                    ///  System.out.println("Caso3:  NORMAL OK:");
                    afd.addRow(object);
                    fil++;
                    afd.setValueAt(afd.getValueAt(corfila, 2), fil, 0);
                    corfila++;
                }
            }
            //Caso 4
            if (col0 == true && col1 == true) {
                corfila++;
            }

//            System.out.println("\n\n\t FINAL");
//            System.out.println("Q--Σ={0}--Σ={1}--FINAL");
//            for (int i = 0; i < afd.getRowCount(); i++) {
//                System.out.println(afd.getValueAt(i, 0) + "  " + afd.getValueAt(i, 1) + "      " + afd.getValueAt(i, 2) + "      " + afd.getValueAt(i, 3));
//            }
            // System.out.println("FILA:" + fil + "   COORFIL: " + corfila);
            if (fil >= corfila) {
                if (afd.getValueAt(corfila, 0).equals(edobucle)) {
                    //System.out.println("Estado BUCLE:");
                    corfila++;
                    if (corfila <= fil) {
                        edoactual = nuevoEstado(afd, corfila, 0);
                    } else {
                        //   System.out.println("FIN");
                        terminar = true;
                    }
                } else {
                    //  System.out.println("Estado NORMAL OK");
                    edoactual = nuevoEstado(afd, corfila, 0);
                }

            } else {
                // System.out.println("FIN");
                terminar = true;
            }

        }
        //FIN DEL WHILE
//        System.out.println("\n\n\t FINAL");
//        System.out.println("Q--Σ={0}--Σ={1}--FINAL");
//        for (int i = 0; i < afd.getRowCount(); i++) {
//            System.out.println(afd.getValueAt(i, 0) + "  " + afd.getValueAt(i, 1) + "      " + afd.getValueAt(i, 2) + "      " + afd.getValueAt(i, 3));
//        }

        afd = convertERtoAFD(afd);

//        System.out.println("\n\n\t FINAL");
//        System.out.println("Q--Σ={0}--Σ={1}--FINAL");
//        for (int i = 0; i < afd.getRowCount(); i++) {
//            System.out.println(afd.getValueAt(i, 0) + "  " + afd.getValueAt(i, 1) + "      " + afd.getValueAt(i, 2) + "      " + afd.getValueAt(i, 3));
//        }

        if (!archivo.equals("")) {
            //System.out.println("vacio");
            return afd;

        } else {
            try {
                //   System.out.println("no vacio");
                return leeArchivo(afd);
            } catch (IOException ex) {
                Logger.getLogger(AFDConstructor.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;

    }
}
