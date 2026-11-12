package Controlador;

import Asistente.EstadoTurno;
import Vista.VistaControl;

// Corre en el hilo de Swing
public class MostrarTurno implements Runnable {
    private final VistaControl vista;
    private final EstadoTurno turno;

    public MostrarTurno(VistaControl vista, EstadoTurno turno) {
        this.vista = vista;
        this.turno = turno;
    }

    @Override
    public void run() {
        vista.mostrarTablero(turno.getTablero());
        vista.mostrarRecursos(turno.getRecursos());
    }
}