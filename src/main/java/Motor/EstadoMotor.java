package Motor;

/**
 * Contrato del patron State para el motor warp: define lo que el motor delega en su estado actual.
 * Tiene dos tipos de metodos:
 * <ul>
 * <li>Los dos pedidos del Anexo (pedirSaltoNormal y pedirSaltoWarp), que llama MotorWarp desde saltar() y saltarWarp(). Cada llamada es un turno y devuelve cuantos casilleros avanzo la nave.</li>
 * <li>Las cuatro transiciones de la Entrega 1 (prepararSalto, saltarWarp, enfriar y cerrar), que cada estado usa por dentro al responder un pedido.</li>
 * </ul>
 * Cada estado concreto (Disponible, PreparandoSalto, EnWarp, Enfriamiento) decide que hace con cada pedido y que transicion usa, de modo que ni MotorWarp ni la mision necesitan condicionales por estado.
 * <br>
 * Transiciones validas:
 * <ul>
 * <li>Disponible -&gt; PreparandoSalto (prepararSalto)</li>
 * <li>PreparandoSalto -&gt; EnWarp (saltarWarp), al completar los turnos de preparacion con energia suficiente</li>
 * <li>EnWarp -&gt; Enfriamiento (enfriar), despues del salto</li>
 * <li>Enfriamiento -&gt; Disponible (cerrar), al turno siguiente, sumando el desgaste del enfriamiento</li>
 * <li>PreparandoSalto -&gt; Disponible, cuando un salto normal cancela la maniobra</li>
 * <li>EnWarp -&gt; PreparandoSalto, cuando falta energia para el salto o un salto normal cancela la maniobra</li>
 * </ul>
 * Una transicion no permitida en el estado actual no tiene efecto sobre la nave y queda registrada como error en la bitacora.
 * <b>invariantes:</b> el nombre de estado del motor coincide siempre con el estado concreto que tiene asignado <br>
 * en todo momento el motor tiene exactamente uno de los cuatro estados
 */
public interface EstadoMotor {
	String[] nomEstados = {"Disponible","Preparando salto","Salto warp","Enfriamiento"};
	/**
	 * Termina el enfriamiento, llevando al motor de Enfriamiento a Disponible
	 * <b>pre:</b> motor != null; para el caso idoneo, el estado actual es Enfriamiento <br>
	 * <b>post:</b> en el caso idoneo se sumo el desgaste del enfriamiento, el motor paso a Disponible y se registro la transicion; en cualquier otro estado el motor y la nave no cambiaron y se registro el error de transicion
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
	 * Concreta la preparacion, llevando al motor de PreparandoSalto a EnWarp
	 * <b>pre:</b> motor != null; para el caso idoneo, el estado actual es PreparandoSalto <br>
	 * <b>post:</b> en el caso idoneo el motor paso a EnWarp (estado y nombre de estado coherentes) y se registro la transicion; en cualquier otro estado el motor y la nave no cambiaron y se registro el error de transicion
	 * @param motor - motor warp sobre el cual se pidio la accion. motor != null
	 */
	void saltarWarp(MotorWarp motor);
	/**
	 * Enfria el motor luego de un salto, llevandolo de EnWarp a Enfriamiento
	 * <b>pre:</b> motor != null; para el caso idoneo, el estado actual es EnWarp <br>
	 * <b>post:</b> en el caso idoneo el motor paso a Enfriamiento y se registro la transicion; en cualquier otro estado el motor y la nave no cambiaron y se registro el error de transicion
	 * @param motor - motor warp sobre el cual se pidio la accion. motor != null
	 */
	void enfriar(MotorWarp motor);
	/**
	 * Responde un pedido de salto warp de la nave para este turno (lo llama MotorWarp.saltarWarp()). Cada pedido avanza un paso de la maniobra
	 * <b>pre:</b> motor != null y la nave del motor tiene asistente asignado <br>
	 * <b>post:</b> el motor quedo en el estado que corresponde, se registro lo ocurrido en la bitacora y se devolvio 0 o 2
	 * @param motor - motor warp que recibio el pedido. motor != null
	 * @return casilleros que avanzo la nave en este turno (0 o 2)
	 */
	int pedirSaltoWarp(MotorWarp motor);
	/**
	 * Responde un pedido de salto normal de la nave para este turno (lo llama MotorWarp.saltar())
	 * <b>pre:</b> motor != null y la nave del motor tiene asistente asignado <br>
	 * <b>post:</b> el motor quedo en el estado que corresponde, se registro lo ocurrido en la bitacora y se devolvio 0 o 1
	 * @param motor - motor warp que recibio el pedido. motor != null
	 * @return casilleros que avanzo la nave en este turno (0 o 1)
	 */
	int pedirSaltoNormal(MotorWarp motor);
}
