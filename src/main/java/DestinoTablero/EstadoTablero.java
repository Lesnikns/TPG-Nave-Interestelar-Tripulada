package DestinoTablero;

/**
 * Foto del tablero en un instante: dimensiones, posicion de la nave, objetivo y tipo de elemento de cada casilla.
 * Es inmutable: copia los datos al crearse y no guarda referencia al tablero, asi no cambia aunque el tablero siga cambiando
 * y la vista puede leerla desde otro hilo sin riesgo. Debe crearse en el hilo que modifica el tablero (el de la mision).
 * Esta en el paquete DestinoTablero para poder leer el objetivo, que el Tablero solo expone a su paquete.
 * <b>invariantes:</b> casillas != null, de tamano filas x columnas <br>
 * la nave esta dentro del tablero <br>
 * si no hay objetivo, objetivoX y objetivoY valen -1
 */
public class EstadoTablero {
    private final int filas;
    private final int columnas;
    private final int naveX;
    private final int naveY;
    private final int objetivoX;
    private final int objetivoY;
    private final String[][] casillas;   // null = casilla vacia; si no, nombre de la clase del elemento

    /**
     * Saca una foto del tablero
     * <b>pre:</b> tablero != null; se llama desde el hilo que modifica el tablero <br>
     * <b>post:</b> se creo la foto con el estado actual del tablero, sin modificarlo
     * @param tablero - tablero a fotografiar. tablero != null
     */
    public EstadoTablero(Tablero tablero) {
        assert tablero != null : "el estado necesita un tablero";
        this.filas = tablero.getX();
        this.columnas = tablero.getY();
        this.naveX = tablero.getPosicionNave().getX();
        this.naveY = tablero.getPosicionNave().getY();
        Posicion objetivo = tablero.getObjetivo();
        this.objetivoX = (objetivo == null) ? -1 : objetivo.getX();
        this.objetivoY = (objetivo == null) ? -1 : objetivo.getY();
        ElementoDelUniverso[][] elementos = tablero.verTablero();
        this.casillas = new String[filas][columnas];
        for (int i = 0; i < filas; i++)
            for (int j = 0; j < columnas; j++)
                if (elementos[i][j] != null)
                    casillas[i][j] = elementos[i][j].getClass().getSimpleName();
    }

    public int getFilas() { return filas; }
    public int getColumnas() { return columnas; }
    public boolean hayNaveEn(int fila, int columna) { return fila == naveX && columna == naveY; }
    public boolean hayObjetivoEn(int fila, int columna) { return fila == objetivoX && columna == objetivoY; }
    /** @return nombre de la clase del elemento de la casilla, o null si esta vacia */
    public String getElementoEn(int fila, int columna) { return casillas[fila][columna]; }
}