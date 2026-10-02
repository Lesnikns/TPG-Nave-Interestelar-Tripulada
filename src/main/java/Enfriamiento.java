/**
 * Cuarto estado del motor warp (patron State): el motor se enfria luego de un salto.
 * Debe existir aunque todavia no se use (R3 de la aclaracion del TP); su comportamiento se define en la entrega 2.
 * Su unica salida prevista es "pasa un tiempo" -&gt; Disponible, que no corresponde a ninguna de las cuatro acciones de EstadoMotor y todavia no se modela.
 * Por eso, cuando el estado entre en uso, cerrar, preparar, saltar y enfriar seran transiciones invalidas: no tendran efecto sobre la nave y quedaran registradas como error en la bitacora.
 * <b>invariantes:</b> pendientes hasta la entrega 2 (el estado necesitara la nave y su asistente de comando para registrar eventos, como el resto de los estados)
 */
public class Enfriamiento implements EstadoMotor{
    /**
     * Intenta cerrar el motor. No es una accion valida en Enfriamiento
     * <b>pre:</b> motor != null <br>
     * <b>post:</b> en la entrega 1 la accion no tiene efecto sobre el motor ni sobre la nave; a partir de la entrega 2 se rechazara la accion y se registrara el error de transicion
     * @param motor - motor warp sobre el cual se pidio la accion. motor != null
     */
    @Override
    public void cerrar(MotorWarp motor) {
    }
    /**
     * Intenta preparar un salto. No es una accion valida en Enfriamiento, ya que antes el motor debe volver a Disponible
     * <b>pre:</b> motor != null <br>
     * <b>post:</b> en la entrega 1 la accion no tiene efecto sobre el motor ni sobre la nave; a partir de la entrega 2 se rechazara la accion y se registrara el error de transicion
     * @param motor - motor warp sobre el cual se pidio la accion. motor != null
     */
    @Override
    public void prepararSalto(MotorWarp motor) {
        asistente.escribeBitacora("No permitida transicion a estado: " + nomEstados[2],"Error");
    }
    /**
     * Intenta saltar. No es una accion valida en Enfriamiento
     * <b>pre:</b> motor != null <br>
     * <b>post:</b> en la entrega 1 la accion no tiene efecto sobre el motor ni sobre la nave; a partir de la entrega 2 se rechazara la accion y se registrara el error de transicion
     * @param motor - motor warp sobre el cual se pidio la accion. motor != null
     */
    @Override
    public void saltar(MotorWarp motor) {
        asistente.escribeBitacora("No permitida transicion a estado: " + nomEstados[2],"Error");
    }
    /**
     * Intenta enfriar el motor. No es una accion valida en Enfriamiento, ya que el motor ya se esta enfriando
     * <b>pre:</b> motor != null <br>
     * <b>post:</b> en la entrega 1 la accion no tiene efecto sobre el motor ni sobre la nave; a partir de la entrega 2 se rechazara la accion y se registrara el error de transicion
     * @param motor - motor warp sobre el cual se pidio la accion. motor != null
     */
    @Override
    public void enfriar(MotorWarp motor) {
        asistente.escribeBitacora("No permitida transicion a estado: " + nomEstados[3],"Error");
    }
}
