public abstract class Mision {
    private int id;
    private String descripcion;
    private String destino;
    protected int energia; // Actúa como bonificación
    protected int combustible; // Costo operativo
    protected int desgaste; // Costo operativo

    // Getters
    public int getId() {
        return id;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public String getDestino() {
        return destino;
    }
    public int getCombustible() {
        return combustible;
    }
    public int getEnergia() {
        return energia;
    }
    public int getDesgaste() {
        return desgaste;
    }

    // Setters
    public void setDestino(String destino) {
        this.destino = destino;
    }
    public void setCombustible(int combustible) {
        this.combustible = combustible;
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
        this.energia = 0; // Por defecto no da bonificacion
    }

    /**
     * Template Method que define el ciclo de vida inalterable de una mision.
     * <b>pre:</b> asistente != null y tiene una nave asignada <br>
     * <b>post:</b> se ejecuto estrictamente la secuencia: preparar, ejecutar, consecuencias y finalizar. La bitacora y los recursos reflejan la operacion
     *
     * @param asistente - coordinador a traves del cual la mision operara. asistente != null
     * @throws Exception si la mision es abortada por falta de recursos o disponibilidad del motor
     */
    public final void iniciarMision(AsistenteDeComando asistente) throws Exception {
        prepararMision(asistente);
        ejecutarMision(asistente);
        aplicarConsecuencias(asistente);
        finalizarMision(asistente);
    }

    /**
     * Valida que la nave este en condiciones operativas antes de actuar.
     * <b>pre:</b> asistente != null <br>
     * <b>post:</b> si la validacion de recursos y motor es exitosa, la mision es autorizada. Si falla, se lanza una excepcion, el estado de la nave permanece intacto y se aborta la ejecucion
     *
     * @param asistente - coordinador encargado de validar a la nave. asistente != null
     * @throws Exception si la nave no esta operativa, recursos son insuficientes o el motor no se encuentra "Disponible"
     */
    public void prepararMision(AsistenteDeComando asistente) throws Exception {
        System.out.println("Iniciando mision " + this.descripcion);
        asistente.escribeBitacora("Iniciando mision M-0" + this.id + ": " + this.descripcion, "EVENTO");

        asistente.verificarRecursos(this.combustible, this.desgaste);

        if (!asistente.verificarMotorDisponible()) {
            throw new Exception("Motor no disponible para salto warp.");
        }
    }

    /**
     * Ejecuta las acciones especificas de la mision concreta (implementado en subclases).
     * <b>pre:</b> la mision supero con exito las validaciones de prepararMision. asistente != null <br>
     * <b>post:</b> se le ordeno al asistente realizar el salto y cobrar los costos operativos (combustible y desgaste). Se imprimieron los reportes correspondientes
     *
     * @param asistente - coordinador que ordenara el salto a la nave. asistente != null
     */
    public abstract void ejecutarMision(AsistenteDeComando asistente);

    /**
     * Otorga las bonificaciones correspondientes al cumplimiento de la mision.
     * <b>pre:</b> la etapa ejecutarMision concluyo de manera exitosa. asistente != null <br>
     * <b>post:</b> se le ordeno al asistente recargar la energia de la nave si la mision lo estipula, dejando registro en la bitacora
     *
     * @param asistente - coordinador encargado de recargar la energia. asistente != null
     */
    public abstract void aplicarConsecuencias(AsistenteDeComando asistente);

    /**
     * Da por concluida formalmente la operacion y asienta el cierre en el registro.
     * <b>pre:</b> las etapas previas finalizaron correctamente sin excepciones. asistente != null <br>
     * <b>post:</b> la mision emitio su informe de finalizacion exitosa a traves de la consola y lo anexo como entrada definitiva en la bitacora
     *
     * @param asistente - coordinador donde se registrara el evento de cierre. asistente != null
     */
    private void finalizarMision(AsistenteDeComando asistente) {
        asistente.escribeBitacora("Mision M-0" + this.id + " finalizada con exito", "EVENTO");
        System.out.println("Misión finalizada.");
    }
}