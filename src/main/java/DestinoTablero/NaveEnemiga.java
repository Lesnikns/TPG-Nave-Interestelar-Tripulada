package DestinoTablero;

import Asistente.AsistenteDeComando;
import Asistente.Mision;
import Naves.Combatiente;
import Naves.Nave;

public abstract class NaveEnemiga implements ElementoDelUniverso, Combatiente {
    private String tipo;

    @Override
    public void interactuar(Nave nave, Mision mision, AsistenteDeComando asistente) {
        ResultadoCombate res = nave.versus(this);
        String enemigo = "nave enemiga " + this.getTipo();
        switch(res) {
            case GANA -> asistente.escribeBitacora("Batalla contra " + enemigo + ": victoria. La nave enemiga fue derrotada.", "EVENTO");
            case PAZ -> asistente.escribeBitacora("Encuentro con " + enemigo + ": pacto de paz. Ambas naves siguen su camino.", "EVENTO");
            case PIERDE -> {
                nave.destruir();
                asistente.escribeBitacora("Batalla contra " + enemigo + ": derrota. La nave fue destruida.", "ERROR");
            } //el bucle de la misión tiene que preguntar nave.estaDestruida()
        }
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
