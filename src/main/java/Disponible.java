/**
 * Estado inicial del motor warp (patron State): el motor esta libre y listo para iniciar un salto.
 * Es el unico estado desde el cual se permite preparar un salto (transicion Disponible -&gt; PreparandoSalto). Cualquier otra accion es una transicion invalida:
 * no tiene efecto sobre la nave y queda registrada como error en la bitacora a traves del asistente de comando.
 * <b>invariantes:</b> nave != null <br>
 * asistente != null (es el asistente que opera esa misma nave)
 */
public class Disponible implements EstadoMotor {
	private Nave nave;
	private AsistenteDeComando asistente;

	/**
	 * Intenta cerrar el motor. No es una accion valida en Disponible, ya que solo se cierra un salto en curso (desde EnWarp)
	 * <b>pre:</b> motor != null <br>
	 * <b>post:</b> el motor y la nave no se modificaron (el motor sigue en Disponible) y se registro en la bitacora un evento de tipo "Error" con la transicion rechazada
	 * @param motor - motor warp sobre el cual se pidio la accion. motor != null
	 */
	@Override
	public void cerrar(MotorWarp motor) {
		asistente.escribeBitacora("No permitida transicion a estado: " + nomEstados[0],"Error");
	}
	/**
	 * Inicia la preparacion de un salto (transicion valida desde Disponible)
	 * <b>pre:</b> motor != null y el estado actual del motor es Disponible <br>
	 * <b>post:</b> el motor paso al estado PreparandoSalto, con nombre de estado "Preparando salto", y se registro en la bitacora un evento de tipo "Transicion"
	 * @param motor - motor warp que cambiara de estado. motor != null
	 */
	@Override
	public void prepararSalto(MotorWarp motor) {
		motor.setEstado(new PreparandoSalto(this.nave));
		motor.setNombreEstado(nomEstados[1]);
		asistente.escribeBitacora("transicion a estado: " + nomEstados[1],"Transicion");
	}
	/**
	 * Intenta saltar. No es una accion valida en Disponible, ya que antes hay que preparar el salto
	 * <b>pre:</b> motor != null <br>
	 * <b>post:</b> el motor y la nave no se modificaron (el motor sigue en Disponible) y se registro en la bitacora un evento de tipo "Error" con la transicion rechazada
	 * @param motor - motor warp sobre el cual se pidio la accion. motor != null
	 */
	@Override
	public void saltar(MotorWarp motor) {
		asistente.escribeBitacora("No permitida transicion a estado: " + nomEstados[2],"Error");
	}
	/**
	 * Intenta enfriar el motor. No es una accion valida en Disponible, ya que solo se enfria un motor que realizo un salto
	 * <b>pre:</b> motor != null <br>
	 * <b>post:</b> el motor y la nave no se modificaron (el motor sigue en Disponible); la accion se rechaza y debe quedar registrada como error (no se aceptan transiciones invalidas silenciosas)
	 * @param motor - motor warp sobre el cual se pidio la accion. motor != null
	 */
	@Override
	public void enfriar(MotorWarp motor) {
		asistente.escribeBitacora("No permitida transicion a estado: " + nomEstados[3],"Error");
	}

	/**
	 * Se construye el estado Disponible asociado a una nave, tomando de ella el asistente que registra los eventos
	 * <b>pre:</b> nave != null y nave.getAsistente() != null (la nave ya tiene su asistente asignado) <br>
	 * <b>post:</b> se ha creado el estado con una referencia a la nave y a su asistente de comando
	 * @param nave - nave a la que pertenece el motor warp. nave != null
	 */
	public Disponible(Nave nave) {
		super();
		this.nave = nave;
		this.asistente = nave.getAsistente();
	}
}
