import java.util.ArrayList;

public abstract class Nave {
    private final int COMBUSTIBLE_MAX = 100;
    private final int ENERGIA_MAX = 100;
    private final int DESGASTE_MAX = 100;
    private static int contadorId = 0;
    private int id;
    private String tipo;
    private int combustible;
    private int energia;
    private int desgaste;
    private MotorWarp motor;
    private ArrayList<Tripulante> tripulacion;

    public Nave(String tipo, int combustible, int energia, int desgaste){
        this.id= Nave.contadorId++;
        this.tipo = tipo;
        this.combustible= combustible;
        this.desgaste= desgaste;
        this.energia= energia;
        this.motor = new MotorWarp();
        this.tripulacion = new ArrayList<>();
    }

    //-- Sección de identidad
    public int getId(){
        return id;
    }
    public String getTipo(){
        return tipo;
    }

    //-- Sección de componentes -> todavía no sé qué métodos podría tener que implementar
    public MotorWarp getMotor(){
        return motor;
    }

    //-- Sección de recursos
    public int getCombustible(){
        return combustible;
    }
    public int getEnergia(){
        return energia;
    }
    public int getDesgaste(){
        return desgaste;
    }
    //public void cargarEnergia(int cantidad){}
    //public void cargarCombustible(int cantidad){}
    //public void consumirEnergia(int cantidad){}
    //public void consumirComustible(int cantidad){}
    //public void aumentarDesgaste(int cantidad){}
    //public boolean requiereMantenimieto(){}
    //public void realizarMantenimiento(){}
    //public boolean enEstadoOperativo(){}

    //- Sección de tripulación
    public ArrayList<Tripulante> getTripulacion(){
        return tripulacion;
    }
    //public void asignarTripulante(Tripulante t){}
    //public void eliminarTripulante(Tripulante t){}
    //public boolean verificaTripulacionMinima(){}

    }