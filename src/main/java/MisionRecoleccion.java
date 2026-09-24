public class MisionRecoleccion extends Mision{
    public MisionRecoleccion(int id, String destino) {
        super(id, "Recoleccion", destino);
        this.energia = 5;
    }

    @Override
    public void aplicarConsecuencias(AsistenteDeComando asistente) {
        asistente.getNave().cargarEnergia(this.energia);
        asistente.escribeBitacora("Bonificación: +5 Energía por éxito en M-01.", "EVENTO");
    }

    @Override
    public void ejecutarMision(AsistenteDeComando asistente) {
        asistente.escribeBitacora("Ejecutando la mision de recoleccion", "EVENTO");
        System.out.println("Ejecutando Recoleccion");
        System.out.print("Objeto recolectado con exito");
        asistente.escribeBitacora("Objeto recolectado con exito", "EVENTO");
    }

}
