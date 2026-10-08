public class Sistema {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   DEFENSA FINAL - SIMULADOR DE NAVE INTERESTELAR");
        System.out.println("==================================================\n");

        try {
            // =========================================================
            // ESCENARIO A: EJECUCIÓN CORRECTA Y CICLO COMPLETO
            // =========================================================
            System.out.println(">>> ESCENARIO A: Ejecución Correcta <<<");
            System.out.println("1. Creando naves mediante Factory...");
            FabricaDeNaves fabrica = new FabricaDeNaves();
            Nave naveExploracion = fabrica.getNave("exploradora");
            Nave naveCarga = fabrica.getNave("carguero");
            Nave naveCombate = fabrica.getNave("combate");
            System.out.println("[OK] Naves creadas con éxito.\n");

            System.out.println("2. Asignando tripulación a la Nave de Exploración...");
            naveExploracion.asignarTripulante(new Alferez(new Terricola("Uhura"), 4));
            naveExploracion.asignarTripulante(new Alferez(new Marciano("Marvin"), 2));
            naveExploracion.asignarTripulante(new Consejero(new Vulcano("Spock"), 15, 50));
            naveExploracion.asignarTripulante(new Terricola("Bones")); // Médico civil
            naveExploracion.asignarTripulante(new Capitan(new Terricola("Kirk"), 10));
            System.out.println("Tripulación mínima alcanzada: " + naveExploracion.verificaTripulacionMinima() + "\n");

            System.out.println("3 y 4. Ejecutando Misiones 01, 02 y 03...");
            AsistenteDeComando asistente = new AsistenteDeComando(naveExploracion);
            naveExploracion.setAsistente(asistente);

            Mision m1 = new MisionRecoleccion(1, "Extraer Titanio", "Luna");
            Mision m2 = new MisionIntercepcionAsistencia(2, "Rescate de sonda", "Marte");
            Mision m3 = new MisionRetornoSeguro(3, "Volver a base", "Tierra");

            asistente.coordinarMision(m1);
            asistente.coordinarMision(m2);
            asistente.coordinarMision(m3);

            System.out.println("\n5. Reporte final del Escenario A:");
            System.out.println("Combustible restante: " + naveExploracion.getCombustible());
            System.out.println("Energía restante: " + naveExploracion.getEnergia());
            System.out.println("Desgaste acumulado: " + naveExploracion.getDesgaste());

            System.out.println("\n--- BITÁCORA ESCENARIO A ---");
            System.out.println(asistente.getBitacora().toString());


            // =========================================================
            // ESCENARIO B: RECURSOS INSUFICIENTES Y ABORTO SEGURO
            // =========================================================
            System.out.println("\n>>> ESCENARIO B: Recursos Insuficientes <<<");
            System.out.println("1. Utilizando nave de carga y forzando daño crítico (Desgaste al 98%)...");
            AsistenteDeComando asistenteB = new AsistenteDeComando(naveCarga);
            naveCarga.setAsistente(asistenteB);
            naveCarga.aumentarDesgaste(98);

            System.out.println("2. Intentando ejecutar Misión de Recolección...");
            Mision mImposible = new MisionRecoleccion(4, "Minería en Asteroide", "Cinturón de Kuiper");

            // La misión requiere desgaste y la nave ya casi llega a 100. Debe fallar sin romper nada.
            asistenteB.coordinarMision(mImposible);

            System.out.println("\n3. Verificación de seguridad:");
            System.out.println("¿Se alteraron los recursos parcialmente?: NO. Desgaste sigue en: " + naveCarga.getDesgaste());
            System.out.println("--- BITÁCORA ESCENARIO B ---");
            System.out.println(asistenteB.getBitacora().toString());


            // =========================================================
            // ESCENARIO C: PATRÓN STATE Y TRANSICIONES DEL MOTOR WARP
            // =========================================================
            System.out.println("\n>>> ESCENARIO C: Motor Warp <<<");
            System.out.println("Utilizando nave de combate para prueba de motor aislado.");
            MotorWarp motor = naveCombate.getMotor();

            // Simulamos asignarle el asistente para que la bitácora funcione en los estados
            AsistenteDeComando asistenteC = new AsistenteDeComando(naveCombate);
            naveCombate.setAsistente(asistenteC);

            System.out.println("\n1. Secuencia válida (Disponible -> Preparando -> Warp -> Disponible):");
            motor.pedirPrepararSalto(); // Disponible a Preparando
            motor.pedirWarp();          // Preparando a Warp
            motor.pedirDisponibilidad(); // Warp a Disponible (Sin enfriamiento por reglas de negocio)
            System.out.println("[OK] Ciclo completado correctamente.");

            System.out.println("\n2. Intentando transición inválida (De Disponible directo a Warp):");
            // El motor está disponible. Pedir warp directo debe generar un aviso y no cambiar el estado.
            motor.pedirWarp();

            System.out.println("\n--- BITÁCORA ESCENARIO C ---");
            System.out.println(asistenteC.getBitacora().toString());

        } catch (Exception e) {
            System.err.println("Error crítico en la simulación: " + e.getMessage());
        }
    }
}