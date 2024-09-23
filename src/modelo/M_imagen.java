package modelo;

import static funciones.NmComponentes.AF;
import funciones.orden.Ordenador;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @authors Alexis, José Carlos, Margarito
 */
public class M_imagen extends JPanel {

    private double factor = 1;
    private int ancho;
    private int alto;
    private int diametro = 50;
    private List<M_estado> estados;
    private List<M_transicion> transiciones;
    private Dimension d;
    private Rectangle area;
    private Point p;

    public Point getP() {
        return p;
    }

    public M_imagen(List<M_estado> estados, List<M_transicion> transiciones) {
        setSize(400, 300);
        this.estados = estados;
        this.transiciones = transiciones;
        setBackground(Color.WHITE);
    }

    public M_imagen() {
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        ancho = (int) (this.getWidth() * factor);
        alto = (int) (this.getHeight() * factor);
        setPreferredSize(new Dimension(ancho, alto));
        revalidate();

        for (M_transicion trans : transiciones) {
            dibujarTransicion(g2, trans);
        }

        for (M_estado estado : estados) {
            switch (estado.getTipo()) {
                case "Edo-Inicial":
                    dibujarEdoInic(g2, estado);
                    break;

                case "Edo-Transicion":
                    if (estado.getIdEstado() == 0) {
                        dibujarEdoInic(g2, estado);

                    } else {
                        dibujarEdoTrans(g2, estado);
                    }
                    break;

                case "Edo-Aceptacion":
                    if (estado.getIdEstado() == 0) {
                        dibujarEdoInic(g2, estado);
                    } else {
                        dibujarEdoTrans(g2, estado);
                    }
                    break;
            }
        }

        factor = 1;
        g2.setColor(Color.black);
        g2.drawRect(area.x + 2, area.y + 2, area.width - 4, area.height - 4);
    }

    /**
     * Dibujar estado
     *
     * @param g2 graficos
     * @param e estado
     */
    public void dibujarEdoTrans(Graphics2D g2, M_estado e) {
        int x, y, nDiametro;
        x = (int) (e.getX() * factor);
        y = (int) (e.getY() * factor);
        nDiametro = (int) (diametro * factor);
        BasicStroke linea;
        if ((diametro * 0.03f) < 1) {
            linea = new BasicStroke(1.5f);
        } else {
            linea = new BasicStroke(diametro * 0.03f);
        }
        g2.setStroke(linea);
        g2.setColor(e.getRelleno()); //color circulo
        g2.fillOval(x - nDiametro / 2, y - nDiametro / 2, nDiametro, nDiametro);
        g2.setColor(e.getContorno());
        g2.drawOval(x - nDiametro / 2, y - nDiametro / 2, nDiametro, nDiametro);

        //Condicion para hacer estado de aceptación
        if (e.getTipo().equals("Edo-Aceptacion")) {
            g2.setColor(e.getContorno()); //color de marcado
            g2.drawOval(x - (int) (nDiametro * 0.4f), y - (int) (nDiametro * 0.4f), (int) (nDiametro * 0.8f), (int) (nDiametro * 0.8f));

        }

        g2.setFont(new Font("Arial", Font.BOLD, (int) (nDiametro * 0.3f)));
        g2.setColor(e.getTexto()); //color texto
        g2.drawString(e.getEtiqueta(), x - (int) (nDiametro * 0.1f), y + (int) (nDiametro * 0.1f));

        e.setX(x);
        e.setY(y);
        diametro = nDiametro;
    }

    /**
     * Dibujar estado inicial
     *
     * @param g2 graficos
     * @param e estado
     */
    public void dibujarEdoInic(Graphics2D g2, M_estado e) {
        int x, y, nDiametro;
        x = (int) (e.getX() * factor);
        y = (int) (e.getY() * factor);
        nDiametro = (int) (diametro * factor);
        BasicStroke linea;
        if ((diametro * 0.03f) < 1) {
            linea = new BasicStroke(1.5f);
        } else {
            linea = new BasicStroke(diametro * 0.03f);
        }
        g2.setStroke(linea);
        g2.setColor(e.getRelleno()); //color circulo  
        g2.fillOval(x - nDiametro / 2, y - nDiametro / 2, nDiametro, nDiametro);
        g2.setColor(e.getContorno());
        g2.drawOval(x - nDiametro / 2, y - nDiametro / 2, nDiametro, nDiametro);

        //Condicion para hacer estado de aceptación
        if (e.getTipo().equals("Edo-Aceptacion")) {
            g2.setColor(e.getContorno()); //color de marcado
            g2.drawOval(x - (int) (nDiametro * 0.4f), y - (int) (nDiametro * 0.4f), (int) (nDiametro * 0.8f), (int) (nDiametro * 0.8f));

        }

        g2.setFont(new Font("Arial", Font.BOLD, (int) (nDiametro * 0.3f)));
        g2.setColor(e.getTexto()); //color texto
        g2.drawString(e.getEtiqueta(), x - (int) (nDiametro * 0.1f), y + (int) (nDiametro * 0.1f));

        g2.setColor(e.getEdoInicial()); //triangulo
        int picoy = y;
        int picox = x - nDiametro / 2;
        int base = picox - (int) (nDiametro * 0.4f);
        int verticey1 = y - (int) (nDiametro * 0.4f);
        int verticey2 = y + (int) (nDiametro * 0.4f);
        int[] Xcoordenadas = {base, base, picox};
        int[] Ycoordenadas = {verticey1, verticey2, picoy};
        g2.fillPolygon(Xcoordenadas, Ycoordenadas, 3);

        e.setX(x);
        e.setY(y);
        diametro = nDiametro;
    }

    /**
     * Traza la punta de flecha de la transicion
     *
     * @param punta point
     * @param t transicion
     * @param g2 graficos
     */
    private void puntaDeFlecha(Point punta, M_transicion t, Graphics2D g2) {
        double pi = Math.toRadians(30); //angulo de abertura
        int barb = (int) (diametro * 0.24f); //largo
        double dy = punta.y - t.getCtrlY();
        double dx = punta.x - t.getCtrlX();
        double theta = Math.atan2(dy, dx);
        double x, y, rho = theta + pi;
        for (int j = 0; j < 2; j++) {
            x = punta.x - barb * Math.cos(rho);
            y = punta.y - barb * Math.sin(rho);
            g2.draw(new Line2D.Double(punta.x, punta.y, x, y));
            rho = theta - pi;
        }
    }

    /**
     * Punto de curvatura
     *
     * @param b point
     * @param pmx punto medio en x
     * @param pmy punto medio en y
     * @param distPm distancia punto medio
     * @param t transicion
     */
    private void puntoControlCurva(Point b, double pmx, double pmy, double distPm, M_transicion t, Graphics2D g2) {
        g2.setColor(t.getEtiqueta());
        double pi = 0;
        double piLabel = 0;
        double c1, c2;
        if (distPm > 0 && distPm < 50) {
            pi = Math.toRadians(30);
            piLabel = Math.toRadians(15);
        } else if (distPm > 50 && distPm < 60) {
            pi = Math.toRadians(25);
            piLabel = Math.toRadians(12.5);
        } else if (distPm > 60 && distPm < 70) {
            pi = Math.toRadians(20);
            piLabel = Math.toRadians(10);
        } else if (distPm > 70 && distPm < 80) {
            pi = Math.toRadians(15);
            piLabel = Math.toRadians(7.5);
        } else if (distPm > 80 && distPm < 100) {
            pi = Math.toRadians(10);
            piLabel = Math.toRadians(5);
        } else if (distPm > 100 && distPm < 150) {
            pi = Math.toRadians(9);
            piLabel = Math.toRadians(4.5);
        } else if (distPm > 150 && distPm < 200) {
            pi = Math.toRadians(8);
            piLabel = Math.toRadians(4);
        } else if (distPm > 200 && distPm < 250) {
            pi = Math.toRadians(7);
            piLabel = Math.toRadians(3.5);
        } else if (distPm > 250 && distPm < 300) {
            pi = Math.toRadians(5);
            piLabel = Math.toRadians(2.5);
        } else if (distPm > 300 && distPm < 400) {
            pi = Math.toRadians(4);
            piLabel = Math.toRadians(2);
        } else if (distPm > 400) {
            pi = Math.toRadians(3);
            piLabel = Math.toRadians(1.5);
        }

        double dy = b.y - pmy;
        double dx = b.x - pmx;
        double theta = Math.atan2(dy, dx);
        double rho = theta + pi;
        double rho1 = theta + piLabel;
        t.setCtrlX(b.x - distPm * Math.cos(rho));
        t.setCtrlY(b.y - distPm * Math.sin(rho));
        c1 = (b.x - distPm * Math.cos(rho1));
        c2 = (b.y - distPm * Math.sin(rho1));

        g2.setFont(new Font("Arial", Font.BOLD, (int) (diametro * 0.3f)));
        if (Math.cos(rho1) < 0 && Math.sin(rho1) < 0) {
            if (Math.sin(rho1) < -0.5) {
                g2.drawString(t.getAlfabeto(), (int) c1 - (int) (diametro * 0.1), (int) c2 + (int) (diametro * 0.3));
            } else {
                g2.drawString(t.getAlfabeto(), (int) c1, (int) c2 + (int) (diametro * 0.3));
            }
        } else if (Math.cos(rho1) > 0 && Math.sin(rho1) < 0) {
            g2.drawString(t.getAlfabeto(), (int) c1 - (int) (diametro * 0.2), (int) c2);
        } else if (Math.cos(rho1) < 0 && Math.sin(rho1) > 0) {
            g2.drawString(t.getAlfabeto(), (int) c1 + (int) (diametro * 0.2), (int) c2 + (int) (diametro * 0.3));
        } else if (Math.cos(rho1) > 0 && Math.sin(rho1) > 0) {
            g2.drawString(t.getAlfabeto(), (int) c1, (int) c2);
        }

    }

    /**
     * Traza la transicion
     *
     * @param g2 graficos
     * @param t transicion
     */
    public void dibujarTransicion(Graphics2D g2, M_transicion t) {
        int txa = (int) (t.getXa() * factor);
        int tya = (int) (t.getYa() * factor);
        int txb = (int) (t.getXb() * factor);
        int tyb = (int) (t.getYb() * factor);
        g2.setColor(t.getLinea());
        BasicStroke linea;
        if ((diametro * 0.03f) < 1) {
            linea = new BasicStroke(1.5f);
        } else {
            linea = new BasicStroke(diametro * 0.03f);
        }
        g2.setStroke(linea);

        if (t.getTipo().equals("simple")) {
            Point b = new Point(txb, tyb);

            ////FORMULA PARA FLECHA:
            int Px, Py, v1, v2;
            double tam, x; //x = regla de 3

            tam = (Math.sqrt(Math.pow(txb - txa, 2) + Math.pow(tyb - tya, 2)));
            x = ((diametro / 2 * 100) / tam) / 100;
            Px = (txb - txa);
            Py = (tyb - tya);
            v1 = (int) (x * (Px));
            v2 = (int) (x * (Py));

            Point Flecha = new Point(txb - v1, tyb - v2);
            double pmx = (txa + txb) / 2;
            double pmy = (tya + tyb) / 2;
            double distPm = Math.sqrt(Math.pow(txb - pmx, 2) + Math.pow(tyb - pmy, 2));
            puntoControlCurva(b, pmx, pmy, distPm, t, g2);
            /////etiqueta se mudo al punto Control;
            g2.setColor(t.getLinea());
            g2.draw(new QuadCurve2D.Double(txa, tya, t.getCtrlX(), t.getCtrlY(), Flecha.x, Flecha.y));
            puntaDeFlecha(Flecha, t, g2);
            t.setMascara(new QuadCurve2D.Double(txa, tya, t.getCtrlX(), t.getCtrlY(), Flecha.x, Flecha.y));
        }

        if (t.getTipo().equals("arco")) {
            g2.draw(new QuadCurve2D.Double(txa - diametro * 0.36f, tya - diametro * 0.36f, txa, tya - diametro * 1.6f, txa + diametro * 0.36f, tya - diametro * 0.36));
            g2.setFont(new Font("Arial", Font.BOLD, (int) (diametro * 0.3f)));
            g2.setColor(t.getEtiqueta());
            g2.drawString(t.getAlfabeto(), txa - (int) (diametro * 0.1f), tya - (int) (diametro * 1.04f));
            g2.setColor(t.getLinea());
            g2.drawLine(txa + (int) (diametro * 0.36f), tya - (int) (diametro * 0.36f), txa + (int) (diametro * 0.36f), tya - (int) (diametro * 0.64f));
            g2.drawLine(txa + (int) (diametro * 0.36f), tya - (int) (diametro * 0.36f), txa + (int) (diametro * 0.16f), tya - (int) (diametro * 0.6f));
            t.setMascara(new QuadCurve2D.Double(txa - diametro * 0.36f, tya - diametro * 0.36f, txa, tya - diametro * 1.6f, txa + diametro * 0.36f, tya - diametro * 0.36f));
        }

        t.setXa(txa);
        t.setYa(tya);
        t.setXb(txb);
        t.setYb(tyb);
    }

    public Rectangle area() {
        ArrayList coordX = new ArrayList();
        ArrayList coordY = new ArrayList();
        for (M_estado edo : estados) {
            coordX.add(edo.getX());
            coordY.add(edo.getY());
        }
        //ordenar de modo ascendente
        new Ordenador().quicksort(coordX);
        new Ordenador().quicksort(coordY);

        p = new Point((int) coordX.get(0) - 60, (int) coordY.get(0) - 80);
        if (p.y < 0) {
            p.y = 0;
        } else if (p.x < 0) {
            p.x = 0;
        }

        d = new Dimension((int) coordX.get(coordX.size() - 1),
                (int) coordY.get(coordY.size() - 1));

        Dimension d2 = new Dimension(((int) coordX.get(coordX.size() - 1) - (int) coordX.get(0)) + 100,
                ((int) coordY.get(coordY.size() - 1) - (int) coordY.get(0)) + 140);

        area = new Rectangle(p, d2);
        setSize(d.width + 100, d.height + 180);

        return area;
    }

}
