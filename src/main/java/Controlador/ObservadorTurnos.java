package Controlador;

import javax.swing.SwingUtilities;

import Asistente.EstadoTurno;
import Eventos.IObservadorModelo;
import Eventos.ObservadoModelo;
import Vista.VistaControl;

// Corre en el hilo de la misión: solo encola la actualización en el hilo de Swing
public class ObservadorTurnos implements IObservadorModelo<EstadoTurno> {
    private final VistaControl vista;

    public ObservadorTurnos(VistaControl vista) {
        this.vista = vista;
    }

    @Override
    public void actualizar(ObservadoModelo<EstadoTurno> origen, EstadoTurno evento) {
        SwingUtilities.invokeLater(new MostrarTurno(vista, evento));
    }
}