package Asistente;

import DestinoTablero.Posicion;
//TODO todavia no existe nave aliada
public class MisionIntercepcionAsistencia extends Mision {
    //M-01 — Intercepción y asistencia: Consume 4 puntos de combustible, 5 puntos de energía y genera 4 puntos de desgaste.
    private static final int consumoCombustible = 4;
    private static final int consumoEnergia = 5;
    private static final int generaDesgaste = 4;
    private static final int MAX_AUXILIO = 50;

    // Auxilio solicitado por la nave aliada: entre 0 y 50 de energía y/o combustible.
    private final int energiaAEntregar;
    private final int combustibleAEntregar;

    public MisionIntercepcionAsistencia(int id, String descripcion, Posicion destino, int energiaT, int combustibleT) {
        super(id, descripcion, destino, generaDesgaste, consumoCombustible, consumoEnergia);
        if (energiaT < 0 || energiaT > MAX_AUXILIO || combustibleT < 0 || combustibleT > MAX_AUXILIO) {
            throw new IllegalArgumentException("El auxilio solicitado debe estar entre 0 y " + MAX_AUXILIO + " puntos.");
        }
        this.energiaAEntregar = energiaT;
        this.combustibleAEntregar = combustibleT;
        this.energia = 5;
    }

    @Override
    protected boolean verificarYEjecutarAccionDestino(AsistenteDeComando asistente) {
        System.out.println("Verificando transferencia en destino...");
        asistente.escribeBitacora("Verificando nave aliada en posición de destino ("
                + getDestino().getX() + ", " + getDestino().getY() + ")", "EVENTO");

        if (asistente.getEnergia() >= this.energiaAEntregar && asistente.getCombustible() >= this.combustibleAEntregar) {
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
        asistente.entregarCombustibleNave(combustibleAEntregar);
    }

    @Override
    protected String obtenerMensajeCierre() {
        return String.format("Transferencia de auxilio completada (%d energía, %d combustible entregados).",
                energiaAEntregar, combustibleAEntregar);
    }
}