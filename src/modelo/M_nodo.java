/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author herma
 */
public class M_nodo {

    private final String idNodo;
    private final String tipo;
    private ArrayList<M_arco> sucesores;

    /**
     * Constructor para crear un nodo normal y agregar sucesores posteriormente
     *
     * @param id del nodo
     * @param tipo de nodo
     */
    public M_nodo(String id, String tipo) {
        this.idNodo = id;
        this.tipo = tipo;
        this.sucesores = new ArrayList();
    }

    /**
     * Constructor para crear un nodo con un solo sucesor, perteneciente a la
     * ruta
     *
     * @param idNodo su id
     * @param tipo de nodo
     * @param arc arco
     */
    public M_nodo(String idNodo, String tipo, M_arco arc) {
        this.idNodo = idNodo;
        this.tipo = tipo;
        this.sucesores = new ArrayList<>();
        sucesores.add(arc);
    }

    /**
     * @return id del nodo
     */
    public String getIdNodo() {
        return idNodo;
    }

    /**
     * @return tipo: ACEPTADO=1, RECHAZO=0
     */
    public String getTipo() {
        return tipo;
    }

    /**
     *
     * @param a arco
     */
    public void addSucesor(M_arco a) {
        sucesores.add(a);
    }

    /**
     *
     * @return ArrayList de sucesores
     */
    public ArrayList<M_arco> getSucesores() {
        return sucesores;
    }

    /**
     *
     * @param index remover sucesor de la posicion especifica
     */
    public void removerSucesor(int index) {
        if (!sucesores.isEmpty()) {
            sucesores.remove(index);
        }
    }

    @Override
    public String toString() {
        return "ID: " + idNodo + "Tipo: " + tipo + Arrays.toString(sucesores.toArray());
    }

}
