/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package funciones.adp;

/** 
 * Clase dedicada al formato de reglas en las transiones segun lo marcado en la
 * sintaxis de un Atomata De Pila
 * 
 *          Input   ,           pop             ,           push
 * ->  Dato leido   , dato a sacar de la pila   , dato a ingresar en la pila
 * @author Pedro JPH
 */
public class Transicion {
    
    public int estde;
    char input, pop, push;
    
    public Transicion (int destino, char lectura, char pop, char push){
        this.estde = destino;
        this.input = lectura;
        this.pop =  pop;
        this.push = push;
    }
    
    public String regla(){
        return input + ", "+pop+", "+push;
    }
        
    @Override
    public String toString(){
        return input + ", "+pop+", "+push+" /"+estde;
    }
}
