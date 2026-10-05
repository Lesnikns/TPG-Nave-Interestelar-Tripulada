public class Sistema {
    // falta crear la nave con factory
    Mision m1 = new MisionIntercepcionAsistencia(1, "Urano");
    Mision m2 = new MisionRecoleccion(2, "Marte");
    Mision m3 = new MisionRetornoSeguro(3, "Tierra");
    AsistenteDeComando asistente = new AsistenteDeComando(nave);

}
