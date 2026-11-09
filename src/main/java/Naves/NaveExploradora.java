package Naves;

import Excepciones.ConfiguracionInicialInvalidaException;
import static Naves.Combatiente.ResultadoCombate.*;

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
public class NaveExploradora extends Nave {
    final private static int COMBUSTIBLE_INICIAL = 60;
    final private static int ENERGIA_INICIAL = 80;
    final private static int DESGASTE_INICIAL = 0;

    public NaveExploradora()  throws ConfiguracionInicialInvalidaException {
        String tipo= "exploradora";

        super(tipo, COMBUSTIBLE_INICIAL, ENERGIA_INICIAL, DESGASTE_INICIAL);
    }

    @Override
    public ResultadoCombate versus(Combatiente o) {
        return o.resultadoParaExploradora();
    }

    //retornan el resultado que obtendría el otro al enfrentarse a mí
    // o sea, resultado que obtiene una exploradora frente a mí (Exploradora):
    @Override
    public ResultadoCombate resultadoParaExploradora() {
        return PAZ; // Exploradora empata, obtiene tratado de paz
    }

    @Override
    public ResultadoCombate resultadoParaCarguero() {
        return PIERDE; // Carguero pierde contra exploradora
    }

    @Override
    public ResultadoCombate resultadoParaDeCombate() {
        return GANA; // DeCombate gana contra exploradora
    }
}