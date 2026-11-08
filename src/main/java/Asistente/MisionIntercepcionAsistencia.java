package Asistente;

import DestinoTablero.Posicion;
import Naves.Nave;

public class MisionIntercepcionAsistencia extends Mision {
    private int energiaATransferir;
    private int combustibleATransferir;

    public MisionIntercepcionAsistencia(int id, String descripcion, Posicion destino, int desgaste, int combustible, int combustibleT, int energiaT) {
        super(id, descripcion, destino, desgaste, combustible, 5 );
        this.combustibleATransferir =  combustibleT;
        this.energiaATransferir = energiaT;
    }

    @Override
    protected boolean verificarYEjecutarAccionDestino(AsistenteDeComando asistente) {
        Nave nave = asistente.getNave();

        System.out.println("Verificando transferencia en destino...");
        asistente.escribeBitacora("Verificando nave aliada en posición de destino ("
                + getDestino().getX() + ", " + getDestino().getY() + ")", "EVENTO");

        if (nave.getEnergia() >= this.energiaATransferir && nave.getCombustible() >= this.combustibleATransferir) {
            asistente.escribeBitacora("Nave aliada contactada. Recursos listos para transferencia.", "EVENTO");
            return true;
        } else {
            asistente.escribeBitacora("Misión fallida: La nave consumió en el trayecto los recursos a transferir.", "ERROR");
            return false;
        }
    }

    @Override
    protected void aplicarCierreDefinitivo(AsistenteDeComando asistente) {
        asistente.getNave().consumirEnergia(this.energiaATransferir);
        asistente.getNave().consumirCombustible(this.combustibleATransferir);
        asistente.escribeBitacora("Transferencia completada.", "EVENTO");
    }

    protected  String obtenerDetalles(){
        return String.format("- Auxilio listo para entrega: %d energía, %d combustible",
                this.energiaATransferir, this.combustibleATransferir);
    }
    protected String obtenerMensajeCierre(){
        return String.format("Transferencia de auxilio completada (%d energía, %d combustible entregados).",
                this.energiaATransferir, this.combustibleATransferir);
    }
}