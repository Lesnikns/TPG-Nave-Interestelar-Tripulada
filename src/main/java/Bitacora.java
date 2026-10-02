import java.util.ArrayList;

/**
 * Bitacora de un asistente de comando: registro cronologico de los eventos que ocurren en la nave que opera (cambios del motor warp, errores,
 * ejecucion de misiones y sus resultados, operaciones sobre recursos). Debe servir como informe de lo ocurrido en cada mision (R5 de la aclaracion del TP, E1-05).
 * Cada asistente lleva su propia bitacora. Los eventos se agregan siempre al final, por lo que se pueden consultar en orden temporal, y una vez registrados no se modifican ni se eliminan.
 * <b>invariantes:</b> entradas != null <br>
 * ninguna entrada es nula <br>
 * las entradas estan ordenadas de la mas antigua a la mas reciente
 */
public class Bitacora {
	private ArrayList<Entrada> entradas;

	/**
	 * Devuelve las entradas registradas hasta el momento, en orden temporal
	 * <b>pre:</b> la bitacora fue construida correctamente (entradas != null) <br>
	 * <b>post:</b> se devolvio la lista de entradas, de la mas antigua a la mas reciente. Quien la consulte no debe modificar ni eliminar entradas (un evento registrado no puede modificarse)
	 * @return lista de entradas de la bitacora
	 */
	protected ArrayList<Entrada> getEntradas() {
		return entradas;
	}

	/**
	 * Registra un nuevo evento al final de la bitacora
	 * <b>pre:</b> entrada != null, y su mensaje y su tipo no deben ser vacios (la bitacora no acepta eventos nulos o vacios) <br>
	 * <b>post:</b> la entrada se agrego al final de la lista (es la mas reciente), la cantidad de entradas aumento en uno y las entradas anteriores no cambiaron
	 * @param entrada - evento a registrar. entrada != null
	 */
	protected void cargaEntrada(Entrada entrada) {
		assert entrada != null : "la entrada no puede estar vacia/ser nula";
		this.entradas.add(entrada);
	}

	/**
	 * Genera el texto de la bitacora completa, util como informe de lo ocurrido
	 * <b>pre:</b> entradas != null, y cada entrada debe poder representarse como texto (toString propio) <br>
	 * <b>post:</b> se devolvio un String con una linea por entrada, en orden temporal. Si no hay entradas se devuelve "" (vacio). La bitacora no se modifico
	 * @return el contenido de la bitacora como texto
	 */
	@Override
	public String toString() {
		String log = "";
		for  (Entrada entrada : this.entradas) {
			log += entrada.toString(); //funcionara?
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
