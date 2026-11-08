package Asistente;

import DestinoTablero.Posicion;

import java.util.Iterator;
import java.util.List;
import Excepciones.*;

public abstract class Mision {
    private int id;
    private String descripcion;
    protected Posicion destino;
    protected int energia;
    protected int combustible;
    protected int desgaste;

    protected boolean exito;
    protected List<Posicion> rutaCalculada;


    public Mision(int id, String descripcion, Posicion destino, int desgaste, int combustible, int energia) {
        this.id = id;
        this.descripcion = descripcion;
        this.destino = destino;
        this.desgaste = desgaste;
        this.combustible = combustible;
        this.energia = energia;
        this.exito = false;
    }

    public int getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public Posicion getDestino() { return destino; }
    public void setDestino(Posicion destino) { this.destino = destino; }
    public int getCombustible() { return combustible; }
    public int getEnergia() { return energia; }
    public int getDesgaste() { return desgaste; }
    public boolean isExito() { return exito; }

    public void setCombustible(int combustible) { this.combustible = combustible; }
    public void setEnergia(int energia) { this.energia = energia; }
    public void setDesgaste(int desgaste) { this.desgaste = desgaste; }

    public final void iniciarMision(AsistenteDeComando asistente)
            throws NaveNoOperativaException, CombustibleInsuficienteException, DesgasteCriticoException, MotorNoDisponibleException {
        prepararMision(asistente);
        if (asistente.getNave().enEstadoOperativo()) {
            ejecutarMision(asistente);
            evaluarMision(asistente);
            finalizarMision(asistente);
        }
    }

    protected void prepararMision(AsistenteDeComando asistente)
            throws NaveNoOperativaException, CombustibleInsuficienteException, DesgasteCriticoException, MotorNoDisponibleException {
        System.out.println("Iniciando preparación de M-0" + this.id + ": " + this.descripcion);
        asistente.escribeBitacora("Iniciando preparación de M-0" + this.id + ": " + this.descripcion, "EVENTO");

        // Valida recursos iniciales de la nave sin consultar al motor
        asistente.verificarRecursos(this.combustible, this.energia, this.desgaste);

        if (this.destino == null) {
            this.destino = asistente.obtenerObjetivoTablero();
        }

        this.rutaCalculada = asistente.obtenerRutaLargoAlcance(this.destino);
    }

    protected void ejecutarMision(AsistenteDeComando asistente) {
        asistente.cobrarCostoInicial(this.combustible, this.energia, this.desgaste);
        asistente.escribeBitacora("Despegue ejecutado. Costos iniciales aplicados: "
                + this.combustible + " comb, " + this.energia + " energía, " + this.desgaste + " desgaste.", "EVENTO");

        if (this.rutaCalculada != null) {
            Iterator<Posicion> iteradorRuta = this.rutaCalculada.iterator();
            int turnosTrabadoWarp = 0;

            while (iteradorRuta.hasNext() && asistente.getNave().enEstadoOperativo()) {
                Posicion siguientePos = iteradorRuta.next();
                boolean hayObstaculoEnRuta = asistente.hayObstaculoEn(siguientePos);
                int pasosAvanzados = 0;

                // Regla de los 10 turnos: Si supera 10 turnos de bloqueo, fuerza salto normal hacia la colisión
                if (turnosTrabadoWarp >= 10) {
                    pasosAvanzados = asistente.getNave().getMotor().saltar();
                } else if (hayObstaculoEnRuta) {
                    Posicion vecinoLibre = asistente.buscarVecinoLibre(siguientePos);

                    if (!asistente.hayObstaculoEn(siguientePos)) {
                        // El camino se liberó: el motor cancela la preparación automáticamente al pedir saltar()
                        pasosAvanzados = asistente.getNave().getMotor().saltar();
                    } else if (vecinoLibre != null) {
                        // Hay un vecino libre: se desvía y ejecuta salto normal
                        asistente.moverNaveA(vecinoLibre);
                        pasosAvanzados = asistente.getNave().getMotor().saltar();
                    } else {
                        // El obstáculo persiste: solicita salto Warp al motor (devuelve 0, 1 o 2)
                        pasosAvanzados = asistente.getNave().getMotor().saltarWarp();
                    }
                } else {
                    // Camino despejado: avance normal
                    pasosAvanzados = asistente.getNave().getMotor().saltar();
                }

                // Control del contador de bloqueo en Warp
                if (pasosAvanzados == 0) {
                    turnosTrabadoWarp++;
                } else {
                    turnosTrabadoWarp = 0;

                    // Si el motor avanzó 1 o 2 posiciones, impacta la nueva ubicación y la interacción
                    asistente.moverNaveA(siguientePos);
                    asistente.interactuarConElementoEn(siguientePos, this);
                }

                // Fase final del turno: escaneo, movimiento de tablero y breve retardo
                if (asistente.getNave().enEstadoOperativo()) {
                    asistente.escanearCortoAlcance();
                    asistente.moverElementosTablero();

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    asistente.escribeBitacora("Misión fallida: La nave quedó inoperativa durante el trayecto.", "ERROR");
                    this.exito = false;
                }
            }
        }

        if (asistente.getNave().enEstadoOperativo()) {
            this.exito = verificarYEjecutarAccionDestino(asistente);
        }
    }

    protected void evaluarMision(AsistenteDeComando asistente) {
        String resultado = this.exito ? "ÉXITO" : "FRACASO";

        StringBuilder balance = new StringBuilder();
        balance.append(String.format("Informe Final M-0%d (%s) [%s]\n", this.id, this.descripcion, resultado));
        balance.append(String.format("- Combustible consumido: %d\n", this.combustible));
        balance.append(String.format("- Desgaste base: %d", this.desgaste));

        String detallesExtra = obtenerDetalles(); // hook para lo de gemas y energia
        if (detallesExtra != null && !detallesExtra.isEmpty()) {
            balance.append("\n").append(detallesExtra);
        }

        String informeFinal = balance.toString();
        asistente.escribeBitacora(informeFinal, this.exito ? "EVALUACION_EXITO" : "EVALUACION_FRACASO");
        System.out.println(informeFinal);
    }

    protected void finalizarMision(AsistenteDeComando asistente) {
        if (this.exito) {
            aplicarCierreDefinitivo(asistente);
            String mensajeDetalle = obtenerMensajeCierre();
            asistente.escribeBitacora("Cierre M-0" + this.id + ": " + mensajeDetalle, "EVENTO");
        } else {
            asistente.escribeBitacora("Cierre M-0" + this.id + ": Misión fallida, no se aplican beneficios.", "EVENTO");
        }
        System.out.println("Misión M-0" + this.id + " concluida.");
    }

    protected abstract boolean verificarYEjecutarAccionDestino(AsistenteDeComando asistente);
    protected abstract void aplicarCierreDefinitivo(AsistenteDeComando asistente);
    protected  String obtenerDetalles(){
        return "";
    }
    protected String obtenerMensajeCierre(){
        return "";
    }

}