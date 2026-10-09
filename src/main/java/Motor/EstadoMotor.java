package Motor;

/**
 * Contrato del patron State para el motor warp: define las acciones que el motor delega en su estado actual.
 * Cada estado concreto (Disponible, PreparandoSalto, EnWarp, Enfriamiento) decide que transiciones son validas, de modo que ni MotorWarp ni Naves.Nave necesitan condicionales por estado (R3 de la aclaracion del TP, E1-02).
 * <br>
 * Transiciones validas:
 * <ul>
 * <li>Disponible -&gt; PreparandoSalto (preparar)</li>
 * <li>PreparandoSalto -&gt; EnWarp (saltar)</li>
 * <li>EnWarp -&gt; Disponible (cerrar). Provisorio: como todavia no se modela el paso del tiempo, una nave en salto warp vuelve a Disponible cuando termina el salto</li>
 * <li>EnWarp -&gt;Enfriamiento (enfriar). Se define en la entrega 2</li>
 * <li>Enfriamiento -&gt; Disponible (pasa un tiempo). Se define en la entrega 2</li>
 * </ul>
 * Una accion no permitida en el estado actual no debe tener efecto sobre la nave, y no puede rechazarse en silencio: debe quedar registrada como error en la bitacora.
 * <b>invariantes:</b> el nombre de estado del motor coincide siempre con el estado concreto que tiene asignado <br>
 * en todo momento el motor tiene exactamente uno de los cuatro estados
 */
public interface EstadoMotor {
	String[] nomEstados = {"Disponible","Preparando salto ","Salto warp","Enfriamiento"};
	/**
	 * Cierra el salto en curso, llevando al motor de EnWarp a Disponible (fin del ciclo de salto)
	 * <b>pre:</b> motor != null; para el caso idoneo, el estado actual es EnWarp <br>
	 * <b>post:</b> en el caso idoneo el motor paso a Disponible (estado y nombre de estado coherentes) y se registro la transicion; en cualquier otro estado el motor y la nave no cambiaron y se registro el error de transicion
	 * @param motor - motor warp sobre el cual se pidio la accion. motor != null
	 */
	void cerrar(MotorWarp motor);
	/**
	 * Inicia la preparacion de un salto, llevando al motor de Disponible a PreparandoSalto
	 * <b>pre:</b> motor != null; para el caso idoneo, el estado actual es Disponible <br>
	 * <b>post:</b> en el caso idoneo el motor paso a PreparandoSalto (estado y nombre de estado coherentes) y se registro la transicion; en cualquier otro estado el motor y la nave no cambiaron y se registro el error de transicion
	 * @param motor - motor warp sobre el cual se pidio la accion. motor != null
	 */
	void prepararSalto(MotorWarp motor);
	/**
	 * Concreta el salto, llevando al motor de PreparandoSalto a EnWarp
	 * <b>pre:</b> motor != null; para el caso idoneo, el estado actual es PreparandoSalto <br>
	 * <b>post:</b> en el caso idoneo el motor paso a EnWarp (estado y nombre de estado coherentes) y se registro la transicion; en cualquier otro estado el motor y la nave no cambiaron y se registro el error de transicion
	 * @param motor - motor warp sobre el cual se pidio la accion. motor != null
	 */
	void saltar(MotorWarp motor);
	/**
	 * Enfria el motor luego de un salto, llevandolo de EnWarp a Enfriamiento (su comportamiento se define en la entrega 2, el estado Enfriamiento existe pero todavia no se usa)
	 * <b>pre:</b> motor != null <br>
	 * <b>post:</b> en la entrega 1 la accion no tiene efecto sobre el motor ni sobre la nave; a partir de la entrega 2, en el caso idoneo el motor pasara a Enfriamiento y en cualquier otro estado se registrara el error de transicion
	 * @param motor - motor warp sobre el cual se pidio la accion. motor != null
	 */
	void enfriar(MotorWarp motor); //metodo enfriar se definira en la entrega 2
}
