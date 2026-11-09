package DestinoTablero;

import java.util.ArrayList;

/**
 * Arma el tablero con el que opera una nave: tamano y elementos del universo.
 * Concentra la configuracion del escenario para que quien crea la nave no dependa de como se construye el tablero.
 */
public class FabricaDeTableros {
    private final int filas;
    private final int columnas;

    public FabricaDeTableros(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
    }

    /**
     * Crea un tablero nuevo con sus elementos distribuidos al azar
     * @throws DimensionesDeTableroInconsistentesException si las dimensiones estan fuera de rango
     * @throws CantidadDeElementosSuperaCasillaException si hay mas elementos de los permitidos
     */
    public Tablero crear() throws DimensionesDeTableroInconsistentesException, CantidadDeElementosSuperaCasillaException {
        ArrayList<ElementoDelUniverso> elementos = new ArrayList<>();
        // hay que agregar asteroides, estaciones de carga y naves enemigas..
        // creí que eso venía hecho por ellos, dice cómo debe hacerse?
        elementos.add(new NaveEnemigaCarguero());
        elementos.add(new NaveEnemigaDeCombate());
        elementos.add(new NaveEnemigaExploradora());

        for (int i = 0; i < 5; i++) elementos.add(new Asteroide());
        for (int i = 0; i < 2; i++) elementos.add(new EstacionDeCarga());
        return new Tablero(filas, columnas, elementos);
    }
}