package Asistente;

import DestinoTablero.Posicion;

public class MisionRecoleccion extends Mision {
// M-02 — Recolección: Consume 4 puntos de combustible, 10 puntos de energía y genera 4 puntos de desgaste.
    private static final int consumoCombustible = 4;
    private static final int consumoEnergia = 10;
    private static final int generaDesgaste = 4;
    private static final int energiaRecompensa = 5;

    public MisionRecoleccion(int id, String descripcion, Posicion destino) {
        super(id, descripcion, destino,generaDesgaste,consumoCombustible,consumoEnergia);
    }

    @Override
    protected boolean verificarYEjecutarAccionDestino(AsistenteDeComando asistente) {
        System.out.println("Arribado a la coordenada de riquezas. Recolectando gemas...");
        asistente.escribeBitacora("Arribo exitoso a la coordenada de recolección: ("
                + getDestino().getX() + ", " + getDestino().getY() + ")", "EVENTO");

        // Recompensa aleatoria entre 20 y 100, ambos inclusive.
        int gemasObtenidas = 20 + new java.util.Random().nextInt(81);
        // Registrar la recompensa para el informe.
        this.registrarGemasRecolectadas(gemasObtenidas);
        asistente.escribeBitacora("Recompensa obtenida: " + gemasObtenidas + " gemas.", "EVENTO");

        return true;
    }

    @Override
    protected void aplicarCierreDefinitivo(AsistenteDeComando asistente) {
        int gemas = this.getGemasRecolectadas();

        asistente.agregarGemasNave(gemas);
        asistente.escribeBitacora("Cierre M-02: " + gemas + " gemas incorporadas al inventario de la nave.", "EVENTO");
    }
}