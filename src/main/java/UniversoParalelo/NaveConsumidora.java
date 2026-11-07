package UniversoParalelo;

import java.util.Random;

import static java.lang.Thread.sleep;

/**
 * Consumidor del Visor Recondito (patron Productor-Consumidor): una nave desconocida del universo paralelo.
 * Corre en su propio hilo y repite su ciclo: solicita una bateria, la retira de la Plataforma de Carga y la usa (tarda 1, 2 o 3 segundos).
 * Si la plataforma esta vacia, el hilo espera dentro de retirar hasta que una generadora deposite. Toda la sincronizacion esta en la plataforma (el monitor):
 * esta clase no usa synchronized salvo para numerar las naves.
 * No hereda de Naves.Nave: es de otra dimension y no tiene motor, asistente, tripulacion ni recursos.
 * Cada nave tiene un nombre unico ("Nave 1", "Nave 2", ...) que identifica sus mensajes en el visor.
 * <b>invariantes:</b> plataforma != null <br>
 * nombre != null y nombre != ""
 */
public class NaveConsumidora implements Runnable {
    private final PlataformaDeCarga plataforma;
    private final String nombre;
    private static int contador = 0;
    private static final Random random = new Random();

    /**
     * Devuelve el nombre de la nave
     * <b>pre:</b> ninguna <br>
     * <b>post:</b> se devolvio el nombre, sin modificar la nave
     * @return nombre de la nave ("Nave N")
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Pone en marcha la nave: anuncia su ingreso en el visor y la ejecuta en un hilo nuevo, daemon, para que no impida cerrar la aplicacion
     * <b>pre:</b> la nave no fue iniciada antes <br>
     * <b>post:</b> se informo el ingreso de la nave y su ciclo quedo corriendo en un hilo nuevo con su nombre
     */
    public void iniciar() {
        plataforma.informar("Ingreso una nueva Nave al universo paralelo! (" + nombre + ")");
        Thread hilo = new Thread(this, nombre);
        hilo.setDaemon(true);
        hilo.start();
    }

    /**
     * Ciclo de la nave: solicita una bateria, la retira de la plataforma (esperando si esta vacia) y la usa.
     * El ciclo no termina solo: el hilo termina si es interrumpido, o junto con la aplicacion por ser daemon
     * <b>pre:</b> se ejecuta en el hilo creado por iniciar() <br>
     * <b>post:</b> cada bateria solicitada fue retirada de la plataforma; si el hilo fue interrumpido, salio del ciclo y termino
     */
    @Override
    public void run() {
        try {
            while (true) {
                plataforma.informar(nombre + " solicitando bateria.");
                plataforma.retirar(nombre);
                sleep(1000 + 1000 * random.nextInt(0, 3)); // tiempo de uso de la bateria: 1, 2 o 3 segundos
            }
        } catch (InterruptedException e) {
            // el hilo fue interrumpido: sale del ciclo y termina
        }
    }

    /**
     * Devuelve el siguiente numero de nave. Es synchronized para que dos naves creadas desde hilos distintos no reciban el mismo numero
     * <b>pre:</b> ninguna <br>
     * <b>post:</b> el contador aumento en uno y se devolvio su nuevo valor
     * @return numero de la nueva nave (1, 2, 3, ...)
     */
    private static synchronized int siguienteNumero() {
        contador++;
        return contador;
    }

    /**
     * Se construye una nave asociada a la plataforma, con el siguiente nombre disponible. No empieza a pedir baterias hasta que se llama a iniciar()
     * <b>pre:</b> plataforma != null <br>
     * <b>post:</b> se ha creado la nave con la plataforma dada y un nombre unico ("Nave N")
     * @param plataforma - plataforma de carga de donde retira las baterias. plataforma != null
     */
    public NaveConsumidora(PlataformaDeCarga plataforma) {
        assert plataforma != null : "la nave necesita una plataforma";
        this.plataforma = plataforma;
        this.nombre = "Nave " + siguienteNumero();
    }
}
