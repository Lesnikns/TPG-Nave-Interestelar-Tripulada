package Vista;

import Asistente.HistorialDeInformes;
import Asistente.Informe;
import Eventos.IObservadorModelo;
import Eventos.ObservadoModelo;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Panel del historial de informes finales de mision, para la Seccion de Control de la Nave (vista del patron MVC).
 * Muestra una tabla con una fila por informe: mision, resultado, causa, balance de combustible, energia y desgaste, gemas, transferido y hora.
 * Se actualiza solo: observa el historial (patron Observer) y agrega cada informe nuevo apenas se registra, sin tener que consultarlo.
 * Los informes llegan desde el hilo de la mision, no desde el hilo de Swing, por eso la tabla se actualiza siempre con SwingUtilities.invokeLater.
 * Al crear una nave nueva se llama otra vez a observar con el historial de su asistente: el panel deja el anterior, se limpia y muestra el nuevo.
 * Es un JPanel, para ubicarlo dentro de la Seccion de Control de la Nave.
 */
public class PanelInformes extends JPanel implements IObservadorModelo<Informe> {
    private final DefaultTableModel filas;
    private final SimpleDateFormat formatoHora;
    private HistorialDeInformes observado;

    /**
     * Se construye el panel del historial de informes, con la tabla vacia y sin observar ningun historial
     * <b>pre:</b> se ejecuta en el hilo de Swing <br>
     * <b>post:</b> se ha creado el panel con el titulo arriba y una tabla de solo lectura, vacia, con sus columnas y barra de desplazamiento
     */
    public PanelInformes() {
        super(new BorderLayout(0, 6));
        this.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JLabel titulo = new JLabel("Historial de informes de mision");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 14f));

        String[] columnas = {"Mision", "Resultado", "Causa", "Combustible", "Energia", "Desgaste", "Gemas", "Transferido", "Hora"};
        this.filas = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        JTable tabla = new JTable(this.filas);
        tabla.setFillsViewportHeight(true);
        tabla.getTableHeader().setReorderingAllowed(false);
        int[] anchos = {170, 80, 220, 85, 70, 70, 60, 85, 70};
        for (int i = 0; i < anchos.length; i++)
            tabla.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);

        this.formatoHora = new SimpleDateFormat("HH:mm:ss");
        this.observado = null;

        this.add(titulo, BorderLayout.NORTH);
        this.add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    /**
     * Empieza a mostrar un historial: deja de observar el anterior (si habia), limpia la tabla, muestra los informes que ya tiene el historial y se suscribe para recibir los nuevos.
     * La lectura de los informes existentes y la suscripcion se hacen dentro de un bloque synchronized sobre el historial (el mismo lock que usa agregarInforme),
     * asi ningun informe que se registre en el medio se pierde ni aparece dos veces
     * <b>pre:</b> historial != null; se ejecuta en el hilo de Swing <br>
     * <b>post:</b> la tabla muestra todos los informes del historial en orden, el panel quedo suscripto a el y ya no recibe informes del historial anterior
     * @param historial - historial de informes del asistente de la nave actual. historial != null
     */
    public void observar(HistorialDeInformes historial) {
        assert historial != null : "el historial no puede ser nulo";
        if (this.observado != null)
            this.observado.quitarObservador(this);
        this.observado = historial;
        this.filas.setRowCount(0);

        ArrayList<Informe> existentes;
        synchronized (historial) {
            existentes = historial.getInformes();
            historial.agregarObservador(this);
        }
        for (Informe informe : existentes)
            this.mostrar(informe);
    }

    /**
     * Recibe un informe nuevo del historial observado y encola su escritura en el hilo de Swing.
     * Lo ejecuta el hilo que registro el informe (el de la mision), con el lock del historial tomado: por eso solo encola y vuelve enseguida.
     * Si el informe viene de un historial que el panel ya dejo de observar (cambio de nave), no se muestra
     * <b>pre:</b> origen != null; evento != null <br>
     * <b>post:</b> quedo encolada en el hilo de Swing la incorporacion del informe como ultima fila de la tabla, si viene del historial observado
     * @param origen - historial que notifico el informe. origen != null
     * @param evento - informe registrado. evento != null
     */
    @Override
    public void actualizar(ObservadoModelo<Informe> origen, Informe evento) {
        assert origen != null && evento != null : "el origen y el informe no pueden ser nulos";
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (origen == observado)
                    mostrar(evento);
            }
        });
    }

    /**
     * Agrega un informe como ultima fila de la tabla. Los balances se muestran con su signo (por ejemplo -14 o +7)
     * <b>pre:</b> informe != null; se ejecuta en el hilo de Swing <br>
     * <b>post:</b> la tabla tiene una fila mas al final, con los datos del informe dado
     * @param informe - informe a mostrar. informe != null
     */
    private void mostrar(Informe informe) {
        this.filas.addRow(new Object[] {
                "M-0" + informe.getId() + " " + informe.getDescripcion(),
                informe.getResultado(),
                informe.getCausa(),
                String.format("%+d", informe.getBalanceCombustible()),
                String.format("%+d", informe.getBalanceEnergia()),
                String.format("%+d", informe.getBalanceDesgaste()),
                informe.getGemasGanadas(),
                informe.getTransferido(),
                this.formatoHora.format(informe.getFecha())
        });
    }
}
