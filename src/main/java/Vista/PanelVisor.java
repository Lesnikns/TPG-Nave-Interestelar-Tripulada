package Vista;

import Eventos.IObservadorModelo;
import Eventos.ObservadoModelo;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;

/**
 * Seccion del Visor Recondito (vista del patron MVC): muestra el universo paralelo de la Plataforma de Carga.
 * Tiene dos botones para agregar naves consumidoras y generadoras de energia en vivo, y una consola de solo lectura con el flujo de eventos.
 * Cumple dos papeles:
 * <ul>
 * <li>Vista del MVC (implementa InterfazVisor): los botones llevan los comandos CREAR_NAVE y CREAR_GENERADORA y los atiende el controlador.</li>
 * <li>Observador del patron Observer (implementa IObservadorModelo): la plataforma le notifica cada evento y el panel lo agrega a la consola.</li>
 * </ul>
 * Los eventos llegan desde los hilos del universo paralelo, no desde el hilo de Swing, por eso la consola se actualiza siempre con SwingUtilities.invokeLater.
 * Es un JPanel, para agregarlo como seccion (por ejemplo, una pestania) de la ventana principal.
 */
public class PanelVisor extends JPanel implements InterfazVisor, IObservadorModelo<String> {
    private final JButton botonNave;
    private final JButton botonGeneradora;
    private final JTextArea consola;

    /**
     * Se construye el panel del visor con sus dos botones y la consola vacia. Todavia no observa ninguna plataforma ni tiene controlador
     * <b>pre:</b> se ejecuta en el hilo de Swing <br>
     * <b>post:</b> se ha creado el panel con el titulo arriba, los botones "Crear Nave Consumidora" y "Crear Generadora de Energia" con sus comandos de accion, y la consola de solo lectura con barra de desplazamiento
     */
    public PanelVisor() {
        super(new BorderLayout(8, 8));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Visor Recondito - Universo Paralelo");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 16f));

        this.botonNave = new JButton("Crear Nave Consumidora");
        this.botonNave.setActionCommand(CREAR_NAVE);
        this.botonGeneradora = new JButton("Crear Generadora de Energia");
        this.botonGeneradora.setActionCommand(CREAR_GENERADORA);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        botones.add(this.botonNave);
        botones.add(this.botonGeneradora);

        JPanel arriba = new JPanel(new BorderLayout(0, 8));
        arriba.add(titulo, BorderLayout.NORTH);
        arriba.add(botones, BorderLayout.CENTER);

        this.consola = new JTextArea();
        this.consola.setEditable(false);
        this.consola.setLineWrap(true);
        this.consola.setWrapStyleWord(true);
        this.consola.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        this.add(arriba, BorderLayout.NORTH);
        this.add(new JScrollPane(this.consola), BorderLayout.CENTER);
    }

    /**
     * Conecta los dos botones con el controlador que atiende sus acciones
     * <b>pre:</b> controlador != null <br>
     * <b>post:</b> al presionar cualquiera de los dos botones se ejecuta controlador.actionPerformed, con el comando CREAR_NAVE o CREAR_GENERADORA
     * @param controlador - escuchador que atiende los botones. controlador != null
     */
    @Override
    public void setControlador(ActionListener controlador) {
        assert controlador != null : "el controlador no puede ser nulo";
        this.botonNave.addActionListener(controlador);
        this.botonGeneradora.addActionListener(controlador);
    }

    /**
     * Suscribe el panel a la fuente de eventos del universo paralelo (la Plataforma de Carga), para que cada evento aparezca en la consola.
     * Recibe el tipo general de objeto observado, asi la vista no depende de la clase concreta del modelo
     * <b>pre:</b> fuente != null; se llama una sola vez <br>
     * <b>post:</b> el panel quedo suscripto a la fuente y mostrara en la consola cada evento que notifique a partir de ahora
     * @param fuente - objeto observado que notifica los eventos del visor (la PlataformaDeCarga). fuente != null
     */
    public void observar(ObservadoModelo<String> fuente) {
        assert fuente != null : "la fuente de eventos no puede ser nula";
        fuente.agregarObservador(this);
    }

    /**
     * Recibe un evento del universo paralelo y lo agrega como una linea nueva al final de la consola.
     * Lo ejecuta el hilo que genero el evento (una generadora, una nave o el hilo de Swing al crear una entidad), con el lock de la plataforma tomado:
     * por eso solo encola la actualizacion de la consola con SwingUtilities.invokeLater y vuelve enseguida
     * <b>pre:</b> origen != null; evento != null <br>
     * <b>post:</b> quedo encolada en el hilo de Swing la escritura del evento al final de la consola, que se desplaza para mostrar la ultima linea
     * @param origen - objeto observado que notifico el evento. origen != null
     * @param evento - mensaje del evento ocurrido. evento != null
     */
    @Override
    public void actualizar(ObservadoModelo<String> origen, String evento) {
        assert origen != null && evento != null : "el origen y el evento no pueden ser nulos";
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                consola.append(evento + "\n");
                consola.setCaretPosition(consola.getDocument().getLength());
            }
        });
    }
}
