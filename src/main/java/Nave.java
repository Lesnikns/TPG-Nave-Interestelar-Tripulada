import java.util.ArrayList;

public abstract class Nave {
    private static int contadorId = 0;
    private int id;
    private String tipo;
    private int combustible;
    private int energia;
    private int desgaste;
    private MotorWarp motor;
    private ArrayList<Tripulante> tripulacion;

    public Nave(){}

    //-- Sección de identidad
    //public int getId(){}
    //public String getTipo(){}

    //-- Sección de componentes
    //public MotorWarp getMotor(){}

    //-- Sección de recursos
    //public int getCombustible(){}
    //public int getEnergia(){}
    //public int getDesgaste(){}
    //public void cargarEnergia(int cantidad){}
    //public void cargarCombustible(int cantidad){}
    //public void consumirEnergia(int cantidad){}
    //public void consumirComustible(int cantidad){}
    //public void aumentarDesgaste(int cantidad){}
    //public boolean requiereMantenimieto(){}
    //public void realizarMantenimiento(){}
    //public boolean enEstadoOperativo(){}

    //- Sección de tripulación
    //public ArrayList<Tripulante> getTripulacion(){}
    //public void asignarTripulante(Tripulante t){}
    //public void eliminarTripulante(Tripulante t){}
    //public boolean verificaTripulacionMinima(){}

    //-- Sección de componentes -> todavía no sé qué métodos podría tener que implementar
    }