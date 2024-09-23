package vista;


import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

// prueba git
public class Arbol extends JPanel {
    private Nodo raiz;

    public Arbol(Nodo raiz) {
        this.raiz = raiz;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(2));
        dibujarNodo(g2d, raiz, getWidth() / 2, 30, getWidth());
    }

    private void dibujarNodo(Graphics2D g, Nodo nodo, int x, int y, int anchoArbol) {
        if (nodo == null)
            return;

        String dato = String.valueOf(nodo.getDato());
        int anchoNodo = obtenerAnchoNodo(nodo);

        int xInicio = x - anchoNodo / 2;
        int xFin = xInicio + anchoNodo;
        int yInicio = y;
        int yFin = yInicio + 50;

        g.setColor(Color.BLACK);
        g.draw(new Line2D.Double(x, yInicio, x, yFin));
        g.drawString(dato, x - anchoNodo / 2, yFin - 25);

        if (nodo.getIzq() != null) {
            int xIzq = x - anchoArbol / 4;
            int yIzq = y + 50;
            g.draw(new Line2D.Double(x, yFin, xIzq, yIzq));
            dibujarNodo(g, nodo.getIzq(), xIzq, yIzq, anchoArbol / 2);
        }

        if (nodo.getDer() != null) {
            int xDer = x + anchoArbol / 4;
            int yDer = y + 50;
            g.draw(new Line2D.Double(x, yFin, xDer, yDer));
            dibujarNodo(g, nodo.getDer(), xDer, yDer, anchoArbol / 2);
        }
    }

    private int obtenerAnchoNodo(Nodo nodo) {
        if (nodo == null)
            return 0;

        String dato = String.valueOf(nodo.getDato());

        FontMetrics fm = getFontMetrics(getFont());
        int anchoTexto = fm.stringWidth(dato);

        return Math.max(100, anchoTexto + 10);
    }
}

