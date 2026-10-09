package Naves;

import Asistente.AsistenteDeComando;
import Excepciones.ConfiguracionInicialInvalidaException;
import Motor.MotorWarp;
import Tripulacion.Tripulante;

import java.util.ArrayList;

public abstract class Nave {
    private static final int COMBUSTIBLE_MAX = 100;
    private static final int ENERGIA_MAX = 100;
    private static final int DESGASTE_MAX = 100;
    private static final int DESGASTE_UMBRAL = 80;
    private static int contadorId = 0;
    private int id;
    private String tipo;
    private int combustible;
    private int energia;
    private int desgaste;
    private MotorWarp motor;
    private AsistenteDeComando asistente;
    private ArrayList<Tripulante> tripulacion;

    /**
     * Construye una nave con una configuración inicial de recursos.
     *
     * Precondición:
     *  - 0 <= combustible <= COMBUSTIBLE_MAX
     *  - 0 <= energia <= ENERGIA_MAX
     *  - 0 <= desgaste <= DESGASTE_MAX
     *
     * Postcondición (si la precondición se cumple):
     *  - La nave queda construida con exactamente esos valores de recursos,
     *    un id único asignado, tripulación vacía y Motor Warp inicializado.
     *
     * @throws ConfiguracionInicialInvalidaException si algún valor recibido
     *         está fuera de su rango válido.
     */
    public Nave(String tipo, int combustible, int energia, int desgaste)
            throws ConfiguracionInicialInvalidaException {
        if (combustible < 0 || combustible > COMBUSTIBLE_MAX
                || energia < 0 || energia > ENERGIA_MAX
                || desgaste < 0 || desgaste > DESGASTE_MAX) {
            throw new ConfiguracionInicialInvalidaException();
        }
        this.id = Nave.contadorId++;
        this.tipo = tipo;
        this.combustible = combustible;
        this.desgaste = desgaste;
        this.energia = energia;
        this.tripulacion = new ArrayList<>();
        this.motor = new MotorWarp(this);
    }

    //-- Sección de identidad

    /**
     * Precondición: ninguna.
     * Postcondición: devuelve el id asignado a la nave; no la modifica.
     */
    public int getId() {
        return id;
    }

    /**
     * Precondición: ninguna.
     * Postcondición: devuelve el tipo de la nave; no la modifica.
     */
    public String getTipo() {
        return tipo;
    }

    //-- Sección de componentes

    /**
     * Precondición: ninguna.
     * Postcondición: devuelve el Motor Warp de la nave; no la modifica.
     */
    public MotorWarp getMotor() {
        return motor;
    }

    /**
     * Precondición: ninguna.
     * Postcondición: devuelve el Asistente de Comando asignado a la nave
     * (puede ser null si todavía no se asignó); no la modifica.
     */
    public AsistenteDeComando getAsistente() {
        return asistente;
    }

    /**
     * Precondición: ninguna.
     * Postcondición: el Asistente de Comando de la nave queda establecido
     * en el valor recibido.
     */
    public void setAsistente(AsistenteDeComando asistente) {
        this.asistente = asistente;
    }

    //-- Sección de recursos

    /**
     * Precondición: ninguna.
     * Postcondición: devuelve el combustible actual; no lo modifica.
     */
    public int getCombustible() {
        return combustible;
    }

    /**
     * Precondición: ninguna.
     * Postcondición: devuelve la energía actual; no la modifica.
     */
    public int getEnergia() {
        return energia;
    }

    /**
     * Precondición: ninguna.
     * Postcondición: devuelve el desgaste actual; no lo modifica.
     */
    public int getDesgaste() {
        return desgaste;
    }

    /**
     * Intenta cargar energía en la nave.
     *
     * Precondición:
     *  - cantidad >= 0
     *
     * Postcondición:
     *  - Si energia + cantidad <= ENERGIA_MAX: la energía aumenta
     *    exactamente en "cantidad" y el método devuelve true.
     *  - En caso contrario (cantidad negativa, o se superaría el máximo):
     *    la nave no se modifica y el método devuelve false.
     *
     * @param cantidad cantidad de energía a cargar, no negativa
     * @return true si se pudo cargar, false si no
     */
    public boolean cargarEnergia(int cantidad) {
        if (cantidad < 0 || this.energia + cantidad > ENERGIA_MAX) {
            return false;
        }
        this.energia += cantidad;
        return true;
    }

    /**
     * Intenta cargar combustible en la nave.
     *
     * Precondición:
     *  - cantidad >= 0
     *
     * Postcondición:
     *  - Si combustible + cantidad <= COMBUSTIBLE_MAX: el combustible
     *    aumenta exactamente en "cantidad" y el método devuelve true.
     *  - En caso contrario (cantidad negativa, o se superaría el máximo):
     *    la nave no se modifica y el método devuelve false.
     *
     * @param cantidad cantidad de combustible a cargar, no negativa
     * @return true si se pudo cargar, false si no
     */
    public boolean cargarCombustible(int cantidad) {
        if (cantidad < 0 || this.combustible + cantidad > COMBUSTIBLE_MAX) {
            return false;
        }
        this.combustible += cantidad;
        return true;
    }

    /**
     * Intenta consumir energía de la nave.
     *
     * Precondición:
     *  - cantidad >= 0
     *
     * Postcondición:
     *  - Si energia >= cantidad: se descuenta exactamente esa cantidad
     *    y el método devuelve true.
     *  - En caso contrario (cantidad negativa, o energía insuficiente):
     *    la nave no se modifica y el método devuelve false.
     *
     * @param cantidad cantidad de energía a consumir, no negativa
     * @return true si se pudo consumir, false si no había suficiente
     */
    public boolean consumirEnergia(int cantidad) {
        if (cantidad < 0 || this.energia - cantidad < 0) {
            return false;
        }
        this.energia -= cantidad;
        return true;
    }

    /**
     * Intenta consumir combustible de la nave.
     *
     * Precondición:
     *  - cantidad >= 0
     *
     * Postcondición:
     *  - Si combustible >= cantidad: se descuenta exactamente esa cantidad
     *    y el método devuelve true.
     *  - En caso contrario (cantidad negativa, o combustible insuficiente):
     *    la nave no se modifica y el método devuelve false.
     *
     * @param cantidad cantidad de combustible a consumir, no negativa
     * @return true si se pudo consumir, false si no había suficiente
     */
    public boolean consumirCombustible(int cantidad) {
        if (cantidad < 0 || this.combustible - cantidad < 0) {
            return false;
        }
        this.combustible -= cantidad;
        return true;
    }

    /**
     * Intenta aumentar el desgaste de la nave.
     *
     * Precondición:
     *  - cantidad >= 0
     *
     * Postcondición:
     *  - Si desgaste + cantidad <= DESGASTE_MAX: el desgaste aumenta
     *    exactamente en "cantidad" y el método devuelve true.
     *  - En caso contrario (cantidad negativa, o se superaría el máximo):
     *    la nave no se modifica y el método devuelve false.
     *
     * @param cantidad cantidad de desgaste a sumar, no negativa
     * @return true si se pudo aplicar, false si no
     */
    public boolean aumentarDesgaste(int cantidad) {
        if (cantidad < 0 || this.desgaste + cantidad > DESGASTE_MAX) {
            return false;
        }
        this.desgaste += cantidad;
        return true;
    }

    /**
     * Precondición: ninguna.
     * Postcondición: la nave no se modifica; el resultado es exactamente
     * (desgaste >= DESGASTE_UMBRAL).
     */
    public boolean requiereMantenimiento() {
        return desgaste >= DESGASTE_UMBRAL;
    }

    /**
     * Precondición: ninguna.
     * Postcondición: el desgaste de la nave queda en 0.
     */
    public void realizarMantenimiento() {
        this.desgaste = 0;
    }

    /**
     * Precondición: ninguna.
     * Postcondición: la nave no se modifica; el resultado es exactamente
     * !requiereMantenimiento().
     */
    public boolean enEstadoOperativo() {
        return !requiereMantenimiento();
    }

    //- Sección de tripulación

    /**
     * Precondición: ninguna.
     * Postcondición: devuelve la lista de tripulantes de la nave.
     */
    public ArrayList<Tripulante> getTripulacion() {
        return tripulacion;
    }

    /**
     * Intenta asignar un tripulante a la nave.
     *
     * Precondición: ninguna.
     *
     * Postcondición:
     *  - Si t no es null y no estaba ya en la tripulación: se agrega y el
     *    método devuelve true.
     *  - En caso contrario: la tripulación no se modifica y el método
     *    devuelve false.
     *
     * @return true si se pudo asignar, false si no
     */
    public boolean asignarTripulante(Tripulante t) {
        if (t == null || tripulacion.contains(t)) {
            return false;
        }
        return tripulacion.add(t);
    }

    /**
     * Intenta eliminar un tripulante de la nave.
     *
     * Precondición: ninguna.
     *
     * Postcondición:
     *  - Si t no es null y estaba en la tripulación: se elimina y el
     *    método devuelve true.
     *  - En caso contrario: la tripulación no se modifica y el método
     *    devuelve false.
     *
     * @return true si se pudo eliminar, false si no
     */
    public boolean eliminarTripulante(Tripulante t) {
        if (t == null || !tripulacion.contains(t)) {
            return false;
        }
        return tripulacion.remove(t);
    }

    /**
     * Precondición: ninguna.
     *
     * Postcondición: la tripulación no se modifica; el resultado es
     * exactamente (hay al menos un tripulante con cargo "Capitán") &&
     * (la cantidad total de tripulantes es >= 5).
     */
    public boolean verificaTripulacionMinima() {
        boolean hayCapitan = false;

        for (Tripulante t : this.getTripulacion()) {
            if ("Capitán".equals(t.getCargo())) {
                hayCapitan = true;
                break;
            }
        }

        return hayCapitan && this.getTripulacion().size() >= 5;
    }

    @Override //para pruebas
    public String toString() {
        return "Naves.Nave{" +
                "id=" + id +
                ", tipo='" + tipo + '\'' +
                ", combustible=" + combustible +
                ", energia=" + energia +
                ", desgaste=" + desgaste +
                ", motor=" + motor +
                ", asistente=" + asistente +
                ", tripulacion=" + tripulacion +
                '}';
    }
}