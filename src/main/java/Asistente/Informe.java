package Asistente;

import Naves.Nave;
import java.util.Date;

/**
 * Informe final de una misión.
 *
 * Se confecciona durante la fase Evaluar, antes de aplicar
 * los cambios definitivos de la fase Cerrar.
 *
 * Conserva una fotografía inmutable del resultado, los recursos
 * de la nave y las estadísticas registradas durante el trayecto.
 */
public class Informe {
    public static final String EXITOSA = "Exitosa";
    public static final String FALLIDA = "Fallida";
    public static final String ABORTADA = "Abortada";

    private final int id;
    private final String descripcion;
    private final Date fecha;
    private final String resultado;
    private final String causa;

    // Combustible
    private final int combustibleInicial;
    private final int combustibleFinal;
    private final int combustibleGanado;
    private final int combustibleConsumido;
    private final int combustiblePerdido;
    private final int balanceCombustible;

    // Energía
    private final int energiaInicial;
    private final int energiaFinal;
    private final int energiaGanada;
    private final int energiaConsumida;
    private final int energiaPerdida;
    private final int balanceEnergia;

    // Desgaste
    private final int desgasteInicial;
    private final int desgasteFinal;
    private final int balanceDesgaste;

    // Resultados particulares de las misiones
    private final int gemasGanadas;
    private final int transferido;

    /**
     * Construye el informe copiando los datos de la misión y de la nave.
     *
     * Los balances y consumos se calculan con los valores de la nave
     * anteriores al cierre definitivo.
     */
    public Informe(Mision mision, Nave nave, String resultado, String causa, int combustibleInicial, int energiaInicial, int desgasteInicial) {
        if (mision == null || nave == null) {
            throw new IllegalArgumentException("El informe necesita una misión y una nave.");
        }

        if (!EXITOSA.equals(resultado) && !FALLIDA.equals(resultado) && !ABORTADA.equals(resultado)) {
            throw new IllegalArgumentException("Resultado de misión inválido: " + resultado);
        }

        if (causa == null) {
            throw new IllegalArgumentException("La causa no puede ser nula.");
        }

        if (!EXITOSA.equals(resultado) && causa.trim().isEmpty()) {
            throw new IllegalArgumentException("Una misión fallida o abortada debe informar su causa.");
        }

        if (combustibleInicial < 0 || energiaInicial < 0 || desgasteInicial < 0) {
            throw new IllegalArgumentException("Los valores iniciales no pueden ser negativos.");
        }

        // Identificación
        this.id = mision.getId();
        this.descripcion = mision.getDescripcion();
        this.fecha = new Date();
        this.resultado = resultado;
        this.causa = causa;

        // Capturar valores finales de la nave.
        this.combustibleInicial = combustibleInicial;
        this.combustibleFinal = nave.getCombustible();
        this.energiaInicial = energiaInicial;
        this.energiaFinal = nave.getEnergia();
        this.desgasteInicial = desgasteInicial;
        this.desgasteFinal = nave.getDesgaste();

        // Copiar estadísticas de combustible.
        this.combustibleGanado = mision.getCombustibleGanado();
        this.combustiblePerdido = mision.getCombustiblePerdido();
        this.combustibleConsumido = combustibleInicial + combustibleGanado - combustiblePerdido - combustibleFinal;
        this.balanceCombustible = combustibleFinal - combustibleInicial;

        // Copiar estadísticas de energía.
        this.energiaGanada = mision.getEnergiaGanada();
        this.energiaPerdida = mision.getEnergiaPerdida();
        this.energiaConsumida = energiaInicial + energiaGanada - energiaPerdida - energiaFinal;
        this.balanceEnergia = energiaFinal - energiaInicial;

        // Desgaste acumulado.
        this.balanceDesgaste = desgasteFinal - desgasteInicial;

        // Resultados particulares.
        this.gemasGanadas = mision.getGemasRecolectadas();
        this.transferido = mision.getTransferido();
    }

    public int getId() {
        return id;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public Date getFecha() {
        return new Date(fecha.getTime());
    }
    public String getResultado() {
        return resultado;
    }
    public String getCausa() {
        return causa;
    }
    public int getCombustibleInicial() {
        return combustibleInicial;
    }
    public int getCombustibleFinal() {
        return combustibleFinal;
    }
    public int getCombustibleGanado() {
        return combustibleGanado;
    }
    public int getCombustibleConsumido() {
        return combustibleConsumido;
    }
    public int getCombustiblePerdido() {
        return combustiblePerdido;
    }
    public int getBalanceCombustible() {
        return balanceCombustible;
    }
    public int getEnergiaInicial() {
        return energiaInicial;
    }
    public int getEnergiaFinal() {
        return energiaFinal;
    }
    public int getEnergiaGanada() {
        return energiaGanada;
    }
    public int getEnergiaConsumida() {
        return energiaConsumida;
    }
    public int getEnergiaPerdida() {
        return energiaPerdida;
    }
    public int getBalanceEnergia() {
        return balanceEnergia;
    }

    public int getDesgasteInicial() {
        return desgasteInicial;
    }

    public int getDesgasteFinal() {
        return desgasteFinal;
    }

    public int getBalanceDesgaste() {
        return balanceDesgaste;
    }
    public int getGemasGanadas() {
        return gemasGanadas;
    }
    public int getTransferido() {
        return transferido;
    }

    /**
     * Genera la representación textual del informe.
     */
    @Override
    public String toString() {
        return "INFORME FINAL M-0" + id + " (" + descripcion + ")\n" +
                "Fecha: " + fecha + "\n" +
                "Resultado: " + resultado + "\n" +
                "Causa: " + (causa.isEmpty() ? "Ninguna" : causa) + "\n\n" +

                "COMBUSTIBLE\n" +
                "- Inicial: " + combustibleInicial + "\n" +
                "- Ganado: +" + combustibleGanado + "\n" +
                "- Consumido: -" + combustibleConsumido + "\n" +
                "- Perdido: -" + combustiblePerdido + "\n" +
                "- Final: " + combustibleFinal + "\n" +
                "- Balance: " + (balanceCombustible >= 0 ? "+" : "") + balanceCombustible + "\n\n" +

                "ENERGÍA\n" +
                "- Inicial: " + energiaInicial + "\n" +
                "- Ganada: +" + energiaGanada + "\n" +
                "- Consumida: -" + energiaConsumida + "\n" +
                "- Perdida: -" + energiaPerdida + "\n" +
                "- Final: " + energiaFinal + "\n" +
                "- Balance: " + (balanceEnergia >= 0 ? "+" : "") + balanceEnergia + "\n\n" +

                "DESGASTE\n" +
                "- Inicial: " + desgasteInicial + "\n" +
                "- Final: " + desgasteFinal + "\n" +
                "- Balance: " + (balanceDesgaste >= 0 ? "+" : "") + balanceDesgaste + "\n\n" +

                "RESULTADOS PARTICULARES\n" +
                "- Gemas ganadas: " + gemasGanadas + "\n" +
                "- Recursos transferidos: " + transferido;
    }
}