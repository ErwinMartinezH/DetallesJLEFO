/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package funciones.ctrlZ_Y;

import control.C_automata;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import modelo.M_estado;
import modelo.M_transicion;
import vista.V_lienzo;

/**
 *
 * @author herma
 */
public class Control {

    private final String sufijo = "-VObj.bin";

    /**
     * Constructor vacio
     */
    public Control() {

    }

    /**
     * Inicializa la estructura para el guardado de las versiones
     *
     * @param rootFolder nombre del folder raiz
     */
    public Control(String rootFolder) {
        try {
            String root = System.getProperty("user.dir") + "/tmp/" + rootFolder.replace(" ", "");
            String tmp_undo = root + "/tmp_undo";
            String tmp_redo = root + "/tmp_redo";
            File directorio = new File(root);
            directorio.mkdir();
            directorio = new File(tmp_undo);
            directorio.mkdir();
            directorio = new File(tmp_undo + "/pointer.txt");
            directorio.createNewFile();
            directorio = new File(tmp_redo);
            directorio.mkdir();
            directorio = new File(tmp_redo + "/pointer.txt");
            directorio.createNewFile();
        } catch (IOException e) {
            System.err.println("IOException directorio: " + e.getMessage());
        }
    }

    /**
     * Crear un objeto en el direcotorio tmp_undo y actualiza el puntero
     *
     * @param rootFolder nombre del folder raiz
     * @param trans objeto de tipo transicion <List>
     * @param est objeto de tipo estado <List>
     * @param edoElim objeto que contiene id de estados eliminados
     * @param version numero de versión del objeto
     */
    public void crearObjetoUndo(String rootFolder, List trans, List est, List edoElim, int version) {
        //preparar ruta para objeto y puntero
        String folder = System.getProperty("user.dir") + "/tmp/" + rootFolder.replace(" ", "") + "/tmp_undo/";
        String pointer = folder + "pointer.txt";
        String objeto = folder + version + sufijo;

        //guardar objeto
        try {
            FileOutputStream fos = new FileOutputStream(objeto);
            ObjectOutputStream ous = new ObjectOutputStream(fos);
            ous.writeObject(est);
            ous.writeObject(edoElim);
            ous.writeObject(trans);
            ous.close();
            fos.close();
        } catch (IOException e) {
            System.err.println("IOException crear objeto undo: " + e.getMessage());
        }

        //actualizar puntero
        if (version == 0) {
            //indicar el puntero a la primera version
            try {
                FileWriter fw = new FileWriter(pointer);
                BufferedWriter br = new BufferedWriter(fw);
                br.write("A-" + version + sufijo);
                br.close();
                fw.close();
            } catch (IOException e) {
                System.err.println("IOException escribir puntero: " + e.getMessage());
            }
        } else {
            //ya tiene el puntero inicial
            //leer el contenido
            ArrayList<String> lineas = new ArrayList();
            try {
                FileReader fr = new FileReader(pointer);
                BufferedReader br = new BufferedReader(fr);
                String linea = br.readLine();
                while (linea != null) {
                    lineas.add(linea);
                    linea = br.readLine();
                }
                br.close();
                fr.close();
            } catch (IOException e) {
                System.err.println("IOException leer puntero: " + e.getMessage());
            }
            //eliminar el puntero de la posicion actual y moverlo a la ultima
            //version creada
            String reemplazar = lineas.get(lineas.size() - 1);
            String reemplazo = reemplazar.replace("A-", "");
            lineas.set(lineas.size() - 1, reemplazo);
            lineas.add("A-" + version + sufijo);
            try {
                FileWriter fw = new FileWriter(pointer);
                BufferedWriter bw = new BufferedWriter(fw);
                for (String s : lineas) {
                    bw.write(s);
                    bw.newLine();
                }
                bw.close();
                fw.close();
            } catch (IOException e) {
                System.err.println("IOException actualizar puntero: " + e.getMessage());
            }

        }

    }

    /**
     * Crear un objeto en el directorio tmp_redo y actualiza el puntero
     *
     * @param rootFolder nombre del folder raiz
     * @param trans objeto de tipo transicion <List>
     * @param est objeto de tipo estado <List>
     * @param edoElim objeto que contiene id de estados eliminados
     * @param version numero de version del objeto
     */
    public void crearObjetoRedo(String rootFolder, List trans, List est, List edoElim, int version) {
        //preparar ruta para objeto y puntero
        String folder = System.getProperty("user.dir") + "/tmp/" + rootFolder + "/tmp_redo/";
        String pointer = folder + "pointer.txt";
        String objeto = folder + version + sufijo;
        String linea = null;

        //crear objeto
        try {
            FileOutputStream fos = new FileOutputStream(objeto);
            ObjectOutputStream ous = new ObjectOutputStream(fos);
            ous.writeObject(est);
            ous.writeObject(edoElim);
            ous.writeObject(trans);
            ous.close();
            fos.close();
        } catch (IOException e) {
            System.err.println("IOException crear objeto redo: " + e.getMessage());
        }

        ArrayList<String> lineas = new ArrayList();
        //verificar si el puntero esta vacio
        try {
            FileReader fr = new FileReader(pointer);
            BufferedReader br = new BufferedReader(fr);
            linea = br.readLine();
            while (linea != null) {
                lineas.add(linea);
                linea = br.readLine();
            }
            br.close();
            fr.close();
        } catch (IOException e) {
            System.err.println("IOException al leer puntero: " + e.getMessage());
        }

        if (lineas.isEmpty()) {
            //actualizar puntero
            try {
                FileWriter fw = new FileWriter(pointer);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write("B-" + version + sufijo);
                bw.close();
                fw.close();
            } catch (IOException e) {
                System.out.println("IOException crear puntero: " + e.getMessage());
            }
        } else {
            String reemplazar = lineas.get(lineas.size() - 1);
            String reemplazo = reemplazar.replace("B-", "");
            lineas.set(lineas.size() - 1, reemplazo);
            lineas.add("B-" + version + sufijo);
            try {
                FileWriter fw = new FileWriter(pointer);
                BufferedWriter bw = new BufferedWriter(fw);
                for (String s : lineas) {
                    bw.write(s);
                    bw.newLine();
                }
                bw.close();
                fw.close();
            } catch (IOException e) {
                System.err.println("IOException actualizar puntero: " + e.getMessage());
            }
        }

    }

    /**
     * Obtener los objetos del archivo para actualizar a la version anterior
     *
     * @param rootFolder nombre del folder raiz
     * @param version numero de version del objeto
     * @param lienzo el area de dibujo para actualizar los arrays
     */
    public void getObjetosUndo(String rootFolder, String version, V_lienzo lienzo) {
        String objeto = System.getProperty("user.dir") + "/tmp/" + rootFolder + "/tmp_undo/" + version;
        try {
            FileInputStream fis = new FileInputStream(objeto);
            ObjectInputStream ois = new ObjectInputStream(fis);
//            lienzo.setEstados((List<M_estado>) ois.readObject());
//            lienzo.setEstadosElim((List) ois.readObject());
//            lienzo.setTransiciones((List<M_transicion>) ois.readObject());
            MouseListener[] Array = lienzo.getMouseListeners();
            C_automata c = (C_automata) Array[0];
            c.setEstados((List<M_estado>) ois.readObject());
            c.setEstadosElim((List) ois.readObject());
            c.setTransiciones((List<M_transicion>) ois.readObject());
            ois.close();
            fis.close();
        } catch (ClassNotFoundException e) {
            System.err.println("ClassNotFoundException retorno objeto estado: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IOException leer objeto estados: " + e.getMessage());
        }
        File f = new File(objeto);
        f.delete();
    }

    /**
     * Obtener el nombre del objeto para su recuperacion
     *
     * @param rootFolder nombre del folder raiz
     * @return el nombre del objeto del directorio tmp_undo
     */
    public String getVersionUndo(String rootFolder) {
        String pointer = System.getProperty("user.dir") + "/tmp/" + rootFolder + "/tmp_undo/pointer.txt";
        ArrayList<String> lineas = new ArrayList();
        String objeto;
        try {
            FileReader fr = new FileReader(pointer);
            BufferedReader br = new BufferedReader(fr);
            String linea = br.readLine();
            while (linea != null) {
                lineas.add(linea);
                linea = br.readLine();
            }
            br.close();
            fr.close();
        } catch (IOException e) {
            System.err.println("IOException leer puntero: " + e.getMessage());
        }

        if (lineas.isEmpty()) {
            return null;
        }

        if (!lineas.get(lineas.size() - 1).equals("A-0-VObj.bin")) {
//            objeto = lineas.get(lineas.size() - 1).replace("A-", "");
//            try {
//                FileWriter fw = new FileWriter(pointer);
//                BufferedWriter bw = new BufferedWriter(fw);
//                bw.write("");
//                bw.close();
//                fw.close();
//            } catch (IOException e) {
//                System.err.println("IOException vaciar puntero: " + e.getMessage());
//            }
            String puntero = lineas.get(lineas.size() - 1).replace("A-", "");
            lineas.remove(lineas.size() - 1);
            objeto = puntero;
            puntero = "A-" + lineas.get(lineas.size() - 1);
            lineas.set(lineas.size() - 1, puntero);
            try {
                FileWriter fw = new FileWriter(pointer);
                BufferedWriter bw = new BufferedWriter(fw);
                for (String l : lineas) {
                    bw.write(l);
                    bw.newLine();
                }
                bw.close();
                fw.close();
            } catch (IOException e) {
                System.err.println("IOException escribir puntero; " + e.getMessage());
            }
        } else {
            return null;
        }
        return objeto;
    }

    /**
     * Obtener el nombre del objeto para su recuperacion
     *
     * @param rootFolder nombre del folder raiz
     * @return el nombre del objeto del directorio tmp_redo
     */
    public String getVersionRedo(String rootFolder) {
        String pointer = System.getProperty("user.dir") + "/tmp/" + rootFolder + "/tmp_redo/pointer.txt";
        ArrayList<String> lineas = new ArrayList();
        String objeto;
        try {
            FileReader fr = new FileReader(pointer);
            BufferedReader br = new BufferedReader(fr);
            String linea = br.readLine();
            while (linea != null) {
                lineas.add(linea);
                linea = br.readLine();
            }
            br.close();
            fr.close();
        } catch (IOException e) {
            System.err.println("IOException leer puntero: " + e.getMessage());
        }
        if (lineas.isEmpty()) {
            return null;
        }

        if (lineas.size() == 1) {
            objeto = lineas.get(lineas.size() - 1).replace("B-", "");
            try {
                FileWriter fw = new FileWriter(pointer);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write("");
                bw.close();
                fw.close();
            } catch (IOException e) {
                System.err.println("IOException vaciar puntero: " + e.getMessage());
            }
        } else {
            String puntero = lineas.get(lineas.size() - 1).replace("B-", "");
            lineas.remove(lineas.size() - 1);
            objeto = puntero;
            puntero = "B-" + lineas.get(lineas.size() - 1);
            lineas.set(lineas.size() - 1, puntero);
            try {
                FileWriter fw = new FileWriter(pointer);
                BufferedWriter bw = new BufferedWriter(fw);
                for (String l : lineas) {
                    bw.write(l);
                    bw.newLine();
                }
                bw.close();
                fw.close();
            } catch (IOException e) {
                System.err.println("IOException escribir puntero; " + e.getMessage());
            }
        }
        return objeto;
    }

    /**
     * Obtener los objetos del archivo para actualizar a la version anterior
     *
     * @param rootFolder nombre del folder raiz
     * @param version numero de version del objeto
     * @param lienzo el area de dibujo para actualizar los arrays
     */
    public void getObjetosRedo(String rootFolder, String version, V_lienzo lienzo) {
        String objeto = System.getProperty("user.dir") + "/tmp/" + rootFolder.replace(" ", "") + "/tmp_redo/" + version;
        try {
            FileInputStream fis = new FileInputStream(objeto);
            ObjectInputStream ois = new ObjectInputStream(fis);
//            lienzo.setEstados((List<M_estado>) ois.readObject());
//            lienzo.setEstadosElim((List) ois.readObject());
//            lienzo.setTransiciones((List<M_transicion>) ois.readObject());
            MouseListener[] Array = lienzo.getMouseListeners();
            C_automata c = (C_automata) Array[0];
            c.setEstados((List<M_estado>) ois.readObject());
            c.setEstadosElim((List) ois.readObject());
            c.setTransiciones((List<M_transicion>) ois.readObject());
            ois.close();
            fis.close();
        } catch (ClassNotFoundException e) {
            System.err.println("ClassNotFoundException retorno objeto estado: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IOException leer objeto estados: " + e.getMessage());
        }
        File f = new File(objeto);
        f.delete();
    }

    /**
     * Eliminar el resto de objetos del directorio tmp_redo cuando se haya
     * efectuado un cambio en el lienzo
     *
     * @param rootFolder nombre del folder raiz
     */
    public void clearDirRedo(String rootFolder) {
        String dir = System.getProperty("user.dir") + "/tmp/" + rootFolder + "/tmp_redo";
        File f = new File(dir);
        String[] archivos = f.list();
        for (String a : archivos) {
            if (a.endsWith("bin")) {
                f = new File(dir + "/" + a);
                f.delete();
            } else if (a.equals("pointer.txt")) {
                try {
                    FileWriter fw = new FileWriter(dir + "/" + a);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write("");
                    bw.close();
                    fw.close();
                } catch (IOException e) {
                    System.err.println("IOException al borrar puntero: " + e.getMessage());
                }

            }
        }
    }

    /**
     * Eliminar toda la estructura generada para el control de versiones
     *
     * @param rootFolder nombre del folder raiz
     */
    public void deleteRootFolder(String rootFolder) {
        String rootDir = System.getProperty("user.dir") + "/tmp/" + rootFolder.replace(" ", "");
        String undoDir = rootDir + "/tmp_undo";
        String redoDir = rootDir + "/tmp_redo";
        ArrayList<String> dirs = new ArrayList();
        dirs.add(undoDir);
        dirs.add(redoDir);
        dirs.add(rootDir);
        for (String dir : dirs) {
            File f = new File(dir);
            String[] archivos = f.list();
            for (String a : archivos) {
                f = new File(dir + "/" + a);
                f.delete();
            }
            f = new File(dir);
            f.delete();
        }
    }

    public String getPunteroUndo(String rootFolder) {
        String pointer = System.getProperty("user.dir") + "/tmp/" + rootFolder + "/tmp_undo/pointer.txt";
        String linea;
        try {
            FileReader fr = new FileReader(pointer);
            BufferedReader br = new BufferedReader(fr);
            linea = br.readLine();
            br.close();
            fr.close();
            if (linea != null) {
                return linea;
            } else {
                return null;
            }
        } catch (IOException e) {
            System.err.println("IOException ocurred" + e.getMessage());
        }
        return null;
    }

    public String getPunteroRedo(String rootFolder) {
        String pointer = System.getProperty("user.dir") + "/tmp/" + rootFolder + "/tmp_redo/pointer.txt";
        String linea;
        try {
            FileReader fr = new FileReader(pointer);
            BufferedReader br = new BufferedReader(fr);
            linea = br.readLine();
            br.close();
            fr.close();
            if (linea != null) {
                return linea;
            } else {
                return null;
            }
        } catch (IOException e) {
            System.err.println("IOException ocurred" + e.getMessage());
        }
        return null;
    }

}
