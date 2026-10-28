package Motor;

import Naves.Nave;
import Asistente.AsistenteDeComando;

/**
 * Segundo estado del motor warp (patron State): el salto fue solicitado y el motor se esta preparando para concretarlo.
 * Es el unico estado desde el cual se permite saltar (transicion PreparandoSalto -&gt; EnWarp). Cualquier otra accion es una transicion invalida:
 * no tiene efecto sobre la nave y queda registrada como error en la bitacora a traves del asistente de comando.
 * <b>invariantes:</b> nave != null <br>
 */
public class PreparandoSalto implements EstadoMotor {
	private Nave nave;
	/**
	 * Intenta cerrar el motor. No es una accion valida en PreparandoSalto, ya que solo se cierra un salto en curso (desde EnWarp)
	 * <b>pre:</b> motor != null <br>
	 * <b>post:</b> el motor y la nave no se modificaron (el motor sigue en PreparandoSalto) y se registro en la bitacora un evento de tipo "Error" con la transicion rechazada
	 * @param motor - motor warp sobre el cual se pidio la accion. motor != null
	 */
	@Override
	public void cerrar(MotorWarp motor) {
		this.nave.getAsistente().escribeBitacora("No permitida transicion a estado: " + nomEstados[0],"Error");
	}
	/**
	 * Intenta preparar un salto. No es una accion valida en PreparandoSalto, ya que la preparacion ya fue iniciada
	 * <b>pre:</b> motor != null <br>
	 * <b>post:</b> el motor y la nave no se modificaron (el motor sigue en PreparandoSalto) y se registro en la bitacora un evento de tipo "Error" con la transicion rechazada
	 * @param motor - motor warp sobre el cual se pidio la accion. motor != null
	 */
	@Override
	public void prepararSalto(MotorWarp motor) {
		this.nave.getAsistente().escribeBitacora("No permitida transicion a estado: " + nomEstados[1],"Error");
	}
	/**
	 * Concreta el salto (transicion valida desde PreparandoSalto)
	 * <b>pre:</b> motor != null y el estado actual del motor es PreparandoSalto <br>
	 * <b>post:</b> el motor paso al estado EnWarp, con nombre de estado "EnWarp", y se registro en la bitacora un evento de tipo "Transicion"
	 * @param motor - motor warp que cambiara de estado. motor != null
	 */
	@Override
	public void saltar(MotorWarp motor) {
		motor.setEstado(new EnWarp(this.nave));
		motor.setNombreEstado(nomEstados[2]);
		this.nave.getAsistente().escribeBitacora("transicion a estado: " + nomEstados[2],"Transicion");
	}
	/**
	 * Intenta enfriar el motor. No es una accion valida en PreparandoSalto, ya que solo se enfria un motor que realizo un salto
	 * <b>pre:</b> motor != null <br>
	 * <b>post:</b> el motor y la nave no se modificaron (el motor sigue en PreparandoSalto); la accion se rechaza y debe quedar registrada como error (no se aceptan transiciones invalidas silenciosas)
	 * @param motor - motor warp sobre el cual se pidio la accion. motor != null
	 */
	@Override
	public void enfriar(MotorWarp motor) {
		this.nave.getAsistente().escribeBitacora("No permitida transicion a estado: " + nomEstados[3],"Error");
	}

	/**
	 * Se construye el estado PreparandoSalto asociado a una nave, tomando de ella el asistente que registra los eventos
	 * <b>pre:</b> nave != null y nave.getAsistente() != null (la nave ya tiene su asistente asignado) <br>
	 * <b>post:</b> se ha creado el estado con una referencia a la nave y a su asistente de comando
	 * @param nave - nave a la que pertenece el motor warp. nave != null
	 */
	public PreparandoSalto(Nave nave) {
		super();
		this.nave = nave;
	}
}
