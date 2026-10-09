package Naves;

import Excepciones.ConfiguracionInicialInvalidaException;

/**
 * Precondición: ninguna (no recibe parámetros).
 *
 * Postcondición: construye una nave de tipo "exploradora" con los valores
 * iniciales fijos de la Ficha de Inicio (combustible=60, energía=80,
 * desgaste=0).
 *
 * @throws ConfiguracionInicialInvalidaException heredado del contrato de
 *         Naves.Nave; no debería dispararse en la práctica, ya que los valores
 *         pasados a super(...) son constantes fijas ya validadas.
 */
public class NaveExploradora extends Nave{
    final private static int COMBUSTIBLE_INICIAL = 60;
    final private static int ENERGIA_INICIAL = 80;
    final private static int DESGASTE_INICIAL = 0;

    public NaveExploradora()  throws ConfiguracionInicialInvalidaException {
        String tipo= "exploradora";

        super(tipo, COMBUSTIBLE_INICIAL, ENERGIA_INICIAL, DESGASTE_INICIAL);
    }
}