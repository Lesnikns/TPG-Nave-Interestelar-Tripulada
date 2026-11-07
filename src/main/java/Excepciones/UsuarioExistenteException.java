package Excepciones;

public class UsuarioExistenteException extends Exception {
    public UsuarioExistenteException(String usuario) {
        super("El usuario '" + usuario + "' ya existe.");
    }
}