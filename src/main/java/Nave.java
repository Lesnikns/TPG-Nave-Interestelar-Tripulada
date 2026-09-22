import java.util.ArrayList;

public abstract class Nave {
    private final int COMBUSTIBLE_MAX = 100;
    private final int ENERGIA_MAX = 100;
    private final int DESGASTE_MAX = 100;
    private final int DESGASTE_UMBRAL = 80;
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
    public void cargarEnergia(int cantidad){
        //errores: cantidad<0 o energia+cantidad>ENERGIA_MAX
        this.energia += cantidad;
    }
    public void cargarCombustible(int cantidad){
        //errores: cantidad<0 o combustible+cantidad>COMBUSTIBLE_MAX
        this.combustible += cantidad;
    }
    public void consumirEnergia(int cantidad){
        //errores: cantidad<0 o energia-cantidad<0
        this.energia -= cantidad;
    }
    public void consumirComustible(int cantidad){
        //errores: cantidad<0 o combustible-cantidad<0
        this.combustible -= cantidad;
    }
    public void aumentarDesgaste(int cantidad){
        //errores: cantidad<0 o desgaste+cantidad>DESGASTE_MAX
        this.desgaste += cantidad;
    }
    public boolean requiereMantenimieto(){
        return desgaste>=DESGASTE_UMBRAL;
    }
    public void realizarMantenimiento(){
        this.desgaste=0;
    }
    public boolean enEstadoOperativo(){
        return !requiereMantenimieto();
    }

    //- Sección de tripulación
    public ArrayList<Tripulante> getTripulacion(){
        return tripulacion;
    }
    //public void asignarTripulante(Tripulante t){}
    //public void eliminarTripulante(Tripulante t){}
    //public boolean verificaTripulacionMinima(){}

    }