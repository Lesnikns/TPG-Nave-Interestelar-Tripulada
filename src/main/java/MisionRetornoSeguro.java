public class MisionRetornoSeguro extends Mision {

    public MisionRetornoSeguro(int id,String descripcion, String destino) {
        super(id, descripcion, destino);
    }

    @Override
    public void ejecutarMision(AsistenteDeComando asistente) {
        System.out.println("Completando retorno seguro...");
        asistente.escribeBitacora("Ejecutando la mision de retorno seguro", "EVENTO");

        asistente.ejecutarSaltoYCostos(this.combustible, this.desgaste);
    }

    @Override
    public void aplicarConsecuencias(AsistenteDeComando asistente) {
        asistente.escribeBitacora("Sin bonificación de energía para M-03.", "EVENTO");
    }
}