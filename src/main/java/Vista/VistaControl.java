package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.*;

import Asistente.EstadoRecursos;
import Asistente.EstadoTurno;
import DestinoTablero.EstadoTablero;
import Eventos.IObservadorModelo;
import Eventos.ObservadoModelo;

public class VistaControl extends JPanel implements IObservadorModelo<EstadoTurno> {
    @Override
    public void actualizar(ObservadoModelo<EstadoTurno> origen, EstadoTurno turno) {
        SwingUtilities.invokeLater(() -> {      // corre en el hilo de la misión: solo encola
            mostrarTablero(turno.getTablero());
            mostrarRecursos(turno.getRecursos());
        });
    }
    public static final String INICIAR_MISION = "INICIAR_MISION";

    private JProgressBar barraCombustible = new JProgressBar(0, 100);
    private JProgressBar barraEnergia = new JProgressBar(0, 100);
    private JProgressBar barraDesgaste = new JProgressBar(0, 100);
    private JLabel etiquetaGemas = new JLabel("");
    private JLabel etiquetaDestruida = new JLabel("");

    private JComboBox<String> comboMision = new JComboBox<>(new String[] {
            "M-01 Intercepción y asistencia", "M-02 Recolección", "M-03 Retorno a punto seguro"});
    private JButton botonIniciar = new JButton("Iniciar misión");
    private JLabel etiquetaEstado = new JLabel("Sin misión en curso");

    private PanelTablero panelTablero = new PanelTablero();
    private JPanel zonaBitacora = new JPanel(new BorderLayout()); // la completa la Persona 4

    public VistaControl() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelRecursos = new JPanel(new GridLayout(0, 2, 8, 8));
        panelRecursos.setBorder(BorderFactory.createTitledBorder("Recursos"));
        for (JProgressBar b : new JProgressBar[] {barraCombustible, barraEnergia, barraDesgaste}) {
            b.setStringPainted(true);
        }
        panelRecursos.add(new JLabel("Combustible:"));
        panelRecursos.add(barraCombustible);
        panelRecursos.add(new JLabel("Energía:"));
        panelRecursos.add(barraEnergia);
        panelRecursos.add(new JLabel("Desgaste:"));
        panelRecursos.add(barraDesgaste);
        panelRecursos.add(new JLabel("Gemas"));
        panelRecursos.add(etiquetaGemas);
        panelRecursos.add(new JLabel("Destruida:"));
        panelRecursos.add(etiquetaDestruida);

        JPanel panelMision = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelMision.setBorder(BorderFactory.createTitledBorder("Misión"));
        panelMision.add(comboMision);
        panelMision.add(botonIniciar);
        panelMision.add(etiquetaEstado);

        JPanel izquierda = new JPanel(new BorderLayout(0, 10));
        izquierda.add(panelRecursos, BorderLayout.NORTH);
        izquierda.add(panelMision, BorderLayout.CENTER);

        JPanel arriba = new JPanel(new BorderLayout(10, 10));
        arriba.add(izquierda, BorderLayout.WEST);
        arriba.add(zonaBitacora, BorderLayout.CENTER);

        panelTablero.setBorder(BorderFactory.createTitledBorder("Tablero"));

        add(arriba, BorderLayout.NORTH);
        add(panelTablero, BorderLayout.CENTER);

        botonIniciar.setActionCommand(INICIAR_MISION);
    }

    public void setControlador(ActionListener controlador) {
        botonIniciar.addActionListener(controlador);
    }

    /** @return 1, 2 o 3 según la misión elegida (M-01, M-02, M-03) */
    public int getMisionSeleccionada() {
        return comboMision.getSelectedIndex() + 1;
    }

    /** Lo usa la Persona 4 para ubicar bitácora e informes en esta sección */
    public void setPanelBitacora(JPanel panel) {
        zonaBitacora.removeAll();
        zonaBitacora.add(panel, BorderLayout.CENTER);
        zonaBitacora.revalidate();
    }

    // Estos métodos se llaman SOLO desde el hilo de Swing (SwingUtilities.invokeLater)
    public void mostrarRecursos(EstadoRecursos r) {
        barraCombustible.setValue(r.getCombustible());
        barraEnergia.setValue(r.getEnergia());
        barraDesgaste.setValue(r.getDesgaste());
        etiquetaGemas.setText(String.valueOf(r.getGemas()));
        if (r.getDestruida()) {
            etiquetaDestruida.setText("Destruida");
            etiquetaDestruida.setForeground(Color.RED);
        } else {
            etiquetaDestruida.setText("No destruida");
            etiquetaDestruida.setForeground(Color.GREEN);
        }
    }

    public void mostrarTablero(EstadoTablero t) {
        panelTablero.setEstado(t);
    }

    public void setMisionEnCurso(boolean enCurso) {
        botonIniciar.setEnabled(!enCurso);
        comboMision.setEnabled(!enCurso);
        etiquetaEstado.setText(enCurso ? "Misión en curso..." : "Sin misión en curso");
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}