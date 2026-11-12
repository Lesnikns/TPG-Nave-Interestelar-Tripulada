package Controlador;

import javax.swing.SwingUtilities;

import Asistente.AsistenteDeComando;
import Asistente.Mision;
import Naves.SesionDeNave;
import Vista.VistaControl;

// Corre en el hilo de la misión
public class EjecutorMision implements Runnable {
    private final AsistenteDeComando asistente;
    private final Mision mision;
    private final VistaControl vista;
    private final SesionDeNave sesion;

    public EjecutorMision(AsistenteDeComando asistente, Mision mision, VistaControl vista, SesionDeNave sesion) {
        this.asistente = asistente;
        this.mision = mision;
        this.vista = vista;
        this.sesion = sesion;
    }

    @Override
    public void run() {
        try {
            asistente.coordinarMision(mision);
        } finally {
            SwingUtilities.invokeLater(new FinMision(vista, sesion));
        }
    }
}