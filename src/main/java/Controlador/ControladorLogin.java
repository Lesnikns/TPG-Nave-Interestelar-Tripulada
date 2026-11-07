package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Excepciones.CredencialesInvalidasException;
import Excepciones.PersistenciaException;
import Persistencia.Negocio;
import Vista.VentanaPrincipal;
import Vista.VistaLogin;

public class ControladorLogin implements ActionListener {
    private VistaLogin vista;
    private VentanaPrincipal ventana;
    private Negocio servicio;

    public ControladorLogin(VistaLogin vista, VentanaPrincipal ventana, Negocio servicio) {
        this.vista = vista;
        this.ventana = ventana;
        this.servicio = servicio;
        vista.setControlador(this);
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        if (evento.getActionCommand().equals(VistaLogin.INGRESAR)) {
            ingresar();
        } 
        else 
        	if (evento.getActionCommand().equals(VistaLogin.IR_A_REGISTRO)) {
            vista.limpiar();
            ventana.mostrarRegistro();
        	}
    }

    private void ingresar() {
        String error = vista.validarFormato();
        if (error != null)
            vista.mostrarError(error);
        else {
	        String usuario = vista.getUsuario();
	        try {
	            servicio.autenticar(usuario, vista.getContrasena());
	            vista.limpiar();
	            ventana.mostrarPrincipal(usuario);
	        } catch (CredencialesInvalidasException e) {
	            vista.mostrarError(e.getMessage());
	        } catch (PersistenciaException e) {
	            vista.mostrarError(e.getMessage());
	        }
        }
    }
}