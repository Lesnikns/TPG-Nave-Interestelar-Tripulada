package UniversoParalelo;

import java.util.Random;

import static java.lang.Thread.sleep;

/**
 * Productor del Visor Recondito (patron Productor-Consumidor): una generadora de energia del universo paralelo.
 * Corre en su propio hilo y repite su ciclo: produce una bateria (tarda 1, 2 o 3 segundos), lo informa y la deposita en la Plataforma de Carga.
 * Si la plataforma esta llena, el hilo espera dentro de depositar hasta que haya lugar. Toda la sincronizacion esta en la plataforma (el monitor):
 * esta clase no usa synchronized salvo para numerar las generadoras.
 * Cada generadora tiene un nombre unico ("Generadora 1", "Generadora 2", ...) que identifica sus mensajes en el visor.
 * <b>invariantes:</b> plataforma != null <br>
 * nombre != null y nombre != ""
 */
public class GeneradoraDeEnergia implements Runnable {
    private final PlataformaDeCarga plataforma;
    private final String nombre;
    private static int contador = 0;
    private static final Random random = new Random();

    /**
     * Devuelve el nombre de la generadora
     * <b>pre:</b> ninguna <br>
     * <b>post:</b> se devolvio el nombre, sin modificar la generadora
     * @return nombre de la generadora ("Generadora N")
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Pone en marcha la generadora: anuncia su ingreso en el visor y la ejecuta en un hilo nuevo, daemon, para que no impida cerrar la aplicacion
     * <b>pre:</b> la generadora no fue iniciada antes <br>
     * <b>post:</b> se informo el ingreso de la generadora y su ciclo quedo corriendo en un hilo nuevo con su nombre
     */
    public void iniciar() {
        plataforma.informar("Ingreso una nueva Generadora al universo paralelo! (" + nombre + ")");
        Thread hilo = new Thread(this, nombre);
        hilo.setDaemon(true);
        hilo.start();
    }

    /**
     * Ciclo de la generadora: produce una bateria, lo informa y la deposita en la plataforma, esperando si esta llena.
     * El ciclo no termina solo: el hilo termina si es interrumpido, o junto con la aplicacion por ser daemon
     * <b>pre:</b> se ejecuta en el hilo creado por iniciar() <br>
     * <b>post:</b> cada bateria producida quedo depositada en la plataforma; si el hilo fue interrumpido, salio del ciclo y termino
     */
    @Override
    public void run() {
        try {
            while (true) {
                sleep(1000 + 1000 * random.nextInt(0, 3)); // tiempo de produccion: 1, 2 o 3 segundos
                plataforma.informar("Bateria producida por " + nombre + ".");
                plataforma.depositar(nombre);
            }
        } catch (InterruptedException e) {
            // el hilo fue interrumpido: sale del ciclo y termina
        }
    }

    /**
     * Devuelve el siguiente numero de generadora. Es synchronized para que dos generadoras creadas desde hilos distintos no reciban el mismo numero
     * <b>pre:</b> ninguna <br>
     * <b>post:</b> el contador aumento en uno y se devolvio su nuevo valor
     * @return numero de la nueva generadora (1, 2, 3, ...)
     */
    private static synchronized int siguienteNumero() {
        contador++;
        return contador;
    }

    /**
     * Se construye una generadora asociada a la plataforma, con el siguiente nombre disponible. No empieza a producir hasta que se llama a iniciar()
     * <b>pre:</b> plataforma != null <br>
     * <b>post:</b> se ha creado la generadora con la plataforma dada y un nombre unico ("Generadora N")
     * @param plataforma - plataforma de carga donde deposita las baterias. plataforma != null
     */
    public GeneradoraDeEnergia(PlataformaDeCarga plataforma) {
        assert plataforma != null : "la generadora necesita una plataforma";
        this.plataforma = plataforma;
        this.nombre = "Generadora " + siguienteNumero();
    }
}
