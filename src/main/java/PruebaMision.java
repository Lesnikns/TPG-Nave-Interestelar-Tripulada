import Asistente.*;
import DestinoTablero.*;
import Naves.*;
import java.util.*;

public class PruebaMision {
    public static void main(String[] args) throws Exception {
        // Tablero con algunos asteroides

        ArrayList<ElementoDelUniverso> elementos = new ArrayList<>();
        for (int i = 0; i < 20; i++) elementos.add(new Asteroide());
        for (int i = 0; i < 1; i++) elementos.add(new NaveEnemigaExploradora());
        for (int i = 0; i < 50; i++) elementos.add(new NaveEnemigaCarguero());
        for (int i = 0; i < 1; i++) elementos.add(new NaveEnemigaDeCombate());


        Tablero tablero = new Tablero(10, 10, elementos);

        // Nave + asistente (se referencian mutuamente)
        Nave nave = new FabricaDeNaves().getNave("exploradora");
        AsistenteDeComando asistente = new AsistenteDeComando(nave, tablero);
        nave.setAsistente(asistente);

        // Misión: destino null => lo elige el tablero
        Mision m = new MisionRecoleccion(2, "Recolección de prueba", null);
        asistente.coordinarMision(m);

        // Resultado
        System.out.println("\n=== BITÁCORA ===");
        System.out.println(asistente.getBitacora());
        System.out.println("Destruida: " + nave.estaDestruida()
                + " | Combustible: " + nave.getCombustible()
                + " | Energía: " + nave.getEnergia()
                + " | Desgaste: " + nave.getDesgaste());
    }
}