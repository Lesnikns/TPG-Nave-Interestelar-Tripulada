public class MisionIntercepcionAsistencia extends Mision{


    public MisionIntercepcionAsistencia(int id, String destino) {
        super(id, "Intercepción y asistencia", destino);
        this.energia = 5;
    }

    @Override
    public void aplicarConsecuencias(AsistenteDeComando asistente) {
        asistente.getNave().cargarEnergia(this.energia);
        asistente.escribeBitacora("Bonificación: +5 Energía por éxito en M-01.", "EVENTO");
    }

    @Override
    public void ejecutarMision(AsistenteDeComando asistente) {
        asistente.escribeBitacora("Ejecutando la mision de intercepcion y asistencia", "EVENTO");
        System.out.print("Ejecutando intercepción y asistencia");
    }



}
