package funciones.glc;


import java.util.ArrayList;
import java.util.List;
import vista.TreeNodeDerivation;

public class Gramatica2 {

    /*Una gramatica esta compuesta por reglas; este objeto es una gramatica
   por lo tanto tiene un conjunto de reglas.*/
    public ArrayList<Regla2> reglas = new ArrayList();
    private String cad = "";
    int max;
    public String[] validos = new String[50];
    public String[] invalidos = new String[50];
    public boolean F = false;
    //hasta aqui son las v ariables que uso
    private String ep = "?";
    private TreeNodeDerivation raiz;
    ArrayList<String> camino = new ArrayList();
    public static String alfabeto = "ab";

    private String hojas = "";
  public ArrayList<Regla2> getReglas() {
        return reglas;
    }

    public void setReglas(ArrayList<Regla2> reglas) {
        this.reglas = reglas;
    }
public void addProduccion(char Not,char[] Ter){ 
        this.reglas.add(new Regla2(Not,Ter));
    }
    public void addProduccion(Regla2[] r){
        for(Regla2 a: r){
            this.reglas.add(a);
        }

    }

    public void removeProduccion(char c){
        ArrayList<Regla2> nuevalista = new ArrayList<>();
        for(Regla2 r : reglas){
            if(r.getInstruccion()[0] != '?')
                nuevalista.add(r);
        }
        reglas = nuevalista;
    }

    public void ValidarCad(String cadena) {//primer metodo que hace llamada a la regla raiz
        cad = cadena;
        F = false;
        String pila = "";
        cadena = "";
        ArrayList<String> camino2 = new ArrayList();

    
        max = cad.length() + 5;
        //   System.out.println("pila " + pila.length() + "max " + max);
        PruebaNT(cadena, pila, "S", 0, camino2);

        //       System.out.println("Ultimo " + F);
        if (F == true) {

            System.out.println("La cadena " + cad + " es aceptada");
            System.out.println(camino);
        } else {
            System.out.println("La cadena " + cad + " es rechazada");
        }
    }
    //Selecciona un No terminal para la cadena
    public void PruebaNT(String cadena, String pila, String nombre, int cont, ArrayList<String> recorrido2) {  
        if (F == false && pila.length() < max && cont < cad.length() * 2 + 8) { //verificaciones de la pilla o movimientos infinitos
            ArrayList<String> aux = new ArrayList();
            String instruccion;
            for (Regla2 regla : reglas) {
                if (F == true) {
                    break;
                }
                aux = new ArrayList<>(recorrido2);
                instruccion = new String(regla.getInstruccion());
                String instruccion2 = instruccion + "\n";
                aux.add(instruccion2);
                if (String.valueOf(regla.getNombre()).equals(nombre)) {
                    Recorrido(cadena, pila, nombre, instruccion, cont, aux);
                }
            }

        }
    }

    public void Recorrido(String cadena, String pila, String nombre, String instruccion, int cont, ArrayList<String> camino2) {
        int min;
        cont++;
        min = (int) pila.chars().filter(Character::isUpperCase).count();
        min = pila.length() - min;

        if (F == false && (cadena.length() + min) <= cad.length() && pila.length() < max) {

            int j = 0;
            String cadC = "";
            String pilaC;
            boolean contieneMayuscula = false;
            char caracter;
            char mayus = '0';
            contieneMayuscula = false;
            for (int i = 0; i < instruccion.length(); i++) {
                caracter = instruccion.charAt(i);
                if (Character.isUpperCase(caracter)) {
                    contieneMayuscula = true;
                    //              System.out.println(caracter + "es mayuscula");
                    mayus = caracter;
                    j = i;
                    break;
                    //    i= regla.instruccion.length();

                }

            }

            if (contieneMayuscula) {

                if (j == (instruccion.length() - 1)) {// la ultima letra es mayuscula
                    if (instruccion.length() == 1) { // verifica si la intruccion solo es de tamaño 1 y es no terminal
                        cadC = cadena;

                    } else {
                        instruccion = instruccion.substring(0, instruccion.length() - 1);
                        //      mayus = instrucion.
                        cadC = cadena + instruccion;
                    }

                    if (cad.startsWith(cadC)) {
                        PruebaNT(cadC, pila, Character.toString(mayus), cont, camino2);
                    }
                } else {// hay mayusculas

                    if (instruccion.equals(instruccion.toUpperCase()) && instruccion.matches("[A-Z]+")) {// si todas son mayusculas
                        cadC = cadena;
                        pilaC = instruccion.substring(j + 1) + pila;

                        //  System.out.println("cadena actualizada: " + cadC);
                    } else if (Character.isUpperCase(instruccion.charAt(0))) { // si la primera en mayuscula
                        pilaC = instruccion.substring(1) + pila;
                        cadC = cadena;

                    } else {

                        cadC = instruccion.substring(0, j);
                        cadC = cadena + cadC;
                        pilaC = instruccion.substring(j + 1) + pila;

                    }

                    if (cad.startsWith(cadC)) {
                        /*                       System.out.println("se activa");
                        System.out.println("cadena actual " + cadC  + " Pila actual " + pilaC + " Instruccion " + Character.toString(mayus) + " cadena actual " +cont);
                         */ PruebaNT(cadC, pilaC, Character.toString(mayus), cont, camino2);
                    }
                }
            } else {
                if (instruccion.equals("?")) {
                    cadena = cadena;
                } else {
                    cadena = cadena + instruccion;
                }

                for (int i = 0; i < pila.length(); i++) {
                    caracter = pila.charAt(i);
                    if (Character.isUpperCase(caracter)) {
                        contieneMayuscula = true;

                        mayus = caracter;
                        j = i;
                        break;

                    }
                }
                if (contieneMayuscula) {
                    if (j == (pila.length() - 1)) {// Realizar algo si la última letra es mayúscula
                        if (pila.length() == 1) {
                            cadC = cadena;
                            pilaC = "";
                        } else {
                            pila = pila.substring(0, pila.length() - 1);
                            //      mayus = instrucion.
                            cadC = cadena + pila;
                            pilaC = "";
                        }
                        if (cad.startsWith(cadC)) {
                            //               System.out.println("si es igual");
                            PruebaNT(cadC, pilaC, Character.toString(mayus), cont, camino2);
                        }
                    } else {
                        if (Character.isUpperCase(pila.charAt(0))) {
                            pilaC = pila.substring(1);
                            cadC = cadena;
                        } else {
                            //aqui tambien me quede editando
                            cadC = pila.substring(0, j);
                            cadC = cadena + cadC;

                            pilaC = pila.substring(j + 1);
                        }

                        if (cad.startsWith(cadC)) {
                            //      System.out.println("Si es igual primis viene:" + mayus+ ", instruccion" + instruccion );
                            PruebaNT(cadC, pilaC, Character.toString(mayus), cont, camino2);
                        }
                    }
                }
                if (!contieneMayuscula) {
                    cadC = cadena + pila;

                    if (cad.equals(cadC)) {
                        camino = camino2;
                        F = true;
                    }
                }

            }
        }
    }
    
    
    public void lista(String alfa) {
        alfabeto = alfa;
        List<String> Combinaciones = Generador_com(1, 6);
        // Combinaciones.forEach(System.out::println);
        recorridocad(Combinaciones);
    }

    public static List<String> Generador_com(int minLength, int maxLength) {
        List<String> Combinaciones = new ArrayList<>(); // Crea una lista para almacenar las combinaciones
        for (int len = minLength; len <= maxLength; len++) {// Bucle para generar combinaciones de longitud de minLength a maxLength
            Generador2(Combinaciones, "", len);// Llama al ayudante para generar combinaciones
        }
        return Combinaciones;
    }

    private static void Generador2(List<String> Combinaciones, String CombinacionActual, int length) {
        if (length == 0) {
            Combinaciones.add(CombinacionActual);// Si la longitud alcanza 0, agrega la combinación actual a la lista
            return;
        }

        for (char c : alfabeto.toCharArray()) {
            Generador2(Combinaciones, CombinacionActual + c, length - 1);
        }
    }

    public void recorridocad(List<String> Combinaciones) {//primer metodo que hace llamada a la regla raiz
        ArrayList<String> Cvalidas = new ArrayList<>();
        ArrayList<String> Cinvalidas = new ArrayList<>();
        for (String cadena : Combinaciones) {
            ArrayList<String> camino2 = new ArrayList();
            cad = cadena;
            F = false;
            String pila = "";
            cadena = "";
            max = cad.length() + 5;
            PruebaNT(cadena, pila, "S", 0, camino2);
            
            //       System.out.println("Ultimo " + F);
            if (F == true) {
                if (Cvalidas.size() < 50) {
                    Cvalidas.add(cad);
                    //  System.out.println("cadena " + cad  + " aceptada");
                }

            } else {
                if (Cinvalidas.size() < 50) {
                    Cinvalidas.add(cad);
                    //    System.out.println("cadena " + cad  + " Rechazada");
                }
            }
        }
        validos = Cvalidas.toArray(new String[Cvalidas.size()]);
        invalidos = Cinvalidas.toArray(new String[Cinvalidas.size()]);
    }

    public void construirArbol() {
        raiz = new TreeNodeDerivation("S"); // Nodo raíz
        TreeNodeDerivation actual = raiz;

        for (String produccion : camino) {
            String[] partes = produccion.split(" ");
            for (String parte : partes) {
                if (!parte.equals(",")) {
                    TreeNodeDerivation nuevoNodo = new TreeNodeDerivation(parte);
                    actual.addChild(nuevoNodo);
                    actual = nuevoNodo;
                }
            }
            actual = raiz; // Reinicia al inicio para la siguiente producción
        }
    }

    // Método para imprimir el árbol de derivación
    public void imprimirArbol() {
        if (raiz != null) {
            System.out.println("Árbol de Derivación:");
            raiz.printTree(0);
        } else {
            System.out.println("El árbol de derivación no ha sido construido.");
        }
    }

    // Método para obtener la raíz del árbol de derivación
    public TreeNodeDerivation getRaiz() {
        return raiz;
    }

}
