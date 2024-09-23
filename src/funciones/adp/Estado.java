/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package funciones.adp;

import java.util.ArrayList;

/**
 * Nodo que contiene un conjunto de transiciones
 * @author pedro
 */
public class Estado {
    private ArrayList<Transicion> Transiciones = new ArrayList();
    private Transicion regla; //regla que aplica
    private int nombre;
    private boolean aceptacion;
    private boolean StateVoid = false;
    
    /**
     * constructor
     * @param nombre
     * @param acep  
     */
    public Estado (int nombre, boolean acep){//luego se debe validar que no se repita el nombre
        this.nombre = nombre;
        this.aceptacion = acep;
    }
    /**
     * Crea el nodo solo con nombre
     * por defaault sera false su estado de aceptación
     * @param nombre 
     */
    public Estado (int nombre){
        this.nombre = nombre;
    }
    /**
     * Cambio de estatus de aceptación
     * @param status 
     */
    public void setAceptacion(boolean status){
        this.aceptacion = status;
    }
    
    /**
     * Añade transiciones al estado
     * @param transicion 
     */
    public void addTransicion(Transicion transicion){
        Transiciones.add(transicion);
    }
    
    //busca la regla valida
    public boolean regla(char input){//input : valor que se esta leyendo
        boolean b = false;
        for(Transicion e : Transiciones){
            if (e.input == input){
                b = true;
                regla = e;
                break;
            }
        }
        return b;
    }
        
    //retorna la regla valida a utilizar
    public Transicion getReglaAplicada(){
        return regla;
    }
    
    //retorna el nombre del estado
    public int getNombre(){
        return nombre;
    }
    
    public ArrayList<Transicion> getTransiciones(){
        return this.Transiciones;
    }
    
    public void setTransiciones(ArrayList<Transicion> t){
        this.Transiciones = t;
    }
    
    public boolean getAceptacion(){
        return this.aceptacion;
    }
    
    public void setStateVoid(boolean t){
        StateVoid = t;
    }
    
    public boolean StateVoid(){
        return StateVoid;
    }
    
    @Override
    public String toString(){
        String s =nombre+" {\n";
        for(Transicion e: Transiciones){
            s += e.toString()+"\n";
        }
        return s+"}\n\n";
    }
}
