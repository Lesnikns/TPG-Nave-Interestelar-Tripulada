package Asistente;

import DestinoTablero.Destino;
import DestinoTablero.ElementoDelUniverso;
import DestinoTablero.Posicion;
import DestinoTablero.Tablero;
import Excepciones.*;
import Naves.Nave;
import Tripulacion.Tripulante;

import java.util.ArrayList;
import java.util.List;

public class AsistenteDeComando {
    private final Nave nave;
    private final Bitacora bitacora;
    private Tablero tablero;
    private Destino destino;

    public AsistenteDeComando(Nave nave, Tablero tablero) {
        this.nave = nave;
        this.bitacora = new Bitacora();
        this.tablero = tablero;
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

    public void cargarEnergiaNave(int energia) {
        nave.cargarEnergia(energia);
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

    public List<Posicion> obtenerRutaLargoAlcance(Posicion destinoPos) {
        this.escribeBitacora("Calculando ruta hacia (" + destinoPos.getX() + ", " + destinoPos.getY() + ")", "EVENTO");
        List<Posicion> ruta = new ArrayList<>();
        ruta.add(destinoPos);
        return ruta;
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
}