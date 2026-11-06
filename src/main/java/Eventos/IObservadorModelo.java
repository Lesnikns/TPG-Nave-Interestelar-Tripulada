package Eventos;

/**
 * Interfaz del observador del patron Observer (Observer-Observable visto en clase): define el metodo que se ejecuta cuando un objeto observado notifica un evento.
 * Es generica: T es el tipo de evento que recibe (Entrada en la bitacora, Informe en el historial, String en la Plataforma de Carga), por lo que no hacen falta casts.
 * La implementan las vistas que se actualizan solas (PanelBitacora, PanelInformes, PanelVisor). El modelo solo conoce esta interfaz, nunca las clases concretas de la vista.
 * @param <T> tipo del evento que recibe el observador
 */
public interface IObservadorModelo<T> {
    /**
     * Se ejecuta cada vez que un objeto observado al que este observador esta suscripto notifica un evento.
     * Lo ejecuta el hilo que genero el evento (la mision o un hilo del Visor), no el hilo de Swing:
     * si la implementacion modifica componentes Swing, debe hacerlo con SwingUtilities.invokeLater.
     * Se ejecuta con el lock del objeto observado tomado, asi que tiene que ser corto
     * <b>pre:</b> origen != null y es un objeto al que este observador se suscribio; evento != null <br>
     * <b>post:</b> el observador proceso el evento (en una vista, quedo encolada la actualizacion de la pantalla)
     * @param origen - objeto observado que notifico el evento (permite verificar que sea el esperado o distinguir entre varios observados). origen != null
     * @param evento - descripcion del cambio ocurrido. evento != null
     */
    void actualizar(ObservadoModelo<T> origen, T evento);
}