public class MisionRetornoSeguro extends Mision{

    public MisionRetornoSeguro(int id, String destino) {
        super(id, "Retorno Seguro", destino);
    }

    @Override
    public void aplicarConsecuencias(AsistenteDeComando asistente) {

    }

    @Override
    public void ejecutarMision(AsistenteDeComando asistente) {
        asistente.escribeBitacora("Ejecutando la mision de retorno seguro", "EVENTO");
        System.out.println("Completando retorno seguro");
    }
}
