package Asistente;

import Eventos.ObservadoModelo;

import java.util.ArrayList;

/**
 * Asistente.Bitacora de un asistente de comando: registro cronologico de los eventos que ocurren en la nave que opera (cambios del motor warp, errores,
 * ejecucion de misiones y sus resultados, operaciones sobre recursos). Debe servir como informe de lo ocurrido en cada mision.
 * Cada asistente lleva su propia bitacora. Los eventos se agregan siempre al final, por lo que se pueden consultar en orden temporal, y una vez registrados no se modifican ni se eliminan.
 * Es observable (patron Observer): cada entrada nueva se notifica a los observadores suscriptos, por ejemplo el panel de la bitacora, sin que tengan que consultarla.
 * La escribe el hilo de la mision mientras la vista la lee, por eso los metodos que usan la lista son synchronized.
 * <b>invariantes:</b> entradas != null <br>
 * ninguna entrada es nula <br>
 * las entradas estan ordenadas de la mas antigua a la mas reciente
 */
public class Bitacora extends ObservadoModelo<Entrada> {
	private final ArrayList<Entrada> entradas;

	/**
	 * Devuelve las entradas registradas hasta el momento, en orden temporal
	 * <b>pre:</b> la bitacora fue construida correctamente (entradas != null) <br>
	 * <b>post:</b> se devolvio una copia de la lista de entradas, de la mas antigua a la mas reciente. Modificar la copia no cambia la bitacora
	 * @return copia de la lista de entradas de la bitacora
	 */
	public synchronized ArrayList<Entrada> getEntradas() {
		return new ArrayList<>(entradas);
	}

	/**
	 * Registra un nuevo evento al final de la bitacora
	 * <b>pre:</b> entrada != null, y su mensaje y su tipo no deben ser vacios (la bitacora no acepta eventos nulos o vacios) <br>
	 * <b>post:</b> la entrada se agrego al final de la lista (es la mas reciente), la cantidad de entradas aumento en uno, las entradas anteriores no cambiaron y se notifico la entrada a los observadores
	 * @param entrada - evento a registrar. entrada != null
	 */
	protected synchronized void cargaEntrada(Entrada entrada) {
		assert entrada != null : "la entrada no puede estar vacia/ser nula";
		this.entradas.add(entrada);
		notificar(entrada);
	}

	/**
	 * Genera el texto de la bitacora completa, util como informe de lo ocurrido
	 * <b>pre:</b> entradas != null, y cada entrada debe poder representarse como texto (toString propio) <br>
	 * <b>post:</b> se devolvio un String con una linea por entrada, en orden temporal. Si no hay entradas se devuelve "" (vacio). La bitacora no se modifico
	 * @return el contenido de la bitacora como texto
	 */
	@Override
	public synchronized String toString() {
		String log = "";
		for  (Entrada entrada : this.entradas) {
			log += entrada.toString();
		}
		return log;
	}

	/**
	 * Se construye una bitacora sin eventos registrados
	 * <b>pre:</b> ninguna <br>
	 * <b>post:</b> se ha creado la bitacora con la lista de entradas inicializada y vacia
	 */
	public Bitacora() {
		super();
		this.entradas = new ArrayList<>();
	}
}
