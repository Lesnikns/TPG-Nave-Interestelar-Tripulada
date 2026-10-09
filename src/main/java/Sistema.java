import Asistente.*;
import Naves.FabricaDeNaves;
import Naves.Nave;
import Tripulacion.*;

public class Sistema {
    public static void main(String[] args) {
        System.out.println("Simulación Final");
        // Escenario A — Ejecución correcta
        try {
            System.out.println("Escenario A — Ejecución correcta");
            System.out.println(" ");
            // 1. Crear mediante Factory una nave de cada tipo
            FabricaDeNaves fabrica = new FabricaDeNaves();
            Nave naveExploracion = fabrica.getNave("exploradora");
            Nave naveCarga = fabrica.getNave("carguero");
            Nave naveCombate = fabrica.getNave("combate");

            // 2. Seleccionar una y asignarle una tripulación válida
            naveExploracion.asignarTripulante(new Alferez(new Terricola("Uhura"), 4));
            naveExploracion.asignarTripulante(new Alferez(new Marciano("Marvin"), 2));
            naveExploracion.asignarTripulante(new Consejero(new Vulcano("Spock"), 15, 50));
            naveExploracion.asignarTripulante(new Terricola("Bones"));
            naveExploracion.asignarTripulante(new Capitan(new Terricola("Kirk"), 10));

            // Configuramos el asistente de la nave seleccionada
            AsistenteDeComando asistente = new AsistenteDeComando(naveExploracion);
            naveExploracion.setAsistente(asistente);

            // 3 y 4. Preparar, ejecutar, evaluar y cerrar M-01, M-02 y M-03
            Mision m1 = new MisionRecoleccion(1, "Extraer Titanio", "Luna");
            Mision m2 = new MisionIntercepcionAsistencia(2, "Rescate de sonda", "Marte");
            Mision m3 = new MisionRetornoSeguro(3, "Volver a base", "Tierra");

            asistente.coordinarMision(m1);
            asistente.coordinarMision(m2);
            asistente.coordinarMision(m3);

            // 5. Mostrar recursos finales y Bitácora
            System.out.println(" ");
            System.out.println("--- Recursos Finales ---");
            System.out.println(" ");
            System.out.println("Combustible: " + naveExploracion.getCombustible());
            System.out.println("Energía: " + naveExploracion.getEnergia());
            System.out.println("Desgaste: " + naveExploracion.getDesgaste());

            System.out.println("\n--- Registro de Bitácora ---");
            System.out.println(asistente.getBitacora().toString());

        } catch (Exception e) {
            System.out.println("Error en la simulación: " + e.getMessage());
        }
        // Escenario B — Recursos insuficientes
        try {
            System.out.println("Escenario B — Recursos insuficientes");
            System.out.println(" ");
            // 1. Utilizar una nave sin recursos suficientes para la misión
            FabricaDeNaves fabrica = new FabricaDeNaves();
            Nave naveCarga = fabrica.getNave("carguero");
            AsistenteDeComando asistente = new AsistenteDeComando(naveCarga);
            naveCarga.setAsistente(asistente);

            // Forzamos la falta de recursos llevando el desgaste al 98%
            naveCarga.aumentarDesgaste(98);
            int desgasteInicial = naveCarga.getDesgaste();

            // 2. Intentar prepararla o ejecutarla
            Mision mImposible = new MisionRecoleccion(4, "Minería en Asteroide", "Cinturón de Kuiper");
            asistente.coordinarMision(mImposible);

            // 3. Verificar que se rechaza, no deja cambios parciales y registra el motivo
            System.out.println("--- Verificación de Seguridad ---");
            System.out.println(" ");
            System.out.println("Desgaste antes de la misión: " + desgasteInicial);
            System.out.println("Desgaste tras el aborto: " + naveCarga.getDesgaste());

            if (desgasteInicial == naveCarga.getDesgaste()) {
                System.out.println("Estado: [OK] No hubo cambios parciales. La operación es atómica.");
            } else {
                System.out.println("Estado: [FALLO] Los recursos se alteraron.");
            }

            System.out.println("\n--- Registro de Bitácora ---");
            System.out.println(" ");
            System.out.println(asistente.getBitacora().toString());

        } catch (Exception e) {
            System.out.println("Error en la simulación: " + e.getMessage());
        }
        // Escenario C — Motor Warp
        try {
            System.out.println(" ");
            System.out.println("Escenario C — Motor Warp");
            System.out.println(" ");
            FabricaDeNaves fabrica = new FabricaDeNaves();
            Nave nave = fabrica.getNave("combate");
            AsistenteDeComando asistente = new AsistenteDeComando(nave);
            nave.setAsistente(asistente);

            // 1. Recorrer la secuencia válida
            System.out.println("--- 1. Secuencia Válida ---");
            System.out.println(" ");
            nave.getMotor().pedirPrepararSalto(); // De Motor.Disponible a Preparando
            nave.getMotor().pedirWarp();          // De Preparando a Warp
            nave.getMotor().pedirDisponibilidad(); // De Warp a Motor.Disponible (cierra el ciclo)
            System.out.println("[OK] Ciclo de motor completado correctamente.\n");
            System.out.println(" ");

            // 2. Intentar transición inválida

            System.out.println("--- 2. Forzando Transición Inválida ---");
            System.out.println(" ");
            System.out.println("Intentando pasar a Warp directo desde Motor.Disponible...");
            // El motor está Motor.Disponible. Pedir warp directo debe ser rechazado.
            nave.getMotor().pedirWarp();

            // 3. Verificar registro
            System.out.println("\n--- 3. Verificación en Bitácora ---");
            System.out.println(asistente.getBitacora().toString());

        } catch (Exception e) {
            System.out.println("Error en la simulación: " + e.getMessage());
        }

        // Escenario D — Contrato inválido
        try {
            System.out.println(" ");
            System.out.println("Escenario D — Contrato Inválido");
            System.out.println(" ");
            FabricaDeNaves fabrica = new FabricaDeNaves();
            Nave nave = fabrica.getNave("exploradora");
            AsistenteDeComando asistente = new AsistenteDeComando(nave);
            nave.setAsistente(asistente);

            System.out.println("--- 1. Forzando Contrato Inválido ---");
            int energiaInicial = nave.getEnergia();
            System.out.println("Energía inicial de la nave: " + energiaInicial);

            // Intentamos cargar una cantidad que exceda groseramente la capacidad
            System.out.println("Intentando inyectar 5000 unidades de energía...");
            System.out.println(" ");
            asistente.cargarEnergiaNave(5000);

            // Verificamos que el estado se haya conservado dentro de los límites
            System.out.println("\n--- 2. Verificación de Seguridad ---");
            System.out.println("Energía tras la operación: " + nave.getEnergia());

            // Asumimos que 100 es el tope lógico de energía de tu modelo
            if (nave.getEnergia() <= 100) {
                System.out.println("Estado: [OK] El contrato se respetó. La nave autolimitó la carga y no quedó fuera de rango.");
            } else {
                System.out.println("Estado: [FALLO] La nave superó su capacidad máxima.");
            }

        } catch (Exception e) {
            System.out.println(" ");
            System.out.println("Error en la simulación: " + e.getMessage());
        }
    }

}
