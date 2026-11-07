-- Esquema de la base de datos del TPG (H2).
-- Solo se persisten las credenciales de los usuarios.
CREATE TABLE IF NOT EXISTS usuarios (
    nombre VARCHAR(50) PRIMARY KEY,
    contrasena VARCHAR(50) NOT NULL
);
 