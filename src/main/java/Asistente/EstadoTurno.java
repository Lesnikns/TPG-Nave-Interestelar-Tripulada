package Asistente;

import DestinoTablero.EstadoTablero;

/** Foto de un turno: tablero y recursos tomados en el mismo instante. Inmutable. */
public class EstadoTurno {
    private final EstadoTablero tablero;
    private final EstadoRecursos recursos;

    public EstadoTurno(EstadoTablero tablero, EstadoRecursos recursos) {
        assert tablero != null && recursos != null;
        this.tablero = tablero;
        this.recursos = recursos;
    }

    public EstadoTablero getTablero() { return tablero; }
    public EstadoRecursos getRecursos() { return recursos; }
}