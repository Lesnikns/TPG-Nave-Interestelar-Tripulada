package Asistente;

import Naves.Nave;

import java.util.Date;

/**
 * Informe final de una mision (fase "evaluar" del ciclo de vida): registra si la mision resulto exitosa, fallida o abortada,
 * junto con el balance de los recursos de la nave durante el trayecto y lo obtenido o entregado por la mision.
 * El balance de cada recurso es el valor de la nave al evaluar menos su valor al comenzar la mision, por lo que incluye todo lo que paso en el recorrido
 * (costo inicial, saltos, asteroides, estaciones de carga). Las recompensas que se aplican en el cierre de la mision se informan aparte (gemas y transferido).
 * Es inmutable: copia los datos de la mision y de la nave al crearse y no guarda referencias a ellas, asi el informe no cambia aunque despues cambien la mision o la nave,
 * y la vista puede leerlo desde otro hilo sin riesgo.
 * <b>invariantes:</b> descripcion != null <br>
 * fecha != null, fijada en el instante de creacion <br>
 * resultado es EXITOSA, FALLIDA o ABORTADA <br>
 * causa != null, y si el resultado no es EXITOSA, causa != "" <br>
 * gemasGanadas &gt;= 0 y transferido &gt;= 0
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
    private final int balanceCombustible;
    private final int balanceEnergia;
    private final int balanceDesgaste;
    private final int gemasGanadas;
    private final int transferido;

    /**
     * Devuelve el numero de la mision informada
     * <b>pre:</b> ninguna <br>
     * <b>post:</b> se devolvio el id de la mision, sin modificar el informe
     * @return id de la mision
     */
    public int getId() {
        return id;
    }
    /**
     * Devuelve la descripcion de la mision informada
     * <b>pre:</b> ninguna <br>
     * <b>post:</b> se devolvio la descripcion de la mision, sin modificar el informe
     * @return descripcion de la mision
     */
    public String getDescripcion() {
        return descripcion;
    }
    /**
     * Devuelve el momento en que se confecciono el informe
     * <b>pre:</b> ninguna <br>
     * <b>post:</b> se devolvio una copia de la fecha (modificar la copia no cambia el informe)
     * @return fecha de confeccion del informe
     */
    public Date getFecha() {
        return new Date(fecha.getTime());
    }
    /**
     * Devuelve el resultado de la mision
     * <b>pre:</b> ninguna <br>
     * <b>post:</b> se devolvio EXITOSA, FALLIDA o ABORTADA, sin modificar el informe
     * @return resultado de la mision
     */
    public String getResultado() {
        return resultado;
    }
    /**
     * Devuelve el motivo por el que la mision no fue exitosa
     * <b>pre:</b> ninguna <br>
     * <b>post:</b> se devolvio la causa ("" si la mision fue exitosa), sin modificar el informe
     * @return causa del fallo o del aborto de la mision
     */
    public String getCausa() {
        return causa;
    }
    /**
     * Devuelve el balance de combustible durante la mision (valor al evaluar menos valor al comenzar)
     * <b>pre:</b> ninguna <br>
     * <b>post:</b> se devolvio el balance: negativo si la nave consumio combustible, positivo si gano
     * @return balance de combustible
     */
    public int getBalanceCombustible() {
        return balanceCombustible;
    }
    /**
     * Devuelve el balance de energia durante la mision (valor al evaluar menos valor al comenzar)
     * <b>pre:</b> ninguna <br>
     * <b>post:</b> se devolvio el balance: negativo si la nave consumio energia, positivo si gano
     * @return balance de energia
     */
    public int getBalanceEnergia() {
        return balanceEnergia;
    }
    /**
     * Devuelve el balance de desgaste durante la mision (valor al evaluar menos valor al comenzar)
     * <b>pre:</b> ninguna <br>
     * <b>post:</b> se devolvio el balance: positivo si el desgaste aumento
     * @return balance de desgaste
     */
    public int getBalanceDesgaste() {
        return balanceDesgaste;
    }
    /**
     * Devuelve las gemas que obtuvo la mision (solo la mision de recoleccion obtiene gemas)
     * <b>pre:</b> ninguna <br>
     * <b>post:</b> se devolvio la cantidad de gemas, mayor o igual a cero
     * @return gemas ganadas
     */
    public int getGemasGanadas() {
        return gemasGanadas;
    }
    /**
     * Devuelve los recursos entregados a la nave aliada (solo la mision de intercepcion y asistencia transfiere recursos)
     * <b>pre:</b> ninguna <br>
     * <b>post:</b> se devolvio la cantidad transferida, mayor o igual a cero
     * @return recursos transferidos
     */
    public int getTransferido() {
        return transferido;
    }

    /**
     * Genera el texto del informe, para mostrarlo en el historial de informes
     * <b>pre:</b> ninguna <br>
     * <b>post:</b> se devolvio un String con la mision, el resultado (y la causa si no fue exitosa), el balance de cada recurso con su signo, lo obtenido y la fecha. El informe no se modifico
     * @return el informe como texto, != null y != ""
     */
    @Override
    public String toString() {
        String texto = "Informe M-0" + id + " (" + descripcion + "): " + resultado;
        if (!causa.isEmpty())
            texto += " - " + causa;
        texto += " | combustible " + String.format("%+d", balanceCombustible)
                + ", energia " + String.format("%+d", balanceEnergia)
                + ", desgaste " + String.format("%+d", balanceDesgaste)
                + " | gemas " + gemasGanadas + ", transferido " + transferido
                + " | " + fecha + "\n";
        return texto;
    }

    /**
     * Se construye el informe final de una mision, copiando los datos de la mision y los recursos actuales de la nave
     * <b>pre:</b> mision != null; nave != null; resultado es EXITOSA, FALLIDA o ABORTADA; causa != null, y si el resultado no es EXITOSA, causa != "";
     * combustibleInicial, energiaInicial y desgasteInicial &gt;= 0 (los valores de la nave al comenzar la mision); mision.getGemasGanadas() &gt;= 0 y mision.getTransferido() &gt;= 0 <br>
     * <b>post:</b> se ha creado el informe con la fecha actual, el resultado y la causa dados, y el balance de cada recurso igual al valor actual de la nave menos su valor inicial
     * @param mision - mision informada (se copian su id, su descripcion, las gemas ganadas y lo transferido). mision != null
     * @param nave - nave que realizo la mision (se copian sus recursos actuales, antes de aplicar las recompensas del cierre). nave != null
     * @param resultado - resultado de la mision. Informe.EXITOSA, Informe.FALLIDA o Informe.ABORTADA
     * @param causa - motivo del fallo o del aborto ("" si fue exitosa). causa != null
     * @param combustibleInicial - combustible de la nave al comenzar la mision. combustibleInicial &gt;= 0
     * @param energiaInicial - energia de la nave al comenzar la mision. energiaInicial &gt;= 0
     * @param desgasteInicial - desgaste de la nave al comenzar la mision. desgasteInicial &gt;= 0
     */
    public Informe(Mision mision, Nave nave, String resultado, String causa, int combustibleInicial, int energiaInicial, int desgasteInicial) {
        assert mision != null : "el informe necesita una mision";
        assert nave != null : "el informe necesita la nave de la mision";
        assert EXITOSA.equals(resultado) || FALLIDA.equals(resultado) || ABORTADA.equals(resultado) : "resultado invalido: " + resultado;
        assert causa != null : "la causa no puede ser nula (vacia si la mision fue exitosa)";
        assert EXITOSA.equals(resultado) || !causa.isEmpty() : "una mision fallida o abortada debe informar su causa";
        assert combustibleInicial >= 0 && energiaInicial >= 0 && desgasteInicial >= 0 : "los valores iniciales no pueden ser negativos";
        assert mision.getGemasGanadas() >= 0 && mision.getTransferido() >= 0 : "las gemas y lo transferido no pueden ser negativos";
        this.id = mision.getId();
        this.descripcion = mision.getDescripcion();
        this.fecha = new Date();
        this.resultado = resultado;
        this.causa = causa;
        this.balanceCombustible = nave.getCombustible() - combustibleInicial;
        this.balanceEnergia = nave.getEnergia() - energiaInicial;
        this.balanceDesgaste = nave.getDesgaste() - desgasteInicial;
        this.gemasGanadas = mision.getGemasGanadas();
        this.transferido = mision.getTransferido();
    }
}
