/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package funciones.backtracking;

import java.util.ArrayList;
import modelo.M_nodo;

/**
 *
 * @author herma
 */
public class Pila extends ArrayList {

    /**
     *
     * @param alfabeto elemento del alfabeto
     */
    public void insertarAlf(String alfabeto) {
        add(alfabeto);
    }

    /**
     *
     * @param sucesores nodo
     */
    public void insertarNod(M_nodo sucesores) {
        add(sucesores);
    }

    /**
     *
     * @return alfabeto de la pila
     */
    public String sacarAlf() {
        if (!isEmpty()) {
            String s = (String) get(size() - 1);
            remove(size() - 1);
            return s;
        }
        return null;
    }

    /**
     *
     * @return nodo de la pila
     */
    public M_nodo sacarNod() {
        if (!isEmpty()) {
            M_nodo n = (M_nodo) get(size() - 1);
            remove(size() - 1);
            return n;
        }
        return null;
    }

    /**
     * Remover nodo de la pila
     */
    public void removerNodo() {
        if (!isEmpty()) {
            remove(size() - 1);
        }
    }
}
