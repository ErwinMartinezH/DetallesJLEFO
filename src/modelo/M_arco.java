/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author herma
 */
public class M_arco {

    private final String origen;
    private final String destino;
    private final String alfabeto;

    public M_arco(String origen, String destino, String alfabeto) {
        this.origen = origen;
        this.destino = destino;
        this.alfabeto = alfabeto;
    }

    /**
     * @return origen del arco (idnodo)
     */
    public String getOrigen() {
        return origen;
    }

    /**
     * @return alfabeto de transicion
     */
    public String getAlfabeto() {
        return alfabeto;
    }
    
    /**
     * 
     * @return deltino del arco (idnodo)
     */
    public String getDestino(){
        return destino;
    }

    @Override
    public String toString() {
        return "Origen:" + origen + " Destino:" + destino + " Alfabeto:" + alfabeto;
    } 

}
