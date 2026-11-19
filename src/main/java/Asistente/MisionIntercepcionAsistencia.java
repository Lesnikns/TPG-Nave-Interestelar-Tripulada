package Asistente;

import DestinoTablero.Posicion;
//TODO todavia no existe nave aliada
public class MisionIntercepcionAsistencia extends Mision {
    //M-01 — Intercepción y asistencia: Consume 4 puntos de combustible, 5 puntos de energía y genera 4 puntos de desgaste.
    private static final int consumoCombustible = 4;
    private static final int consumoEnergia = 5;
    private static final int generaDesgaste = 4;
    private final int energiaAEntregar = 5;

    public MisionIntercepcionAsistencia(int id, String descripcion, Posicion destino) {
        super(id, descripcion, destino,consumoCombustible,consumoEnergia,generaDesgaste);
        this.energia = 5;
    }

    @Override
    protected boolean verificarYEjecutarAccionDestino(AsistenteDeComando asistente) {
        System.out.println("Verificando transferencia en destino...");

        if (asistente.getEnergia()>= this.energia) {
            this.registrarTransferido(energiaAEntregar);
            asistente.escribeBitacora("Nave aliada contactada. Recursos listos para transferencia.", "EVENTO");
            return true;
        } else {
            asistente.escribeBitacora("Misión fallida: La nave consumió en el trayecto los recursos a transferir.", "ERROR");
            return false;
        }
    }

    @Override
    protected void aplicarCierreDefinitivo(AsistenteDeComando asistente) {
        asistente.entregarEnergiaNave(energiaAEntregar);
        asistente.escribeBitacora("Transferencia completada. Se entregaron " + energiaAEntregar + " unidades de energía.", "EVENTO");
    }
}