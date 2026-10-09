package Excepciones;

public class CombustibleInsuficienteException extends Exception {
    public CombustibleInsuficienteException(String mensaje) {
        super(mensaje);
    }
}