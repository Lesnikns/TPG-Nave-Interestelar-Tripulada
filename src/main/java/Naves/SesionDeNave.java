package Naves;

import Eventos.ObservadoModelo;
import Tripulacion.Tripulante;

import java.util.List;

// Guarda la única nave activa.
public class SesionDeNave extends ObservadoModelo<String> {
    private Nave nave;

    public boolean hayNave() {
        return nave != null;
    }

    public Nave getNave() {
        return nave;
    }

    /**
     * Establece la nave activa, reemplazando a la anterior (y su tripulación)
     * @pre: nave != null
     * @post: Se avisa a los observadores y cambia la nave actual
     */
    public void setNave(Nave nave) {
        assert nave != null : "La nave no puede ser nula";
        this.nave = nave;
        notificar("Nueva nave asignada de tipo " + nave.getTipo());
    }

    /**
     * Asigna un tripulante a la nave activa.
     * @pre: nave != null;
     * @post: si devuelve true, el tripulante forma parte de la tripulación y se avisa a los observadores
     * @return false si no se pudo asignar (es nulo o ya estaba)
     */
    public boolean agregarTripulante(Tripulante t) {
        assert nave != null : "No hay una nave activa";
        boolean agregado = nave.asignarTripulante(t);
        if (agregado)
            notificar("Tripulante agregado: " + t.getNombre());
        return agregado;
    }

    public List<Tripulante> getTripulantes() {
        if (nave == null) {
            return List.of();
        }
        return nave.getTripulacion();
    }
}