public class MisionRecoleccion extends Mision {

    public MisionRecoleccion(int id,String descripcion, String destino) {
        super(id, descripcion, destino);
        this.energia = 5;
    }

    @Override
    public void ejecutarMision(AsistenteDeComando asistente) {
        System.out.println("Ejecutando Recoleccion...");
        asistente.escribeBitacora("Ejecutando la mision de recoleccion", "EVENTO");

        asistente.ejecutarSaltoYCostos(this.combustible, this.desgaste);

        System.out.println("Objeto recolectado con exito.");
        asistente.escribeBitacora("Objeto recolectado con exito", "EVENTO");
    }

    @Override
    public void aplicarConsecuencias(AsistenteDeComando asistente) {
        asistente.cargarEnergiaNave(this.energia);
        asistente.escribeBitacora("Bonificación: +5 Energía por éxito en M-02.", "EVENTO");
    }
}