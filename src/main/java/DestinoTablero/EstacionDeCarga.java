package DestinoTablero;

import Asistente.*;
import DestinoTablero.ElementoDelUniverso;
import Naves.Nave;

public class EstacionDeCarga implements ElementoDelUniverso {
    private final int recarga = 20;

    @Override
    public void interactuar(Nave nave, Mision mision, AsistenteDeComando asistente) {
        asistente.cargarEnergiaNave(recarga);
        mision.registrarEnergiaGanada(recarga);
        asistente.escribeBitacora("Reabastecimiento en estación espacial: +" + recarga + " de energía.", "EVENTO");
    }
}