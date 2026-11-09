package Naves;

import DestinoTablero.Asteroide;
import DestinoTablero.ElementoDelUniverso;
import DestinoTablero.Posicion;
import DestinoTablero.Tablero;

public class RadarCortoAlcance {
    //solo se considera obstáculo al Asteroide
    public boolean hayObstaculoAdelante(Tablero tablero, Posicion siguiente){
        ElementoDelUniverso[][] alrededores= tablero.observarAlrededores(); // mi posición será [1][1]
        Posicion actual = tablero.getPosicionNave();
        int dx= siguiente.getX()-actual.getX();
        int dy= siguiente.getY()-actual.getY();
        if(Math.abs(dx)>1 || Math.abs(dy)>1) //siguiente no es de los adyacentes
            return false;
        return alrededores[1+dx][1+dy] instanceof Asteroide;
    }
}
