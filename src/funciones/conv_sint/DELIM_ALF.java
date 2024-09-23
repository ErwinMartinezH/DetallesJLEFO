/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package funciones.conv_sint;

/**
 *
 * @author pedro
 */
public class DELIM_ALF {
    
    private final static char[] NT ={'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
        'J', 'K', 'L', 'M', 'N','O','P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    
    private final static char[] T ={'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
        'j', 'k', 'l', 'm', 'n','o','p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    
    public static boolean isNT(char c){
        boolean b = false;
        for(int i = 0; i < NT.length; i++){
            if(c == NT[i]){
                b = true;
                break;
            }
        }
        return b;
    }
    
    public static boolean isT(char c){
        boolean b = false;
        for(int i = 0; i < T.length; i++){
            if(c == T[i]){
                b = true;
                break;
            }
        }
        return b;
    }
}
