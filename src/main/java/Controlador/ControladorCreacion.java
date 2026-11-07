package Controlador;

import Excepciones.TipoDeNaveInvalidoException;
import Naves.FabricaDeNaves;
import Naves.Nave;
import Naves.SesionDeNave;
import Tripulacion.Alferez;
import Tripulacion.Capitan;
import Tripulacion.Consejero;
import Tripulacion.Marciano;
import Tripulacion.Teniente;
import Tripulacion.Terricola;
import Tripulacion.Tripulante;
import Tripulacion.Vulcano;
import Vista.VistaCreacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorCreacion implements ActionListener {
    private VistaCreacion vista;
    private SesionDeNave sesion;
    private FabricaDeNaves fabrica = new FabricaDeNaves();

    public ControladorCreacion(VistaCreacion vista, SesionDeNave sesion) {
        this.vista = vista;
        this.sesion = sesion;
        vista.setControlador(this);
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        if (evento.getActionCommand().equals(VistaCreacion.CREAR_NAVE))
            crearNave();
        else {
            if (evento.getActionCommand().equals(VistaCreacion.AGREGAR_TRIPULANTE))
            	agregarTripulante();
            else {
            	if (evento.getActionCommand().equals(VistaCreacion.CAMBIO_CARGO)) 
            		vista.actualizarCampoConsejos();
    		}
        }
    }

    private void crearNave() {
        if (!sesion.hayNave() || vista.confirmar("Ya hay una nave activa. Si creas otra se pierde la actual y su tripulación. ¿Continuar?")) {
	        try {
	            Nave nave = fabrica.getNave(vista.getTipoNave().toLowerCase());
	            sesion.setNave(nave);
	            actualizarVista();
	        } 
	        catch (TipoDeNaveInvalidoException e) {
	            vista.mostrarError(e.getMessage());
	        }
        }
    }

    private void agregarTripulante() {
        if (!sesion.hayNave())
            vista.mostrarError("Todavia no hay una nave creada.");
        else {
	        String error = vista.validarFormatoTripulante();
	        if (error != null)
	            vista.mostrarError(error);
	        else {
		        Tripulante tripulante = armarTripulante();
		        if (sesion.agregarTripulante(tripulante)) {
		            vista.limpiarFormularioTripulante();
		            actualizarVista();
		        } else {
		            vista.mostrarError("No se pudo asignar el tripulante a la nave.");
		        }
	        }
        }
    }

    private Tripulante armarTripulante() {
        Tripulante persona;
        switch (vista.getOrigen()) {
            case "Vulcano":
                persona = new Vulcano(vista.getNombre());
                break;
            case "Marciano":
                persona = new Marciano(vista.getNombre());
                break;
            default:
                persona = new Terricola(vista.getNombre());
        }

        int antiguedad = vista.getAntiguedad();
        switch (vista.getCargo()) {
            case "Capitán":
                return new Capitan(persona, antiguedad);
            case "Consejero":
                return new Consejero(persona, antiguedad, vista.getCantConsejos());
            case "Teniente":
                return new Teniente(persona, antiguedad);
            default:
                return new Alferez(persona, antiguedad);
        }
    }

    private void actualizarVista() {
        Nave nave = sesion.getNave();
        int cantidad = nave.getTripulacion().size();
        String minima = nave.verificaTripulacionMinima() ? "mínima cumplida" : "no cumple minima (1 capitán y 4 mas)";
        vista.mostrarNave(nave.getTipo(), cantidad + " tripulante(s) - " + minima);
        vista.setTripulanteHabilitado(true);
    }
}