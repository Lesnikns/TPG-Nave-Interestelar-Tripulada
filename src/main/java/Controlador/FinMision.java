package Controlador;

import Naves.SesionDeNave;
import Vista.VistaControl;

// Corre en el hilo de Swing
public class FinMision implements Runnable {
    private final VistaControl vista;
    private final SesionDeNave sesion;

    public FinMision(VistaControl vista, SesionDeNave sesion) {
        this.vista = vista;
        this.sesion = sesion;
    }

    @Override
    public void run() {
        sesion.setMisionEnCurso(false);
        vista.setMisionEnCurso(false);
    }
}