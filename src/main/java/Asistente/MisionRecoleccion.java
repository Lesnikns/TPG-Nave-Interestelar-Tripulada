package Asistente;

import DestinoTablero.Posicion;

import java.util.Random;

public class MisionRecoleccion extends Mision {
    private int gemas;

    public MisionRecoleccion(int id, String descripcion, Posicion destino, int desgaste, int combustible) {
        super(id, descripcion, destino, desgaste, combustible, 10 );
        this.gemas = 0;
    }

    @Override
    protected boolean verificarYEjecutarAccionDestino(AsistenteDeComando asistente) {
        asistente.escribeBitacora("Arribado a la coordenada de recolección. Extrayendo recursos...", "EVENTO");
        int gemasEncontradas = 20 + new Random().nextInt(81);
        this.gemas = gemasEncontradas;
        asistente.escribeBitacora("Yacimiento procesado: " + this.gemas + " gemas obtenidas.", "EVENTO");

        return true;
    }


    @Override
    protected String obtenerDetalles() {
        return String.format("- Gemas recolectadas en destino: %d unidades", this.gemas);
    }

    // Hook de Cierre: Pide al asistente que agregue las gemas a la nave
    @Override
    protected void aplicarCierreDefinitivo(AsistenteDeComando asistente) {
        asistente.agregarGemasANave(this.gemas); // agregar gemas al inventario de la nave
    }

    @Override
    protected String obtenerMensajeCierre() {
        return String.format("Extracción finalizada. %d gemas fueron transferidas al inventario.", this.gemas);
    }

}