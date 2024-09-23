package vista;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.LayoutStyle;

/**
 *
 * @authors Alexis, José Carlos, Margarito
 */
public class V_splash extends JPanel {

    private ImageIcon img_splash;
    private JProgressBar barraCarga;
    private JLabel texto;
    private JLabel textoCarga;
    private final String ruta = "/img_icon/splash.png";

    public V_splash() {
        iniciarComponentes();
    }

    private void iniciarComponentes() {
        img_splash = new ImageIcon(getClass().getResource(ruta));
        textoCarga = new JLabel();
        barraCarga = new JProgressBar();
        texto = new JLabel();
        setSize(img_splash.getIconWidth(), img_splash.getIconHeight());
        textoCarga.setForeground(new Color(9, 30, 95));
        barraCarga.setForeground(new Color(203, 44, 26));
        barraCarga.setBorderPainted(false);
        texto.setText("HOLA, ESTAMOS DESARROLLANDO");
        layouts();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img_splash.getImage(), 0, 0,
                img_splash.getIconWidth(),
                img_splash.getIconHeight(), this);
        this.setOpaque(false);
    }

    public void progresoBar() throws InterruptedException {
        String txt = "MODO PRUEBA.";
        for (int i = 0; i <= 100; i++) {  // de 0% a 100%
            Thread.sleep(5);
            barraCarga.setValue(i);  //se asigna el valor de la barra %

            switch (i) //Mostrando texto 
            {
                case 01:
                    textoCarga.setText(txt);
                    break;
                case 20:
                    textoCarga.setText("Cargando módulo AFD . . . .");
                    break;
                case 40:
                    textoCarga.setText("Cargando módulo AFND . . . . . . .");
                    break;
                case 60:
                    textoCarga.setText("Cargando módulo e.r. . . . . . . . . . .");
                    break;
                case 70:
                    textoCarga.setText("Cargando librerias y componentes. . . . . . . . . . . . .");
                    break;
                case 90:
                    textoCarga.setText("Iniciando GUI. . . . . . . . . . . . . . . .");
                    break;
            }
        }
    }

    private void layouts() {
        javax.swing.GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(barraCarga, javax.swing.GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(texto, GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(textoCarga, GroupLayout.PREFERRED_SIZE, 303, GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(239, Short.MAX_VALUE)
                                .addComponent(textoCarga)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(barraCarga, GroupLayout.PREFERRED_SIZE, 5, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(texto)
                                .addContainerGap())
        );
    }

}
