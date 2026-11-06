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
            throws NaveNoOperativaException, CombustibleInsuficienteException, DesgasteCriticoException, MotorNoDisponibleException {
        prepararMision(asistente);
        if (asistente.getNave().enEstadoOperativo()) {
            ejecutarMision(asistente);
            evaluarMision(asistente);
            finalizarMision(asistente);
        }
    }

    public void prepararMision(AsistenteDeComando asistente)
            throws NaveNoOperativaException, CombustibleInsuficienteException, DesgasteCriticoException, MotorNoDisponibleException {
        System.out.println("Iniciando preparación de M-0" + this.id + ": " + this.descripcion);
        asistente.escribeBitacora("Iniciando preparación de M-0" + this.id + ": " + this.descripcion, "EVENTO");

        asistente.verificarRecursos(this.combustible, this.desgaste);
        asistente.verificarMotorDisponible();

        if (this.destino == null) {
            this.destino = asistente.obtenerObjetivoTablero();
        }

        this.rutaCalculada = asistente.obtenerRutaLargoAlcance(this.destino);
    }

    public void ejecutarMision(AsistenteDeComando asistente) {
        asistente.ejecutarSaltoYCostos(this.combustible, this.desgaste);
        asistente.escribeBitacora("Despegue ejecutado. Costo: " + this.combustible + " combustible, " + this.desgaste + " desgaste.", "EVENTO");

        if (this.rutaCalculada != null) {
            for (Posicion coordActual : this.rutaCalculada) {
                if (!asistente.getNave().enEstadoOperativo()) {
                    break;
                }

                asistente.moverNaveA(coordActual);
                asistente.interactuarConElementoEn(coordActual, this);

                if (!asistente.getNave().enEstadoOperativo()) {
                    asistente.escribeBitacora("Misión fallida: La nave quedó destruida o inoperativa en ("
                            + coordActual.getX() + "," + coordActual.getY() + ")", "ERROR");
                    this.exito = false;
                    return;
                }

                asistente.escanearCortoAlcance();
                asistente.moverElementosTablero();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        if (asistente.getNave().enEstadoOperativo()) {
            this.exito = verificarYEjecutarAccionDestino(asistente);
        }
    }

    public void evaluarMision(AsistenteDeComando asistente) {
        String resultado = this.exito ? "ÉXITO" : "FRACASO";
        String balance = String.format(
                "Informe Final M-0%d (%s) [%s]\n- Combustible consumido: %d\n- Desgaste base: %d\n- Energía ganada: %d\n- Energía perdida: %d\n- Gemas recolectadas: %d",
                this.id, this.descripcion, resultado, this.combustible, this.desgaste, this.energiaGanada, this.energiaPerdida, this.gemasRecolectadas
        );
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
    public void registrarEnergiaGanada(int cantidad) { this.energiaGanada += cantidad; }
    public void registrarGemasRecolectadas(int cantidad) { this.gemasRecolectadas += cantidad; }
}