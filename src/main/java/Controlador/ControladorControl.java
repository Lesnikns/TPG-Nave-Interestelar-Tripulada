package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Asistente.*;
import Naves.SesionDeNave;
import Vista.VistaControl;

public class ControladorControl implements ActionListener {
    private final VistaControl vista;
    private final SesionDeNave sesion;

    public ControladorControl(VistaControl vista, SesionDeNave sesion, PublicadorDeTurnos publicador) {
        this.vista = vista;
        this.sesion = sesion;
        vista.setControlador(this);
        publicador.agregarObservador(new ObservadorTurnos(vista));
    }

    // ---- Botón "Iniciar misión" ----
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!VistaControl.INICIAR_MISION.equals(e.getActionCommand())) return;
        if (!sesion.hayNave()) {
            vista.mostrarError("Primero creá una nave.");
            return;
        }
        AsistenteDeComando asistente = sesion.getNave().getAsistente();
        Mision m = crearMision(vista.getMisionSeleccionada());
        if (m == null) {
            vista.mostrarError("Esa misión todavía no está disponible.");
            return;
        }

        sesion.setMisionEnCurso(true);
        vista.setMisionEnCurso(true);
        new Thread(new EjecutorMision(asistente, m, vista, sesion)).start();
    }

    private Mision crearMision(int numero) {
        return switch (numero) {
            case 1 -> null; // M-01: pendiente (nave aliada)
            case 2 -> new MisionRecoleccion(2, "Recolección", null);
            case 3 -> null; // M-03: pendiente (base como destino)
            default -> null;
        };
    }
}