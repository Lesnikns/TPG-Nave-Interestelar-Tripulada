package Asistente;

import DestinoTablero.Posicion;
import Naves.Nave;

public class MisionRetornoSeguro extends Mision {

    public MisionRetornoSeguro(int id, String descripcion, Posicion destino, int desgaste, int combustible) {
        super(id, descripcion, destino, desgaste, combustible, 0 );
    }

    @Override
    protected boolean verificarYEjecutarAccionDestino(AsistenteDeComando asistente) {
        System.out.println("Verificando arribo a la base de origen...");
        asistente.escribeBitacora("Verificando llegada a la base de origen: ("
                + getDestino().getX() + ", " + getDestino().getY() + ")", "EVENTO");

        if (asistente.getNave().enEstadoOperativo()) {
            asistente.escribeBitacora("Llegada a base confirmada. Nave en estado operativo válido.", "EVENTO");
            return true;
        } else {
            asistente.escribeBitacora("Misión fallida: La nave llegó a la base con daños críticos (Inoperativa).", "ERROR");
            return false;
        }
    }

    @Override
    protected void aplicarCierreDefinitivo(AsistenteDeComando asistente) {
        Nave nave = asistente.getNave();
        nave.realizarMantenimiento();
        nave.recargarTotalmenteRecursos();
        asistente.escribeBitacora("Cierre M-03: Mantenimiento completo realizado. Nave reparada y recursos recargados al 100%.", "EVENTO");
    }
}