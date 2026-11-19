package Asistente;

import DestinoTablero.Posicion;
//TODO en el controlador habrá que poner que no se pueda ejecutar dos veces consecutivas (ya está en la base)
public class MisionRetornoSeguro extends Mision {
    private static final int consumoCombustible = 4;
    private static final int consumoEnergia = 0;
    private static final int generaDesgaste = 4;

    public MisionRetornoSeguro(int id, String descripcion, Posicion destino) {
        //M-03 — Retorno a punto seguro: Consume 4 puntos de combustible, 0 puntos de energía y genera 4 puntos de desgaste.
        super(id, descripcion, destino,consumoCombustible,consumoEnergia,generaDesgaste);
        this.energia = 0;
    }

    @Override
    protected boolean verificarYEjecutarAccionDestino(AsistenteDeComando asistente) {
        System.out.println("Verificando arribo a la base de origen...");
        asistente.escribeBitacora("Verificando llegada a la base de origen: ("
                + getDestino().getX() + ", " + getDestino().getY() + ")", "EVENTO");

        if (asistente.naveEstaOperativa()) {
            asistente.escribeBitacora("Llegada a base confirmada. Nave en estado operativo válido.", "EVENTO");
            return true;
        } else {
            asistente.escribeBitacora("Misión fallida: La nave llegó a la base con daños críticos (Inoperativa).", "ERROR");
            return false;
        }
    }

    @Override
    protected void aplicarCierreDefinitivo(AsistenteDeComando asistente) {

        asistente.realizarMantenimientoNave();
        asistente.recargarTotalmenteRecursosNave();
        asistente.escribeBitacora("Cierre M-03: Mantenimiento completo realizado. Nave reparada y recursos recargados al 100%.", "EVENTO");
    }
}