
package DestinoTablero;

import Asistente.*;
import Naves.Nave;

import java.util.Random;

public class EstacionDeCarga implements ElementoDelUniverso {

    /**
     * Reabastece energía y/o combustible al interactuar con la estación.
     *
     * Si ambos recursos están por debajo del 25%, recarga ambos.
     * Si solamente uno está por debajo del 25%, prioriza ese recurso.
     * Si ninguno es crítico, selecciona uno aleatoriamente.
     *
     * El objetivo de cada recarga se determina aleatoriamente
     * entre el 75% y el 100% de la capacidad correspondiente.
     *
     * Las ganancias reales se registran en la misión para
     * que puedan incluirse en el informe final.
     */
    @Override
    public void interactuar(Nave nave, Mision mision, AsistenteDeComando asistente) {
        Random random = new Random();

        int maxEnergia = 100;
        int maxCombustible = 100;

        int energiaActual = nave.getEnergia();
        int combustibleActual = nave.getCombustible();

        boolean energiaCritica = (energiaActual < maxEnergia * 0.25);
        boolean combustibleCritico = (combustibleActual < maxCombustible * 0.25);

        boolean recargarEnergia = false;
        boolean recargarCombustible = false;

        if (energiaCritica && combustibleCritico) {
            recargarEnergia = true;
            recargarCombustible = true;
        } else if (energiaCritica) {
            recargarEnergia = true;
        } else if (combustibleCritico) {
            recargarCombustible = true;
        } else {
            if (random.nextBoolean()) {
                recargarEnergia = true;
            } else {
                recargarCombustible = true;
            }
        }
        if (recargarEnergia) {
            int energiaGanada = aplicarRecargaEnergia(asistente, random, maxEnergia, energiaActual);
            mision.registrarEnergiaGanada(energiaGanada);
        }

        if (recargarCombustible) {
            int combustibleGanado = aplicarRecargaCombustible(asistente, random, maxCombustible, combustibleActual);
            mision.registrarCombustibleGanado(combustibleGanado);
        }
    }

    /**
     * Recarga energía hasta el porcentaje objetivo.
     * Devuelve la cantidad realmente aplicada.
     */
    private int aplicarRecargaEnergia(AsistenteDeComando asistente, Random random, int maxEnergia, int energiaActual) {
        int porcentajeObjetivo = 75 + random.nextInt(26); // Entre 75% y 100%
        int energiaObjetivo = (maxEnergia * porcentajeObjetivo) / 100;
        int cantidadACargar = Math.max(0, energiaObjetivo - energiaActual);

        if (cantidadACargar > 0) {
            int energiaAplicada = asistente.recargarEnergiaNaveConTope(cantidadACargar);
            asistente.escribeBitacora("Reabastecimiento en estación espacial: +" + energiaAplicada + " de energía (" + porcentajeObjetivo + "% objetivo).", "EVENTO");
            return energiaAplicada;
        } else {
            asistente.escribeBitacora("Estación de Carga: La nave ya posee niveles óptimos de energía.", "EVENTO");
            return 0;
        }
    }

    /**
     * Recarga combustible hasta el porcentaje objetivo.
     * Devuelve la cantidad realmente aplicada.
     */
    private int aplicarRecargaCombustible(AsistenteDeComando asistente, Random random, int maxCombustible, int combustibleActual) {
        int porcentajeObjetivo = 75 + random.nextInt(26); // Entre 75% y 100%
        int combustibleObjetivo = (maxCombustible * porcentajeObjetivo) / 100;
        int cantidadACargar = Math.max(0, combustibleObjetivo - combustibleActual);

        if (cantidadACargar > 0) {
            int combustibleAplicado = asistente.recargarCombustibleNaveConTope(cantidadACargar);
            asistente.escribeBitacora("Reabastecimiento en estación espacial: +" + combustibleAplicado + " de combustible (" + porcentajeObjetivo + "% objetivo).", "EVENTO");
            return combustibleAplicado;
        } else {
            asistente.escribeBitacora("Estación de Carga: La nave ya posee niveles óptimos de combustible.", "EVENTO");
            return 0;
        }
    }
}