package DestinoTablero;

import Naves.Combatiente;

import static Naves.Combatiente.ResultadoCombate.*;

public class NaveEnemigaExploradora extends NaveEnemiga{
    public NaveEnemigaExploradora(){
        this.setTipo("exploradora");
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
