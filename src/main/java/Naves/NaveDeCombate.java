package Naves;

import Excepciones.ConfiguracionInicialInvalidaException;

import static Naves.Combatiente.ResultadoCombate.*;

/**
 * Precondición: ninguna (no recibe parámetros).
 *
 * Postcondición: construye una nave de tipo "combate" con los valores
 * iniciales fijos de la Ficha de Inicio (combustible=80, energía=100,
 * desgaste=0).
 *
 * @throws ConfiguracionInicialInvalidaException heredado del contrato de
 *         Naves.Nave; no debería dispararse en la práctica, ya que los valores
 *         pasados a super(...) son constantes fijas ya validadas.
 */
public class NaveDeCombate extends Nave {
    final private static int COMBUSTIBLE_INICIAL = 80;
    final private static int ENERGIA_INICIAL = 100;
    final private static int DESGASTE_INICIAL = 0;

    public NaveDeCombate()  throws ConfiguracionInicialInvalidaException{
        String tipo= "combate";

        super(tipo, COMBUSTIBLE_INICIAL, ENERGIA_INICIAL, DESGASTE_INICIAL);
    }

    @Override
    public ResultadoCombate versus(Combatiente o) {
        return o.resultadoParaDeCombate();
    }

    //retornan el resultado que obtendría el otro al enfrentarse a mí
    // o sea, resultado que obtiene una exploradora frente a mí (DeCombate):
    @Override
    public ResultadoCombate resultadoParaExploradora() {
        return PIERDE; // Exploradora Pierde contra DeCombate
    }

    @Override
    public ResultadoCombate resultadoParaCarguero() {
        return GANA; // Carguero gana contra DeCombate
    }

    @Override
    public ResultadoCombate resultadoParaDeCombate() {
        return PAZ; // DeCombate empata, obtiene tratado de paz
    }
}

