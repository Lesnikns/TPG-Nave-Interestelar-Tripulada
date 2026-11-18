package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Asistente.*;
import Naves.SesionDeNave;
import Vista.VistaControl;

import javax.swing.*;

public class ControladorControl implements ActionListener {
    private final VistaControl vista;
    private final SesionDeNave sesion;

    public ControladorControl(VistaControl vista, SesionDeNave sesion) {
        this.vista = vista;
        this.sesion = sesion;
        vista.setControlador(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!VistaControl.INICIAR_MISION.equals(e.getActionCommand())) return;
        if (!sesion.hayNave()) {
            vista.mostrarError("Primero creá una nave.");
            return;
        }
        if (!sesion.getNave().enEstadoOperativo()) {
            vista.mostrarError("La nave no está operativa (destruida o requiere mantenimiento).");
            return;
        }
        if(!sesion.getNave().verificaTripulacionMinima()){
            vista.mostrarError("La nave no cuenta con la tripulación mínima");
            return;
        }
        Mision m = crearMision(vista.getMisionSeleccionada());
        if (m == null) {
            vista.mostrarError("Esa misión todavía no está disponible.");
            return;
        }
        AsistenteDeComando asistente = sesion.getNave().getAsistente();

        sesion.setMisionEnCurso(true);
        vista.setMisionEnCurso(true);
        new Thread(() -> {
            try { asistente.coordinarMision(m); }
            finally { SwingUtilities.invokeLater(this::terminarMision); } //TODO...
        }).start();
    }

    private void terminarMision() {
        sesion.setMisionEnCurso(false);
        vista.setMisionEnCurso(false);
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