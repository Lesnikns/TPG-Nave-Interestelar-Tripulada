package Vista;

import java.awt.event.ActionListener;

/**
 * Interfaz de la vista del Visor Recondito (patron MVC): es lo unico de la vista que conoce el controlador.
 * Define los comandos de accion de los dos botones del visor (CREAR_NAVE y CREAR_GENERADORA) y el metodo para conectarlos con el controlador,
 * de modo que el controlador no depende de la clase concreta de Swing que dibuja el panel.
 * Los mensajes de la consola no pasan por esta interfaz: llegan directo del modelo a la vista por el patron Observer.
 */
public interface InterfazVisor {
    String CREAR_NAVE = "CREAR_NAVE";
    String CREAR_GENERADORA = "CREAR_GENERADORA";

    /**
     * Conecta los botones de la vista con el controlador que atiende sus acciones
     * <b>pre:</b> controlador != null <br>
     * <b>post:</b> al presionar cualquiera de los dos botones se ejecuta controlador.actionPerformed, con el comando CREAR_NAVE o CREAR_GENERADORA
     * @param controlador - escuchador que atiende los botones. controlador != null
     */
    void setControlador(ActionListener controlador);
}
