package Persistencia;
 
import Excepciones.PersistenciaException;
 
public interface DAOInterfaz {
 
    /**
     * Guarda un usuario nuevo
     * 
     * @pre: usuario != null y todavía no existe uno con ese nombre 
     * @post: el usuario queda guardado
     * 
     * @throws PersistenciaException si falla el acceso a la base de datos
     */
    void insertar(DTO usuario) throws PersistenciaException;
 
    /**
     * Busca un usuario por su nombre
     * 
     * @pre: nombre != null
     * @post: no modifica nada y devuelve el usuario o null si no existe
     * 
     * @throws PersistenciaException si falla el acceso a la base de datos
     */
    DTO buscarPorNombre(String nombre) throws PersistenciaException;
}