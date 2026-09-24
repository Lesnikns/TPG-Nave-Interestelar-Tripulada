import java.util.Date;

public abstract class Mision {
    private int id;
    private String descripcion;
    private String destino;
    protected int energia;
    protected int combustible;
    protected int desgaste;

    // Getters y setters
    public int getCombustible() {
        return combustible;
    }

    public int getId() {
        return id;
    }

    public int getEnergia() {
        return energia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getDestino() {
        return destino;
    }

    public void setCombustible(int combustible) {
        this.combustible = combustible;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public void setEnergia(int energia) {
        this.energia = energia;
    }

    public void setDesgaste(int desgaste) {
        this.desgaste = desgaste;
    }

    public Mision(int id, String descripcion, String destino) {
        this.id = id;
        this.descripcion = descripcion;
        this.destino = destino;
        this.desgaste = 4;
        this.combustible = 4;
        this.energia = 0;
    }

    public final void IniciarMision(AsistenteDeComando asistente) throws Exception {
        prepararMision(asistente);
        ejecutarMision(asistente);
        aplicarConsecuencias(asistente);
        finalizarMision(asistente);

    }

    public abstract void aplicarConsecuencias(AsistenteDeComando asistente);

    public void prepararMision(AsistenteDeComando asistente) throws Exception {
        MotorWarp motor = asistente.getNave().getMotor();

        System.out.println("Iniciando mision " + this.descripcion);
        asistente.escribeBitacora("Iniciando mision M-0:" + this.id + ": " + this.descripcion, "EVENTO");

        if (!motor.disponibleParaSaltar()) { // boleana que comprueba estado del motod
            asistente.escribeBitacora("No se pudo ejecutar la mision porque el motor no estaba disponible para saltar", "ERROR");
            throw new Exception("Motor no disponible para salto");
        }
        motor.pedirPrepararSalto(); // prepara el estado del motor para realizar el salto ya que se comprobo que estaba disponible
        motor.aplicarCostos(combustible, desgaste, energia); // el motor produce desgaste y consume combustible
    }

    public abstract void ejecutarMision(AsistenteDeComando asistente);

    private void finalizarMision(AsistenteDeComando asistente) {
        asistente.escribeBitacora("Mision M-0:" + this.id + " finalizada con exito", "EVENTO");
        System.out.println("Misión finalizada.");
    }

}