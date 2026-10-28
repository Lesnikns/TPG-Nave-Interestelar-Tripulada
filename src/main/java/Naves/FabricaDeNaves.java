package Naves;

import Excepciones.TipoDeNaveInvalidoException;

public class FabricaDeNaves {
    public Nave getNave(String tipo) throws TipoDeNaveInvalidoException {
        return switch (tipo) {
            case "carguero" -> new NaveCarguero();
            case "combate" -> new NaveDeCombate();
            case "exploradora" -> new NaveExploradora();
            default -> throw new TipoDeNaveInvalidoException(tipo);
        };
    }
}
//quité el static y abstract para poder sobreescribir el método y usar super() si la extiendo
// ese cambio implica tener que instanciar la fábrica (revisar que no se mencione que deba ser única)