package Eventos;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Clase abstracta del objeto observado en el patron Observer (cumple el papel de la clase Observable vista en clase, deprecada desde Java 9).
 * Mantiene la coleccion de observadores y les notifica cada evento. Las clases del modelo que deben avisar cambios a la vista heredan de ella
 * (Bitacora, HistorialDeInformes, PlataformaDeCarga) y llaman a notificar desde los metodos que cambian su estado.
 * A diferencia de java.util.Observable no usa setChanged(): cada llamada a notificar es un aviso, asi no se pierden eventos cuando notifican varios hilos a la vez.
 * La lista es una CopyOnWriteArrayList para poder agregar o quitar observadores mientras otro hilo esta notificando.
 * <b>invariantes:</b> observadores != null <br>
 * ningun observador es nulo (lo asegura la precondicion de agregarObservador)
 * @param <T> tipo del evento que se notifica
 */
public abstract class Observado<T> {
    private final List<IObservador<T>> observadores = new CopyOnWriteArrayList<>();

    /**
     * Suscribe un observador para que reciba los proximos eventos
     * <b>pre:</b> o != null <br>
     * <b>post:</b> o quedo en la lista de observadores y recibira cada evento que se notifique a partir de ahora (no recibe los anteriores)
     * @param o - observador a suscribir. o != null
     */
    public void agregarObservador(IObservador<T> o) { observadores.add(o); }
    /**
     * Desuscribe un observador: deja de recibir eventos
     * <b>pre:</b> ninguna <br>
     * <b>post:</b> o ya no esta en la lista de observadores. Si no estaba, la lista no cambio
     * @param o - observador a quitar
     */
    public void quitarObservador(IObservador<T> o)  { observadores.remove(o); }

    /**
     * Avisa un evento a todos los observadores suscriptos, en el orden en que se suscribieron. Lo llaman las subclases desde los metodos que cambian su estado.
     * Se ejecuta en el hilo que llama: cada observador decide si tiene que pasar la actualizacion al hilo de Swing
     * <b>pre:</b> evento != null <br>
     * <b>post:</b> se ejecuto actualizar(this, evento) en cada observador suscripto
     * @param evento - evento a notificar. evento != null
     */
    protected void notificar(T evento) {
        for (IObservador<T> o : observadores)
            o.actualizar(this, evento);
    }
}