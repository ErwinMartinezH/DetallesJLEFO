/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.awt.Color;
import java.io.Serializable;

/**
 *
 * @author herma
 */
public class M_estado implements Serializable{
        private int x, y;
    private String etiqueta;
    private String tipo;
    private int idEstado;
    private Color relleno = M_colores.NORM_ESTADO;
    private Color contorno = M_colores.NORM_CONTORNO;
    private Color texto = M_colores.NORM_IDESTADO;
    private Color edoInicial = M_colores.NORM_EDOINICIAL;

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setIdEstado(int idEstado) {
        etiqueta = "q" + String.valueOf(idEstado);
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public String getTipo() {
        return tipo;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setColores(Color c1, Color c2, Color c3) {
        relleno = c1;
        contorno = c2;
        texto = c3;
    }

    public void resetColores() {
        relleno = M_colores.NORM_ESTADO;
        contorno = M_colores.NORM_CONTORNO;
        texto = M_colores.NORM_IDESTADO;
    }

    //Para todo estado recibimos los mismos parametros
    public M_estado(int x, int y, int idEstado, String tipo) {
        this.x = x;
        this.y = y;
        this.idEstado = idEstado;
        etiqueta = "q" + String.valueOf(this.idEstado);
        this.tipo = tipo;
    }

    /**
     * @return the relleno
     */
    public Color getRelleno() {
        return relleno;
    }

    /**
     * @return the contorno
     */
    public Color getContorno() {
        return contorno;
    }

    /**
     * @return the texto
     */
    public Color getTexto() {
        return texto;
    }

    /**
     * @return the edoInicial
     */
    public Color getEdoInicial() {
        return edoInicial;
    }

    @Override
    public String toString() {
        return "etiqueta: " + etiqueta + "tipo: " + tipo;
    }

}
