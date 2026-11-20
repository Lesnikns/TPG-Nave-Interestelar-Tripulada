package Vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public class VistaCreacion extends JPanel {
    public static final String CREAR_NAVE = "CREAR_NAVE";
    public static final String AGREGAR_TRIPULANTE = "AGREGAR_TRIPULANTE";
    public static final String CAMBIO_CARGO = "CAMBIO_CARGO";

    private JComboBox<String> comboTipoNave = new JComboBox<>(new String[] {"Exploradora", "Carguero", "Combate"});
    private JButton botonCrearNave = new JButton("Crear nave");
    private JLabel etiquetaNave = new JLabel("ninguna");
    private JLabel etiquetaTripulacion = new JLabel("-");
    private JLabel etiquetaMinima = new JLabel("-");

    private JTextField campoNombre = new JTextField(15);
    private JComboBox<String> comboOrigen = new JComboBox<>(new String[] {"Terrícola", "Vulcano", "Marciano"});
    private JComboBox<String> comboCargo = new JComboBox<>(new String[] {"Capitán", "Consejero", "Teniente", "Alférez"});
    private JSpinner campoAntiguedad = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
    private JSpinner campoConsejos = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));
    private JButton botonAgregar = new JButton("Agregar tripulante");

    public VistaCreacion() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel panelNave = new JPanel(new GridLayout(0, 2, 8, 8));
        panelNave.setBorder(BorderFactory.createTitledBorder("Nave"));
        panelNave.add(new JLabel("Tipo de nave:"));
        panelNave.add(comboTipoNave);
        panelNave.add(new JLabel(""));
        panelNave.add(botonCrearNave);
        panelNave.add(new JLabel("Nave activa:"));
        panelNave.add(etiquetaNave);
        panelNave.add(new JLabel("Tripulación:"));
        panelNave.add(etiquetaTripulacion);
        add(panelNave);

        JPanel panelTripulante = new JPanel(new GridLayout(0, 2, 8, 8));
        panelTripulante.setBorder(BorderFactory.createTitledBorder("Nuevo tripulante"));
        panelTripulante.add(new JLabel("Nombre:"));
        panelTripulante.add(campoNombre);
        panelTripulante.add(new JLabel("Origen:"));
        panelTripulante.add(comboOrigen);
        panelTripulante.add(new JLabel("Cargo:"));
        panelTripulante.add(comboCargo);
        panelTripulante.add(new JLabel("Antigüedad (años):"));
        panelTripulante.add(campoAntiguedad);
        panelTripulante.add(new JLabel("Consejos dados:"));
        panelTripulante.add(campoConsejos);
        panelTripulante.add(new JLabel(""));
        panelTripulante.add(botonAgregar);
        add(panelTripulante);
        
        JPanel abajo = new JPanel(new BorderLayout());
        abajo.add(panelTripulante, BorderLayout.NORTH);
 
        JPanel contenido = new JPanel(new BorderLayout(0, 15));
        contenido.add(panelNave, BorderLayout.NORTH);
        contenido.add(abajo, BorderLayout.CENTER);
        add(contenido, BorderLayout.CENTER);
 
        botonCrearNave.setActionCommand(CREAR_NAVE);
        botonAgregar.setActionCommand(AGREGAR_TRIPULANTE);
        comboCargo.setActionCommand(CAMBIO_CARGO);
        setTripulanteHabilitado(false);
        
    }

    public void actualizarCampoConsejos() {
        campoConsejos.setEnabled(comboCargo.isEnabled() && comboCargo.getSelectedItem().equals("Consejero"));
    }

    public void setControlador(ActionListener controlador) {
        botonCrearNave.addActionListener(controlador);
        botonAgregar.addActionListener(controlador);
        comboCargo.addActionListener(controlador);
    }

    public String getTipoNave() {
        return (String) comboTipoNave.getSelectedItem();
    }

    public String getNombre() {
        return campoNombre.getText().trim();
    }

    public String getOrigen() {
        return (String) comboOrigen.getSelectedItem();
    }

    public String getCargo() {
        return (String) comboCargo.getSelectedItem();
    }

    public int getAntiguedad() {
        return (Integer) campoAntiguedad.getValue();
    }

    public int getCantConsejos() {
        return (Integer) campoConsejos.getValue();
    }

    public String validarFormatoTripulante() {
        if (getNombre().isEmpty()) {
            return "Ingresa el nombre del tripulante.";
        }
        if (getNombre().length() > 40) {
            return "El nombre no puede superar los 40 caracteres.";
        }
        return null;
    }

    public void mostrarNave(String descripcion, String tripulacion) {
        etiquetaNave.setText(descripcion);
        etiquetaTripulacion.setText(tripulacion);
    }

    public void setTripulanteHabilitado(boolean habilitado) {
        campoNombre.setEnabled(habilitado);
        comboOrigen.setEnabled(habilitado);
        comboCargo.setEnabled(habilitado);
        campoAntiguedad.setEnabled(habilitado);
        botonAgregar.setEnabled(habilitado);
        actualizarCampoConsejos();
    }

    public void limpiarFormularioTripulante() {
        campoNombre.setText("");
        campoAntiguedad.setValue(0);
        campoConsejos.setValue(0);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public boolean confirmar(String mensaje) {
        return JOptionPane.showConfirmDialog(this, mensaje, "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}