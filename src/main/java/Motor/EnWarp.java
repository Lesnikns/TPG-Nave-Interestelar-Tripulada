package Motor;

import Naves.Nave;

import static Motor.MotorWarp.CASILLEROS_SALTO_WARP;
import static Motor.MotorWarp.ENERGIA_SALTO_WARP;

/**
 * Tercer estado del motor warp (patron State, "Salto warp"): la preparacion termino y el motor esta listo para materializar el salto.
 * Ante un pedido de salto warp, si la nave aun tiene la energia, la consume, avanza dos casilleros y pasa a Enfriamiento (EnWarp -&gt; Enfriamiento).
 * Si ya no tiene la energia, el salto se aborta y vuelve a PreparandoSalto, que retoma en el turno 2.
 * Un pedido de salto normal pasa por PreparandoSalto, que cancela la maniobra y salta: asi toda cancelacion del warp pasa por la preparacion.
 * Las transiciones que no salen de este estado no tienen efecto sobre la nave y quedan registradas como error en la bitacora.
 * <b>invariantes:</b> nave != null <br>
 */
public class EnWarp implements EstadoMotor {
	private Nave nave;

	/**
	 * Intenta cerrar el motor. No es una accion valida en EnWarp: despues del salto el motor pasa por Enfriamiento
	 * <b>pre:</b> motor != null <br>
	 * <b>post:</b> el motor y la nave no se modificaron (el motor sigue en EnWarp) y se registro en la bitacora un evento de tipo "Error" con la transicion rechazada
	 * @param motor - motor warp sobre el cual se pidio la accion. motor != null
	 */
	@Override
	public void cerrar(MotorWarp motor) {
		this.nave.getAsistente().escribeBitacora("No permitida transicion a estado: " + nomEstados[0],"Error");
	}
	/**
	 * Intenta preparar un salto. No es una accion valida en EnWarp, ya que la preparacion ya termino
	 * <b>pre:</b> motor != null <br>
	 * <b>post:</b> el motor y la nave no se modificaron (el motor sigue en EnWarp) y se registro en la bitacora un evento de tipo "Error" con la transicion rechazada
	 * @param motor - motor warp sobre el cual se pidio la accion. motor != null
	 */
	@Override
	public void prepararSalto(MotorWarp motor) {
		this.nave.getAsistente().escribeBitacora("No permitida transicion a estado: " + nomEstados[1],"Error");
	}
	/**
	 * Intenta pasar a EnWarp. No es una accion valida en EnWarp, ya que el motor ya esta en ese estado
	 * <b>pre:</b> motor != null <br>
	 * <b>post:</b> el motor y la nave no se modificaron (el motor sigue en EnWarp) y se registro en la bitacora un evento de tipo "Error" con la transicion rechazada
	 * @param motor - motor warp sobre el cual se pidio la accion. motor != null
	 */
	@Override
	public void saltarWarp(MotorWarp motor) {
		this.nave.getAsistente().escribeBitacora("No permitida transicion a estado: " + nomEstados[2],"Error");
	}
	/**
	 * Enfria el motor luego del salto (transicion valida desde EnWarp)
	 * <b>pre:</b> motor != null y el estado actual del motor es EnWarp <br>
	 * <b>post:</b> el motor paso al estado Enfriamiento y se registro en la bitacora un evento de tipo "Transicion"
	 * @param motor - motor warp que cambiara de estado. motor != null
	 */
	@Override
	public void enfriar(MotorWarp motor) {
		motor.setEstado(new Enfriamiento(this.nave));
		motor.setNombreEstado(nomEstados[3]);
		this.nave.getAsistente().escribeBitacora("transicion a estado: " + nomEstados[3],"Transicion");
	}

	/**
	 * Materializa el salto warp: si la nave aun tiene la energia, la consume, avanza dos casilleros y pasa a Enfriamiento.
	 * Si ya no la tiene, el salto se aborta y el motor vuelve a PreparandoSalto con el turno 1 cumplido (retoma en el turno 2)
	 * <b>pre:</b> motor != null y el estado actual del motor es EnWarp <br>
	 * <b>post:</b> con energia: se descontaron ENERGIA_SALTO_WARP unidades, el motor paso a Enfriamiento y se devolvio 2. Sin energia: la nave no cambio, el motor paso a PreparandoSalto y se devolvio 0. Se registro en la bitacora
	 * @param motor - motor warp que recibio el pedido. motor != null
	 * @return casilleros que avanzo la nave (0 o 2)
	 */
	@Override
	public int pedirSaltoWarp(MotorWarp motor) {
		assert nave.getAsistente() != null : "la nave no tiene asistente asignado";
		if (nave.consumirEnergia(ENERGIA_SALTO_WARP)) {
			this.nave.getAsistente().escribeBitacora("Salto warp: -" + ENERGIA_SALTO_WARP + " de energia (quedan " + nave.getEnergia() + "), avanza " + CASILLEROS_SALTO_WARP + " casilleros","Evento");
			this.enfriar(motor);
			return CASILLEROS_SALTO_WARP;
		}
		this.nave.getAsistente().escribeBitacora("Salto warp abortado: energia insuficiente (requiere " + ENERGIA_SALTO_WARP + ", tiene " + nave.getEnergia() + "). Vuelve a la preparacion","Error");
		motor.setEstado(new PreparandoSalto(nave, 1));
		motor.setNombreEstado(nomEstados[1]);
		this.nave.getAsistente().escribeBitacora("transicion a estado: " + nomEstados[1],"Transicion");
		return 0;
	}

	/**
	 * Un salto normal durante la maniobra: vuelve a PreparandoSalto y le pasa el pedido, que cancela la maniobra y salta
	 * <b>pre:</b> motor != null y el estado actual del motor es EnWarp <br>
	 * <b>post:</b> el motor termino en Disponible (pasando por PreparandoSalto) y se devolvio lo que devolvio el salto normal
	 * @param motor - motor warp que recibio el pedido. motor != null
	 * @return casilleros que avanzo la nave (0 o 1)
	 */
	@Override
	public int pedirSaltoNormal(MotorWarp motor) {
		assert nave.getAsistente() != null : "la nave no tiene asistente asignado";
		motor.setEstado(new PreparandoSalto(nave, 1));
		motor.setNombreEstado(nomEstados[1]);
		this.nave.getAsistente().escribeBitacora("transicion a estado: " + nomEstados[1],"Transicion");
		return motor.saltar();
	}

	/**
	 * Se construye el estado EnWarp asociado a una nave
	 * <b>pre:</b> nave != null <br>
	 * <b>post:</b> se ha creado el estado con una referencia a la nave
	 * @param nave - nave a la que pertenece el motor warp. nave != null
	 */
	public EnWarp(Nave nave) {
		super();
		assert nave != null : "el estado necesita una nave";
		this.nave = nave;
	}
}
