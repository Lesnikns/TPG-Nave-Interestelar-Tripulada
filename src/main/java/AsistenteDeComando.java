public class AsistenteDeComando {
    private Nave nave;
    private Bitacora bitacora;

    public AsistenteDeComando(Nave nave) {
        this.nave = nave;
        this.bitacora = new Bitacora();
    }

    public Bitacora getBitacora() {
        return bitacora;
    }
    /**
     * Verifica si la nave tiene los recursos necesarios para soportar una operación.
     *
     * PRECONDICIÓN: combReq y desgasteReq deben ser mayores o iguales a cero.
     * La nave asignada al asistente no debe ser nula.
     *
     * POSTCONDICIÓN: El estado de la nave y sus recursos quedan inalterados (metodo de consulta).
     *
     * @param combReq Cantidad de combustible requerida.
     * @param desgasteReq Puntos de desgaste que sumará la operación.
     * @return true si el combustible alcanza y el desgaste final es <= 100; false en caso contrario.
     */
    public boolean verificarRecursos(int combReq, int desgasteReq) {
        boolean tieneCombustible = nave.getCombustible() >= combReq;
        boolean soportaDesgaste = (nave.getDesgaste() + desgasteReq) <= 100;
        return tieneCombustible && soportaDesgaste;
    }
    /**
     * Verifica que el motor se encuentre listo para iniciar un salto warp.
     *
     * PRECONDICIÓN: La nave asignada y su objeto motor no deben ser nulos.
     *
     * POSTCONDICIÓN: Devuelve el estado de disponibilidad del motor sin
     * alterar su estado actual ni producir transiciones.
     *
     * @return true si el motor está en estado "Disponible", false de lo contrario.
     */
    public boolean verificarMotorDisponible() {
        return nave.getMotor().disponibleParaSaltar();
    }
    /**
     * Ejecuta las transiciones de estado del motor y cobra los recursos del viaje.
     *
     * PRECONDICIÓN: El motor debe estar en estado "Disponible". La nave debe contar
     * con el combustible suficiente y soportar el desgaste (verificarRecursos debe ser true).
     *
     * POSTCONDICIÓN: El motor pasó a "Preparando salto" y luego a "Salto warp".
     * El combustible y el desgaste de la nave fueron actualizados.
     *
     * @param combustible Unidades a descontar.
     * @param desgaste Puntos de desgaste a sumar.
     */
    public void ejecutarSaltoYCostos(int combustible, int desgaste) {
        nave.getMotor().pedirPrepararSalto();
        nave.getMotor().aplicarCostos(combustible, desgaste, 0);
        nave.getMotor().pedirWarp();
    }
    /**
     * Carga energía en la nave asignada, respetando sus límites de capacidad.
     *
     * PRECONDICIÓN: La cantidad proporcionada debe ser un valor entero positivo o cero.
     *
     * POSTCONDICIÓN: La energía actual de la nave aumentó en la cantidad indicada. Si el
     * aumento supera la capacidad máxima (100), la energía queda limitada a dicho tope.
     *
     * @param cantidad Unidades de energía a cargar.
     */
    public void cargarEnergiaNave(int cantidad) {
        nave.cargarEnergia(cantidad);
    }
    /**
     * Delega el inicio de la misión solicitada, actuando como coordinador.
     *
     * PRECONDICIÓN: La misión proporcionada (m) no es nula. El asistente cuenta con
     * una nave y una bitácora inicializadas y válidas.
     *
     * POSTCONDICIÓN: Se invocó el ciclo de la misión. El saldo final de recursos y los
     * registros de la bitácora dependerán del éxito o fracaso de la misión ejecutada.
     *
     * @param m Objeto misión que se desea ejecutar.
     * @throws Exception Si ocurre un fallo en los recursos o disponibilidad del motor.
     */
    public void coordinarMision(Mision m) throws Exception {
        m.IniciarMision(this);
    }
    /**
     * Inscribe un nuevo evento en el registro histórico del asistente.
     *
     * PRECONDICIÓN: Ni 'mensaje' ni 'tipo' deben ser nulos o estar vacíos.
     * La bitácora interna debe estar instanciada.
     *
     * POSTCONDICIÓN: Se añadió exitosamente una nueva entrada inmutable a la bitácora.
     * El orden cronológico del historial se mantuvo inalterado.
     *
     * @param mensaje Descripción del evento ocurrido.
     * @param tipo Categoría del evento (ej. "EVENTO", "ERROR").
     */
    public void escribeBitacora(String mensaje, String tipo) {
        Entrada e = new Entrada(mensaje, tipo);
        bitacora.cargaEntrada(e);
    }
}