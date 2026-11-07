package Persistencia;

import Excepciones.PersistenciaException;

import java.sql.*;


/**
 * Se encarga de todo el trabajo con JDBC: abre la conexión, ejecuta las sentencias SQL y
 * devuelve los resultados. Los DAO solo le pasan el SQL y sus parámetros.
 * @pre: url != null
 */
public class BaseDeDatos {
    private String url;
    private Connection conexion;

    /**
     * ejemplo de url: "jdbc:h2:./database/tpg"
     * @pre: url != null (por ejemplo "jdbc:h2:./database/tpg")
     * <b>post:</b> queda lista para ejecutar sentencias. El driver se registra solo (JDBC 4).
     */
    public BaseDeDatos(String url) {
        assert url != null : "La url no puede ser nula";
        this.url = url;
    }

    
    /** Carga el driver de H2 y abre la conexión. */
    private void conectar() throws PersistenciaException, SQLException {
	        try {
	            Class.forName("org.h2.Driver");
	        } catch (ClassNotFoundException e) {
	            throw new PersistenciaException("No se encontró el driver de H2");
	        }
	        conexion = DriverManager.getConnection(url, "sa", "");
	}
	

	/**
	 * Ejecuta una sentencia que no devuelve datos (CREATE, INSERT, UPDATE, DELETE).
	 * @pre:sql != null 
	 * @post: la sentencia se ejecutó y la conexión quedó cerrada
	 * @throws PersistenciaException si falla la conexión o la sentencia
	 */
    public void ejecutar(String sql) throws PersistenciaException {
        assert sql != null : "El sql no puede ser nulo";
        try {
            conectar();
            Statement sentencia = conexion.createStatement();
            sentencia.execute(sql);
        } catch (SQLException e) {
            throw new PersistenciaException("Error en la base de datos: " + e.getMessage());
        } finally {
            cerrar();
        }
    }
    
    /**
     * Cierra la conexión
     * @post: no hay ninguna conexión abierta. Si ya estaba cerrada no hace nada
     */
    public void cerrar() {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
            }
            conexion = null;
        }
    }
    
    
    /**
     * Ejecuta una consulta (SELECT) y devuelve su resultado.
     * @pre: sql != null
     * @post:la conexión queda abierta para poder leer el resultado, quien llama debe cerrarla con cerrar()
     * @throws PersistenciaException si falla la conexión o la consulta (en ese caso la conexión ya queda cerrada)
     */
    public ResultSet consultar(String sql) throws PersistenciaException {
        assert sql != null : "El sql no puede ser nulo";
        try {
            conectar();
            Statement sentencia = conexion.createStatement();
            return sentencia.executeQuery(sql);
        } catch (SQLException e) {
            cerrar();
            throw new PersistenciaException("Error en la base de datos: " + e.getMessage());
        }
    }

}