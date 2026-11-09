package Asistente;

/**
 * Foto de los recursos de la nave en un instante: combustible, energia y desgaste.
 * Es inmutable: copia los valores al crearse y no guarda referencia a la nave, asi no cambia aunque la nave siga cambiando
 * y la vista puede leerla desde otro hilo sin riesgo. Es el evento que notifica el asistente cuando cambian los recursos.
 */
public class EstadoRecursos {
    private final int combustible;
    private final int energia;
    private final int desgaste;

    /**
     * Devuelve el combustible de la nave en el instante de la foto
     * <b>pre:</b> ninguna <br>
     * <b>post:</b> se devolvio el combustible, sin modificar el estado
     * @return combustible
     */
    public int getCombustible() {
        return combustible;
    }
    /**
     * Devuelve la energia de la nave en el instante de la foto
     * <b>pre:</b> ninguna <br>
     * <b>post:</b> se devolvio la energia, sin modificar el estado
     * @return energia
     */
    public int getEnergia() {
        return energia;
    }
    /**
     * Devuelve el desgaste de la nave en el instante de la foto
     * <b>pre:</b> ninguna <br>
     * <b>post:</b> se devolvio el desgaste, sin modificar el estado
     * @return desgaste
     */
    public int getDesgaste() {
        return desgaste;
    }

    @Override
    public String toString() {
        return "Recursos [combustible = " + combustible + ", energia = " + energia + ", desgaste = " + desgaste + "]\n";
    }

    /**
     * Se construye la foto de recursos con los valores dados
     * <b>pre:</b> combustible &gt;= 0; energia &gt;= 0; 0 &lt;= desgaste &lt;= 100 <br>
     * <b>post:</b> se ha creado el estado con los tres valores dados
     * @param combustible - combustible de la nave. combustible &gt;= 0
     * @param energia - energia de la nave. energia &gt;= 0
     * @param desgaste - desgaste de la nave. 0 &lt;= desgaste &lt;= 100
     */
    public EstadoRecursos(int combustible, int energia, int desgaste) {
        assert combustible >= 0 && energia >= 0 : "los recursos no pueden ser negativos"; //TODO: revisarlos pos cerrar lo de los métodos en Nave
        assert desgaste >= 0 && desgaste <= 100 : "el desgaste debe estar entre 0 y 100";
        this.combustible = combustible;
        this.energia = energia;
        this.desgaste = desgaste;
    }
}