/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package funciones.conv_sint;

import java.util.ArrayList;

import funciones.adp.Estado;
import funciones.adp.Transicion;
import funciones.glc.Regla2;
import static funciones.conv_sint.DELIM_ALF.isNT;

/**
 *
 * @author pedro
 */
public class Con_GRtoADP2 {
    //variable para almacenar todas las producciones que componen a la gramatica
    ArrayList <Regla2> producciones;
    
    //variable para definir el tipo de gramatica
    byte TYPE_GR = -1;
    final byte TYPE_GR_0 = 0;
    final byte TYPE_GR_1 = 1;
    final byte TYPE_GR_2 = 2;
    
    /*variables para crear el automata*/
    public ArrayList <Estado> estados = new ArrayList<>();
    
    private int q = 0;
    
    /*CONSTRUCTOR
    Una gramatica es el conjunto de reglas gramaticales, por lo tanto el constructor de esta clase dedicada
    a la conversion de una GLC a un ADP necesita el conjunto de reglas gramaticales
    Estas almacenadas en un objeto ArrayList
    */    
    /**
     * ## Constructor
     * @param producciones es un arreglo de Regla
     */
    public Con_GRtoADP2(ArrayList <Regla2> producciones) {
        estados.add(new Estado(0));
        this.producciones = producciones; 
        removeProduccion();
        conversion('S'); 
        for(Estado e: estados){
                    if(e.StateVoid()){
                        e.addTransicion(new Transicion(q, '?', 'Z', '?'));
                    }
                }
    }

    /*@Pedro    
    */
    public void conversion(char c){
        int QO = q;
        Integer QE = null;
        Regla2[] p = getNPTerminals(c);
               
        for (int i = 0; i<p.length; i++){
            char[] r = p[i].getInstruccion();
            for(int j = 0; j < r.length; j++){
                Transicion t;
                String pop, push;
                if(isNT(r[j]) && r[j] != c)conversion(r[j]);
                else if(isNT(r[j]) && r[j] == c){ 
                    estados.get(q).addTransicion(new Transicion(QO, '?', '?', '?'));
                    addTerminals(c);
                    if (j == r.length-1) estados.get(q).setStateVoid(true);
                    QE = q;
                }else if(!isNT(r[j])){
                    //----->
                    if (j>0) pop = (isNT(r[j-1])) ? "1" : "?";                                              
                    else pop = "?";
                    //<-----
                    //----->
                    if (j<r.length-1){
                        if (isNT(r[j+1])) push = (j+1 == r.length-1) ? "?" : "1";                            
                        else push = "?";
                    }else push = "?";                    
                    //<-----
                    estados.get(j == 0 ? QO : q).addTransicion(new Transicion(q+1, (r[j]), pop.charAt(0), push.charAt(0)));
                    estados.add(new Estado(++q));
                    if(j == r.length-1){
                        estados.get(q).addTransicion(new Transicion(QE, '?', '?', '?'));
                        estados.get(q).setStateVoid(true);
                        QE = null;
                        }
                }
            }            
        }
        addTerminals(c,QO,q);
        estados.get(q).setStateVoid(true);
    }
    public void addTerminals(char c, int QO, int qc){
        int QU  = qc + 1;        
        Regla2[] terminales = getPTerminals(c);
        if(terminales.length > 0){
            Transicion t =  null;
            for (int i = 0; i < terminales.length; i++){
                QU += (terminales[i].getInstruccion().length - 1);
            }
            for (int i = 0; i< terminales.length; i++){

                char[] terminal= terminales[i].getInstruccion();
                for (int j = 0; j< terminal.length; j++){
                    if(j+1 == terminal.length){
                        t = new Transicion(QU, terminal[j], '?', '?');
                        estados.get(j == 0 ? QO : qc).addTransicion(t);  
                    }else{
                        t = new Transicion(qc+1, terminal[j], '?', '?');
                        estados.get(j == 0 ? QO : qc).addTransicion(t);                    
                        estados.add(new Estado(++qc));
                    }   
                }            
            }   
            estados.add(new Estado(QU));
            q++;
        }
    }
    public void addTerminals(char c){
        int QO = q;
        int QU  = q + 1;        
        Regla2[] terminales = getPTerminals(c);
        if(terminales.length > 0){
            Transicion t =  null;
            for (int i = 0; i < terminales.length; i++){
                QU += (terminales[i].getInstruccion().length - 1);
            }
            for (int i = 0; i< terminales.length; i++){

                char[] terminal= terminales[i].getInstruccion();
                for (int j = 0; j< terminal.length; j++){
                    if(j+1 == terminal.length){
                        t = new Transicion(QU, terminal[j], '?', '?');
                        estados.get(j == 0 ? QO : q).addTransicion(t);  
                    }else{
                        t = new Transicion(q+1, terminal[j], '?', '?');
                        estados.get(j == 0 ? QO : q).addTransicion(t);                    
                        estados.add(new Estado(++q));
                    }   
                }            
            }   
            estados.add(new Estado(QU));
            q++;
        }
    }
    
    public Regla2[] getPTerminals(char c){
        ArrayList<Regla2> term = new ArrayList();
        for(Regla2 e : this.producciones){
            if(e.getNombre() == c){
                boolean terminal = true;
                char[] cr = e.getInstruccion();
                for(int i = 0; i<cr.length; i++){
                    if(isNT(cr[i])) {
                        terminal = false;
                        break;
                    }
                }
                if(terminal) term.add(e);
            }
        }        
        int i = 0;
        Regla2[] terminals = new Regla2[term.size()];
        for(Regla2 e : term) terminals[i++] = new Regla2(e.getNombre(), e.getInstruccion());
      
        return terminals;
    }
    
    public Regla2[] getNPTerminals(char c){
        ArrayList<Regla2> term = new ArrayList<>();
        for (Regla2 e : this.producciones){
            if(e.getNombre() == c){
                char[] cr = e.getInstruccion();
                for(int i = 0; i < cr.length; i++)
                    if(isNT(cr[i])){
                        term.add(e);
                        break;
                    }                
            }
        }
        int i = 0;
        Regla2[] terminals = new Regla2[term.size()];
        for(Regla2 e : term) terminals[i++] = new Regla2(e.getNombre(), e.getInstruccion());       
        
        return terminals;
    }
    
    /** 
     * ### Elimina las producciones epsilon de la gramatica 
     * @
     */
    public void removeProduccion(){
        ArrayList<Regla2> nuevalista = new ArrayList<>();
        for(Regla2 r : producciones){
            if(r.getInstruccion()[0] == '?');
            else nuevalista.add(r);
        }
        producciones = nuevalista;
    }
}
