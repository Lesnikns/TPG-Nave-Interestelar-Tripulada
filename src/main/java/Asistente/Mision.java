package Asistente;

import DestinoTablero.Posicion;
import java.util.List;
import Excepciones.*;

public abstract class Mision {
    private int id;
    private String descripcion;
    protected Posicion destino;
    protected int energia;
    protected int combustible;
    protected int desgaste;
    protected int combustibleInicial;
    protected int energiaInicial;
    protected int desgasteInicial;
    protected int combustibleGanado;   // para poder separar lo cargado de lo consumido

    protected boolean exito;
    protected List<Posicion> rutaCalculada;
    protected int energiaPerdida;
    protected int energiaGanada;
    protected int gemasRecolectadas;

    public Mision(int id, String descripcion, Posicion destino) {
        this.id = id;
        this.descripcion = descripcion;
        this.destino = destino;
        this.desgaste = 4;
        this.combustible = 4;
        this.energia = 0;
        this.exito = false;
        this.energiaPerdida = 0;
        this.energiaGanada = 0;
        this.gemasRecolectadas = 0;
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
            throws NaveNoOperativaException, CombustibleInsuficienteException, EnergiaInsuficienteException, DesgasteCriticoException, MotorNoDisponibleException {
        prepararMision(asistente);
        if (asistente.getNave().enEstadoOperativo()) {
            ejecutarMision(asistente);
            evaluarMision(asistente);
            finalizarMision(asistente);
        }
    }

    protected static final int MAX_TURNOS_SIN_AVANZAR = 10;

    protected void prepararMision(AsistenteDeComando asistente) throws NaveNoOperativaException, CombustibleInsuficienteException, EnergiaInsuficienteException, DesgasteCriticoException, MotorNoDisponibleException {
        System.out.println("Iniciando preparación de M-0" + this.id + ": " + this.descripcion);
        asistente.escribeBitacora("Iniciando preparación de M-0" + this.id + ": " + this.descripcion, "EVENTO");

        // Valida recursos iniciales de la nave sin consultar al motor
        asistente.verificarRecursos(this.combustible, this.energia, this.desgaste);

        if (this.destino == null) {
            this.destino = asistente.obtenerObjetivoTablero();
        }

        this.rutaCalculada = asistente.obtenerRutaLargoAlcance(asistente.getPosicionNave(), destino);
    }

    protected void ejecutarMision(AsistenteDeComando asistente) {
        combustibleInicial = asistente.getCombustible();
        energiaInicial     = asistente.getEnergia();
        desgasteInicial    = asistente.getDesgaste();

        asistente.despegar(combustible, energia, desgaste);

        //lo cambio
        int i = 0;                  // próxima posición de la ruta a alcanzar
        int turnosSinAvanzar = 0;

        while (i < rutaCalculada.size() && !asistente.getNave().estaDestruida()) {
            Posicion siguiente = rutaCalculada.get(i);
            boolean hayLugarParaWarp = i + 1 < rutaCalculada.size(); // al menos 2
            int avanzados;

            try {
                if (turnosSinAvanzar >= MAX_TURNOS_SIN_AVANZAR) {
                    // Intentar salir del atasco mediante un salto normal.
                    avanzados = asistente.saltarNormal();
                } else if (hayLugarParaWarp && asistente.hayObstaculoAdelante(siguiente)) {
                    avanzados = asistente.saltarWarp();
                } else {
                    avanzados = asistente.saltarNormal();
                }
            } catch (CombustibleInsuficienteException e) {
                this.exito = false;
                asistente.escribeBitacora(
                        "Misión fallida: no se pudo realizar el movimiento. " +
                                e.getMessage(),
                        "ERROR"
                );
                return;
            }

            if (avanzados > 0) {

                i += avanzados;

                Posicion llegada = rutaCalculada.get(i - 1);

                asistente.moverNaveA(llegada);

                // Comprobar si el costo del movimiento destruyó la nave.
                if (asistente.getNave().estaDestruida()) {
                    this.exito = false;
                    asistente.escribeBitacora(
                            "Misión fallida: la nave fue destruida durante el movimiento.",
                            "ERROR"
                    );
                    return;
                }
                asistente.interactuarConElementoEn(llegada, this);
                turnosSinAvanzar = 0;
            } else {
                turnosSinAvanzar++;
            }

            // Fase final del turno: movimiento de tablero y breve retardo
            if (!asistente.getNave().estaDestruida()) {
                asistente.cerrarTurno();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            } else {
                asistente.escribeBitacora("Misión fallida: La nave quedó destruida durante el trayecto.", "ERROR");
                this.exito = false;
                return;
            }
        }

        if (!asistente.getNave().estaDestruida()) {
            this.exito = verificarYEjecutarAccionDestino(asistente);
        }
    }

    public void evaluarMision(AsistenteDeComando asistente) {
        int combustibleConsumido = combustibleInicial + combustibleGanado - asistente.getCombustible();
        int energiaConsumida     = energiaInicial + energiaGanada - energiaPerdida - asistente.getEnergia();
        int desgasteAcumulado    = asistente.getDesgaste() - desgasteInicial;


        String resultado = this.exito ? "ÉXITO" : "FRACASO";
        String balance = String.format(
                "Informe Final M-0%d (%s) [%s]\n- Combustible consumido: %d\n- Energía consumida: %d\n- Desgaste acumulado: %d\n- Energía ganada: %d\n- Energía perdida (impactos): %d\n- Gemas recolectadas: %d",
                id, descripcion, resultado, combustibleConsumido, energiaConsumida,
                desgasteAcumulado, energiaGanada, energiaPerdida, gemasRecolectadas);
        asistente.escribeBitacora(balance, this.exito ? "EVALUACION_EXITO" : "EVALUACION_FRACASO");
        System.out.println(balance);
    }

    public void finalizarMision(AsistenteDeComando asistente) {
        if (this.exito) {
            aplicarCierreDefinitivo(asistente);
            asistente.escribeBitacora("Cierre M-0" + this.id + ": Recursos aplicados definitivamente a la nave.", "EVENTO");
        } else {
            asistente.escribeBitacora("Cierre M-0" + this.id + ": Misión fallida, no se aplican beneficios.", "EVENTO");
        }
        System.out.println("Misión M-0" + this.id + " concluida.");
    }

    protected abstract boolean verificarYEjecutarAccionDestino(AsistenteDeComando asistente);
    protected abstract void aplicarCierreDefinitivo(AsistenteDeComando asistente);

    public void registrarEnergiaPerdida(int cantidad) { this.energiaPerdida += cantidad; }
    public void registrarCombustibleGanado(int cantidad) { this.combustibleGanado += cantidad; }
    public void registrarEnergiaGanada(int cantidad) { this.energiaGanada += cantidad; }
    public void registrarGemasRecolectadas(int cantidad) { this.gemasRecolectadas += cantidad; }
}