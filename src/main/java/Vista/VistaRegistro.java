package Vista;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

public class VistaRegistro extends JPanel {
    public static final String REGISTRAR = "REGISTRAR";
    public static final String VOLVER = "VOLVER";

    private JTextField campoUsuario = new JTextField(15);
    private JPasswordField campoContrasena = new JPasswordField(15);
    private JPasswordField campoRepetir = new JPasswordField(15);
    private JButton botonRegistrar = new JButton("Registrarse");
    private JButton botonVolver = new JButton("Volver");

    public VistaRegistro() {
        setLayout(new GridBagLayout());
        JPanel formulario = new JPanel(new GridLayout(4, 2, 8, 8));
        formulario.setBorder(BorderFactory.createTitledBorder("Crear cuenta"));
        formulario.add(new JLabel("Usuario:"));
        formulario.add(campoUsuario);
        formulario.add(new JLabel("Contraseña:"));
        formulario.add(campoContrasena);
        formulario.add(new JLabel("Repetir contraseña:"));
        formulario.add(campoRepetir);
        formulario.add(botonRegistrar);
        formulario.add(botonVolver);
        add(formulario);

        botonRegistrar.setActionCommand(REGISTRAR);
        botonVolver.setActionCommand(VOLVER);
    }

    public void setControlador(ActionListener controlador) {
        botonRegistrar.addActionListener(controlador);
        botonVolver.addActionListener(controlador);
    }

    public String getUsuario() {
        return campoUsuario.getText().trim();
    }

    public String getContrasena() {
        return new String(campoContrasena.getPassword());
    }

    public String validarFormato() {
        // La tabla de usuarios admite hasta 50 caracteres por campo
        if (getUsuario().length() < 1 || getUsuario().length() > 50) {
            return "El usuario debe tener entre 1 y 50 caracteres.";
        }
        if (getContrasena().length() < 1 || getContrasena().length() > 50) {
            return "La contraseña debe tener entre 1 y 50 caracteres.";
        }
        if (!getContrasena().equals(new String(campoRepetir.getPassword()))) {
            return "Las contraseñas no coinciden.";
        }
        return null;
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Registro", JOptionPane.INFORMATION_MESSAGE);
    }

    public void limpiar() {
        campoUsuario.setText("");
        campoContrasena.setText("");
        campoRepetir.setText("");
    }
}