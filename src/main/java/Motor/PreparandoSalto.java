package Motor;

import Naves.Nave;

import static Motor.MotorWarp.ENERGIA_SALTO_WARP;
import static Motor.MotorWarp.TURNOS_PREPARACION;

/**
 * Segundo estado del motor warp (patron State): el motor se esta preparando para el salto warp.
 * La preparacion lleva TURNOS_PREPARACION turnos. En cada turno evalua si la nave tiene la energia del salto: si no la tiene, la maniobra no avanza y el turno no cuenta.
 * Al completar la preparacion pasa a EnWarp. Un pedido de salto normal cancela la maniobra: vuelve a Disponible y hace el salto normal.
 * Las transiciones que no salen de este estado no tienen efecto sobre la nave y quedan registradas como error en la bitacora.
 * <b>invariantes:</b> nave != null <br>
 * 0 &lt;= turnosCumplidos &lt; TURNOS_PREPARACION
 */
public class PreparandoSalto implements EstadoMotor {
	private Nave nave;
	private int turnosCumplidos;
	/**
	 * Intenta cerrar el motor. No es una accion valida en PreparandoSalto, ya que solo se cierra el enfriamiento
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
	 * Concreta la preparacion (transicion valida desde PreparandoSalto)
	 * <b>pre:</b> motor != null y el estado actual del motor es PreparandoSalto <br>
	 * <b>post:</b> el motor paso al estado EnWarp y se registro en la bitacora un evento de tipo "Transicion"
	 * @param motor - motor warp que cambiara de estado. motor != null
	 */
	@Override
	public void saltarWarp(MotorWarp motor) {
		motor.setEstado(new EnWarp(this.nave));
		motor.setNombreEstado(nomEstados[2]);
		this.nave.getAsistente().escribeBitacora("transicion a estado: " + nomEstados[2],"Transicion");
	}
	/**
	 * Intenta enfriar el motor. No es una accion valida en PreparandoSalto, ya que solo se enfria un motor que realizo un salto
	 * <b>pre:</b> motor != null <br>
	 * <b>post:</b> el motor y la nave no se modificaron (el motor sigue en PreparandoSalto) y se registro en la bitacora un evento de tipo "Error" con la transicion rechazada
	 * @param motor - motor warp sobre el cual se pidio la accion. motor != null
	 */
	@Override
	public void enfriar(MotorWarp motor) {
		this.nave.getAsistente().escribeBitacora("No permitida transicion a estado: " + nomEstados[3],"Error");
	}

	/**
	 * Un turno de preparacion: evalua la energia y, si alcanza, cuenta el turno. Al completar TURNOS_PREPARACION turnos pasa a EnWarp
	 * <b>pre:</b> motor != null y el estado actual del motor es PreparandoSalto <br>
	 * <b>post:</b> sin energia suficiente el turno no conto y el motor sigue en PreparandoSalto; con energia se sumo un turno y, si se completaron los turnos, el motor paso a EnWarp. Se registro en la bitacora
	 * @param motor - motor warp que recibio el pedido. motor != null
	 * @return 0: durante la preparacion la nave no avanza
	 */
	@Override
	public int pedirSaltoWarp(MotorWarp motor) {
		assert nave.getAsistente() != null : "la nave no tiene asistente asignado";
		if (nave.getEnergia() < ENERGIA_SALTO_WARP) {
			this.nave.getAsistente().escribeBitacora("Preparacion detenida: energia insuficiente (requiere " + ENERGIA_SALTO_WARP + ", tiene " + nave.getEnergia() + "). El turno no cuenta","Error");
			return 0;
		}
		turnosCumplidos++;
		this.nave.getAsistente().escribeBitacora("Preparando salto warp: turno " + turnosCumplidos + " de " + TURNOS_PREPARACION,"Evento");
		if (turnosCumplidos == TURNOS_PREPARACION) //no se cumple por ej. llamando a saltar warp desde disponible!!
			this.saltarWarp(motor);
		return 0;
	}

	/**
	 * Cancela la maniobra warp: vuelve a Disponible y le pasa el pedido, que hace el salto normal
	 * <b>pre:</b> motor != null y el estado actual del motor es PreparandoSalto <br>
	 * <b>post:</b> el motor paso a Disponible, se registro la cancelacion y se devolvio lo que devolvio el salto normal
	 * @param motor - motor warp que recibio el pedido. motor != null
	 * @return casilleros que avanzo la nave (0 o 1)
	 */
	@Override
	public int pedirSaltoNormal(MotorWarp motor) {
		assert nave.getAsistente() != null : "la nave no tiene asistente asignado";
		this.nave.getAsistente().escribeBitacora("Maniobra warp cancelada: se hace un salto normal","Evento");
		motor.setEstado(new Disponible(nave));
		motor.setNombreEstado(nomEstados[0]);
		this.nave.getAsistente().escribeBitacora("transicion a estado: " + nomEstados[0],"Transicion");
		return motor.saltar();
	}

	/**
	 * Se construye el estado PreparandoSalto asociado a una nave, indicando cuantos turnos de preparacion ya estan cumplidos
	 * <b>pre:</b> nave != null; 0 &lt;= turnosCumplidos &lt; TURNOS_PREPARACION (0 al venir de Disponible, 1 al volver desde EnWarp) <br>
	 * <b>post:</b> se ha creado el estado con una referencia a la nave y los turnos cumplidos indicados
	 * @param nave - nave a la que pertenece el motor warp. nave != null
	 * @param turnosCumplidos - turnos de preparacion ya cumplidos. 0 &lt;= turnosCumplidos &lt; TURNOS_PREPARACION
	 */
	public PreparandoSalto(Nave nave, int turnosCumplidos) {
		super();
		assert nave != null : "el estado necesita una nave";
		assert turnosCumplidos >= 0 && turnosCumplidos < TURNOS_PREPARACION : "turnos cumplidos fuera de rango";
		this.nave = nave;
		this.turnosCumplidos = turnosCumplidos;
	}
}
