package Vista;
 
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.CardLayout;
import java.util.ArrayList;
 
public class VentanaPrincipal extends JFrame {
    private CardLayout cartas = new CardLayout();
    private JPanel contenedor = new JPanel(cartas);
    private JTabbedPane pestanas = new JTabbedPane();
    private ArrayList<JPanel> panelesConNave = new ArrayList<>();
 
    public VentanaPrincipal(JPanel vistaLogin, JPanel vistaRegistro) {
        super("Nave interestelar");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
 
        contenedor.add(vistaLogin, "LOGIN");
        contenedor.add(vistaRegistro, "REGISTRO");
        contenedor.add(pestanas, "PRINCIPAL");
        setContentPane(contenedor);
        mostrarLogin();
    }
 
    /**
     * Agrega un panel como pestaña nueva
     * @pre: titulo != null y panel != null
     * @post: si requiereNave, la pestaña queda deshabilitada hasta que haya una nave
     */
    public void agregarPanel(String titulo, JPanel panel, boolean requiereNave) {
    	assert titulo != null: "El titulo del panel a agregar no puede estar vacio.";
    	assert panel != null: "El panel a agregar no puede ser nulo.";
        pestanas.addTab(titulo, panel);
        if (requiereNave) {
            panelesConNave.add(panel);
            pestanas.setEnabledAt(pestanas.indexOfComponent(panel), false);
        }
    }
 
    /** Habilita o deshabilita las pestañas que necesitan una nave */
    public void setNaveDisponible(boolean disponible) {
        for (JPanel panel : panelesConNave) {
            pestanas.setEnabledAt(pestanas.indexOfComponent(panel), disponible);
        }
    }
 
    public void mostrarLogin() {
        setTitle("Nave interestelar");
        cartas.show(contenedor, "LOGIN");
    }
 
    public void mostrarRegistro() {
        cartas.show(contenedor, "REGISTRO");
    }
 
    public void mostrarPrincipal(String usuario) {
        setTitle("Nave interestelar - Operador: " + usuario);
        cartas.show(contenedor, "PRINCIPAL");
    }
}