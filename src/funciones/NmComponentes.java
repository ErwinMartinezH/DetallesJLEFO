/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package funciones;

/**
 *
 * @author herma
 */
public class NmComponentes {

    //Interfaz
    public static final String BARRAMENU = "Barra de Menú";
    public static final String ARCHIVO = "Archivo";
    public static final String EDITAR = "Editar";
    public static final String NUEVO = "Nuevo";
    public static final String AYUDA = "Ayuda";
    //Definicion de nombres de herramientas
    public static final String AF = "Autómata Finito";
    public static final String ER = "Expresión Regular";
    public static final String ADP = "Automata de Pila";
    public static final String GLC = "Gramatica Libre de contexto";
    public static final String MT = "Maquina de Turing";
    public static final String MTMC = "Maquina de Turing multicinta";
    public static final String MTBD = "Maquina de Turing bidireccional";
    public static final String MTB = "Maquina de Turing por bloques";
    //
    public static final String ABRIR = "Abrir";
    public static final String GUARDAR = "Guardar";
    public static final String GUARDARCOMO = "Guardar como...";
    public static final String ACERCAR = "Acercar";
    public static final String ALEJAR = "Alejar";
    public static final String DESHACER = "Deshacer";
    public static final String REHACER = "Rehacer";
    public static final String TUTORIALES = "Tutoriales";
    public static final String MANUAL = "Manual";
    public static final String EJERCICIOS = "Ejercicios";
    public static final String ACERCADE = "JLEFO";
    public static final String BARRAHERR = "Barra de Herramientas";
    public static final String MENU = "Deslizar Menú de Herramientas";
    public static final String IMPRIMIR = "Imprimir";
    public static final String IMAGEN = "Guardar como Imagen";

    //Slide menu
    public static final String ESTADO = "Dibujar Estados";
    public static final String TRANSICION = "Dibujar Transición";
    public static final String SELECCIONAR = "Mover Estados";
    public static final String ANALIZAR = "Analizar Autómata";
    public static final String GENERAL = "General";
    public static final String BOTON ="Boton";
    
    //PopupMenu
    public static final String ESTADO_ACEP = "Estado de Aceptación";
    public static final String EDITAR_TRANS = "Editar Trnasición";
    public static final String ELIM_ESTADO = "Estado";
    public static final String ELIM_TRANS = "Transición";
    
    //Rastreo
    public static final String AFD = "AFD";
    public static final String ORDENAR = "Ordenar";
    public static final String PASO_A_PASO = "Paso a Paso";
    public static final String CERRAR = "Cerrar";
    public static final String MINIMIZAR = "Minimizar";
    public static final String MAXIMIZAR = "Maximizar";
    public static final String SLIDER = "Ajusta la velocidad";
    public static final String TABLACADENAS = "Tabla cadenas";
    public static final String AFND = "AFND";
    public static final String RETORNAR = "RETORNAR";
    public static final String DESCONECTADOS = "DESCONECTADOS";
    
    //Expresiones regulares
    public static final String CERO = "0";
    public static final String UNO = "1";
    public static final String PARENI = "(";
    public static final String PAREND = ")";
    public static final String O = "|";
    public static final String ASTERISCO = "*";
    public static final String MAS = "+";
    public static final String BORRAR = "BORRAR";
    public static final String ELIMINAR = "ELIMINAR";
    public static final String EJECUTAR = "ANALIZAR";
    public static final String AFD_FUNCION = "AFD";
    public static final String ORDEN = "Ordenar";

    //TEXTOS
    public static final String BIENVENIDA = "JLEFO (Java Lenguajes Formales)"
            + "\n\nBienvenido a JLEFO una nueva y útil herramienta que te servirá de "
            + "apoyo en el análisis y diseño de modelos de:"
            + "\n\nAutómatas Finitos Deterministas"
            + "\n\nAutómatas Finitos No Deterministas "
            + "\n\nExpresiones Regulares "
            + "\n\nDesarrollada por alumnos del Instituto Tecnológico de "
            + "Veracruz y financiada por el Instituto Tecnológico Nacional de México."
            + "\n\nVersión 1.0";

    public static final String INFO_ESTADO = "Estado: "
            + "\nSelecciona para dibujar un estado sobre el área de dibujo, mientras esta opción esta seleccionada dibuja cuantos estados te sea necesario."
            + "\n\nPara eliminar un estado da clic derecho sobre él y de menú emérgete selecciona la opción eliminar y posterior la opción estado."
            + "\n\nPara hacer estado de aceptación da clic derecho sobre el estado deseado del menú emergente selecciona la opción estado de aceptación."
            + "\n\nMientras esta opción este activa podrás dibujar únicamente estados."
            + "\n\nPara desactivar selecciona una de las otras opciones. ";

    public static String INFO_TRANSICION = "Transición: "
            + "\n\nSelecciona para dibujar las transiciones de estado a estado."
            + "\n\nPara dibuja un a traición da clic en el centro del estado inicial y sin soltar desliza al centro del estado final suelta y selecciona el alfabeto."
            + "\n\nPara editar el alfabeto de una transición da clic sobre ella y del menú emergente selecciona la opción editar transición y elige el alfabeto deseado."
            + "\n\nPara eliminar una transición da clic derecho sobre ella y de menú emérgete selecciona la opción eliminar y posterior la opción transición."
            + "\n\nMientras esta opción este activa podrás dibujar únicamente transiciones."
            + " Para desactivar selecciona una de las otras opciones. ";

    public static String INFO_SELECCIONAR = "Seleccionar: "
            + "\n\nSelecciona para manipular los estados del diagrama. "
            + "\n\nPodrás mover dando clic y sin soltar desliza el estado al lugar de tu preferencia.";

    public static String INFO_ANALIZAR = "Analizar: "
            + "\n\nSelecciona para realizar el análisis de diagrama que se tiene dibujado en el momento ya sea AFD o AFND."
            + "\n\nSe desplegara una ventana con el rastreo del diagrama que contiene:"
            + "\n\n• La tabla de transiciones."
            + "\n\n• La tabla de cadenas."
            + "\n\n• En caso de ser AFND la opción de convertir a AFD."
            + "\n\n• La opción de ordenar y desordenar las cadenas."
            + "\n\n• La opción de rastreo paso a paso que inicia la simulación del rastreo en el diagrama seleccionando una cadena de la tabla de cadenas."
            + "\n\n• La opción de   regular la velocidad del rastreo paso a paso."
            + "\n\n• La visualización de la cadena que se está rastreando.";
    
    public static final String RASTREANDO = "Para  editar  el  diagrama,\ncierre la ventana de Rastreo.";
    public static final String SIN_TRANS = "El estado inicial no tiene transiciones";
    public static final String SIN_CONEXION = "Uno o más estados son inalcanzables";
    
    public static final String URL = "https://www.youtube.com/channel/UC-bWxxYxT5aKQECC2yjpVnQ?view_as=subscriber";
    public static final String NO_CONEXION = "No se ha podido conectar al sitio web";
    public static final String NO_ENCONTRADO = "No se encontró el archivo solicitado";
    
    
     public static final String ER_INFO = "Expresiones regulares"
                                    + "\n"
                                    + "\n"
                                    + "En este módulo podrás ingresar y analizar expresiones regulares, así como obtener AFD equivalente."
                                    + "\n"
                                    + "\n"
                                    + "Ingresa una expresión regular en el campo especificado, siguiendo el formato correspondiente."
                                    + "\n"
                                    + "\n"
                                    + "Ejemplo:"
                                    + "\n" 
                                    + "\n"
                                    +" (0|1)*011 ";
    
    public static final String C_INFO = "Eliminar : "
                                   + "\n" 
                                   + "\n"
                                   + "Elimina toda la expresión regular ingresada.";

    public static final String CE_INFO = "Borrar : "
                                    + "\n" 
                                    + "\n" 
                                    + "Borra carácter por carácter de la expresión regular.";

    public static final String GO_INFO = "Analizar :  "
                                    + "\n" 
                                    + "\n"
                                    + "Realiza el análisis de la expresión regular ingresada, obtiene la tabla de cadenas aceptadas y no aceptadas.";
    
    public static final String AFD_INFO = "AFD : "
                                   + "\n"
                                   + "\n"
                                   + "Realiza la conversión de la expresión regular introducida a su autómata finito determinista equivalente.";
    
    public static final String ORDENAR_INFO = "Desordenar/Ordenar : "
                                       + "\n" 
                                       + "\n"
                                       + "Desordena y ordena las cadenas.";
    
    public static final String CERO_INFO = "\" 0 \""
                                    + "\n"
                                    + "\n"
                                    + "Símbolo del alfabeto, inserta un 0 en la expresión regular.";
    
    public static final String UNO_INFO = "\" 1 \""
                                    + "\n"
                                    + "\n"
                                    + "Símbolo del alfabeto, inserta un 1 en la expresión regular.";
    
    public static final String PARENIZQ_INFO = "\" ( \""
                                        + "\n"
                                        + "\n"
                                        + "Operador, paréntesis izquierdo.\n";
    
    public static final String PARENDER_INFO = "\" ) \""
                                        + "\n" 
                                        + "\n"
                                        + "Operador, paréntesis derecho.";
    
    public static final String PIPE_INFO = "\" | \""
                                    + "\n"
                                    + "\n"
                                    + "Operador, pleca. \n\nSe utiliza como operador lógico OR ";
    
    public static final String ESTRELLAMAS_INFO = "\" + \""
                                           + "\n" 
                                           + "\n"
                                           + "Operador, cruz."
                                           + "\n"
                                           + "\n"
                                           + "Se utiliza para denotar lo siguiente: \n r+ es una e.r. que denota a RR*\n Es decir:\n"
                                           + " 0+ ={0}+ = {0}{0}+";
    
    public static final String ESTRELLA_INFO = "\" * \""
                                        + "\n"
                                        + "\n"
                                        + "Operador, estrella."
                                        + "\n"
                                        + "\n"
                                        + "Se utiliza para denotar lo siguiente: \n r* es una e.r. que denota a R*\n Es decir:\n"
                                           + " 0* = {ε,0,00,000,...}}";
    
    
}
