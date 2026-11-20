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

    public void verificarRecursos(int combReq, int desgasteReq, int energiaReq)
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
            throw new EnergiaInsuficienteException("Energia insuficiente (requiere " + energiaReq + ", tiene " + nave.getEnergia() + ").");
        }
    }

    public void cobrarCostoInicial(int combustible, int energia, int desgaste) {
        nave.consumirCombustible(combustible);
        nave.consumirEnergia(energia);
        nave.aumentarDesgaste(desgaste);
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
        if (this.tablero != null && nuevaPosicion != null) {
            this.tablero.moverNave(nuevaPosicion);
            this.escribeBitacora("Nave desplazada a: (" + nuevaPosicion.getX() + ", " + nuevaPosicion.getY() + ")", "EVENTO");
        }
    }

    public void interactuarConElementoEn(Posicion pos, Mision misionActual) {
        if (this.tablero != null) {
            ElementoDelUniverso elemento = this.tablero.inspeccionarPosicion();

            if (elemento != null) {
                this.escribeBitacora("Elemento detectado en casilla (" + pos.getX() + ", " + pos.getY() + "). Interactuando...", "EVENTO");
                elemento.interactuar(this.nave, misionActual, this);
            }
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

    public void agregarGemasANave(int cantidad) {
        if (this.nave != null) {
            this.nave.agregarGemas(cantidad); // Invocación directa a la nave
            this.escribeBitacora("Inventario actualizado: +" + cantidad + " gemas en bodega.", "EVENTO");
        }
    }

    public boolean hayObstaculoEn(Posicion pos) {
        if (this.tablero != null && pos != null) {
            ElementoDelUniverso[][] mapa = this.tablero.verTablero();
            int px = pos.getX();
            int py = pos.getY();

            if (px >= 0 && px < this.tablero.getX() && py >= 0 && py < this.tablero.getY()) {
                return mapa[px][py] != null;
            }
        }
        return false;
    }

    public Posicion buscarVecinoLibre(Posicion posActual) {
        if (this.tablero == null || posActual == null) {
            return null;
        }

        ElementoDelUniverso[][] mapa = this.tablero.verTablero();
        int filas = this.tablero.getX();
        int columnas = this.tablero.getY();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx != 0 || dy != 0) {
                    int nx = posActual.getX() + dx;
                    int ny = posActual.getY() + dy;

                    if (nx >= 0 && nx < filas && ny >= 0 && ny < columnas) {
                        if (mapa[nx][ny] == null) {
                            return new Posicion(nx, ny);
                        }
                    }
                }
            }
        }
        return null;
    }
}