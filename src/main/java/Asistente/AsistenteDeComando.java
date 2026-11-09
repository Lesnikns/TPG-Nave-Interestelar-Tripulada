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

    public void coordinarMision(Mision m) {
        try {
            m.iniciarMision(this);
        } catch (NaveNoOperativaException | CombustibleInsuficienteException | DesgasteCriticoException e) {
            System.out.println("El asistente informa: La misión fue abortada. Causa: " + e.getMessage());
            this.escribeBitacora("Misión abortada - " + e.getMessage(), "ERROR");
        } catch (Exception e) {
            System.out.println("Error crítico inesperado en el sistema: " + e.getMessage());
            this.escribeBitacora("Error crítico inesperado: " + e.getMessage(), "ERROR");
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
    public int saltarNormal() { return nave.getMotor().saltar(); }
    public int saltarWarp()   { return nave.getMotor().saltarWarp(); }

    public void cobrarCostoInicial(int combustible, int energia, int desgaste) {
        nave.consumirCombustible(combustible);
        nave.consumirEnergia(energia);
        nave.aumentarDesgaste(desgaste);
    }
    public void verificarRecursos(int combReq, int energiaReq, int desgasteReq)
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
        if (nave.getEnergia() < energiaReq){
            //throw new EnergiaInsuficienteException("Energia insuficiente (requiere " + energiaReq + ", tiene " + nave.getEnergia() + ").");
        }
    }
}