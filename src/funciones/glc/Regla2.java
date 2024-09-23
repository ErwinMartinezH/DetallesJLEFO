/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package funciones.glc;

/**
 * # REGLA
 * Estructura basica de las reglas donde se tiene:
 * Cabecera -> produccion
 * @author pedro.jph
 */
public class Regla2 {
    private char nombre;
    private char[] instruccion;
    
    public Regla2(char n, char[] i){
        this.nombre = n;
        this.instruccion = i;
    }    
       
    public Regla2 getRegla(){
        return this;
    }

    public char getNombre() {
        return nombre;
    }

    public char[] getInstruccion() {
        return instruccion;
    }
}
