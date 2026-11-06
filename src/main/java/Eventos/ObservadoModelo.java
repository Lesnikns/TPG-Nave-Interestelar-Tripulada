package Eventos;

import java.util.ArrayList;

/**
 * Clase abstracta del objeto observado en el patron Observer (cumple el papel de la clase Observable vista en clase, deprecada desde Java 9).
 * Mantiene la coleccion de observadores y les notifica cada evento. Las clases del modelo que deben avisar cambios a la vista heredan de ella
 * (Bitacora, HistorialDeInformes, PlataformaDeCarga) y llaman a notificar desde los metodos que cambian su estado.
 * A diferencia de java.util.Observable no usa setChanged(): cada llamada a notificar es un aviso, asi no se pierden eventos cuando notifican varios hilos a la vez.
 * La lista es un ArrayList comun, que no admite que un hilo la modifique mientras otro la recorre (lanzaria ConcurrentModificationException).
 * Por eso los tres metodos que la usan son synchronized: la lista es una seccion critica protegida por el lock del propio objeto observado.
 * Como notificar se ejecuta con ese lock tomado, cada observador debe resolver actualizar rapido (las vistas solo encolan con SwingUtilities.invokeLater).
 * <b>invariantes:</b> observadores != null <br>
 * ningun observador es nulo (lo asegura la precondicion de agregarObservador)
 * @param <T> tipo del evento que se notifica
 */
public abstract class ObservadoModelo<T> {
    private final ArrayList<IObservadorModelo<T>> observadores = new ArrayList<>();

    /**
     * Crea el objeto observado sin observadores suscriptos (lo llaman las subclases desde sus constructores)
     * <b>pre:</b> ninguna <br>
     * <b>post:</b> la lista de observadores existe y esta vacia
     */
    protected ObservadoModelo() {
    }

    /**
     * Suscribe un observador para que reciba los proximos eventos
     * <b>pre:</b> o != null <br>
     * <b>post:</b> o quedo en la lista de observadores y recibira cada evento que se notifique a partir de ahora (no recibe los anteriores)
     * @param o - observador a suscribir. o != null
     */
    public synchronized void agregarObservador(IObservadorModelo<T> o) {
        assert o != null : "el observador no puede ser nulo";
        observadores.add(o);
    }
    /**
     * Desuscribe un observador: deja de recibir eventos
     * <b>pre:</b> ninguna <br>
     * <b>post:</b> o ya no esta en la lista de observadores. Si no estaba, la lista no cambio
     * @param o - observador a quitar
     */
    public synchronized void quitarObservador(IObservadorModelo<T> o)  { observadores.remove(o); }

    /**
     * Avisa un evento a todos los observadores suscriptos, en el orden en que se suscribieron. Lo llaman las subclases desde los metodos que cambian su estado.
     * Se ejecuta en el hilo que llama: cada observador decide si tiene que pasar la actualizacion al hilo de Swing
     * <b>pre:</b> evento != null <br>
     * <b>post:</b> se ejecuto actualizar(this, evento) en cada observador suscripto
     * @param evento - evento a notificar. evento != null
     */
    protected synchronized void notificar(T evento) {
        assert evento != null : "el evento no puede ser nulo";
        for (IObservadorModelo<T> o : observadores)
            o.actualizar(this, evento);
    }
}