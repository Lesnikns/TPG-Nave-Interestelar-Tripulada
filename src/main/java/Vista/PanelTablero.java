package Vista;

import java.awt.*;
import javax.swing.JPanel;

import DestinoTablero.EstadoTablero;

public class PanelTablero extends JPanel {
    private EstadoTablero estado; // inmutable: se reemplaza entera, nunca se modifica

    public void setEstado(EstadoTablero estado) {
        this.estado = estado;



        if (estado != null) {
            int lado = 20;
            setPreferredSize(new Dimension(
                    estado.getColumnas() * lado,
                    estado.getFilas() * lado
            ));
            int filaNave = estado.getFilaNave();
            int columnaNave = estado.getColumnaNave();
            int x = columnaNave * lado;
            int y = filaNave * lado;
            revalidate(); // avisa al JScrollPane
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        EstadoTablero e = estado;                 // copia local
        if (e == null) return;

        int lado = 20;
        for (int f = 0; f < e.getFilas(); f++) {
            for (int c = 0; c < e.getColumnas(); c++) {
                g.setColor(colorDe(e, f, c));
                g.fillRect(c * lado, f * lado, lado - 1, lado - 1);
            }
        }

    }

    private Color colorDe(EstadoTablero e, int f, int c) {
        if (e.hayNaveEn(f, c)) return Color.GREEN;
        if (e.hayObjetivoEn(f, c)) return Color.YELLOW;
        String el = e.getElementoEn(f, c);
        if (el == null) return Color.DARK_GRAY;
        if (el.startsWith("Asteroide")) return Color.GRAY; //TODO: cambiar el starts..
        if (el.startsWith("NaveEnemiga")) return Color.RED;
        return Color.CYAN;                        // estaciones u otros
    }
}