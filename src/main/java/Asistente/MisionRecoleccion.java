package Asistente;

import DestinoTablero.Posicion;

public class MisionRecoleccion extends Mision {
    private int gemasAEncontrar;

    public MisionRecoleccion(int id, String descripcion, Posicion destino, int gemasAEncontrar) {
        super(id, descripcion, destino);
        this.energia = 5;
        this.gemasAEncontrar = gemasAEncontrar;
    }

    public MisionRecoleccion(int id, String descripcion, Posicion destino) {
        this(id, descripcion, destino, 10);
    }

    @Override
    protected boolean verificarYEjecutarAccionDestino(AsistenteDeComando asistente) {
        System.out.println("Arribado a la coordenada de riquezas. Recolectando gemas...");
        asistente.escribeBitacora("Arribo exitoso a la coordenada de recolección: ("
                + getDestino().getX() + ", " + getDestino().getY() + ")", "EVENTO");

        this.registrarGemasRecolectadas(this.gemasAEncontrar);
        asistente.escribeBitacora("Gemas recolectadas en casilla: " + this.gemasAEncontrar + " unidades.", "EVENTO");

        return true;
    }

    @Override
    protected void aplicarCierreDefinitivo(AsistenteDeComando asistente) {
        asistente.cargarEnergiaNave(this.energia);
        this.registrarEnergiaGanada(this.energia);

        asistente.escribeBitacora("Cierre M-02: " + this.gemasAEncontrar + " gemas recolectadas y +" + this.energia + " Energía aplicada.", "EVENTO");
    }
}