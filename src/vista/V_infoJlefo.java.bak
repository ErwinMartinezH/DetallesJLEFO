/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JWindow;

/**
 *
 * @author herma
 */
public class V_infoJlefo extends JWindow {

    final String ruta = "/img_icon/creditos.png";
    ImageIcon img;
    JButton btn;
    JPanel panel;

    public V_infoJlefo() {
        img = new ImageIcon(getClass().getResource(ruta));
        btn = new JButton("Cerrar");
        btn.setFont(new Font("Arial", Font.PLAIN, 13));
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        
        panel = new JPanel(new BorderLayout()) {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img.getImage(), 0, 0,
                        img.getIconWidth(),
                        img.getIconHeight(), this);
                setOpaque(false);
            }
        };
        panel.setSize(img.getIconWidth(), img.getIconHeight());
        panel.add(btn, BorderLayout.PAGE_END);

        this.setAlwaysOnTop(true);
        this.add(panel, BorderLayout.CENTER);
        this.setSize(panel.getWidth(), panel.getHeight());
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

}
