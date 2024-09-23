package ejecutar;

import java.awt.BorderLayout;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.JWindow;
import vista.V_splash;
import vista.V_interfaz;

/**
 *
 * @authors Alexis, José Carlos, Margarito
 */
public final class Jlefo extends JWindow {

    public Jlefo(String[] file) {
        try {
            V_splash panel = new V_splash();
            add(panel, BorderLayout.CENTER);
            setSize(panel.getWidth(), panel.getHeight());
            setLocationRelativeTo(null);
            setVisible(true);
            panel.progresoBar();
            dispose();
            empezar(file);
        } catch (InterruptedException e) {
            JOptionPane.showMessageDialog(null, "Error al iniciar", "Ups!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void empezar(String[] file) {
        //Ambiente de Windows
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info
                    : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException
                | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Jlefo.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }

        //Ejecución frame
        java.awt.EventQueue.invokeLater(() -> {
            new V_interfaz(file).setVisible(true);
        });

    }

    public static void main(String file[]) throws InterruptedException {
        File f = new File(System.getProperty("user.dir") + "/tmp");
        if (!f.exists()) {
            f.mkdir();
            new Jlefo(file);
        } else {
            new Jlefo(file);
        }
    }

}
