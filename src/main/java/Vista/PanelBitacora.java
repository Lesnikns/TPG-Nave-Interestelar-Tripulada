package Vista;

import java.awt.*;
import java.text.SimpleDateFormat;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import Asistente.Bitacora;
import Asistente.Entrada;
import Eventos.IObservadorModelo;
import Eventos.ObservadoModelo;
import Naves.SesionDeNave;

public class PanelBitacora extends JPanel implements IObservadorModelo<Entrada> {
    private final JTextArea area = new JTextArea();
    private final SimpleDateFormat hora = new SimpleDateFormat("HH:mm:ss");
    private Bitacora actual;                       // solo se toca desde el hilo de Swing

    public PanelBitacora(SesionDeNave sesion) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Bitácora"));
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        add(new JScrollPane(area), BorderLayout.CENTER);

        sesion.agregarObservador(new IObservadorModelo<String>() {
            @Override
            public void actualizar(ObservadoModelo<String> origen, String evento) {
                seguirBitacoraDe(sesion);          // SesionDeNave se modifica desde Swing
            }
        });
    }

    private void seguirBitacoraDe(SesionDeNave sesion) {
        if (!sesion.hayNave()) return;
        Bitacora nueva = sesion.getNave().getAsistente().getBitacora();
        if (nueva == actual) return;               // misma nave (por ejemplo, se agregó un tripulante)
        if (actual != null) actual.quitarObservador(this);
        actual = nueva;
        area.setText("");
        actual.agregarObservador(this);
    }

    private String formatear(Entrada e) {
        return "[" + hora.format(e.getFecha()) + "] " + e.getTipo() + ": "
                + e.getMensaje().replace("\n", "\n    ");
    }

        @Override
    public void actualizar(ObservadoModelo<Entrada> origen, Entrada entrada) {
        SwingUtilities.invokeLater(() -> {
            if (origen == actual) {
                area.append(formatear(entrada) + "\n");// descarta avisos de una nave anterior
                area.setCaretPosition(area.getDocument().getLength());// se ve la última entrada
            }
        });
    }
}
