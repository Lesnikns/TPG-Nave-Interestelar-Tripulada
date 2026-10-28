import Asistente.*;
import Naves.FabricaDeNaves;
import Naves.Nave;
import Tripulacion.*;

/**
 * Clase main de demostración de la Entrega 1.
 * Ejecuta los escenarios A, B, C y D de la Ficha de Inicio, más Haberes y Mantenimiento.
 * Cada comprobación imprime [OK] o [FALLO] según el estado real de los objetos.
 */
public class Sistema {

    public static void main(String[] args) {
        System.out.println("Simulación Final");
        escenarioA();
        escenarioB();
        escenarioC();
        escenarioD();
        escenarioHaberes();
        escenarioMantenimiento();
    }

    // ---------------------------------------------------------------- Escenario A
    private static void escenarioA() {
        try {
            titulo("Escenario A — Ejecución correcta");

            // 1. Crear mediante Factory una nave de cada tipo
            FabricaDeNaves fabrica = new FabricaDeNaves();
            Nave exploradora = fabrica.getNave("exploradora");
            Nave carguero = fabrica.getNave("carguero");
            Nave combate = fabrica.getNave("combate");

            subtitulo("1. Naves creadas por la fábrica (configuración inicial)");
            mostrarRecursos("Exploradora", exploradora);
            mostrarRecursos("Carguero   ", carguero);
            mostrarRecursos("Combate    ", combate);

            // 2. Seleccionar una y asignarle una tripulación válida
            subtitulo("2. Tripulación de la nave exploradora");
            verificar("Antes de asignar, la tripulación mínima NO se cumple",
                    !exploradora.verificaTripulacionMinima());
            for (Tripulante t : tripulacionDeEjemplo()) {
                exploradora.asignarTripulante(t);
            }
            verificar("Tras asignar (1 capitán + 4 con cargo), la tripulación mínima se cumple",
                    exploradora.verificaTripulacionMinima());

            AsistenteDeComando asistente = new AsistenteDeComando(exploradora);
            exploradora.setAsistente(asistente);

            // 3 y 4. Preparar, ejecutar, evaluar y cerrar M-01, M-02 y M-03
            subtitulo("3. Misiones");
            Mision[] misiones = {
                    new MisionIntercepcionAsistencia(1, "Rescate de sonda", "Marte"),
                    new MisionRecoleccion(2, "Extraer Titanio", "Luna"),
                    new MisionRetornoSeguro(3, "Volver a base", "Tierra")
            };
            String[] codigos = {"M-01", "M-02", "M-03"};
            for (int i = 0; i < misiones.length; i++) {
                asistente.coordinarMision(misiones[i]);
                mostrarRecursos("Recursos tras " + codigos[i], exploradora);
            }

            // 5. Recursos finales y Bitácora
            subtitulo("4. Verificación de recursos finales");
            verificar("Combustible final 48 (60 - 3 x 4)", exploradora.getCombustible() == 48);
            verificar("Energía final 90 (80 + 5 + 5 + 0)", exploradora.getEnergia() == 90);
            verificar("Desgaste final 12 (3 x 4)", exploradora.getDesgaste() == 12);

            subtitulo("5. Bitácora (orden de inserción = orden temporal)");
            System.out.println(asistente.getBitacora().toString());

        } catch (Exception e) {
            System.out.println("Error en la simulación: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------- Escenario B
    private static void escenarioB() {
        try {
            titulo("Escenario B — Recursos insuficientes");
            FabricaDeNaves fabrica = new FabricaDeNaves();

            subtitulo("Caso 1: desgaste crítico (98)");
            Nave n1 = fabrica.getNave("carguero");
            n1.aumentarDesgaste(98);
            intentarMision(n1, new MisionRecoleccion(2, "Minería en Asteroide", "Cinturón de Kuiper"));

            subtitulo("Caso 2: combustible insuficiente (quedan 3 y la misión requiere 4)");
            Nave n2 = fabrica.getNave("carguero");
            n2.consumirCombustible(97);
            intentarMision(n2, new MisionIntercepcionAsistencia(1, "Rescate de sonda", "Marte"));

        } catch (Exception e) {
            System.out.println("Error en la simulación: " + e.getMessage());
        }
    }

    /** Intenta una misión sobre una nave que no puede hacerla y verifica que no queden cambios parciales. */
    private static void intentarMision(Nave nave, Mision mision) {
        AsistenteDeComando asistente = new AsistenteDeComando(nave);
        nave.setAsistente(asistente);

        int combustible = nave.getCombustible();
        int energia = nave.getEnergia();
        int desgaste = nave.getDesgaste();
        boolean motorDisponible = nave.getMotor().disponibleParaSaltar();

        mostrarRecursos("Antes  ", nave);
        asistente.coordinarMision(mision);
        mostrarRecursos("Después", nave);

        verificar("Sin cambios parciales: combustible, energía y desgaste intactos",
                nave.getCombustible() == combustible
                        && nave.getEnergia() == energia
                        && nave.getDesgaste() == desgaste);
        verificar("El motor sigue Disponible",
                motorDisponible && nave.getMotor().disponibleParaSaltar());
        System.out.println("Bitácora (debe registrar el motivo):");
        System.out.println(asistente.getBitacora().toString());
    }

    // ---------------------------------------------------------------- Escenario C
    private static void escenarioC() {
        try {
            titulo("Escenario C — Motor Warp");
            FabricaDeNaves fabrica = new FabricaDeNaves();
            Nave nave = fabrica.getNave("combate");
            AsistenteDeComando asistente = new AsistenteDeComando(nave);
            nave.setAsistente(asistente);

            subtitulo("1. Secuencia válida");
            verificar("Estado inicial: Disponible", nave.getMotor().disponibleParaSaltar());
            nave.getMotor().pedirPrepararSalto();
            verificar("Tras preparar salto: ya no está disponible", !nave.getMotor().disponibleParaSaltar());
            nave.getMotor().pedirWarp();
            verificar("En warp: no está disponible", !nave.getMotor().disponibleParaSaltar());
            nave.getMotor().pedirDisponibilidad();
            verificar("Tras cerrar el ciclo: Disponible", nave.getMotor().disponibleParaSaltar());
            // Limitación conocida: la secuencia pasa de En warp a Disponible sin recorrer Enfriamiento (ver 2.5).

            subtitulo("2. Transiciones inválidas");
            System.out.println("Pidiendo warp directo desde Disponible...");
            nave.getMotor().pedirWarp();
            verificar("Transición rechazada: el motor sigue Disponible", nave.getMotor().disponibleParaSaltar());

            nave.getMotor().pedirPrepararSalto(); // transición válida
            System.out.println("Pidiendo preparar salto estando en Preparando salto...");
            nave.getMotor().pedirPrepararSalto();
            verificar("Transición rechazada: el motor sigue en Preparando salto (no Disponible)",
                    !nave.getMotor().disponibleParaSaltar());

            subtitulo("3. Bitácora (cada rechazo debe figurar como Error)");
            System.out.println(asistente.getBitacora().toString());

        } catch (Exception e) {
            System.out.println("Error en la simulación: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------- Escenario D
    private static void escenarioD() {
        try {
            titulo("Escenario D — Contrato inválido");
            FabricaDeNaves fabrica = new FabricaDeNaves();
            Nave nave = fabrica.getNave("exploradora");
            AsistenteDeComando asistente = new AsistenteDeComando(nave);
            nave.setAsistente(asistente);

            subtitulo("1. Operación válida (control)");
            int energiaInicial = nave.getEnergia();
            boolean cargaValida = nave.cargarEnergia(10);
            verificar("Carga de 10 de energía aceptada", cargaValida);
            verificar("Energía = inicial + 10", nave.getEnergia() == energiaInicial + 10);

            subtitulo("2. Cargas que exceden la capacidad (tope 100)");
            int energiaAntes = nave.getEnergia();
            int combustibleAntes = nave.getCombustible();

            boolean energiaAceptada = nave.cargarEnergia(5000);
            verificar("cargarEnergia(5000) devuelve false (rechazo completo)", !energiaAceptada);
            verificar("Energía conservada (" + energiaAntes + ")", nave.getEnergia() == energiaAntes);

            asistente.cargarEnergiaNave(5000); // mismo intento, a través del Asistente
            verificar("Energía conservada también a través del Asistente", nave.getEnergia() == energiaAntes);

            boolean combustibleAceptado = nave.cargarCombustible(5000);
            verificar("cargarCombustible(5000) devuelve false (rechazo completo)", !combustibleAceptado);
            verificar("Combustible conservado (" + combustibleAntes + ")", nave.getCombustible() == combustibleAntes);

        } catch (Exception e) {
            System.out.println("Error en la simulación: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------- Haberes
    private static void escenarioHaberes() {
        try {
            titulo("Haberes — 4 cargos x 3 orígenes (5 años de antigüedad; Consejero con 10 consejos)");

            String[] cargos = {"Capitán", "Consejero", "Teniente", "Alférez"};
            String[] origenes = {"Vulcano", "Marciano", "Terricola"};
            int[][] esperado = {
                    {2030, 2018, 2020},
                    {800, 788, 790},
                    {490, 478, 480},
                    {235, 223, 225}
            };

            for (int c = 0; c < cargos.length; c++) {
                for (int o = 0; o < origenes.length; o++) {
                    Tripulante t = cargo(cargos[c], origen(origenes[o], "Tripulante"));
                    double sueldo = t.getSueldo();
                    verificar(String.format("%-9s %-9s esperado=%d obtenido=%.1f",
                                    cargos[c], origenes[o], esperado[c][o], sueldo),
                            Math.abs(sueldo - esperado[c][o]) < 0.001);
                }
            }

            subtitulo("Liquidación mensual de la tripulación de ejemplo (a través del Asistente)");
            Nave nave = new FabricaDeNaves().getNave("exploradora");
            double total = 0;
            for (Tripulante t : tripulacionDeEjemplo()) {
                nave.asignarTripulante(t);
                total += t.getSueldo();
            }
            AsistenteDeComando asistente = new AsistenteDeComando(nave);
            nave.setAsistente(asistente);
            asistente.liquidarHaberesMensuales();
            verificar(String.format("Total esperado 5100 (3020 + 1180 + 456 + 224 + 220), obtenido %.1f", total),
                    Math.abs(total - 5100) < 0.001);
            System.out.println(asistente.getBitacora().toString());

        } catch (Exception e) {
            System.out.println("Error en la simulación: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------- Mantenimiento
    private static void escenarioMantenimiento() {
        try {
            titulo("Mantenimiento — recursos antes y después de una misión y de un mantenimiento");
            Nave nave = new FabricaDeNaves().getNave("exploradora");
            AsistenteDeComando asistente = new AsistenteDeComando(nave);
            nave.setAsistente(asistente);

            mostrarRecursos("Inicial", nave);
            asistente.coordinarMision(new MisionRetornoSeguro(3, "Volver a base", "Tierra"));
            mostrarRecursos("Tras la misión", nave);
            verificar("Con desgaste 4 no requiere mantenimiento", !nave.requiereMantenimiento());

            subtitulo("Desgaste en el umbral (80)");
            nave.aumentarDesgaste(76);
            mostrarRecursos("Con desgaste alto", nave);
            verificar("Requiere mantenimiento", nave.requiereMantenimiento());
            verificar("La nave deja de estar operativa", !nave.enEstadoOperativo());
            System.out.println("Intentando una misión con la nave sin operatividad...");
            asistente.coordinarMision(new MisionRetornoSeguro(3, "Volver a base", "Tierra"));
            mostrarRecursos("Tras el intento", nave);

            subtitulo("Mantenimiento");
            int combustible = nave.getCombustible();
            int energia = nave.getEnergia();
            nave.realizarMantenimiento();
            mostrarRecursos("Tras el mantenimiento", nave);
            verificar("Desgaste = 0", nave.getDesgaste() == 0);
            verificar("Combustible y energía sin cambios",
                    nave.getCombustible() == combustible && nave.getEnergia() == energia);
            verificar("La nave vuelve a estar operativa", nave.enEstadoOperativo());

            System.out.println("Repitiendo la misión tras el mantenimiento...");
            asistente.coordinarMision(new MisionRetornoSeguro(3, "Volver a base", "Tierra"));
            mostrarRecursos("Tras la misión", nave);

            subtitulo("Bitácora");
            System.out.println(asistente.getBitacora().toString());

        } catch (Exception e) {
            System.out.println("Error en la simulación: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------- Auxiliares
    /** Tripulación mínima válida: 1 capitán y 4 tripulantes más, todos con cargo. */
    private static Tripulante[] tripulacionDeEjemplo() {
        return new Tripulante[] {
                new Capitan(new Terricola("Kirk"), 10),
                new Consejero(new Vulcano("Spock"), 15, 50),
                new Teniente(new Terricola("Bones"), 3),
                new Alferez(new Terricola("Uhura"), 4),
                new Alferez(new Marciano("Marvin"), 2)
        };
    }

    private static Tripulante origen(String origen, String nombre) {
        switch (origen) {
            case "Vulcano":
                return new Vulcano(nombre);
            case "Marciano":
                return new Marciano(nombre);
            default:
                return new Terricola(nombre);
        }
    }

    /** Envuelve al tripulante con el cargo indicado, con 5 años de antigüedad (Consejero: 10 consejos). */
    private static Tripulante cargo(String cargo, Tripulante base) {
        switch (cargo) {
            case "Capitán":
                return new Capitan(base, 5);
            case "Consejero":
                return new Consejero(base, 5, 10);
            case "Teniente":
                return new Teniente(base, 5);
            default:
                return new Alferez(base, 5);
        }
    }

    private static void mostrarRecursos(String etiqueta, Nave nave) {
        System.out.println(etiqueta + ": combustible=" + nave.getCombustible()
                + ", energía=" + nave.getEnergia()
                + ", desgaste=" + nave.getDesgaste());
    }

    private static void verificar(String descripcion, boolean condicion) {
        System.out.println((condicion ? "[OK] " : "[FALLO] ") + descripcion);
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("=== " + texto + " ===");
        System.out.println();
    }

    private static void subtitulo(String texto) {
        System.out.println();
        System.out.println("--- " + texto + " ---");
    }
}