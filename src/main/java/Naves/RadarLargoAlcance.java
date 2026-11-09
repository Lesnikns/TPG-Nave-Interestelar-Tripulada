package Naves;

// debe tener un método para poder determinar una ruta entre a y b

import DestinoTablero.Asteroide;
import DestinoTablero.ElementoDelUniverso;
import DestinoTablero.Posicion;
import DestinoTablero.Tablero;

import java.util.*;

public class RadarLargoAlcance {

    // La Ruta es sin incluir el origen e incluyendo el destino.
     /*  Esquiva asteroides si puede; si no hay camino libre, usa la ruta directa. */
    public List<Posicion> calcularRuta(Tablero tablero,Posicion origen, Posicion destino) {
        List<Posicion> ruta = rutaEsquivando(tablero,origen, destino);
        return ruta.isEmpty() ? rutaDirecta(origen, destino) : ruta;
    }

    // tuve que implementarlo con IA porque mi versión no funcionó;
    // es independiente del resto por lo que todavía puedo intentar corregir lo mio,
    // sino tendré que aprenderla de memoria.
    //TODO: recordar modificar; No enviar sin confirmarlo.
    /// TODO: quedan pendiente las consultas.
    private List<Posicion> rutaEsquivando(Tablero tablero,Posicion origen, Posicion destino) {
        ElementoDelUniverso[][] mapa = tablero.verTablero();
        int filas = mapa.length;
        int cols = mapa[0].length;

        boolean[][] visitado = new boolean[filas][cols];
        Posicion[][] padre = new Posicion[filas][cols];
        Deque<Posicion> cola = new ArrayDeque<>();

        visitado[origen.getX()][origen.getY()] = true;
        cola.add(origen);

        while (!cola.isEmpty()) {
            Posicion actual = cola.poll();
            if (actual.getX() == destino.getX() && actual.getY() == destino.getY()) {
                return reconstruir(padre, origen, destino);
            }
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue;
                    int nx = actual.getX() + dx;
                    int ny = actual.getY() + dy;
                    if (nx < 0 || ny < 0 || nx >= filas || ny >= cols) continue;
                    if (visitado[nx][ny]) continue;

                    boolean esDestino = nx == destino.getX() && ny == destino.getY();
                    if (!esDestino && mapa[nx][ny] instanceof Asteroide) continue;

                    visitado[nx][ny] = true;
                    padre[nx][ny] = actual;
                    cola.add(new Posicion(nx, ny));
                }
            }
        }
        return new ArrayList<>(); // sin camino libre
    }

    private List<Posicion> reconstruir(Posicion[][] padre, Posicion origen, Posicion destino) {
        LinkedList<Posicion> ruta = new LinkedList<>();
        Posicion p = destino;
        while (!(p.getX() == origen.getX() && p.getY() == origen.getY())) {
            ruta.addFirst(p);
            p = padre[p.getX()][p.getY()];
        }
        return ruta;
    }

    //esta fue la primera versión, no tiene mucho de RADAR.. se usa si el camino ya empieza bloqueado
    private List<Posicion> rutaDirecta(Posicion origen, Posicion destino) {
        List<Posicion> ruta = new ArrayList<>();
        int x = origen.getX();
        int y = origen.getY();
        while (x != destino.getX() || y != destino.getY()) {
            x += Integer.compare(destino.getX(), x);
            y += Integer.compare(destino.getY(), y);
            ruta.add(new Posicion(x, y));
        }
        return ruta;
    }
}



