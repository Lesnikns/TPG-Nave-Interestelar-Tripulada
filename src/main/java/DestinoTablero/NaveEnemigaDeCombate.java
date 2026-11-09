package DestinoTablero;

import Naves.Combatiente;

import static Naves.Combatiente.ResultadoCombate.*;

public class NaveEnemigaDeCombate extends NaveEnemiga{

    public NaveEnemigaDeCombate(){
        this.setTipo("de combate");
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
