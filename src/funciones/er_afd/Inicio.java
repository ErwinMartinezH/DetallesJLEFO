/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package funciones.er_afd;

import java.io.File;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mhg19
 */
public class Inicio {

    //se define una variable global para la transicion epsilon
    public static final String EPSILON = "ε";
    public static char EPSILON_CHAR = EPSILON.charAt(0);
    String regex = "";
    String archivo = "";
    String alfabetoER = "";
    AFDConstructor AFD;
    SyntaxTree syntaxTree;
 
    public static ArrayList<String> valido;
    public static ArrayList<String> nvalido;

    public Inicio(String er, String archivo, String alfabetoER) {
        this.regex = er;
        this.archivo = archivo;
        this.alfabetoER = alfabetoER;
        
        Mod_RegexConverter convert = new Mod_RegexConverter();
        try {

            regex = convert.infixToPostfix(regex);

        } catch (Exception e) {
            System.out.println("Expresión mal ingresada");
        }

        String regexExtended = regex + "#.";

        syntaxTree = new SyntaxTree();
        syntaxTree.buildTree(regexExtended);

        AFD = new AFDConstructor();
         valido = AFD.valido;
         nvalido = AFD.nvalido;
    }

    public DefaultTableModel tabla() {
        return AFD.creacionDirecta(syntaxTree, archivo, alfabetoER);
    }

}
