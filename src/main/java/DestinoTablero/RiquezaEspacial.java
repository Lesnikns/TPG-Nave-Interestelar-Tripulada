package DestinoTablero;

import Asistente.AsistenteDeComando;
import Asistente.Mision;
import Naves.Nave;

public class RiquezaEspacial implements ElementoDelUniverso{
    private final int cantidadGemas;

    public RiquezaEspacial(int cantidadGemas) {
        this.cantidadGemas = cantidadGemas;
    }
    @Override
    public void interactuar(Nave nave, Mision mision, AsistenteDeComando asistente) {
        //asistente.agregarGemasANave(this.cantidadGemas);
        //asistente.escribeBitacora("¡Gemas encontradas en el camino! +" + cantidadGemas + " gemas recolectadas.", "EVENTO");
        asistente.escribeBitacora("¡Gemas encontradas en el camino! +" +0 + " gemas recolectadas. (no implementado)", "EVENTO");
    }
}
