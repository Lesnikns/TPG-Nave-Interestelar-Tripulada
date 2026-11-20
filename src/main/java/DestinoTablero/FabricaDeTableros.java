package DestinoTablero;

import java.util.ArrayList;
import java.util.Random;

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
    //Las dimensiones se expresan en filas (x) y columnas (y), entre 5 y 100 inclusive
    public FabricaDeTableros() {
        Random random = new Random();
        this.filas = random.nextInt(96) + 5;      // 5-100
        this.columnas = random.nextInt(96) + 5;   // 5-100
    }

    public static ArrayList<ElementoDelUniverso> crearElementos(int filas, int columnas) {
        Random rand = new Random();

        int maxElementos = filas * columnas / 4 * 3;
        int cantidad = rand.nextInt(maxElementos + 1); // entre 0 y maxElementos

        ArrayList<ElementoDelUniverso> elementos = new ArrayList<>();

        for (int i = 0; i < cantidad; i++) {
            switch (rand.nextInt(10)) {
                //no lo dice, pero supongo que deberían ser más probables los asteroides
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                    elementos.add(new Asteroide());
                    break;
                case 5:
                case 6:
                    elementos.add(new EstacionDeCarga());
                    break;
                case 7:
                    elementos.add(new NaveEnemigaCarguero());
                    break;
                case 8:
                    elementos.add(new NaveEnemigaDeCombate());
                    break;
                case 9:
                    elementos.add(new NaveEnemigaExploradora());
                    break;
            }
        }
        return elementos;
    }

    /**
     * Crea un tablero nuevo con sus elementos distribuidos al azar
     * @throws DimensionesDeTableroInconsistentesException si las dimensiones estan fuera de rango
     * @throws CantidadDeElementosSuperaCasillaException si hay mas elementos de los permitidos
     */
    public Tablero crear() throws DimensionesDeTableroInconsistentesException, CantidadDeElementosSuperaCasillaException {
        // Al construir el tablero, sus elementos se distribuyen en casillas distintas. La cantidad máxima admitida es x * y / 4 * 3
        ArrayList<ElementoDelUniverso> elementos = crearElementos(filas,columnas);
        return new Tablero(filas, columnas, elementos);
    }
}