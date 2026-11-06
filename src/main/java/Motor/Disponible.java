package Motor;

import Naves.Nave;

import static Motor.MotorWarp.CASILLEROS_SALTO_NORMAL;
import static Motor.MotorWarp.COMBUSTIBLE_SALTO_NORMAL;

/**
 * Estado inicial del motor warp (patron State): el motor esta libre y listo para saltar.
 * Ante un pedido de salto normal, la nave avanza un casillero consumiendo combustible. Ante un pedido de salto warp, empieza la preparacion (Disponible -&gt; PreparandoSalto).
 * Las transiciones que no salen de este estado no tienen efecto sobre la nave y quedan registradas como error en la bitacora a traves del asistente de comando.
 * <b>invariantes:</b> nave != null <br>
 */
public class Disponible implements EstadoMotor {
	private Nave nave;

	/**
	 * Intenta cerrar el motor. No es una accion valida en Disponible, ya que solo se cierra el enfriamiento
	 * <b>pre:</b> motor != null <br>
	 * <b>post:</b> el motor y la nave no se modificaron (el motor sigue en Disponible) y se registro en la bitacora un evento de tipo "Error" con la transicion rechazada
	 * @param motor - motor warp sobre el cual se pidio la accion. motor != null
	 */
	@Override
	public void cerrar(MotorWarp motor) {
		this.nave.getAsistente().escribeBitacora("No permitida transicion a estado: " + nomEstados[0],"Error");
	}
	/**
	 * Inicia la preparacion de un salto (transicion valida desde Disponible)
	 * <b>pre:</b> motor != null y el estado actual del motor es Disponible <br>
	 * <b>post:</b> el motor paso al estado PreparandoSalto, sin turnos de preparacion cumplidos, y se registro en la bitacora un evento de tipo "Transicion"
	 * @param motor - motor warp que cambiara de estado. motor != null
	 */
	@Override
	public void prepararSalto(MotorWarp motor) {
		motor.setEstado(new PreparandoSalto(this.nave,0));
		motor.setNombreEstado(nomEstados[1]);
		this.nave.getAsistente().escribeBitacora("transicion a estado: " + nomEstados[1],"Transicion");
	}
	/**
	 * Intenta pasar a EnWarp. No es una accion valida en Disponible, ya que antes hay que preparar el salto
	 * <b>pre:</b> motor != null <br>
	 * <b>post:</b> el motor y la nave no se modificaron (el motor sigue en Disponible) y se registro en la bitacora un evento de tipo "Error" con la transicion rechazada
	 * @param motor - motor warp sobre el cual se pidio la accion. motor != null
	 */
	@Override
	public void saltarWarp(MotorWarp motor) {
		this.nave.getAsistente().escribeBitacora("No permitida transicion a estado: " + nomEstados[2],"Error");
	}
	/**
	 * Intenta enfriar el motor. No es una accion valida en Disponible, ya que solo se enfria un motor que realizo un salto
	 * <b>pre:</b> motor != null <br>
	 * <b>post:</b> el motor y la nave no se modificaron (el motor sigue en Disponible) y se registro en la bitacora un evento de tipo "Error" con la transicion rechazada
	 * @param motor - motor warp sobre el cual se pidio la accion. motor != null
	 */
	@Override
	public void enfriar(MotorWarp motor) {
		this.nave.getAsistente().escribeBitacora("No permitida transicion a estado: " + nomEstados[3],"Error");
	}

	/**
	 * Empieza la maniobra warp: pasa a PreparandoSalto y le pasa el pedido, para que evalue la energia y cuente el primer turno de preparacion
	 * <b>pre:</b> motor != null y el estado actual del motor es Disponible <br>
	 * <b>post:</b> el motor quedo en PreparandoSalto (con el turno 1 cumplido si habia energia) y la nave no avanzo
	 * @param motor - motor warp que recibio el pedido. motor != null
	 * @return 0: durante la preparacion la nave no avanza
	 */
	@Override
	public int pedirSaltoWarp(MotorWarp motor) {
		assert nave.getAsistente() != null : "la nave no tiene asistente asignado";
		this.prepararSalto(motor);
		return motor.saltarWarp(); //se continua proceso a saltar warp, ahora en preparacion
	}

	/**
	 * Hace un salto normal: la nave consume combustible y avanza un casillero. Si no le alcanza el combustible, no avanza
	 * <b>pre:</b> motor != null y el estado actual del motor es Disponible <br>
	 * <b>post:</b> si alcanzaba el combustible, se descontaron COMBUSTIBLE_SALTO_NORMAL unidades y se devolvio 1; si no, la nave no cambio y se devolvio 0. En los dos casos se registro en la bitacora
	 * @param motor - motor warp que recibio el pedido. motor != null
	 * @return casilleros que avanzo la nave (0 o 1)
	 */
	@Override
	public int pedirSaltoNormal(MotorWarp motor) {
		assert nave.getAsistente() != null : "la nave no tiene asistente asignado";
		if (nave.consumirCombustible(COMBUSTIBLE_SALTO_NORMAL)) {
			this.nave.getAsistente().escribeBitacora("Salto normal: -" + COMBUSTIBLE_SALTO_NORMAL + " de combustible (quedan " + nave.getCombustible() + "), avanza " + CASILLEROS_SALTO_NORMAL + " casilleros","Evento");
			return CASILLEROS_SALTO_NORMAL;
		}
		this.nave.getAsistente().escribeBitacora("Salto normal rechazado: combustible insuficiente (requiere " + COMBUSTIBLE_SALTO_NORMAL + ", tiene " + nave.getCombustible() + ")","Error");
		return 0;
	}

	/**
	 * Se construye el estado Disponible asociado a una nave
	 * <b>pre:</b> nave != null <br>
	 * <b>post:</b> se ha creado el estado con una referencia a la nave
	 * @param nave - nave a la que pertenece el motor warp. nave != null
	 */
	public Disponible(Nave nave) {
		super();
		assert nave != null : "el estado necesita una nave";
		this.nave = nave;
	}
}
