package Asistente;

import Eventos.ObservadoModelo;

public class PublicadorDeTurnos extends ObservadoModelo<EstadoTurno> {
    public void publicar(EstadoTurno estado) {
        notificar(estado);
    }
}