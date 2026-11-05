package DestinoTablero;

import Asistente.*;
import DestinoTablero.ElementoDelUniverso;
import Naves.Nave;

import java.util.Random;

public class EstacionDeCarga implements ElementoDelUniverso {
    private final int recarga = 20;

    /**
     * Reabastece a la nave con energía y/o combustible al interactuar con la estación de carga.
     * <p>
     * Evalúa los niveles actuales de la nave: si ambos recursos están en nivel crítico (inferior al 25%),
     * se recargan ambos obligatoriamente. Si solo uno se encuentra crítico, se prioriza ese recurso.
     * En caso de que ninguno sea crítico, se selecciona uno aleatoriamente.
     * La recarga calcula un objetivo aleatorio entre el 75% y el 100% de la capacidad de cada recurso a reabastecer.
     * </p>
     *
     * <b>pre:</b> nave != null, asistente != null.<br>
     * <b>post:</b> los recursos correspondientes de la nave se incrementan hasta alcanzar el porcentaje objetivo
     * y se registra el evento en la bitácora mediante el asistente.
     *
     * @param nave nave espacial que ingresa a la estación y recibe el reabastecimiento. nave != null
     * @param mision misión en curso durante el desplazamiento en el tablero
     * @param asistente coordinador a través del cual se realiza la recarga y se asienta el evento en la bitácora. asistente != null
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
            // Ambos críticos: se cargan los dos obligatoriamente
            recargarEnergia = true;
            recargarCombustible = true;
        } else if (energiaCritica) {
            recargarEnergia = true;
        } else if (combustibleCritico) {
            recargarCombustible = true;
        } else {
            // Ninguno es crítico: elige uno 50/50 al azar
            if (random.nextBoolean()) {
                recargarEnergia = true;
            } else {
                recargarCombustible = true;
            }
        }

        // Ejecución de recargas según corresponda
        if (recargarEnergia) {
            aplicarRecargaEnergia(nave, asistente, random, maxEnergia, energiaActual);
        }

        if (recargarCombustible) {
            aplicarRecargaCombustible(nave, asistente, random, maxCombustible, combustibleActual);
        }
    }

    private void aplicarRecargaEnergia(Nave nave, AsistenteDeComando asistente, Random random, int maxEnergia, int energiaActual) {
        int porcentajeObjetivo = 75 + random.nextInt(26); // Entre 75% y 100%
        int energiaObjetivo = (maxEnergia * porcentajeObjetivo) / 100;
        int cantidadACargar = Math.max(0, energiaObjetivo - energiaActual);

        if (cantidadACargar > 0) {
            asistente.cargarEnergiaNave(cantidadACargar);
            asistente.escribeBitacora(
                    "Reabastecimiento en estación espacial: +" + cantidadACargar + " de energía (" + porcentajeObjetivo + "% alcanzado).",
                    "EVENTO"
            );
        } else {
            asistente.escribeBitacora("Estación de Carga: La nave ya posee niveles óptimos de energía.", "EVENTO");
        }
    }

    private void aplicarRecargaCombustible(Nave nave, AsistenteDeComando asistente, Random random, int maxCombustible, int combustibleActual) {
        int porcentajeObjetivo = 75 + random.nextInt(26); // Entre 75% y 100%
        int combustibleObjetivo = (maxCombustible * porcentajeObjetivo) / 100;
        int cantidadACargar = Math.max(0, combustibleObjetivo - combustibleActual);

        if (cantidadACargar > 0) {
            nave.cargarCombustible(cantidadACargar);
            asistente.escribeBitacora(
                    "Reabastecimiento en estación espacial: +" + cantidadACargar + " de combustible (" + porcentajeObjetivo + "% alcanzado).",
                    "EVENTO"
            );
        } else {
            asistente.escribeBitacora("Estación de Carga: La nave ya posee niveles óptimos de combustible.", "EVENTO");
        }
    }
}