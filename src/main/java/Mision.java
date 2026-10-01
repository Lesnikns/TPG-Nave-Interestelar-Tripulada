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
     * Template Method que define el ciclo de vida inalterable de una misión.
     *
     * PRECONDICIÓN: El asistente proporcionado no es nulo y tiene una nave asignada.
     *
     * POSTCONDICIÓN: Se ejecutó estrictamente la secuencia: preparar, ejecutar,
     * consecuencias y finalizar. La bitácora y los recursos reflejan la operación.
     *
     * @param asistente El coordinador a través del cual la misión operará.
     * @throws Exception Si la misión es abortada por falta de recursos o disponibilidad.
     */
    public final void IniciarMision(AsistenteDeComando asistente) throws Exception {
        prepararMision(asistente);
        ejecutarMision(asistente);
        aplicarConsecuencias(asistente);
        finalizarMision(asistente);
    }
    /**
     * Valida que la nave esté en condiciones operativas antes de actuar.
     *
     * PRECONDICIÓN: El asistente proporcionado no debe ser nulo.
     *
     * POSTCONDICIÓN: Si la validación de recursos y motor es exitosa, la misión es
     * autorizada. Si alguna verificación falla, se lanza una excepción y el estado
     * de la nave permanece intacto, abortando la ejecución parcial.
     *
     * @param asistente El coordinador encargado de validar a la nave.
     * @throws Exception Si el combustible es insuficiente, se supera el límite de
     * desgaste, o si el motor no se encuentra "Disponible".
     */
    public void prepararMision(AsistenteDeComando asistente) throws Exception {
        System.out.println("Iniciando mision " + this.descripcion);
        asistente.escribeBitacora("Iniciando mision M-0" + this.id + ": " + this.descripcion, "EVENTO");

        // La mision verifica recursos y disponibilidad a traves del asistente
        if (!asistente.verificarRecursos(this.combustible, this.desgaste)) {
            asistente.escribeBitacora("Misión abortada: Recursos insuficientes (combustible o desgaste)", "ERROR");
            throw new Exception("Recursos insuficientes para la misión.");
        }

        if (!asistente.verificarMotorDisponible()) {
            asistente.escribeBitacora("Misión abortada: Motor no disponible", "ERROR");
            throw new Exception("Motor no disponible para salto");
        }
    }
    /**
     * Ejecuta las acciones específicas de la misión concreta (implementado en subclases).
     *
     * PRECONDICIÓN: La misión superó con éxito las validaciones de 'prepararMision'.
     *
     * POSTCONDICIÓN: Se le ordenó al asistente realizar el salto y cobrar los costos
     * operativos (combustible y desgaste). Se imprimieron los reportes correspondientes.
     *
     * @param asistente El coordinador que ordenará el salto a la nave.
     */
    public abstract void ejecutarMision(AsistenteDeComando asistente);

    /**
     * Otorga las bonificaciones correspondientes al cumplimiento de la misión.
     *
     * PRECONDICIÓN: La etapa 'ejecutarMision' concluyó de manera exitosa.
     *
     * POSTCONDICIÓN: Se le ordenó al asistente recargar la energía de la nave si la misión
     * lo estipula, dejando registro en la bitácora.
     *
     * @param asistente El coordinador encargado de recargar la energía.
     */
    public abstract void aplicarConsecuencias(AsistenteDeComando asistente);

    /**
     * Da por concluida formalmente la operación y asienta el cierre en el registro.
     *
     * PRECONDICIÓN: Las etapas previas finalizaron correctamente sin excepciones.
     *
     * POSTCONDICIÓN: La misión emitió su informe de finalización exitosa a través de
     * la consola y lo anexó como entrada definitiva en la bitácora.
     *
     * @param asistente El coordinador donde se registrará el evento de cierre.
     */
    private void finalizarMision(AsistenteDeComando asistente) {
        asistente.escribeBitacora("Mision M-0" + this.id + " finalizada con exito", "EVENTO");
        System.out.println("Misión finalizada.");
    }
}