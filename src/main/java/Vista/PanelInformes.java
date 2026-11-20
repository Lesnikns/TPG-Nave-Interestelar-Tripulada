package Vista;

import Asistente.HistorialDeInformes;
import Asistente.Informe;
import Eventos.IObservadorModelo;
import Eventos.ObservadoModelo;

import javax.swing.*;
import java.awt.*;

public class PanelInformes extends JPanel implements IObservadorModelo<Informe> {
    private final JTextArea area = new JTextArea();

    public PanelInformes(HistorialDeInformes historial) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Historial de informes"));
        area.setEditable(false);
        add(new JScrollPane(area), BorderLayout.CENTER);
        historial.agregarObservador(this);
    }

    @Override
    public void actualizar(ObservadoModelo<Informe> origen, Informe evento) {
        SwingUtilities.invokeLater(() -> area.append(evento + "\n\n"));
    }
}
