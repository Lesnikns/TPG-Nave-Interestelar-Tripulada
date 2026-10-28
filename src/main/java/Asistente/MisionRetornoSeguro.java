package Asistente;

public class MisionRetornoSeguro extends Mision {

    public MisionRetornoSeguro(int id,String descripcion, String destino) {
        super(id, descripcion, destino);
    }

    @Override
    public void ejecutarMision(AsistenteDeComando asistente) {
        System.out.println("Completando retorno seguro...");
        asistente.escribeBitacora("Ejecutando la mision de retorno seguro", "EVENTO");
        asistente.ejecutarSaltoYCostos(this.combustible, this.desgaste);
        System.out.println("Retorno realizado con exito.");
        asistente.escribeBitacora("Retorno realizado con exito", "EVENTO");

    }

    @Override
    public void aplicarConsecuencias(AsistenteDeComando asistente) {
        // Evaluamos: ¿La nave superó el viaje sin daños críticos?
        if (asistente.getNave().enEstadoOperativo()) {
            asistente.cargarEnergiaNave(this.energia);
            asistente.escribeBitacora("Evaluación exitosa: Retorno completado en estado operativo válido. Bonificación: +" + this.energia + " Energía", "EVENTO");
        } else {
            asistente.escribeBitacora("Evaluación fallida: La nave llegó a destino pero con daños críticos (Inoperativa).", "ERROR");
        }
    }
}