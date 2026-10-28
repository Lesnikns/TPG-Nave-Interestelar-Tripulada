package Excepciones;

public class ConfiguracionInicialInvalidaException extends RuntimeException {
  public ConfiguracionInicialInvalidaException() {
    super("Configuración inicial inválida: combustible, energía o desgaste fuera de rango");
  }

  public ConfiguracionInicialInvalidaException(String mensaje) {
    super(mensaje);
  }
}
