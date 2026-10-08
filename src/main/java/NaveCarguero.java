import java.util.ArrayList;

/**
 * Precondición: ninguna (no recibe parámetros).
 *
 * Postcondición: construye una nave de tipo "carguero" con los valores
 * iniciales fijos de la Ficha de Inicio (combustible=100, energía=60,
 * desgaste=0).
 *
 * @throws ConfiguracionInicialInvalidaException heredado del contrato de
 *         Nave; no debería dispararse en la práctica, ya que los valores
 *         pasados a super(...) son constantes fijas ya validadas.
 */
public class NaveCarguero extends Nave{
    final private static int COMBUSTIBLE_INICIAL = 100;
    final private static int ENERGIA_INICIAL = 60;
    final private static int DESGASTE_INICIAL = 0;

    public NaveCarguero() throws ConfiguracionInicialInvalidaException  {
        String tipo= "carguero";

        super(tipo, COMBUSTIBLE_INICIAL, ENERGIA_INICIAL, DESGASTE_INICIAL);
    }
}