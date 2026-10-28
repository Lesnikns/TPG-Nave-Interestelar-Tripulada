package Naves;

import Excepciones.ConfiguracionInicialInvalidaException;

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
public class NaveDeCombate extends Nave{
    final private static int COMBUSTIBLE_INICIAL = 80;
    final private static int ENERGIA_INICIAL = 100;
    final private static int DESGASTE_INICIAL = 0;

    public NaveDeCombate()  throws ConfiguracionInicialInvalidaException{
        String tipo= "combate";

        super(tipo, COMBUSTIBLE_INICIAL, ENERGIA_INICIAL, DESGASTE_INICIAL);
    }
}

