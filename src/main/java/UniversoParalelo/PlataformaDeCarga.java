package UniversoParalelo;

import Eventos.ObservadoModelo;

/**
 * Plataforma de Carga del Visor Recondito: el recurso compartido del patron Productor-Consumidor, resuelto como monitor de Java.
 * Las generadoras depositan baterias y las naves consumidoras las retiran. Tiene capacidad limitada y acceso exclusivo:
 * sus metodos son synchronized, asi que en cada instante una sola entidad la usa (o una generadora depositando, o una nave retirando).
 * Si esta llena, la generadora espera (wait) hasta que haya lugar; si esta vacia, la nave espera hasta que haya una bateria.
 * Las condiciones de guarda se evaluan en un while, para volver a comprobarlas despues de despertar, y cada cambio despierta a todos los que esperan (notifyAll).
 * Es observable (patron Observer): informa cada evento como texto a sus observadores, por ejemplo la consola del visor.
 * <b>invariantes:</b> 0 &lt;= contadorBaterias &lt;= CAPACIDAD_LIMITADA
 */
public class PlataformaDeCarga extends ObservadoModelo<String> {
    private final static int CAPACIDAD_LIMITADA = 5; //arbitrario
    private int contadorBaterias;

    /**
     * Deposita una bateria en la plataforma. Si la plataforma esta llena, el hilo que llama espera hasta que una nave retire una bateria
     * <b>pre:</b> nombre != null y nombre != "" (nombre de la generadora que deposita) <br>
     * <b>post:</b> la plataforma tiene una bateria mas (sin superar la capacidad), se informaron las esperas y el deposito, y se desperto a los hilos que esperaban
     * @param nombre - nombre de la generadora que deposita, para los mensajes del visor. nombre != null, nombre != ""
     * @throws InterruptedException si el hilo es interrumpido mientras espera lugar en la plataforma
     */
    public synchronized void depositar(String nombre) throws InterruptedException {
        assert nombre != null && !nombre.isEmpty() : "el nombre de la generadora no puede ser nulo ni vacio";
        while (contadorBaterias >= CAPACIDAD_LIMITADA) {
            informar(nombre + " esperando a que se libere espacio en la Plataforma.");
            wait();
        }
        this.contadorBaterias++;
        informar("Bateria depositada por " + nombre + " (hay " + this.contadorBaterias + " de " + CAPACIDAD_LIMITADA + ").");
        notifyAll();
    }

    /**
     * Retira una bateria de la plataforma. Si la plataforma esta vacia, el hilo que llama espera hasta que una generadora deposite una bateria
     * <b>pre:</b> nombre != null y nombre != "" (nombre de la nave que retira) <br>
     * <b>post:</b> la plataforma tiene una bateria menos (sin bajar de cero), se informaron las esperas y el retiro, y se desperto a los hilos que esperaban
     * @param nombre - nombre de la nave que retira, para los mensajes del visor. nombre != null, nombre != ""
     * @throws InterruptedException si el hilo es interrumpido mientras espera una bateria
     */
    public synchronized void retirar(String nombre) throws InterruptedException {
        assert nombre != null && !nombre.isEmpty() : "el nombre de la nave no puede ser nulo ni vacio";
        while (contadorBaterias <= 0) {
            informar(nombre + " esperando por falta de baterias en la Plataforma.");
            wait();
        }
        this.contadorBaterias--;
        informar("Bateria retirada y utilizada por " + nombre + " (quedan " + this.contadorBaterias + " de " + CAPACIDAD_LIMITADA + ").");
        notifyAll();
    }

    /**
     * Informa un evento del universo paralelo a los observadores de la plataforma. Lo usan la propia plataforma, las generadoras y las naves para emitir sus mensajes
     * <b>pre:</b> evento != null y evento != "" <br>
     * <b>post:</b> se notifico el evento a todos los observadores suscriptos. La plataforma no cambio
     * @param evento - mensaje a mostrar en el visor. evento != null, evento != ""
     */
    public synchronized void informar(String evento) {
        assert evento != null && !evento.isEmpty() : "el evento no puede ser nulo ni vacio";
        notificar(evento);
    }

    /**
     * Se construye una plataforma vacia, sin baterias y sin observadores
     * <b>pre:</b> ninguna <br>
     * <b>post:</b> se ha creado la plataforma con 0 baterias
     */
    public PlataformaDeCarga() {
        super();
        this.contadorBaterias = 0;
    }
}
