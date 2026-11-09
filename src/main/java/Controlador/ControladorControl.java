package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;

import Asistente.*;
import DestinoTablero.EstadoTablero;
import Eventos.IObservadorModelo;
import Eventos.ObservadoModelo;
import Naves.Nave;
import Naves.SesionDeNave;
import Vista.VistaControl;

public class ControladorControl implements ActionListener, IObservadorModelo<String> {
    private final VistaControl vista;
    private final SesionDeNave sesion;
    private final ObservadorTurnos observadorTurnos = new ObservadorTurnos();
    private AsistenteDeComando asistente;   // el de la nave actual

    public ControladorControl(VistaControl vista, SesionDeNave sesion) {
        this.vista = vista;
        this.sesion = sesion;
        vista.setControlador(this);
        sesion.agregarObservador(this);
    }

    // ---- Aviso de SesionDeNave (nave nueva o tripulante agregado) ----
    @Override
    public void actualizar(ObservadoModelo<String> origen, String evento) {
        SwingUtilities.invokeLater(new CambiarNave());
    }

    // ---- Botón "Iniciar misión" ----
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!VistaControl.INICIAR_MISION.equals(e.getActionCommand())) return;
        if (asistente == null) {
            vista.mostrarError("Primero creá una nave.");
            return;
        }
        Mision m = crearMision(vista.getMisionSeleccionada());
        if (m == null) {
            vista.mostrarError("Esa misión todavía no está disponible.");
            return;
        }
        vista.setMisionEnCurso(true);
        new Thread(new EjecutorMision(asistente, m)).start();
    }

    private Mision crearMision(int numero) {
        return switch (numero) {
            case 1 -> null; // M-01: pendiente (nave aliada)
            case 2 -> new MisionRecoleccion(2, "Recolección", null);
            case 3 -> null; // M-03: pendiente (base como destino)
            default -> null;
        };
    }

    // ---- Observador de los turnos: corre en el hilo de la misión ----
    private class ObservadorTurnos implements IObservadorModelo<EstadoTurno> {
        @Override
        public void actualizar(ObservadoModelo<EstadoTurno> origen, EstadoTurno evento) {
            SwingUtilities.invokeLater(new MostrarTurno(evento));  // solo encola
        }
    }

    // ---- Tareas que corren en el hilo de Swing ----
    private class MostrarTurno implements Runnable {
        private final EstadoTurno turno;
        MostrarTurno(EstadoTurno turno) { this.turno = turno; }
        @Override
        public void run() {
            vista.mostrarTablero(turno.getTablero());
            vista.mostrarRecursos(turno.getRecursos());
        }
    }

    private class CambiarNave implements Runnable {
        @Override
        public void run() {
            if (!sesion.hayNave()) return;
            Nave nave = sesion.getNave();
            AsistenteDeComando nuevo = nave.getAsistente();
            if (nuevo == null || nuevo == asistente) return;   // sin cambio de nave

            if (asistente != null) asistente.quitarObservador(observadorTurnos);
            asistente = nuevo;
            asistente.agregarObservador(observadorTurnos);

            // Primer refresco, antes de cualquier misión
            vista.mostrarTablero(new EstadoTablero(asistente.getTablero()));
            vista.mostrarRecursos(new EstadoRecursos(
                    nave.getCombustible(), nave.getEnergia(), nave.getDesgaste()));
        }
    }

    private class FinMision implements Runnable {
        @Override
        public void run() { vista.setMisionEnCurso(false); }
    }

    // ---- Hilo de la misión ----
    private class EjecutorMision implements Runnable {
        private final AsistenteDeComando asistenteMision;
        private final Mision mision;
        EjecutorMision(AsistenteDeComando a, Mision m) {
            this.asistenteMision = a;
            this.mision = m;
        }
        @Override
        public void run() {
            asistenteMision.coordinarMision(mision);
            SwingUtilities.invokeLater(new FinMision());
        }
    }
}