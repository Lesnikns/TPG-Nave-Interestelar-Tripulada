package Persistencia;
 
import Excepciones.PersistenciaException;
import java.sql.*;
 
 
public class DAOJDBC implements DAOInterfaz {
    private BaseDeDatos baseDeDatos;
 
    /**
     * @pre: baseDeDatos != null
     * @post: la tabla usuarios existe (se crea si todavía no estaba)
     * 
     * @throws PersistenciaException si no se puede crear la tabla
     */
    public DAOJDBC(BaseDeDatos baseDeDatos) throws PersistenciaException {
        assert baseDeDatos != null : "La base de datos no puede ser nula";
        
        this.baseDeDatos = baseDeDatos;
        baseDeDatos.ejecutar("CREATE TABLE IF NOT EXISTS usuarios ("
                + "nombre VARCHAR(50) PRIMARY KEY, contrasena VARCHAR(50) NOT NULL)");
    }
 
    @Override
    public void insertar(DTO usuario) throws PersistenciaException {
        assert usuario != null : "El usuario no puede ser nulo";
        baseDeDatos.ejecutar("INSERT INTO usuarios (nombre, contrasena) VALUES ('"
                + usuario.getNombre() + "', '" + usuario.getContrasena() + "')");
    }
 
    @Override
    public DTO buscarPorNombre(String nombre) throws PersistenciaException {
        assert nombre != null : "El nombre no puede ser nulo";
        DTO usuario = null;
        try {
            ResultSet resultado = baseDeDatos.consultar("SELECT nombre, contrasena FROM usuarios WHERE nombre = '" + nombre + "'");
            if (resultado.next()) {
                usuario = new DTO(resultado.getString("nombre"), resultado.getString("contrasena"));
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error en la base de datos: " + e.getMessage());
        } finally {
            baseDeDatos.cerrar();
        }
        return usuario;
    }
}