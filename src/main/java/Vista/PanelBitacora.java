package Vista;

import Asistente.Bitacora;
import Asistente.Entrada;
import Eventos.IObservadorModelo;
import Eventos.ObservadoModelo;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Panel de la bitacora de eventos, para la Seccion de Control de la Nave (vista del patron MVC).
 * Muestra las entradas de la bitacora del asistente de la nave, una por linea con su hora, su tipo y su mensaje, y se actualiza solo:
 * observa la bitacora (patron Observer) y agrega cada entrada nueva apenas se registra, sin tener que consultarla.
 * Las entradas llegan desde el hilo de la mision, no desde el hilo de Swing, por eso el area de texto se actualiza siempre con SwingUtilities.invokeLater.
 * Al crear una nave nueva se llama otra vez a observar con la bitacora de su asistente: el panel deja la anterior, se limpia y muestra la nueva.
 * Es un JPanel, para ubicarlo dentro de la Seccion de Control de la Nave.
 */
public class PanelBitacora extends JPanel implements IObservadorModelo<Entrada> {
    private final JTextArea area;
    private final SimpleDateFormat formatoHora;
    private Bitacora observada;

    /**
     * Se construye el panel de la bitacora, vacio y sin observar ninguna bitacora
     * <b>pre:</b> se ejecuta en el hilo de Swing <br>
     * <b>post:</b> se ha creado el panel con el titulo arriba y un area de texto de solo lectura, vacia y con barra de desplazamiento
     */
    public PanelBitacora() {
        super(new BorderLayout(0, 6));
        this.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JLabel titulo = new JLabel("Bitacora de eventos");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 14f));

        this.area = new JTextArea();
        this.area.setEditable(false);
        this.area.setLineWrap(true);
        this.area.setWrapStyleWord(true);
        this.area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        this.formatoHora = new SimpleDateFormat("HH:mm:ss");
        this.observada = null;

        this.add(titulo, BorderLayout.NORTH);
        this.add(new JScrollPane(this.area), BorderLayout.CENTER);
    }

    /**
     * Empieza a mostrar una bitacora: deja de observar la anterior (si habia), limpia el area, muestra las entradas que ya tiene la bitacora y se suscribe para recibir las nuevas.
     * La lectura de las entradas existentes y la suscripcion se hacen dentro de un bloque synchronized sobre la bitacora (el mismo lock que usa cargaEntrada),
     * asi ninguna entrada que se registre en el medio se pierde ni aparece dos veces
     * <b>pre:</b> bitacora != null; se ejecuta en el hilo de Swing <br>
     * <b>post:</b> el panel muestra todas las entradas de la bitacora en orden temporal, quedo suscripto a ella y ya no recibe eventos de la bitacora anterior
     * @param bitacora - bitacora del asistente de la nave actual. bitacora != null
     */
    public void observar(Bitacora bitacora) {
        assert bitacora != null : "la bitacora no puede ser nula";
        if (this.observada != null)
            this.observada.quitarObservador(this);
        this.observada = bitacora;
        this.area.setText("");

        ArrayList<Entrada> existentes;
        synchronized (bitacora) {
            existentes = bitacora.getEntradas();
            bitacora.agregarObservador(this);
        }
        for (Entrada entrada : existentes)
            this.mostrar(entrada);
    }

    /**
     * Recibe una entrada nueva de la bitacora observada y encola su escritura en el hilo de Swing.
     * Lo ejecuta el hilo que registro la entrada (por ejemplo, el de la mision), con el lock de la bitacora tomado: por eso solo encola y vuelve enseguida.
     * Si la entrada viene de una bitacora que el panel ya dejo de observar (cambio de nave), no se muestra
     * <b>pre:</b> origen != null; evento != null <br>
     * <b>post:</b> quedo encolada en el hilo de Swing la escritura de la entrada al final del area, si viene de la bitacora observada
     * @param origen - bitacora que notifico la entrada. origen != null
     * @param evento - entrada registrada. evento != null
     */
    @Override
    public void actualizar(ObservadoModelo<Entrada> origen, Entrada evento) {
        assert origen != null && evento != null : "el origen y la entrada no pueden ser nulos";
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (origen == observada)
                    mostrar(evento);
            }
        });
    }

    /**
     * Escribe una entrada al final del area, con el formato "[hora] Tipo: mensaje", y desplaza el area para mostrar la ultima linea
     * <b>pre:</b> entrada != null; se ejecuta en el hilo de Swing <br>
     * <b>post:</b> el area tiene una linea mas al final, con la entrada dada, y muestra la ultima linea
     * @param entrada - entrada a mostrar. entrada != null
     */
    private void mostrar(Entrada entrada) {
        this.area.append("[" + this.formatoHora.format(entrada.getFecha()) + "] " + entrada.getTipo() + ": " + entrada.getMensaje() + "\n");
        this.area.setCaretPosition(this.area.getDocument().getLength());
    }
}
