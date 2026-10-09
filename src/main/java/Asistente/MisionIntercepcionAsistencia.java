package Asistente;

public class MisionIntercepcionAsistencia extends Mision {

    public MisionIntercepcionAsistencia(int id,String descripcion, String destino) {
        super(id, descripcion, destino);
        this.energia = 5;
    }

    @Override
    public void ejecutarMision(AsistenteDeComando asistente) {
        System.out.println("Ejecutando intercepción y asistencia...");
        asistente.escribeBitacora("Ejecutando la mision de intercepcion y asistencia", "EVENTO");

        asistente.ejecutarSaltoYCostos(this.combustible, this.desgaste);
    }

    @Override
    public void aplicarConsecuencias(AsistenteDeComando asistente) {
        asistente.cargarEnergiaNave(this.energia);
        asistente.escribeBitacora("Bonificación: +5 Energía por éxito en M-01.", "EVENTO");
    }
}
