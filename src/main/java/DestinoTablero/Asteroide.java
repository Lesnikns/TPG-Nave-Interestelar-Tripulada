package DestinoTablero;

import Asistente.AsistenteDeComando;
import Asistente.Mision;
import Naves.Nave;

public class Asteroide implements ElementoDelUniverso {
    private final int danoEnergia = 15;

    @Override
    public void interactuar(Nave nave, Mision mision, AsistenteDeComando asistente) {
        nave.consumirEnergia(danoEnergia);
        asistente.escribeBitacora("Impacto con asteroide: -" + danoEnergia + " de energía.", "EVENTO");
    }

}
