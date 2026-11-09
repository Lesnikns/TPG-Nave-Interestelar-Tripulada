import DestinoTablero.*;
import Naves.*;
import java.util.*;

public class PruebaRadares {

    public static void main(String[] args) throws Exception {
        // --- Prueba 1: tablero vacío (resultado predecible) ---
        Tablero vacio = new Tablero(5, 5, new ArrayList<>());
        mostrar(vacio);
        probarRuta(vacio, new Posicion(0, 4));

        // --- Prueba 2: tablero con asteroides (la ubicación es al azar) ---
        ArrayList<ElementoDelUniverso> elementos = new ArrayList<>();
        for (int i = 0; i < 8; i++) elementos.add(new Asteroide());
        Tablero lleno = new Tablero(5, 5, elementos);
        mostrar(lleno);
        probarRuta(lleno, new Posicion(0, 4));
        probarCortoAlcance(lleno);
    }

    static void probarRuta(Tablero t, Posicion destino) {
        Posicion origen = t.getPosicionNave();
        List<Posicion> ruta = new RadarLargoAlcance().calcularRuta(t, origen, destino);
        System.out.print("Ruta de (" + origen.getX() + "," + origen.getY() + ") a ("
                + destino.getX() + "," + destino.getY() + "): ");
        for (Posicion p : ruta) System.out.print("(" + p.getX() + "," + p.getY() + ") ");
        System.out.println("\n");
    }

    static void probarCortoAlcance(Tablero t) {
        Posicion n = t.getPosicionNave();
        RadarCortoAlcance radar = new RadarCortoAlcance();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                Posicion vecino = new Posicion(n.getX() + dx, n.getY() + dy);
                System.out.println("Vecino (" + vecino.getX() + "," + vecino.getY()
                        + ") ¿obstáculo? " + radar.hayObstaculoAdelante(t, vecino));
            }
        }
        System.out.println();
    }

    /** Dibuja el tablero: N = nave, A = asteroide, o = otro elemento, . = vacío */
    static void mostrar(Tablero t) {
        ElementoDelUniverso[][] m = t.verTablero();
        Posicion nave = t.getPosicionNave();
        for (int f = 0; f < m.length; f++) {
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < m[0].length; c++) {
                if (f == nave.getX() && c == nave.getY()) sb.append("N ");
                else if (m[f][c] instanceof Asteroide) sb.append("A ");
                else if (m[f][c] != null) sb.append("o ");
                else sb.append(". ");
            }
            System.out.println(sb);
        }
        System.out.println();
    }
}