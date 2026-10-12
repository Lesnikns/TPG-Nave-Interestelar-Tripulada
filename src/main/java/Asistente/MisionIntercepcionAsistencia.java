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
        System.out.println("Intercepcion y asistencia realizadas con exito.");
        asistente.escribeBitacora("Intercepcion y asistencia realizadas con exito", "EVENTO");

    }

    @Override
    public void aplicarConsecuencias(AsistenteDeComando asistente) {
        // Evaluamos: ¿Sobrevivió con recursos suficientes? (Ej: combustible mayor a 0)
        if (asistente.getNave().getCombustible() > 0 && asistente.getNave().getEnergia() > 0) {
            asistente.cargarEnergiaNave(this.energia);
            asistente.escribeBitacora("Evaluación exitosa: Misión completada con recursos estables. Bonificación: +" + this.energia + " Energía", "EVENTO");
        } else {
            asistente.escribeBitacora("Evaluación fallida: La nave finalizó la asistencia sin recursos suficientes para operar normalmente.", "ERROR");
        }
    }
}
