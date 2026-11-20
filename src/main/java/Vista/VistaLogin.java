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

public class VistaLogin extends JPanel {
    public static final String INGRESAR = "INGRESAR";
    public static final String IR_A_REGISTRO = "IR_A_REGISTRO";

    private JTextField campoUsuario = new JTextField(15);
    private JPasswordField campoContrasena = new JPasswordField(15);
    private JButton botonIngresar = new JButton("Ingresar");
    private JButton botonRegistro = new JButton("Crear cuenta");

    public VistaLogin() {
        setLayout(new GridBagLayout());
        JPanel formulario = new JPanel(new GridLayout(3, 2, 8, 8));
        formulario.setBorder(BorderFactory.createTitledBorder("Iniciar sesión"));
        formulario.add(new JLabel("Usuario:"));
        formulario.add(campoUsuario);
        formulario.add(new JLabel("Contraseña:"));
        formulario.add(campoContrasena);
        formulario.add(botonIngresar);
        formulario.add(botonRegistro);
        add(formulario);

        botonIngresar.setActionCommand(INGRESAR);
        campoContrasena.setActionCommand(INGRESAR);
        botonRegistro.setActionCommand(IR_A_REGISTRO);
    }

    public void setControlador(ActionListener controlador) {
        botonIngresar.addActionListener(controlador);
        campoContrasena.addActionListener(controlador);
        botonRegistro.addActionListener(controlador);
    }

    public String getUsuario() {
        return campoUsuario.getText().trim();
    }

    public String getContrasena() {
        return new String(campoContrasena.getPassword());
    }

    /** @return el mensaje de error si el formato no es válido, o null si está bien */
    public String validarFormato() {
        if (getUsuario().isEmpty()) {
            return "Ingresá tu usuario.";
        }
        if (getContrasena().isEmpty()) {
            return "Ingresá tu contraseña.";
        }
        return null;
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void limpiar() {
        campoUsuario.setText("");
        campoContrasena.setText("");
    }
}