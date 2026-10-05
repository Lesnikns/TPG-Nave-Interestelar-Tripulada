import java.util.Date;

/**
 * Representa un evento registrado en la bitacora de un asistente de comando: que paso (mensaje), de que clase fue (tipo) y cuando sucedio (fecha).
 * Una entrada es inmutable: una vez creada no puede modificarse. Los tipos usados hasta ahora son "Error" y "Transicion";
 * tambien corresponde registrar ejecucion de misiones, resultados y operaciones sobre recursos.
 * <b>invariantes:</b> mensaje != null y mensaje != "" <br>
 * tipo != null y tipo != "" <br>
 * fecha != null, fijada en el instante de creacion y sin cambios posteriores
 */
public class Entrada {
	private String mensaje;
	private Date fecha;
	private String tipo;

	/**
	 * Devuelve el texto que describe lo sucedido en el evento
	 * <b>pre:</b> la entrada fue construida correctamente (mensaje no-nulo y no vacio) <br>
	 * <b>post:</b> se devolvio una referencia al mensaje, sin modificarlo
	 * @return el mensaje del evento
	 */
	protected String getMensaje() {
		return this.mensaje;
	}
	/**
	 * Devuelve el momento en que se registro el evento (permite ordenar los eventos en forma temporal)
	 * <b>pre:</b> la entrada fue construida correctamente (fecha no-nula) <br>
	 * <b>post:</b> se devolvio la fecha de creacion de la entrada, sin modificarla
	 * @return la fecha en que sucedio el evento
	 */
	protected Date getFecha() {
		return this.fecha;
	}
	/**
	 * Devuelve la clase de evento registrado (por ejemplo "Error" o "Transicion")
	 * <b>pre:</b> la entrada fue construida correctamente (tipo no-nulo y no vacio) <br>
	 * <b>post:</b> se devolvio una referencia al tipo, sin modificarlo
	 * @return el tipo del evento
	 */
	protected String getTipo() {
		return this.tipo;
	}

	/**
	 * override del metodo toString(), devolviendo fecha, tipo y mensaje de la entrada
	 * <b>pre:</b> Objeto Entrada != null; campos fecha, tipo, mensaje distintos de nulo o "" (bitacora no tendra eventos nulos o vacios) <br>
	 * <b>post:</n> se devolvera un String con los campos principales de la entrada, para mostrar por sistema
	 * @return string de retorno != null, != ""
	 */
	@Override
	public String toString() {
		return "Entrada fecha=" + this.fecha + ", tipo='" + this.tipo + ", mensaje='" + this.mensaje + '\n';
	}
	/**
	 * Se construye una entrada inmutable, tomando como fecha el instante exacto de su creacion
	 * <b>pre:</b> mensaje != null y mensaje != "" ; tipo != null y tipo != "" (la bitacora no acepta eventos nulos o vacios) <br>
	 * <b>post:</b> se ha creado la entrada con el mensaje y el tipo dados, y con fecha igual al momento de la creacion
	 * @param mensaje - descripcion de lo sucedido. mensaje != null, mensaje != ""
	 * @param tipo - clase de evento ("Error","Transicion", etc). tipo != null, tipo != ""
	 */
	public Entrada(String mensaje, String tipo) {
		super();
		this.mensaje = mensaje;
		this.tipo = tipo;
		this.fecha = new Date();
	}
}
