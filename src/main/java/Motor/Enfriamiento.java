package Motor;

import Naves.Nave;

/**
 * Cuarto estado del motor warp (patron State): el motor se enfria luego de un salto.
 * Debe existir aunque todavia no se use, su comportamiento se define en la entrega 2.
 * Su unica salida prevista es "pasa un tiempo" -> Disponible, que no corresponde a ninguna de las cuatro acciones de EstadoMotor y todavia no se modela.
 * Por eso, cuando el estado entre en uso, cerrar, preparar, saltar y enfriar seran transiciones invalidas: no tendran efecto sobre la nave y quedaran registradas como error en la bitacora.
 * <b>invariantes:</b> pendientes hasta la entrega 2 (el estado necesitara la nave y su asistente de comando para registrar eventos, como el resto de los estados)
 */
public class Enfriamiento implements EstadoMotor {

    private Nave nave;

    /**
     * Se construye el estado Enfriamiento asociado a una nave, tomando de ella el asistente que registra los eventos
     * <b>pre:</b> nave != null y nave.getAsistente() != null (la nave ya tiene su asistente asignado) <br>
     * <b>post:</b> se ha creado el estado con una referencia a la nave y a su asistente de comando
     * @param nave - nave a la que pertenece el motor warp. nave != null
     */
    public Enfriamiento(Nave nave) {
        super();
        this.nave = nave;
    }

    /**
     * Intenta cerrar el motor. No es una accion valida en Enfriamiento
     * <b>pre:</b> motor != null <br>
     * <b>post:</b> la accion se rechazara y se registrara el error de transicion
     * @param motor - motor warp sobre el cual se pidio la accion. motor != null
     */
    @Override
    public void cerrar(MotorWarp motor) {
        this.nave.getAsistente().escribeBitacora("No permitida transicion a estado: " + nomEstados[0], "Error");
    }

    /**
     * Intenta preparar un salto. No es una accion valida en Enfriamiento, ya que antes el motor debe volver a Disponible
     * <b>pre:</b> motor != null <br>
     * <b>post:</b> la accion se rechazara y se registrara el error de transicion
     * @param motor - motor warp sobre el cual se pidio la accion. motor != null
     */
    @Override
    public void prepararSalto(MotorWarp motor) {
        this.nave.getAsistente().escribeBitacora("No permitida transicion a estado: " + nomEstados[1], "Error");
    }

    /**
     * Intenta saltar. No es una accion valida en Enfriamiento
     * <b>pre:</b> motor != null <br>
     * <b>post:</b> la accion se rechazara y se registrara el error de transicion
     * @param motor - motor warp sobre el cual se pidio la accion. motor != null
     */
    @Override
    public void saltar(MotorWarp motor) {
        this.nave.getAsistente().escribeBitacora("No permitida transicion a estado: " + nomEstados[2], "Error");
    }

    /**
     * Intenta enfriar el motor. No es una accion valida en Enfriamiento, ya que el motor ya se esta enfriando
     * <b>pre:</b> motor != null <br>
     * <b>post:</b> la accion se rechazara y se registrara el error de transicion
     * @param motor - motor warp sobre el cual se pidio la accion. motor != null
     */
    @Override
    public void enfriar(MotorWarp motor) {
        this.nave.getAsistente().escribeBitacora("No permitida transicion a estado: " + nomEstados[3], "Error");
    }
}