package Asistente;

import DestinoTablero.Posicion;

public class MisionIntercepcionAsistencia extends Mision {

    public MisionIntercepcionAsistencia(int id, String descripcion, Posicion destino) {
        super(id, descripcion, destino);
        this.energia = 5;
    }

    @Override
    protected boolean verificarYEjecutarAccionDestino(AsistenteDeComando asistente) {
        System.out.println("Verificando transferencia en destino...");
        asistente.escribeBitacora("Verificando nave aliada en posición de destino ("
                + getDestino().getX() + ", " + getDestino().getY() + ")", "EVENTO");

        if (asistente.getNave().getEnergia() >= this.energia) {
            asistente.escribeBitacora("Nave aliada contactada. Recursos listos para transferencia.", "EVENTO");
            return true;
        } else {
            asistente.escribeBitacora("Misión fallida: La nave consumió en el trayecto los recursos a transferir.", "ERROR");
            return false;
        }
    }

    @Override
    protected void aplicarCierreDefinitivo(AsistenteDeComando asistente) {
        asistente.cargarEnergiaNave(this.energia);
        this.registrarEnergiaGanada(this.energia);
        asistente.escribeBitacora("Transferencia completada. Se aplicó bonificación de +" + this.energia + " Energía.", "EVENTO");
    }
}