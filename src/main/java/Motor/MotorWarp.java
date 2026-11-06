package Motor;

import Naves.Nave;

/**
 * Motor warp de una nave. Es el contexto del patron State: delega en su estado actual (EstadoMotor) los dos pedidos que recibe de la mision,
 * de modo que ni la nave ni la mision necesitan condicionales por estado.
 * Como pide el Anexo, solo expone publicamente dos operaciones: saltar() y saltarWarp(). Cada llamada es un turno de la nave y devuelve cuantos casilleros avanzo,
 * porque quien mueve la nave en el tablero es la mision.
 * El motor puede estar en cuatro estados: Disponible, PreparandoSalto, EnWarp y Enfriamiento, y siempre arranca en Disponible.
 * Un salto warp completo lleva cuatro turnos: dos de preparacion, el salto y uno de enfriamiento.
 * Los costos de cada movimiento los descuenta el estado que corresponde, pidiendoselos a la nave, y quedan registrados en la bitacora con las cantidades reales.
 * <b>invariantes:</b> estado != null y es uno de los cuatro estados definidos <br>
 * nombreEstado != null, nombreEstado != "" y coincide con el estado concreto actual
 */
public class MotorWarp{
	private EstadoMotor estado;
	private String nombreEstado;
	public static final int ENERGIA_SALTO_WARP = 5;
	static final int COMBUSTIBLE_SALTO_NORMAL = 2;
	static final int DESGASTE_ENFRIAMIENTO = 3;
	static final int TURNOS_PREPARACION = 2;
	static final int CASILLEROS_SALTO_NORMAL = 1;
	static final int CASILLEROS_SALTO_WARP = 2;


	/**
	 * devuelve el nombre del estado actual del motor
	 * <b>pre:</b> nombreEstado debe ser no-nulo y tampoco debe estar vacio ("") <br>
	 * <b>post:</b> se devolvio una referencia al nombre de estado
	 * @return el nombre de estado
	 */
	String getNombreEstado() {
		return this.nombreEstado;
	}

	/**
	 * Actualiza el nombre de estado a base de uno nuevo dado como parametro
	 * <b>pre:</b> nombreEstado != null y nombreEstado != "" <br>
	 * <b>post:</b> se seteo correctamente el nuevo nombre de estado actual
	 * @param nombreEstado - nuevo nombre de estado a setear como estado actual. nombreEstado != null, nombreEstado != "" (uno de EstadoMotor.nomEstados)
	 */
	void setNombreEstado(String nombreEstado) {
		assert nombreEstado != null && !nombreEstado.isEmpty() : "el nombre de estado no puede ser nulo ni vacio";
		this.nombreEstado = nombreEstado;
	}

	/**
	 * Cambia el estado del objeto EstadoMotor en el motor
	 * <b>pre:</b> estado != null <br>
	 * <b>post:</b> se ha seteado un nuevo estado valido
	 * @param estado - el nuevo estado del motor. estado != null (Disponible, PreparandoSalto, EnWarp o Enfriamiento)
	 */
	void setEstado(EstadoMotor estado) {
		assert estado != null : "el estado no puede ser nulo";
		this.estado = estado;
	}

	/**
	 * Pedido de salto normal de la nave para este turno. La respuesta depende del estado del motor:
	 * en Disponible salta, durante la maniobra warp la cancela y salta, y en Enfriamiento termina el enfriamiento sin avanzar
	 * <b>pre:</b> la nave del motor tiene asistente asignado <br>
	 * <b>post:</b> se devolvio cuantos casilleros avanzo la nave (0 o 1) y se registro lo ocurrido en la bitacora
	 * @return casilleros que avanzo la nave en este turno (0 o 1)
	 */
	public int saltar(){
		return estado.pedirSaltoNormal(this);
	}

	/**
	 * Pedido de salto warp de la nave para este turno (la mision lo pide cuando el radar detecta un obstaculo adelante). Cada pedido avanza un paso de la maniobra:
	 * dos turnos de preparacion, el salto de dos casilleros y un turno de enfriamiento
	 * <b>pre:</b> la nave del motor tiene asistente asignado <br>
	 * <b>post:</b> se devolvio cuantos casilleros avanzo la nave (0 o 2), el motor paso al estado que corresponde y se registro lo ocurrido en la bitacora
	 * @return casilleros que avanzo la nave en este turno (0 o 2)
	 */
	public int saltarWarp(){
		return estado.pedirSaltoWarp(this);
	}

	/**
	 * Se construye al motor warp en estado Disponible (estado inicial del patron State)
	 * <b>pre:</b> nave != null <br>
	 * <b>post:</b> se ha creado el motor en estado Disponible, asociado a la nave
	 * @param nave - nave a la que pertenece el motor. nave != null
	 */
	public MotorWarp(Nave nave) {
		assert nave != null : "el motor necesita una nave";
		this.estado = new Disponible(nave);
		this.nombreEstado = EstadoMotor.nomEstados[0];
	}
}
