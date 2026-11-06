package Asistente;

import DestinoTablero.Posicion;

public class MisionRetornoSeguro extends Mision {

    public MisionRetornoSeguro(int id, String descripcion, Posicion destino) {
        super(id, descripcion, destino);
        this.energia = 0;
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
        asistente.getNave().realizarMantenimiento();
        asistente.cargarEnergiaNave(100);
        asistente.escribeBitacora("Cierre M-03: Mantenimiento completo realizado. Nave reparada y recursos recargados al 100%.", "EVENTO");
    }
}