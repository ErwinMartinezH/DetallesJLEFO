/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.awt.Color;
import java.awt.geom.QuadCurve2D;
import java.io.Serializable;

/**
 *
 * @author herma
 */
public class M_transicion implements Serializable{
    //    (Xa,Ya) definen la coordenada de origen, (Xb,Yb) definen la coordenada 
//    destino
    private int Xa, Ya, Xb, Yb;
    private String tipo;
//    almacenan el id del estado
    private int origen, destino;
//    puntos de control para generar la curvatura de la transición
    private double ctrlX = 0, ctrlY = 0;
    private String alfabeto;

    //colisionador con el clic del mouse
    private QuadCurve2D mascara = null;

    private Color linea = M_colores.NORM_TRANSICION;
    private Color etiqueta = M_colores.ETIQUETA;

    public void setColores(Color c1, Color c2) {
        linea = c1;
        etiqueta = c2;
    }

    public void resetColores() {
        linea = M_colores.NORM_TRANSICION;
        etiqueta = M_colores.ETIQUETA;
    }
    

    /*
     * Getters
     */
    public int getXa() {
        return Xa;
    }

    public int getYa() {
        return Ya;
    }

    public int getXb() {
        return Xb;
    }

    public int getYb() {
        return Yb;
    }

    public String getTipo() {
        return tipo;
    }

    public int getOrigen() {
        return origen;
    }

    public int getDestino() {
        return destino;
    }

    public String getAlfabeto() {
        return alfabeto;
    }

    public QuadCurve2D getMascara() {
        return mascara;
    }

    /*
     * Setters
     */
    public void setXa(int Xa) {
        this.Xa = Xa;
    }

    public void setYa(int Ya) {
        this.Ya = Ya;
    }

    public void setXb(int Xb) {
        this.Xb = Xb;
    }

    public void setYb(int Yb) {
        this.Yb = Yb;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setOrigen(int origen) {
        this.origen = origen;
    }

    public void setDestino(int destino) {
        this.destino = destino;
    }

    public void setAlfabeto(String alfabeto) {
        this.alfabeto = alfabeto;
    }

    /**
     * Constructor
     *
     * @param Xa coordenada X del origen
     * @param Ya coordenada Y del origen
     * @param Xb coordenada X del destino
     * @param Yb coordenada Y del destino
     * @param tipo SIMPLE O ARCO
     * @param origen id del estado origen
     * @param destino id del estado destino
     * @param alfabeto
     */
    public M_transicion(int Xa, int Ya, int Xb, int Yb, String tipo, int origen, int destino, String alfabeto) {
        this.Xa = Xa;
        this.Ya = Ya;
        this.Xb = Xb;
        this.Yb = Yb;
        this.tipo = tipo;
        this.origen = origen;
        this.destino = destino;
        this.alfabeto = alfabeto;
    }

    /**
     * @return the ctrlX
     */
    public double getCtrlX() {
        return ctrlX;
    }

    /**
     * @return the ctrlY
     */
    public double getCtrlY() {
        return ctrlY;
    }

    /**
     * @return the linea
     */
    public Color getLinea() {
        return linea;
    }
    
    public Color getEtiqueta(){
        return etiqueta;
    }

    /**
     * @param ctrlX the ctrlX to set
     */
    public void setCtrlX(double ctrlX) {
        this.ctrlX = ctrlX;
    }

    /**
     * @param ctrlY the ctrlY to set
     */
    public void setCtrlY(double ctrlY) {
        this.ctrlY = ctrlY;
    }

    /**
     * @param mascara the mascara to set
     */
    public void setMascara(QuadCurve2D mascara) {
        this.mascara = mascara;
    }

    @Override
    public String toString() {
        return "Origen:" + getOrigen() + "destino:" + getDestino() 
                + "alfa:" + getAlfabeto() + "tipo:" + getTipo();
    }
}
