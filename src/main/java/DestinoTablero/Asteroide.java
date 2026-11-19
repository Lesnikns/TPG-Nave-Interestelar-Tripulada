package DestinoTablero;

import Asistente.AsistenteDeComando;
import Asistente.Mision;
import Naves.Nave;

public class Asteroide implements ElementoDelUniverso {
    private final int danoEnergia = 15;

    @Override
    public void interactuar(Nave nave, Mision mision, AsistenteDeComando asistente) {

        int energiaAnterior = nave.getEnergia();
        nave.recibirDanoEnergia(danoEnergia);
        int energiaPerdida = energiaAnterior - nave.getEnergia();
        // Registrar la pérdida real para el informe de misión.
        mision.registrarEnergiaPerdida(energiaPerdida);
        asistente.escribeBitacora("Impacto con asteroide: -" + energiaPerdida + " de energía (daño solicitado: " + danoEnergia + ", energía restante: " + nave.getEnergia() + ").", "EVENTO");
    }

}
