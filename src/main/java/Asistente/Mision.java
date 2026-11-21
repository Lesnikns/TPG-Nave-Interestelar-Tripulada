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

    // Recursos al momento del despegue (para el informe)
    protected int combustibleInicial;
    protected int energiaInicial;
    protected int desgasteInicial;

    // Contadores para el informe
    protected int combustibleGanado;   // para poder separar lo cargado de lo consumido
    protected int combustiblePerdido;
    protected int energiaPerdida;
    protected int energiaGanada;
    protected int gemasRecolectadas;
    protected int transferido;

    // Informe generado en la fase de evaluación.
    private Informe informeFinal;
    private String motivoFinalizacion;

    protected static final int MAX_TURNOS_SIN_AVANZAR = 10;

    private enum EstadoResultado {
        PENDIENTE,
        PREPARANDO,
        EJECUTANDO,
        EXITOSA,
        FALLIDA,
        ABORTADA
    }
    private EstadoResultado estadoResultado = EstadoResultado.PENDIENTE;

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
            throws NaveNoOperativaException, CombustibleInsuficienteException, EnergiaInsuficienteException, DesgasteCriticoException, MotorNoDisponibleException {

        reiniciarRegistroMision();
        // Registrar recursos ANTES del despegue.
        this.combustibleInicial = asistente.getCombustible();
        this.energiaInicial = asistente.getEnergia();
        this.desgasteInicial = asistente.getDesgaste();
        boolean ejecucionIniciada = false;

        try {
            // Fase 1: preparación.
            this.estadoResultado = EstadoResultado.PREPARANDO;
            prepararMision(asistente);
            if (!asistente.naveEstaOperativa()) {
                throw new NaveNoOperativaException("La nave no está operativa para iniciar la misión.");
            }

            // Fase 2: ejecución.
            ejecucionIniciada = true;
            this.estadoResultado = EstadoResultado.EJECUTANDO;
            ejecutarMision(asistente);

            if (this.exito) {
                this.estadoResultado = EstadoResultado.EXITOSA;
            } else {
                this.estadoResultado = EstadoResultado.FALLIDA;
                if (this.motivoFinalizacion == null) {
                    this.motivoFinalizacion = "No se cumplieron las condiciones necesarias para completar la misión.";
                }
            }
        } catch (NaveNoOperativaException | CombustibleInsuficienteException | EnergiaInsuficienteException | DesgasteCriticoException | MotorNoDisponibleException e) {
            this.exito = false;
            this.estadoResultado = ejecucionIniciada ? EstadoResultado.FALLIDA : EstadoResultado.ABORTADA;
            this.motivoFinalizacion = e.getMessage();
            throw e;
        } catch (RuntimeException e) {
            this.exito = false;
            this.estadoResultado = ejecucionIniciada ? EstadoResultado.FALLIDA : EstadoResultado.ABORTADA;
            this.motivoFinalizacion = "Error durante la misión: " + e.getMessage();
            throw e;
        } finally {
            // Fase 3: evaluación.
            evaluarMision(asistente);
            // Fase 4: cierre.
            finalizarMision(asistente);
        }
    }

    protected void prepararMision(AsistenteDeComando asistente)
            throws NaveNoOperativaException, CombustibleInsuficienteException, EnergiaInsuficienteException, DesgasteCriticoException, MotorNoDisponibleException {
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
        asistente.despegar(combustible, energia, desgaste);

        // Si los costos de despegue destruyeron la nave, la misión fracasa inmediatamente.
        if (asistente.naveEstaDestruida()) {
            this.exito = false;
            this.motivoFinalizacion = "La nave fue destruida al aplicar los costos de despegue.";
            asistente.escribeBitacora("Misión fallida: la nave fue destruida durante el despegue.", "ERROR");
            return;
        }

        int i = 0;                  // próxima posición de la ruta a alcanzar
        int turnosSinAvanzar = 0;

        while (!asistente.naveEstaDestruida()) {

            // 1. Inspeccionar la posición actual de la nave.
            Posicion actual = asistente.getPosicionNave();
            asistente.interactuarConElementoEn(actual, this);

            // 2. Comprobar si la interacción destruyó la nave.
            if (asistente.naveEstaDestruida()) {
                this.exito = false;
                this.motivoFinalizacion = "La nave fue destruida durante la interacción.";
                asistente.escribeBitacora("Misión fallida: la nave fue destruida durante la interacción.", "ERROR");
                return;
            }

            // 3. Comprobar si se alcanzó el destino.
            if (actual.getX() == destino.getX() && actual.getY() == destino.getY()) {
                break;
            }

            // 4. Comprobar que todavía haya posiciones por recorrer.
            if (rutaCalculada == null || i >= rutaCalculada.size()) {
                this.exito = false;
                this.motivoFinalizacion = "Se agotó la ruta sin alcanzar el destino.";
                asistente.escribeBitacora("Misión fallida: se agotó la ruta sin alcanzar el destino.", "ERROR");
                return;
            }

            // 5. Identificar el siguiente casillero.
            Posicion siguiente = rutaCalculada.get(i);
            boolean hayLugarParaWarp = i + 1 < rutaCalculada.size();
            int avanzados;

            // 6. Planificar y ejecutar el movimiento.
            try {
                // Regla de los 10 turnos: si supera el bloqueo, fuerza salto normal
                if (turnosSinAvanzar >= MAX_TURNOS_SIN_AVANZAR) {
                    avanzados = asistente.saltarNormal();
                } else if (hayLugarParaWarp && asistente.hayObstaculoAdelante(siguiente)) {
                    avanzados = asistente.saltarWarp();
                } else {
                    avanzados = asistente.saltarNormal();
                }
            } catch (CombustibleInsuficienteException e) {
                this.exito = false;
                this.motivoFinalizacion = e.getMessage();
                asistente.escribeBitacora("Misión fallida: " + e.getMessage(), "ERROR");
                return;
            }

            // 7. Actualizar la posición si hubo movimiento.
            if (avanzados > 0) {
                int restantes = rutaCalculada.size() - i;
                if (avanzados > restantes) {
                    this.exito = false;
                    this.motivoFinalizacion = "El motor avanzó más posiciones que las disponibles en la ruta.";
                    asistente.escribeBitacora("Misión fallida: el motor avanzó más posiciones que las disponibles en la ruta.", "ERROR");
                    return;
                }

                i += avanzados;
                asistente.moverNaveA(rutaCalculada.get(i - 1));

                // Comprobar si el movimiento destruyó la nave.
                if (asistente.naveEstaDestruida()) {
                    this.exito = false;
                    this.motivoFinalizacion = "La nave fue destruida durante el movimiento.";
                    asistente.escribeBitacora("Misión fallida: la nave fue destruida durante el movimiento.", "ERROR");
                    return;
                }
                turnosSinAvanzar = 0;
            } else {
                turnosSinAvanzar++;
            }

            // 8. Fase final del turno: escaneo y movimiento de tablero.
            asistente.cerrarTurno();

            // 9. Breve retardo para visualizar el avance.
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                this.exito = false;
                this.motivoFinalizacion = "La ejecución fue interrumpida durante la espera entre turnos.";
                asistente.escribeBitacora("Misión interrumpida durante la espera entre turnos.", "ERROR");
                return;
            }
        }

        if (!asistente.naveEstaDestruida()) {
            Posicion posicionFinal = asistente.getPosicionNave();
            boolean llegoAlDestino = posicionFinal.getX() == destino.getX() && posicionFinal.getY() == destino.getY();
            if (!llegoAlDestino) {
                this.exito = false;
                this.motivoFinalizacion = "La nave no alcanzó las coordenadas de destino.";
                asistente.escribeBitacora("Misión fallida: la posición final no coincide con el destino.", "ERROR");
            } else {
                this.exito = verificarYEjecutarAccionDestino(asistente);
                if (!this.exito && this.motivoFinalizacion == null) {
                    this.motivoFinalizacion = "No se pudieron cumplir las condiciones requeridas en el destino.";
                }
            }
        }
    }

    protected void evaluarMision(AsistenteDeComando asistente) {
        String resultado;
        String causa;

        if (this.exito) {
            resultado = Informe.EXITOSA;
            causa = "";
        } else if (this.estadoResultado == EstadoResultado.ABORTADA) {
            resultado = Informe.ABORTADA;
            causa = this.motivoFinalizacion != null ? this.motivoFinalizacion : "La misión fue abortada durante la preparación.";
        } else {
            resultado = Informe.FALLIDA;
            causa = this.motivoFinalizacion != null ? this.motivoFinalizacion : "La misión no pudo completarse.";
        }

        // Crear la fotografía final del estado de la nave.
        this.informeFinal = new Informe(this, asistente.getNave(), resultado, causa,
                this.combustibleInicial, this.energiaInicial, this.desgasteInicial);

        String texto = this.informeFinal.toString();
        String detallesExtra = obtenerDetalles(); // hook para lo de gemas y energia
        if (detallesExtra != null && !detallesExtra.isEmpty()) {
            texto += "\n" + detallesExtra;
        }

        asistente.escribeBitacora(texto, this.exito ? "EVALUACION_EXITO" : "EVALUACION_FRACASO");
        asistente.publicarInforme(informeFinal);
        System.out.println(texto);
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

    private void reiniciarRegistroMision() {
        this.exito = false;
        this.combustibleInicial = 0;
        this.energiaInicial = 0;
        this.desgasteInicial = 0;
        this.combustibleGanado = 0;
        this.combustiblePerdido = 0;
        this.energiaGanada = 0;
        this.energiaPerdida = 0;
        this.gemasRecolectadas = 0;
        this.transferido = 0;
        this.rutaCalculada = null;
        this.motivoFinalizacion = null;
        this.estadoResultado = EstadoResultado.PENDIENTE;
        this.informeFinal = null;
    }

    public void registrarEnergiaPerdida(int cantidad) {
        if (cantidad > 0) {
            this.energiaPerdida += cantidad;
        }
    }
    public void registrarEnergiaGanada(int cantidad) {
        if (cantidad > 0) {
            this.energiaGanada += cantidad;
        }
    }
    public void registrarCombustibleGanado(int cantidad) {
        if (cantidad > 0) {
            this.combustibleGanado += cantidad;
        }
    }
    public void registrarCombustiblePerdido(int cantidad) {
        if (cantidad > 0) {
            this.combustiblePerdido += cantidad;
        }
    }
    public void registrarGemasRecolectadas(int cantidad) {
        if (cantidad > 0) {
            this.gemasRecolectadas += cantidad;
        }
    }
    public void registrarTransferido(int cantidad) {
        if (cantidad > 0) {
            this.transferido += cantidad;
        }
    }

    // Para consultar desde el informe
    public int getCombustibleGanado() { return combustibleGanado; }
    public int getCombustiblePerdido() { return combustiblePerdido; }
    public int getEnergiaGanada() { return energiaGanada; }
    public int getEnergiaPerdida() { return energiaPerdida; }
    public int getGemasRecolectadas() { return gemasRecolectadas; }
    public int getTransferido() { return transferido; }
    public Informe getInformeFinal() { return informeFinal; }
}