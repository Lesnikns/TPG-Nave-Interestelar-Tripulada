/**
 * Motor warp de una nave. Es el contexto del patron State: delega en su estado actual (EstadoMotor) el comportamiento de cada accion
 * (preparar salto, saltar, cerrar, enfriar), de modo que la nave no necesita condicionales por estado.
 * El motor puede estar en cuatro estados: Disponible, PreparandoSalto, EnWarp y Enfriamiento, y siempre arranca en Disponible.
 * Solo se puede pasar de un estado a otro por las transiciones validas: Disponible -&gt; PreparandoSalto -&gt; EnWarp -&gt; (Enfriamiento) -&gt; Disponible.
 * Como todavia no se modela el paso del tiempo, un motor EnWarp vuelve a Disponible cuando termina el salto; Enfriamiento existe pero todavia no se usa.
 * Una accion no permitida en el estado actual no tiene efecto sobre la nave y queda registrada como error en la bitacora (no se aceptan transiciones invalidas silenciosas).
 * <b>invariantes:</b> estado != null y es uno de los cuatro estados definidos <br>
 * nombreEstado != null, nombreEstado != "" y coincide con el estado concreto actual ("Disponible","PreparandoSalto","EnWarp","Enfriamiento") <br>
 * nave != null
 */
public class MotorWarp{
	private EstadoMotor estado;
	private String nombreEstado;
	private Nave nave;

	/**
	 * devuelve el nombre del estado actual del motor
	 * <b>pre:</b> nombreEstado debe ser no-nulo y tampoco debe estar vacio ("") <br>
	 * <b>post:</b> se devolvio una referencia al nombre de estado
	 * @return el nombre de estado
	 */
	protected String getNombreEstado() {
		return this.nombreEstado;
	}

	/**
	 * Actualiza el nombre de estado a base de uno nuevo dado como parametro
	 * <b>pre:</b> nombreEstado debe estar inicializado (no es null)
	 * <b>post:</b> se seteo correctamente el nuevo nombre de estado actual
	 * @param nombreEstado - nuevo nombre de estado a setear como estado actual. NombreEstado != null,Nombre != "" (nombreEstado = "Disponible","PreparandoSalto","EnWarp","Enfriamiento")
	 */
	public void setNombreEstado(String nombreEstado) { //deberia tirar excepcion si el valor seteado es erroneo?
		this.nombreEstado = nombreEstado;
	}

	/**
	 * Cambia el estado del objeto EstadoMotor en el motor
	 * <b>pre:</b> el estado debe estar inicializado en alguno de los 4 disponibles (no null)
	 * <b>post:</b> se ha seteado un nuevo estado valido
	 * @param estado - el nuevo estado del motor. estado != null, (estado = Disponible,PreparandoSalto,EnWarp,Enfriamiento)
	 */
	public void setEstado(EstadoMotor estado) {
		this.estado = estado;
	}

	/**
	 * Chequea que el motor este disponible para preparar un salto
	 * <b>pre:</b> nombreEstado debe ser no-nulo y no vacio
	 * <b>post:</b> se ha chequeado si nombre de estado es "Disponible" o no, y se devolvera la respuesta
	 * @return respuesta nombre de estado == "Disponible" (True - False)
	 */
	protected boolean disponibleParaSaltar(){
		return this.nombreEstado.equals("Disponible");
	}

	/**
	 * A traves de esta funcion, se pedira la disponibilidad del motor, "cerrandolo" (llevandose del estado de Salto al estado Disponible, finalizandose el ciclo del salto)
	 * <b>pre:</b> estado debe ser no nulo, y deberia estar en estado EnWarp para un funcionamiento idoneo
	 * <b>post:</b> se ha cerrado el motor, cambiando el estado del motor (caso idoneo), o informado del error de transicion
	 */
	protected void pedirDisponibilidad() {
		estado.cerrar(this);
	}
	/**
	 * Se pedira la preparacion del salto (en caso idoneo, se llevara al estado PreparandoSalto desde el estado Disponible)
	 * <b>pre:</b> estado no nulo, y en deberia ser Disponible para caso idoneo
	 * <b>post:</b> en caso ideal se ha comenzado la preparacion del salto, caso contrario se ha registrado la transicion erronea
	 */
	protected void pedirPrepararSalto() {
		estado.prepararSalto(this);
	}
	/**
	 * Se pedira la preparacion para saltar, entrando en estado EnWarp idealmente (sino se registrara error en la transicion)
	 * <b>pre:</b> estado no nulo, en PrepararSalto para caso idoneo
	 * <b>post:</b> en caso ideal el pedido de warp se concreto correctamente, caso contrario se registra transicion erronea
	 */
	protected void pedirWarp() {
		estado.saltar(this);
	}

	/**
	 * Se pide preparacion para enfriar luego de salto (aun no definido, pues en 1er entrega estado de enfriamiento aun no estara definido)
	 * <b>pre:</b> estado no nulo, y deberia estar en EnWarp para caso idoneo <br>
	 * <b>post:</b> en la entrega 1 no tiene efecto sobre el motor ni sobre la nave; a partir de la entrega 2, en caso ideal se llevara al estado Enfriamiento, caso contrario se registrara transicion erronea
	 */
	protected void pedirEnfriamiento() {
		estado.enfriar(this); //no definido hasta 2da entrega
	}

	/**
	 * Se construye al motor warp, definiendo sus atributos con valores iniciales (en estado Disponible segun el uso del patron State)
	 * <b>pre:</b> la nave pasada como paramtero no debe ser nula, y el estado Disponible debe estar definido (?)
	 * <b>post:</b> se ha creado correctamente el motor, seteado con estado inicial Disponible, y con una referencia a la nave como su atributo
	 * @param nave - referencia a la nave que interactuara con el "universo". nave != null
	 */
	public MotorWarp(Nave nave) {
		this.estado = new Disponible(nave);
		this.nave = nave;
		this.nombreEstado = "Disponible";
	}
}
