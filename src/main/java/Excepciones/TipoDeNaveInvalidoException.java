package Excepciones;

public class TipoDeNaveInvalidoException extends RuntimeException {
    public TipoDeNaveInvalidoException(String tipo) {
        super("Tipo de nave no reconocido: " + tipo);
    }
}
