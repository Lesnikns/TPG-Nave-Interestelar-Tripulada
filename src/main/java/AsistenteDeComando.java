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
     * Verifica si la nave tiene los recursos necesarios para soportar una operacion.
     * <b>pre:</b> combReq y desgasteReq deben ser mayores o iguales a cero. La nave asignada al asistente no debe ser nula <br>
     * <b>post:</b> el estado de la nave y sus recursos quedan inalterados (metodo de consulta)
     *
     * @param combReq Cantidad de combustible requerida.
     * @param desgasteReq Puntos de desgaste que sumara la operacion.
     * @throws Exception si la nave requiere mantenimiento, falta combustible o el desgaste supera el 100%.
     */
    public void verificarRecursos(int combReq, int desgasteReq) throws Exception {
        if (!nave.enEstadoOperativo()) {
            throw new Exception("Nave dañada, requiere mantenimiento previo.");
        }
        if (nave.getCombustible() < combReq) {
            throw new Exception("Combustible insuficiente (requiere " + combReq + ", tiene " + nave.getCombustible() + ").");
        }
        if (nave.getDesgaste() + desgasteReq > 100) {
            throw new Exception("Riesgo crítico: La misión superará el 100% de desgaste permitido.");
        }
    }

    /**
     * Verifica que el motor se encuentre listo para iniciar un salto warp.
     * <b>pre:</b> la nave asignada y su objeto motor no deben ser nulos <br>
     * <b>post:</b> devuelve el estado de disponibilidad del motor sin alterar su estado actual ni producir transiciones
     *
     * @return true si el motor esta en estado "Disponible", false de lo contrario.
     */
    public boolean verificarMotorDisponible() {
        return nave.getMotor().disponibleParaSaltar();
    }

    /**
     * Ejecuta las transiciones de estado del motor y cobra los recursos del viaje.
     * <b>pre:</b> el motor debe estar en estado "Disponible". La nave debe contar con el combustible suficiente y soportar el desgaste <br>
     * <b>post:</b> el motor paso a "Preparando salto" y luego a "Salto warp". El combustible y el desgaste de la nave fueron actualizados
     *
     * @param combustible Unidades a descontar.
     * @param desgaste Puntos de desgaste a sumar.
     */
    public void ejecutarSaltoYCostos(int combustible, int desgaste) {
        nave.getMotor().pedirPrepararSalto();
        nave.consumirCombustible(combustible);
        nave.aumentarDesgaste(desgaste);
        nave.getMotor().pedirWarp();
    }

    /**
     * Delega el inicio de la mision solicitada, actuando como coordinador y manejando posibles fallos.
     * <b>pre:</b> la mision proporcionada (m) no es nula. El asistente cuenta con una nave y una bitacora inicializadas y validas <br>
     * <b>post:</b> se invoco el ciclo de la mision. Si ocurre una excepcion por falta de recursos o indisponibilidad del motor, el error es atrapado y gestionado internamente
     *
     * @param m Objeto mision que se desea ejecutar.
     */
    public void coordinarMision(Mision m) {
        try {
            m.iniciarMision(this);
            nave.getMotor().pedirDisponibilidad();

        } catch (Exception e) {
            System.out.println("El asistente informa: La misión fue abortada. Causa: " + e.getMessage());
            this.escribeBitacora("Misión abortada - " + e.getMessage(), "ERROR");
        }
    }

    /**
     * Inscribe un nuevo evento en el registro historico del asistente.
     * <b>pre:</b> ni 'mensaje' ni 'tipo' deben ser nulos o estar vacios. La bitacora interna debe estar instanciada <br>
     * <b>post:</b> se anadio exitosamente una nueva entrada inmutable a la bitacora. El orden cronologico del historial se mantuvo inalterado
     *
     * @param mensaje Descripcion del evento ocurrido.
     * @param tipo Categoria del evento (ej. "EVENTO", "ERROR").
     */
    public void escribeBitacora(String mensaje, String tipo) {
        Entrada e = new Entrada(mensaje, tipo);
        bitacora.cargaEntrada(e);
    }

    /**
     * Delega la recarga de energia a la nave, utilizandose para aplicar bonificaciones.
     * <b>pre:</b> el valor de energia proporcionado debe ser mayor o igual a cero <br>
     * <b>post:</b> la energia actual de la nave se incremento. La nave se autolimita internamente para no superar su capacidad maxima
     *
     * @param energia Unidades de energia a recargar en la nave.
     */
    public void cargarEnergiaNave(int energia) {
        nave.cargarEnergia(energia);
    }
}