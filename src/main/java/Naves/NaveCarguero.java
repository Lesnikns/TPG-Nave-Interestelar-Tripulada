package Naves;

import Excepciones.ConfiguracionInicialInvalidaException;

import static Naves.Combatiente.ResultadoCombate.*;

/**
 * Precondición: ninguna (no recibe parámetros).
 *
 * Postcondición: construye una nave de tipo "carguero" con los valores
 * iniciales fijos de la Ficha de Inicio (combustible=100, energía=60,
 * desgaste=0).
 *
 * @throws ConfiguracionInicialInvalidaException heredado del contrato de
 *         Naves.Nave; no debería dispararse en la práctica, ya que los valores
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

    @Override
    public ResultadoCombate versus(Combatiente o) {
        return o.resultadoParaCarguero();
    }

    //retornan el resultado que obtendría el otro al enfrentarse a mí
    // o sea, resultado que obtiene una exploradora frente a mí (Carguero):
    @Override
    public ResultadoCombate resultadoParaExploradora() {
        return GANA; // Exploradora gana contra carguero
    }

    @Override
    public ResultadoCombate resultadoParaCarguero() {
        return PAZ; // Carguero empata, obtiene tratado de paz
    }

    @Override
    public ResultadoCombate resultadoParaDeCombate() {
        return PIERDE; // DeCombate pierde contra carguero
    }
}