/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import funciones.orden.Ordenador;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author herma
 */
public class indicePestaña {
    
    private static int indice = 0;
    private static List pesEliminada = new ArrayList();

    public static void maxIndice() {
        indice++;
    }

    public static void minIndice() {
        indice--;
    }

    public static int getIndice() {
        return indice;
    }

    public static List getPesEliminada() {
        return pesEliminada;
    }

    public static void setPesEliminada(List pesEliminada) {
        indicePestaña.pesEliminada = new Ordenador().quicksort(pesEliminada);
    }
    
}
