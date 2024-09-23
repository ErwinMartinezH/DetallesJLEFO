/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package funciones.orden;

import java.util.List;

/**
 *
 * @author herma
 */
public class Ordenador {
    
    /**
     * Genera un ordenamiento de tipo ascendente sobre un Arraylist de objetos
     * 
     * @param objeto el List que quiere ordenar
     * @return List ordenado de modo ascendente 
     */
    public List quicksort(List objeto) {
        return quicksort(objeto, 0, objeto.size() - 1);
    }

    private List quicksort(List objeto, int inf, int sup) {
        if (inf >= sup) {
            return objeto;
        }
        int i = inf, s = sup;
        if (inf != sup) {
            int pivote;
            int aux;
            pivote = inf;
            while (inf != sup) {

                while ((int) objeto.get(sup) >= (int) objeto.get(pivote) && inf < sup) {
                    sup--;
                }
                while ((int) objeto.get(inf) < (int) objeto.get(pivote) && inf < sup) {
                    inf++;
                }

                if (sup != inf) {
                    aux = (int) objeto.get(sup);
                    objeto.set(sup, (int) objeto.get(inf));
                    objeto.set(inf, aux);
                }

            }

            if (inf == sup) {
                quicksort(objeto, i, inf - 1);
                quicksort(objeto, inf + 1, s);
            }

        } else {
            return objeto;
        }
        return objeto;
    }
    
}
