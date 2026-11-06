package Motor;

import Naves.Nave;

import static Motor.MotorWarp.DESGASTE_ENFRIAMIENTO;

/**
 * Cuarto estado del motor warp (patron State): el motor se enfria luego de un salto warp.
 * Dura un turno: cualquier pedido (saltar o saltarWarp) termina el enfriamiento, suma el desgaste del enfriamiento y devuelve el motor a Disponible (Enfriamiento -&gt; Disponible), sin que la nave avance.
 * Las transiciones que no salen de este estado no tienen efecto sobre la nave y quedan registradas como error en la bitacora.
 * <b>invariantes:</b> nave != null
 */
public class Enfriamiento implements EstadoMotor {

    private Nave nave;

    /**
     * Se construye el estado Enfriamiento asociado a una nave
     * <b>pre:</b> nave != null <br>
     * <b>post:</b> se ha creado el estado con una referencia a la nave
     * @param nave - nave a la que pertenece el motor warp. nave != null
     */
    public Enfriamiento(Nave nave) {
        super();
        assert nave != null : "el estado necesita una nave";
        this.nave = nave;
    }

    /**
     * Termina el enfriamiento (transicion valida desde Enfriamiento): suma el desgaste del enfriamiento y vuelve a Disponible
     * <b>pre:</b> motor != null y el estado actual del motor es Enfriamiento <br>
     * <b>post:</b> se sumaron DESGASTE_ENFRIAMIENTO puntos de desgaste (si la nave los admite), el motor paso a Disponible y se registro en la bitacora
     * @param motor - motor warp que cambiara de estado. motor != null
     */
    @Override
    public void cerrar(MotorWarp motor) {
        if (nave.aumentarDesgaste(DESGASTE_ENFRIAMIENTO))
            this.nave.getAsistente().escribeBitacora("Enfriamiento: +" + DESGASTE_ENFRIAMIENTO + " de desgaste (total " + nave.getDesgaste() + ")","Evento");
        else
            this.nave.getAsistente().escribeBitacora("Enfriamiento: el desgaste de " + DESGASTE_ENFRIAMIENTO + " supera el maximo (tiene " + nave.getDesgaste() + ")","Error");
        motor.setEstado(new Disponible(this.nave));
        motor.setNombreEstado(nomEstados[0]);
        this.nave.getAsistente().escribeBitacora("transicion a estado: " + nomEstados[0],"Transicion");
    }

    /**
     * Intenta preparar un salto. No es una accion valida en Enfriamiento, ya que antes el motor debe volver a Disponible
     * <b>pre:</b> motor != null <br>
     * <b>post:</b> el motor y la nave no se modificaron y se registro en la bitacora un evento de tipo "Error" con la transicion rechazada
     * @param motor - motor warp sobre el cual se pidio la accion. motor != null
     */
    @Override
    public void prepararSalto(MotorWarp motor) {
        this.nave.getAsistente().escribeBitacora("No permitida transicion a estado: " + nomEstados[1], "Error");
    }

    /**
     * Intenta pasar a EnWarp. No es una accion valida en Enfriamiento
     * <b>pre:</b> motor != null <br>
     * <b>post:</b> el motor y la nave no se modificaron y se registro en la bitacora un evento de tipo "Error" con la transicion rechazada
     * @param motor - motor warp sobre el cual se pidio la accion. motor != null
     */
    @Override
    public void saltarWarp(MotorWarp motor) {
        this.nave.getAsistente().escribeBitacora("No permitida transicion a estado: " + nomEstados[2], "Error");
    }

    /**
     * Intenta enfriar el motor. No es una accion valida en Enfriamiento, ya que el motor ya se esta enfriando
     * <b>pre:</b> motor != null <br>
     * <b>post:</b> el motor y la nave no se modificaron y se registro en la bitacora un evento de tipo "Error" con la transicion rechazada
     * @param motor - motor warp sobre el cual se pidio la accion. motor != null
     */
    @Override
    public void enfriar(MotorWarp motor) {
        this.nave.getAsistente().escribeBitacora("No permitida transicion a estado: " + nomEstados[3], "Error");
    }

    /**
     * Turno de enfriamiento ante un pedido de salto warp: termina el enfriamiento sin avanzar
     * <b>pre:</b> motor != null y el estado actual del motor es Enfriamiento <br>
     * <b>post:</b> se sumo el desgaste del enfriamiento, el motor paso a Disponible y la nave no avanzo
     * @param motor - motor warp que recibio el pedido. motor != null
     * @return 0: en el turno de enfriamiento la nave no avanza
     */
    @Override
    public int pedirSaltoWarp(MotorWarp motor) {
        assert nave.getAsistente() != null : "la nave no tiene asistente asignado";
        this.cerrar(motor);
        return 0;
    }

    /**
     * Turno de enfriamiento ante un pedido de salto normal: termina el enfriamiento sin avanzar
     * <b>pre:</b> motor != null y el estado actual del motor es Enfriamiento <br>
     * <b>post:</b> se sumo el desgaste del enfriamiento, el motor paso a Disponible y la nave no avanzo
     * @param motor - motor warp que recibio el pedido. motor != null
     * @return 0: en el turno de enfriamiento la nave no avanza
     */
    @Override
    public int pedirSaltoNormal(MotorWarp motor) {
        assert nave.getAsistente() != null : "la nave no tiene asistente asignado";
        this.cerrar(motor);
        return 0;
    }
}
