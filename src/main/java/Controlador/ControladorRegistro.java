package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Excepciones.PersistenciaException;
import Excepciones.UsuarioExistenteException;
import Persistencia.Negocio;
import Vista.VentanaPrincipal;
import Vista.VistaRegistro;

public class ControladorRegistro implements ActionListener {
    private VistaRegistro vista;
    private VentanaPrincipal ventana;
    private Negocio servicio;

    public ControladorRegistro(VistaRegistro vista, VentanaPrincipal ventana, Negocio servicio) {
        this.vista = vista;
        this.ventana = ventana;
        this.servicio = servicio;
        vista.setControlador(this);
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        if (evento.getActionCommand().equals(VistaRegistro.REGISTRAR))
            registrar();
        else 
        	if (evento.getActionCommand().equals(VistaRegistro.VOLVER))
        		volverAlLogin();
    }

    private void registrar() {
        String error = vista.validarFormato();
        if (error != null)
            vista.mostrarError(error);
        else {
	        try {
	            servicio.registrar(vista.getUsuario(), vista.getContrasena());
	            vista.mostrarInfo("Cuenta creada. Ya podés iniciar sesión.");
	            volverAlLogin();
	        } catch (UsuarioExistenteException e) {
	            vista.mostrarError(e.getMessage());
	        } catch (PersistenciaException e) {
	            vista.mostrarError(e.getMessage());
	        }
        }
    }

    private void volverAlLogin() {
        vista.limpiar();
        ventana.mostrarLogin();
    }
}