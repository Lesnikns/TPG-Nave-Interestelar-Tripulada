package Controlador;

import Vista.InterfazVisor;
import UniversoParalelo.GeneradoraDeEnergia;
import UniversoParalelo.NaveConsumidora;
import UniversoParalelo.PlataformaDeCarga;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controlador del Visor Recondito (patron MVC): atiende los dos botones de la vista y crea las entidades del universo paralelo en el modelo.
 * Al presionar "Crear Nave Consumidora" crea una NaveConsumidora y la pone en marcha; al presionar "Crear Generadora de Energia" hace lo mismo con una GeneradoraDeEnergia.
 * Conoce la vista solo a traves de InterfazVisor, y no maneja hilos: cada entidad se pone en marcha a si misma con iniciar().
 * Los mensajes que se generan llegan a la consola por el patron Observer, sin pasar por el controlador.
 * <b>invariantes:</b> vista != null <br>
 * plataforma != null
 */
public class ControladorVisor implements ActionListener {
    private final InterfazVisor vista;
    private final PlataformaDeCarga plataforma;

    /**
     * Se construye el controlador del visor y se conecta con los botones de la vista
     * <b>pre:</b> vista != null; plataforma != null (la unica plataforma de la aplicacion) <br>
     * <b>post:</b> se ha creado el controlador y quedo registrado como escuchador de los dos botones de la vista
     * @param vista - vista del visor cuyos botones atiende. vista != null
     * @param plataforma - plataforma de carga donde operan las entidades que se crean. plataforma != null
     */
    public ControladorVisor(InterfazVisor vista, PlataformaDeCarga plataforma) {
        assert vista != null : "el controlador necesita una vista";
        assert plataforma != null : "el controlador necesita una plataforma";
        this.vista = vista;
        this.plataforma = plataforma;
        this.vista.setControlador(this);
    }

    /**
     * Atiende un boton de la vista: segun el comando de accion, crea una nave consumidora o una generadora de energia y la pone en marcha.
     * Se ejecuta en el hilo de Swing y vuelve enseguida: el ciclo de la entidad corre en su propio hilo
     * <b>pre:</b> evento != null y su comando de accion es InterfazVisor.CREAR_NAVE o InterfazVisor.CREAR_GENERADORA <br>
     * <b>post:</b> ingreso al universo paralelo una entidad nueva del tipo pedido, con su hilo en marcha, y se informo su ingreso en el visor
     * @param evento - evento del boton presionado. evento != null
     */
    @Override
    public void actionPerformed(ActionEvent evento) {
        assert evento != null : "el evento no puede ser nulo";
        String comando = evento.getActionCommand();
        if (comando.equals(InterfazVisor.CREAR_NAVE)) {
            new NaveConsumidora(this.plataforma).iniciar();
        }
        else if (comando.equals(InterfazVisor.CREAR_GENERADORA)) {
            new GeneradoraDeEnergia(this.plataforma).iniciar();
        }
        else {
            assert false : "comando de accion desconocido: " + comando;
        }
    }
}
