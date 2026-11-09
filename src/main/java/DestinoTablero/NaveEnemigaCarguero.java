package DestinoTablero;

import Naves.Combatiente;

import static Naves.Combatiente.ResultadoCombate.*;

public class NaveEnemigaCarguero extends NaveEnemiga{

    public NaveEnemigaCarguero(){
        this.setTipo("carguero");
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
