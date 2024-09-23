package vista;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JTextArea;

import funciones.adp.Estado;
import funciones.adp.Transicion;
import modelo.M_estado;
import modelo.M_transicion;

public class V_panelConversionGLCAunADO extends JTextArea {

    private ArrayList<M_estado> mEstados;
    private ArrayList<M_transicion> mTrasiciones;
    private ArrayList<Estado> estadosGeneradosDeLaConversion;
    private Map<Integer, Integer[]> mPosicionesDeLosEstadossGeneradosDeLaConversion;
    private double factor = 1;
    private int diametro = 50;
    private int ancho;
    private int alto;
    private int iMultiplicadorDelLargoDeLaPantalla = 2;
    private int iFactorDeSeparacion = 100;
    private int iFactorDeDistanciaX = 90;
    private int iFactorDeDistanciaY = 70;
    private int iFactorDeColumnas = 20;
    private int iFactorDeRenglones = 7;

    public V_panelConversionGLCAunADO(ArrayList<Estado> ess) {
        // this.setBackground(Color.CYAN);
        // this.setLayout(new GridLayout(10, 2));
        this.estadosGeneradosDeLaConversion = ess;
        http: // 54.167.34.104
        this.mEstados = new ArrayList<>();
        this.mTrasiciones = new ArrayList<>();
        this.mPosicionesDeLosEstadossGeneradosDeLaConversion = new HashMap<>();

        // String sSroll = this.generarScroll();
        // this.setText(sSroll);

        this.setEnabled(false);
        // int iPosicionX = 50;
        // iContador = (sSroll.length() * iMultiplicadorDelLargoDeLaPantalla) /
        // this.estadosGeneradosDeLaConversion.size();
        this.setRows(this.estadosGeneradosDeLaConversion.size() * iFactorDeRenglones);
        this.setColumns(this.estadosGeneradosDeLaConversion.size() * iFactorDeColumnas);
        System.out.println(this.getRows());
        System.out.println(this.getColumns());
        for (int i = 0; i < this.estadosGeneradosDeLaConversion.size(); i++) {
            M_estado mEstado1;
            // int iPosicionY = 100;
            // int iPosicionX = this.obtenerPosicionAleatoria(sSroll.length() *
            // iMultiplicadorDelLargoDeLaPantalla);
            int iPosicionX = this
                    .obtenerPosicionAleatoria(this.estadosGeneradosDeLaConversion.size() * iFactorDeDistanciaX);
            int iPosicionY = this
                    .obtenerPosicionAleatoria(this.estadosGeneradosDeLaConversion.size() * iFactorDeDistanciaY);

            if (this.evaluarSiEstaChocandoConOtroNodo(iPosicionX, iPosicionY)) {
                i = i - 1;
                System.out.println("estan chocando........................" + i);
                continue;
            }

            if (i == 0) {
                mEstado1 = new M_estado(iPosicionX, iPosicionY, i, "Edo-Inicial");
            } else if (i == this.estadosGeneradosDeLaConversion.size() - 1) {
                mEstado1 = new M_estado(iPosicionX, iPosicionY, i, "Edo-Aceptacion");
            } else if (i % 2 == 0 && i != 0) {
                mEstado1 = new M_estado(iPosicionX, iPosicionY, i, "Edo-Transicion");
            } else {
                // iPosicionY = 250;
                mEstado1 = new M_estado(iPosicionX, iPosicionY, i, "Edo-Transicion");

            }

            Integer[] iPosiciones = { iPosicionX, iPosicionY };
            this.mPosicionesDeLosEstadossGeneradosDeLaConversion.put(i, iPosiciones);
            // iPosicionX += 120;
            this.mEstados.add(mEstado1);
        }
        // miText.setVisible(false);
        // miText.setOpaque(false);

        // JLabel miText = new JLabel(sSroll);
        // miText.setBackground(Color.MAGENTA);
        // miText.setSize(2100, 100);
        // miText.setBorder(null);
        // System.out.println(this.getSize().width);
        // System.out.println(this.getSize().height);
        // this.add(miText);

    }

    private boolean evaluarSiEstaChocandoConOtroNodo(int iPosicionX, int iPosicionY) {
        for (Integer iEstado : this.mPosicionesDeLosEstadossGeneradosDeLaConversion.keySet()) {
            Integer[] iPosiciones = this.mPosicionesDeLosEstadossGeneradosDeLaConversion.get(iEstado);
            if (puntoEstaDentroCircunferencia(iPosicionX, iPosicionY, iPosiciones[0], iPosiciones[1],
                    iFactorDeSeparacion)) {
                return true;
            }
        }
        return false;
    }

    private boolean puntoEstaDentroCircunferencia(int xPunto, int yPunto, double xCircunferencia,
            double yCircunferencia, double radio) {
        double distancia = Point2D.distance(xCircunferencia, yCircunferencia, xPunto, yPunto);
        return distancia < radio;
    }

    private int obtenerPosicionAleatoria(int max) {
        int rangoInicio = 20;
        int rangoFin = max;

        // Crear una instancia de Random
        Random random = new Random();

        // Obtener un valor aleatorio dentro del rango
        int valorAleatorio = random.nextInt(rangoFin - rangoInicio + 1) + rangoInicio;
        // System.out.println(valorAleatorio);
        return valorAleatorio;
    }

    private String generarScroll() {
        String sResultado = "x                   ";
        for (int i = 0; i < this.estadosGeneradosDeLaConversion.size() * iMultiplicadorDelLargoDeLaPantalla; i++) {
            sResultado += "                   x";
        }
        for (int i = 0; i < this.estadosGeneradosDeLaConversion.size() * iMultiplicadorDelLargoDeLaPantalla; i++) {
            sResultado += "        \n\n\nx";
        }
        return sResultado;
    }

    // private obt

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

        // for (M_estado m_estado : this.mEstados) {
        // this.dibujarEdoTrans(g2, m_estado);
        // }
        this.dibujarEdoInic(g2, this.mEstados.get(0));
        this.dibujarEdoTrans(g2, mEstados.get(0));
        for (int i = 1; i < this.mEstados.size() - 1; i++) {
            this.dibujarEdoTrans(g2, mEstados.get(i));
        }
        this.dibujarEdoTrans(g2, this.mEstados.get(this.mEstados.size() - 1));

        ArrayList<Transicion> t;
        for (Estado est : this.estadosGeneradosDeLaConversion) {
            t = est.getTransiciones();
            for (Transicion f : t) {
                int iEstadoOrigen = est.getNombre();
                int iEstadoDestino = f.estde;
                String sAlfabeto = f.regla();
                // System.out.println(f);
                Integer[] iPosicionesOrigen = this.mPosicionesDeLosEstadossGeneradosDeLaConversion.get(iEstadoOrigen);
                Integer[] iPosicionesDestino = this.mPosicionesDeLosEstadossGeneradosDeLaConversion.get(f.estde);
                M_transicion mTransicion;
//                mTransicion = new M_transicion(iPosicionesOrigen[0], iPosicionesOrigen[1],
//                        iPosicionesDestino[0], iPosicionesDestino[1],
//                        "simple", iEstadoOrigen, iEstadoDestino, sAlfabeto);
                if(iEstadoOrigen==iEstadoDestino){
                    mTransicion = new M_transicion(iPosicionesOrigen[0], iPosicionesOrigen[1],
                            iPosicionesDestino[0], iPosicionesDestino[1],
                            "arco", iEstadoOrigen, iEstadoDestino, sAlfabeto);
                }else{
                    mTransicion = new M_transicion(iPosicionesOrigen[0], iPosicionesOrigen[1],
                            iPosicionesDestino[0], iPosicionesDestino[1],
                            "simple", iEstadoOrigen, iEstadoDestino, sAlfabeto);
                }
                this.dibujarTransicion(g2, mTransicion);
            }
            // System.out.println("------------");
        }

    }

    /**
     * Dibujar estado inicial
     *
     * @param g2 graficos
     * @param e  estado
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
        g2.setColor(e.getRelleno()); // color circulo
        g2.fillOval(x - nDiametro / 2, y - nDiametro / 2, nDiametro, nDiametro);
        g2.setColor(e.getContorno());
        g2.drawOval(x - nDiametro / 2, y - nDiametro / 2, nDiametro, nDiametro);

        // Condicion para hacer estado de aceptación
        if (e.getTipo().equals("Edo-Aceptacion")) {
            g2.setColor(e.getContorno()); // color de marcado
            g2.drawOval(x - (int) (nDiametro * 0.4f), y - (int) (nDiametro * 0.4f), (int) (nDiametro * 0.8f),
                    (int) (nDiametro * 0.8f));

        }

        g2.setFont(new Font("Arial", Font.BOLD, (int) (nDiametro * 0.3f)));
        g2.setColor(e.getTexto()); // color texto
        g2.drawString(e.getEtiqueta(), x - (int) (nDiametro * 0.1f), y + (int) (nDiametro * 0.1f));

        g2.setColor(e.getEdoInicial()); // triangulo
        int picoy = y;
        int picox = x - nDiametro / 2;
        int base = picox - (int) (nDiametro * 0.4f);
        int verticey1 = y - (int) (nDiametro * 0.4f);
        int verticey2 = y + (int) (nDiametro * 0.4f);
        int[] Xcoordenadas = { base, base, picox };
        int[] Ycoordenadas = { verticey1, verticey2, picoy };
        g2.fillPolygon(Xcoordenadas, Ycoordenadas, 3);

        e.setX(x);
        e.setY(y);
        diametro = nDiametro;
    }

    /**
     * Dibujar estado
     *
     * @param g2 graficos
     * @param e  estado
     */
    public void dibujarEdoTrans(Graphics2D g2, M_estado e) {
        int x, y, nDiametro;
        x = (int) (e.getX() * factor);
        y = (int) (e.getY() * factor);
        nDiametro = (int) (diametro * factor);
        // System.out.println("X: " + x + ", Y: " + y);
        BasicStroke linea;
        if ((diametro * 0.03f) < 1) {
            linea = new BasicStroke(1.5f);
        } else {
            linea = new BasicStroke(diametro * 0.03f);
        }
        g2.setStroke(linea);
        g2.setColor(e.getRelleno()); // color circulo
        g2.fillOval(x - nDiametro / 2, y - nDiametro / 2, nDiametro, nDiametro);
        g2.setColor(e.getContorno());
        g2.drawOval(x - nDiametro / 2, y - nDiametro / 2, nDiametro, nDiametro);

        // Condicion para hacer estado de aceptación
        if (e.getTipo().equals("Edo-Aceptacion")) {
            g2.setColor(e.getContorno()); // color de marcado
            g2.drawOval(x - (int) (nDiametro * 0.4f), y - (int) (nDiametro * 0.4f), (int) (nDiametro * 0.8f),
                    (int) (nDiametro * 0.8f));

        }

        g2.setFont(new Font("Arial", Font.BOLD, (int) (nDiametro * 0.3f)));
        g2.setColor(e.getTexto()); // color texto
        g2.drawString(e.getEtiqueta(), x - (int) (nDiametro * 0.1f), y + (int) (nDiametro * 0.1f));

        e.setX(x);
        e.setY(y);
        diametro = nDiametro;
    }

    /**
     * Traza la punta de flecha de la transicion
     *
     * @param punta point
     * @param t     transicion
     * @param g2    graficos
     */
    private void puntaDeFlecha(Point punta, M_transicion t, Graphics2D g2) {
        double pi = Math.toRadians(30); // angulo de abertura
        int barb = (int) (diametro * 0.24f); // largo
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
     * @param b      point
     * @param pmx    punto medio en x
     * @param pmy    punto medio en y
     * @param distPm distancia punto medio
     * @param t      transicion
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
     * @param t  transicion
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

            //// FORMULA PARA FLECHA:
            int Px, Py, v1, v2;
            double tam, x; // x = regla de 3

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
            ///// etiqueta se mudo al punto Control;
            g2.setColor(t.getLinea());
            // g2.setColor(Color.GREEN);
            g2.draw(new QuadCurve2D.Double(txa, tya, t.getCtrlX(), t.getCtrlY(), Flecha.x, Flecha.y));
            puntaDeFlecha(Flecha, t, g2);
            t.setMascara(new QuadCurve2D.Double(txa, tya, t.getCtrlX(), t.getCtrlY(), Flecha.x, Flecha.y));
        }

        if (t.getTipo().equals("arco")) {
            g2.draw(new QuadCurve2D.Double(txa - diametro * 0.36f, tya - diametro * 0.36f, txa, tya - diametro * 1.6f,
                    txa + diametro * 0.36f, tya - diametro * 0.36));
            g2.setFont(new Font("Arial", Font.BOLD, (int) (diametro * 0.3f)));
            g2.setColor(t.getEtiqueta());
            g2.drawString(t.getAlfabeto(), txa - (int) (diametro * 0.1f), tya - (int) (diametro * 1.04f));
            g2.setColor(t.getLinea());
            g2.drawLine(txa + (int) (diametro * 0.36f), tya - (int) (diametro * 0.36f), txa + (int) (diametro * 0.36f),
                    tya - (int) (diametro * 0.64f));
            g2.drawLine(txa + (int) (diametro * 0.36f), tya - (int) (diametro * 0.36f), txa + (int) (diametro * 0.16f),
                    tya - (int) (diametro * 0.6f));
            t.setMascara(new QuadCurve2D.Double(txa - diametro * 0.36f, tya - diametro * 0.36f, txa,
                    tya - diametro * 1.6f, txa + diametro * 0.36f, tya - diametro * 0.36f));
        }

        t.setXa(txa);
        t.setYa(tya);
        t.setXb(txb);
        t.setYb(tyb);
    }

}
