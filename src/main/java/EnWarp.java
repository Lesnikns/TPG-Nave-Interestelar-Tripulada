/**
 * Tercer estado del motor warp (patron State, "Salto warp"): la nave esta realizando el salto.
 * Como todavia no se modela el paso del tiempo, el salto termina directamente con un cierre que devuelve el motor a Disponible (transicion EnWarp -&gt; Disponible).
 * El enfriamiento posterior al salto (EnWarp -&gt; Enfriamiento) se define en la entrega 2. Cualquier otra accion es una transicion invalida:
 * no tiene efecto sobre la nave y queda registrada como error en la bitacora a traves del asistente de comando.
 * <b>invariantes:</b> nave != null <br>
 */
public class EnWarp implements EstadoMotor {
	private Nave nave;

	/**
	 * Cierra el salto en curso, devolviendo el motor a Disponible (transicion valida desde EnWarp)
	 * <b>pre:</b> motor != null y el estado actual del motor es EnWarp <br>
	 * <b>post:</b> el motor paso al estado Disponible, con nombre de estado "Disponible", y se registro en la bitacora un evento de tipo "Transicion"
	 * @param motor - motor warp que cambiara de estado. motor != null
	 */
	@Override
	public void cerrar(MotorWarp motor) {
		motor.setEstado(new Disponible(this.nave));
		motor.setNombreEstado(nomEstados[0]);
		this.nave.getAsistente().escribeBitacora("transicion a estado: " + nomEstados[0],"Transicion");
	}
	/**
	 * Intenta preparar un salto. No es una accion valida en EnWarp, ya que hay un salto en curso
	 * <b>pre:</b> motor != null <br>
	 * <b>post:</b> el motor y la nave no se modificaron (el motor sigue en EnWarp) y se registro en la bitacora un evento de tipo "Error" con la transicion rechazada
	 * @param motor - motor warp sobre el cual se pidio la accion. motor != null
	 */
	@Override
	public void prepararSalto(MotorWarp motor) {
		this.nave.getAsistente().escribeBitacora("No permitida transicion a estado: " + nomEstados[1],"Error");
	}
	/**
	 * Intenta saltar. No es una accion valida en EnWarp, ya que el salto ya esta en curso
	 * <b>pre:</b> motor != null <br>
	 * <b>post:</b> el motor y la nave no se modificaron (el motor sigue en EnWarp) y se registro en la bitacora un evento de tipo "Error" con la transicion rechazada
	 * @param motor - motor warp sobre el cual se pidio la accion. motor != null
	 */
	@Override
	public void saltar(MotorWarp motor) {
		this.nave.getAsistente().escribeBitacora("No permitida transicion a estado: " + nomEstados[2],"Error");
	}
	/**
	 * Enfria el motor luego del salto (EnWarp -&gt; Enfriamiento). Su comportamiento no se define hasta la entrega 2: el estado Enfriamiento existe pero todavia no se usa
	 * <b>pre:</b> motor != null y el estado actual del motor es EnWarp <br>
	 * <b>post:</b> en la entrega 1 la accion no tiene efecto sobre el motor ni sobre la nave (el motor sigue en EnWarp); a partir de la entrega 2 el motor pasara a Enfriamiento y se registrara la transicion
	 * @param motor - motor warp sobre el cual se pidio la accion. motor != null
	 */
	@Override
	public void enfriar(MotorWarp motor) {
		//no se define pasaje a estado de enfriamiento hasta 2da entrega
	}

	/**
	 * Se construye el estado EnWarp asociado a una nave, tomando de ella el asistente que registra los eventos
	 * <b>pre:</b> nave != null y nave.getAsistente() != null (la nave ya tiene su asistente asignado) <br>
	 * <b>post:</b> se ha creado el estado con una referencia a la nave y a su asistente de comando
	 * @param nave - nave a la que pertenece el motor warp. nave != null
	 */
	public EnWarp(Nave nave) {
		super();
		this.nave = nave;
	}
}
