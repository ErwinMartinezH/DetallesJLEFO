/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package img_icon;

import java.awt.Image;
import java.awt.Toolkit;

/**
 *
 * @author Alexis
 */
public class importarImg {
    public Image cargar(String ruta) {
        return Toolkit.getDefaultToolkit().createImage((getClass().getResource(ruta)));
    }
}
