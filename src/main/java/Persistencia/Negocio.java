package Persistencia;

import Excepciones.CredencialesInvalidasException;
import Excepciones.PersistenciaException;
import Excepciones.UsuarioExistenteException;

/** Reglas de negocio del acceso al sistema. Usa el DAO para consultar y guardar usuarios. */
public class Negocio{
    private DAOInterfaz dao;

    public Negocio(DAOInterfaz dao) {
        assert dao != null : "El DAO no puede ser nulo";
        this.dao = dao;
    }

    /**
     * Registra un usuario nuevo.
     * @pre: nombre y contrasena no nulos ni vacíos
     * @post: el usuario queda guardado y puede iniciar sesión
     * @throws UsuarioExistenteException si ya hay un usuario con ese nombre (no se guarda nada)
     * @throws PersistenciaException si falla la base de datos
     */
    public void registrar(String nombre, String contrasena) throws UsuarioExistenteException, PersistenciaException {
        assert nombre != null && !nombre.isEmpty() : "El nombre no puede ser nulo ni vacío";
        assert contrasena != null && !contrasena.isEmpty() : "La contraseña no puede ser nula ni vacía";
        if (dao.buscarPorNombre(nombre) != null) {
            throw new UsuarioExistenteException(nombre);
        }
        dao.insertar(new DTO(nombre, contrasena));
    }

    /**
     * Comprueba las credenciales de un usuario.
     * @pre: nombre y contrasena no nulos
     * @post: si son correctas retorna sin modificar ningun dato
     * 
     * @throws CredencialesInvalidasException si el usuario no existe o la contraseña no coincide
     * @throws PersistenciaException si falla la base de datos
     */
    public void autenticar(String nombre, String contrasena) throws CredencialesInvalidasException, PersistenciaException {
        assert nombre != null : "El nombre no puede ser nulo";
        assert contrasena != null : "La contraseña no puede ser nula";
        DTO usuario = dao.buscarPorNombre(nombre);
        if (usuario == null || !usuario.getContrasena().equals(contrasena)) {
            throw new CredencialesInvalidasException();
        }
    }
}