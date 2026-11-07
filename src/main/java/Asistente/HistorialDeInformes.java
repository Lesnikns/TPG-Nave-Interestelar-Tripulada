package Asistente;

import Eventos.ObservadoModelo;

import java.util.ArrayList;

/**
 * Historial de los informes finales de las misiones que coordino un asistente de comando, en el orden en que se registraron.
 * Es observable (patron Observer): cada informe nuevo se notifica a los observadores suscriptos, por ejemplo el panel de informes, sin que tengan que consultarlo.
 * Lo escribe el hilo de la mision mientras la vista lo lee, por eso los metodos que usan la lista son synchronized.
 * Los informes no se modifican ni se eliminan una vez registrados.
 * <b>invariantes:</b> informes != null <br>
 * ningun informe es nulo <br>
 * los informes estan ordenados del mas antiguo al mas reciente
 */
public class HistorialDeInformes extends ObservadoModelo<Informe> {
    private final ArrayList<Informe> informes;

    /**
     * Registra un nuevo informe al final del historial y lo notifica a los observadores
     * <b>pre:</b> informe != null <br>
     * <b>post:</b> el informe se agrego al final del historial (es el mas reciente), los informes anteriores no cambiaron y se notifico el informe a los observadores
     * @param informe - informe final de una mision. informe != null
     */
    public synchronized void agregarInforme(Informe informe){
        assert informe != null : "el informe no puede ser nulo";
        informes.add(informe);
        notificar(informe);
    }

    /**
     * Devuelve los informes registrados hasta el momento, en orden temporal
     * <b>pre:</b> ninguna <br>
     * <b>post:</b> se devolvio una copia de la lista de informes, del mas antiguo al mas reciente. Modificar la copia no cambia el historial
     * @return copia de la lista de informes
     */
    public synchronized ArrayList<Informe> getInformes(){
        return new ArrayList<>(informes);
    }

    /**
     * Se construye un historial sin informes registrados
     * <b>pre:</b> ninguna <br>
     * <b>post:</b> se ha creado el historial con la lista de informes inicializada y vacia
     */
    public HistorialDeInformes() {
        super();
        this.informes = new ArrayList<>();
    }
}
