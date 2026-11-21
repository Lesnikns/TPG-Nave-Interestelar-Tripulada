package Asistente;

import DestinoTablero.*;
import Excepciones.*;
import Naves.Nave;
import Tripulacion.Tripulante;

import java.util.ArrayList;
import java.util.List;

public class AsistenteDeComando{
    private final Nave nave;
    private final Bitacora bitacora;
    private Tablero tablero;
    private Destino destino;
    private final PublicadorDeTurnos turnos;
    private final HistorialDeInformes historial;

    public AsistenteDeComando(Nave nave, Tablero tablero, PublicadorDeTurnos turnos, HistorialDeInformes historial) {
        this.nave = nave;
        this.turnos = turnos;
        this.bitacora = new Bitacora();
        this.tablero = tablero;
        this.historial = historial;
        if (tablero != null) {
            this.destino = new Destino(tablero);
        }
    }

    public Bitacora getBitacora() { return bitacora; }
    public Nave getNave() { return nave; }
    public Tablero getTablero() { return tablero; }
    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
        if (tablero != null) {
            this.destino = new Destino(tablero);
        }
    }

    public void verificarRecursos(int combReq, int desgasteReq)
            throws NaveNoOperativaException, CombustibleInsuficienteException, DesgasteCriticoException {
        if (!nave.enEstadoOperativo()) {
            throw new NaveNoOperativaException("Nave dañada, requiere mantenimiento previo.");
        }
        if (nave.getCombustible() < combReq) {
            throw new CombustibleInsuficienteException("Combustible insuficiente (requiere " + combReq + ", tiene " + nave.getCombustible() + ").");
        }
        if (nave.getDesgaste() + desgasteReq > 100) {
            throw new DesgasteCriticoException("Riesgo crítico: La misión superará el 100% de desgaste permitido.");
        }
    }

    public void verificarMotorDisponible() throws MotorNoDisponibleException {
        if (!nave.getMotor().disponibleParaSaltar()) {
            throw new MotorNoDisponibleException("Motor no disponible para salto warp.");
        }
    }

    public void ejecutarSaltoYCostos(int combustible, int desgaste) {
        nave.getMotor().pedirPrepararSalto();
        nave.consumirCombustible(combustible);
        nave.aumentarDesgaste(desgaste);
        nave.getMotor().pedirWarp();
    }

    public void coordinarMision(Mision m) {
        try {
            m.iniciarMision(this);
            nave.getMotor().pedirDisponibilidad();
        } catch (NaveNoOperativaException | CombustibleInsuficienteException |
                 DesgasteCriticoException | MotorNoDisponibleException e) {
            System.out.println("El asistente informa: La misión fue abortada. Causa: " + e.getMessage());
            this.escribeBitacora("Misión abortada - " + e.getMessage(), "ERROR");
        } catch (Exception e) {
            System.out.println("Error crítico inesperado en el sistema: " + e.getMessage());
            this.escribeBitacora("Error crítico inesperado: " + e.getMessage(), "ERROR");
        }
    }

    public void escribeBitacora(String mensaje, String tipo) {
        Entrada e = new Entrada(mensaje, tipo);
        bitacora.cargaEntrada(e);
    }

    public boolean cargarEnergiaNave(int energia) {
        return nave.cargarEnergia(energia);
    }

    public void liquidarHaberesMensuales() {
        double total = 0;
        for (Tripulante t : nave.getTripulacion()) {
            double sueldo = t.getSueldo();
            total += sueldo;
            this.escribeBitacora("Haber de " + t.getNombre() + ": $" + String.format("%.2f", sueldo), "LIQUIDACION");
        }
        this.escribeBitacora("Total de haberes de la tripulación: $" + String.format("%.2f", total), "LIQUIDACION");
    }

    public Posicion obtenerObjetivoTablero() {
        return tablero.obtenerObjetivo();
    }

    public void coordinarMision(Mision m) {
        try {
            m.iniciarMision(this);
        } catch (NaveNoOperativaException | CombustibleInsuficienteException | EnergiaInsuficienteException |  DesgasteCriticoException e) {
            System.out.println("El asistente informa: La misión fue abortada. Causa: " + e.getMessage());
            this.escribeBitacora("Misión abortada - " + e.getMessage(), "ERROR");
        } catch (Exception e) {
            System.out.println("Error crítico inesperado en el sistema: " + e.getMessage());
            this.escribeBitacora("Error crítico inesperado: " + e.getMessage(), "ERROR");
        }finally {
            publicarTurno();                   // cubre éxito, fallo, destrucción y abortos
        }
    }

    public void moverNaveA(Posicion nuevaPosicion) {
        tablero.moverNave(nuevaPosicion);
        this.escribeBitacora("Nave desplazada a: (" + nuevaPosicion.getX() + ", " + nuevaPosicion.getY() + ")", "EVENTO");
    }

    public void interactuarConElementoEn(Posicion pos, Mision misionActual) {
        ElementoDelUniverso elemento = tablero.inspeccionarPosicion();
        if (elemento != null) {
            this.escribeBitacora("Elemento detectado en casilla. Iniciando interacción...", "EVENTO");
            elemento.interactuar(this.nave, misionActual, this);
        }
    }

    public ElementoDelUniverso[][] escanearCortoAlcance() {
        return tablero.observarAlrededores();
    }

    public void moverElementosTablero() {
        if (destino != null) {
            destino.turnoDestino();
        }
    }

    public Posicion getPosicionNave() {
        return tablero.getPosicionNave();
    }

    public List<Posicion> obtenerRutaLargoAlcance(Posicion origen, Posicion destino) {
        escribeBitacora("Calculando ruta de (" + origen.getX() + "," + origen.getY() + ") a (" + destino.getX() + "," + destino.getY() + ")", "EVENTO");
        return nave.getRadarLA().calcularRuta(tablero, origen, destino);
    }

    public boolean hayObstaculoAdelante(Posicion siguiente) {
        return nave.getRadarCA().hayObstaculoAdelante(tablero, siguiente);
    }

    //estos 2 hacer que se usen después, con varios otros posibles; cosa de que mision necesite conocer a nave o motor
    public int saltarNormal() throws CombustibleInsuficienteException { return nave.getMotor().saltar(); }
    public int saltarWarp()   { return nave.getMotor().saltarWarp(); }
    public int getCombustible() { return nave.getCombustible(); }
    public int getEnergia() { return nave.getEnergia(); }
    public int getDesgaste() { return nave.getDesgaste(); }
    public void recargarTotalmenteRecursosNave() {
        nave.recargarTotalmenteRecursos();
    }
    public void agregarGemasNave(int cantidad) {
        nave.agregarGemas(cantidad);
    }
    public void entregarCombustibleNave(int cantidad) {
        if (!nave.consumirEnergia(cantidad)) {
            throw new IllegalStateException("No se pudo completar la transferencia de combustible.");
        }
    }
    public void entregarEnergiaNave(int cantidad) {
        if (!nave.consumirEnergia(cantidad)) {
            throw new IllegalStateException("No se pudo completar la transferencia de energía.");
        }
    }
    public int recargarEnergiaNaveConTope(int cantidad) {
        int anterior = nave.getEnergia();
        nave.recargarEnergiaConTope(cantidad);
        return nave.getEnergia() - anterior;
    }
    public int recargarCombustibleNaveConTope(int cantidad) {
        int anterior = nave.getCombustible();
        nave.recargarCombustibleConTope(cantidad);
        return nave.getCombustible() - anterior;
    }
    public boolean naveEstaDestruida() {
        return nave.estaDestruida();
    }
    public boolean naveEstaOperativa() {
        return nave.enEstadoOperativo();
    }
    public void realizarMantenimientoNave() {
        nave.realizarMantenimiento();
    }

    public void cobrarCostoInicial(int combustible, int energia, int desgaste) {

        boolean aplicado = nave.aplicarCostosIniciales(
                combustible,
                energia,
                desgaste
        );

        if (!aplicado) {
            throw new IllegalStateException(
                    "No se pudieron aplicar completamente los costos iniciales de la misión."
            );
        }

        escribeBitacora(
                "Costos iniciales aplicados: " +
                        combustible + " combustible, " +
                        energia + " energía, " +
                        desgaste + " desgaste.",
                "EVENTO"
        );
    }
    public void verificarRecursos(int combReq, int energiaReq, int desgasteReq)
            throws NaveNoOperativaException,
            CombustibleInsuficienteException,
            EnergiaInsuficienteException,
            DesgasteCriticoException {

        // Los costos no pueden ser negativos.
        if (combReq < 0 || energiaReq < 0 || desgasteReq < 0) {
            throw new IllegalArgumentException(
                    "Los costos de una misión no pueden ser negativos."
            );
        }

        // Verificar que la nave pueda iniciar una misión.
        if (!nave.enEstadoOperativo()) {
            throw new NaveNoOperativaException(
                    "Nave no operativa. Requiere mantenimiento previo."
            );
        }

        // Comprobar que puede pagar el combustible inicial.
        if (nave.getCombustible() < combReq) {
            throw new CombustibleInsuficienteException(
                    "Combustible insuficiente. Requiere " + combReq +
                            " y tiene " + nave.getCombustible() + "."
            );
        }

        // Comprobar que puede pagar la energía inicial.
        if (nave.getEnergia() < energiaReq) {
            throw new EnergiaInsuficienteException(
                    "Energía insuficiente. Requiere " + energiaReq +
                            " y tiene " + nave.getEnergia() + "."
            );
        }

        // Comprobar que el costo de desgaste no supere el máximo.
        if (nave.getDesgaste() + desgasteReq > 100) {
            throw new DesgasteCriticoException(
                    "El costo de desgaste supera el máximo permitido."
            );
        }
    }
    /** Saca ambas fotos en el hilo de la misión y avisa. Reemplaza a publicarRecursos(). */
    public void publicarTurno() {
        if (tablero == null) return;
        EstadoRecursos r = new EstadoRecursos(nave.getCombustible(), nave.getEnergia(), nave.getDesgaste(), nave.getInventarioGemas(), nave.estaDestruida(),nave.getEstadoMotor());
        EstadoTablero t = new EstadoTablero(tablero);
        turnos.publicar(new EstadoTurno(t, r));
    }

    public void despegar(int combustible, int energia, int desgaste) {
        cobrarCostoInicial(combustible, energia, desgaste);
        escribeBitacora("Despegue ejecutado. Costos iniciales: " + combustible + " comb, " + energia + " energía, " + desgaste + " desgaste.", "EVENTO");
        publicarTurno();                       // bitácora y después publicar
    }

    public void cerrarTurno() {
        moverElementosTablero();
        publicarTurno();
    }

    //--- ahora para el historial de informes
    void publicarInforme(Informe informe){
        //asistente necesita recibir la referencia al HistorialDeInformes, que debe crearse al iniciar el sistema
        historial.agregarInforme(informe);
    }

}