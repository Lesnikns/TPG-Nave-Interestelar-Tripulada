package Excepciones;

public class CredencialesInvalidasException extends Exception {
    public CredencialesInvalidasException() {
        super("Usuario o contraseña incorrectos.");
    }
}