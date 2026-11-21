import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import Asistente.EstadoRecursos;
import Asistente.EstadoTurno;
import Asistente.HistorialDeInformes;
import Asistente.PublicadorDeTurnos;
import Controlador.ControladorControl;
import Controlador.ControladorCreacion;
import Controlador.ControladorLogin;
import Controlador.ControladorRegistro;
import DestinoTablero.EstadoTablero;
import DestinoTablero.FabricaDeTableros;
import DestinoTablero.Tablero;
import Eventos.IObservadorModelo;
import Eventos.ObservadoModelo;
import Excepciones.PersistenciaException;
import Naves.SesionDeNave;
import Persistencia.BaseDeDatos;
import Persistencia.DAOInterfaz;
import Persistencia.DAOJDBC;
import Persistencia.Negocio;
import Vista.*;

public class TestSistema{
 
    public static void main(String[] args) {
        try {
            BaseDeDatos baseDeDatos = new BaseDeDatos("jdbc:h2:./database/tpg");
            DAOInterfaz dao = new DAOJDBC(baseDeDatos);
            Negocio servicio = new Negocio(dao);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    iniciar(servicio);
                }
            });
        } catch (PersistenciaException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "No se pudo abrir la base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    private static void iniciar(Negocio servicio) {
        SesionDeNave sesion = new SesionDeNave();

        PublicadorDeTurnos publicador = new PublicadorDeTurnos();
        HistorialDeInformes historial = new HistorialDeInformes();

        // Vistas
        VistaControl vistaControl = new VistaControl();
        VistaLogin vistaLogin = new VistaLogin();
        VistaRegistro vistaRegistro = new VistaRegistro();
        VistaCreacion vistaCreacion = new VistaCreacion();
        VistaTripulantes vistaTripulantes = new VistaTripulantes(new TripulantesTableModel(sesion));
        VentanaPrincipal ventana = new VentanaPrincipal(vistaLogin, vistaRegistro);



        ventana.agregarPanel("Creación", vistaCreacion, false);
        ventana.agregarPanel("Tripulantes", vistaTripulantes, true);
        ventana.agregarPanel("Control", vistaControl, true);

        // Agregar mas paneles con ventana.agregarPanel(Titulo, vista, y un booleano que indica si el panel requiere
        // el uso de la nave o no)
        PanelInformes panelInformes = new PanelInformes();
        //ventana.agregarPanel("Informes", panelInformes, false);   // pestaña temporal para probar

        //PanelBitacora panelBitacora = new PanelBitacora(sesion);
        PanelBitacora panelBitacora = new PanelBitacora(); //el de juan
        //ventana.agregarPanel("Bitácora", panelBitacora, true);
        vistaControl.setPanelesRegistro(panelBitacora, panelInformes);
        publicador.agregarObservador(vistaControl);
        new ControladorControl(vistaControl, sesion);
        new ControladorLogin(vistaLogin, ventana, servicio);
        new ControladorRegistro(vistaRegistro, ventana, servicio);
        new ControladorCreacion(vistaCreacion, sesion, panelBitacora,panelInformes,publicador,historial);

        // Las pestañas que necesitan nave se habilitan cuando la sesión tiene una
        sesion.agregarObservador(new IObservadorModelo<String>() {
            @Override
            public void actualizar(ObservadoModelo<String> origen, String evento) {
                ventana.setNaveDisponible(sesion.hayNave());
            }
        });
 
        ventana.setVisible(true);
    }
}
 