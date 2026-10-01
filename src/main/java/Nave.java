import java.util.ArrayList;

public abstract class Nave {
    private static final int COMBUSTIBLE_MAX = 100;
    private static final int ENERGIA_MAX = 100;
    private static final int DESGASTE_MAX = 100;
    private static final int DESGASTE_UMBRAL = 80;
    private static int contadorId = 0;
    private int id;
    private String tipo;
    private int combustible;
    private int energia;
    private int desgaste;
    private MotorWarp motor;
    private AsistenteDeComando asistente;
    private ArrayList<Tripulante> tripulacion;

    public Nave(String tipo, int combustible, int energia, int desgaste){
        if (combustible < 0 || combustible > COMBUSTIBLE_MAX
                || energia < 0 || energia > ENERGIA_MAX
                || desgaste < 0 || desgaste > DESGASTE_MAX) {
            //... excepción
        }
        this.id= Nave.contadorId++;
        this.tipo = tipo;
        this.combustible= combustible;
        this.desgaste= desgaste;
        this.energia= energia;
        this.motor = new MotorWarp(this);
        this.asistente = new AsistenteDeComando(this);
        this.tripulacion = new ArrayList<>();
    }

    //-- Sección de identidad
    public int getId(){
        return id;
    }
    public String getTipo(){
        return tipo;
    }

    //-- Sección de componentes
    public MotorWarp getMotor(){
        return motor;
    }
    public AsistenteDeComando getAsistente() {
        return asistente;
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
        if(cantidad < 0 || this.energia + cantidad > ENERGIA_MAX){
            //... excepción
        }
        this.energia += cantidad;
    }
    public void cargarCombustible(int cantidad){
        if(cantidad < 0 || this.combustible + cantidad > COMBUSTIBLE_MAX){
            //... excepción
        }
        this.combustible += cantidad;
    }
    public void consumirEnergia(int cantidad){
        if(cantidad < 0 || this.energia - cantidad < 0){
            //... excepción
        }
        this.energia -= cantidad;
    }
    public void consumirCombustible(int cantidad){
        if (cantidad < 0 || this.combustible - cantidad < 0) {
            //... excepción
        }
        this.combustible -= cantidad;
    }
    public void aumentarDesgaste(int cantidad){
        if(cantidad < 0 || this.desgaste + cantidad > DESGASTE_MAX){
            //... excepción
        }
        this.desgaste += cantidad;
    }
    public boolean requiereMantenimiento(){
        return desgaste>=DESGASTE_UMBRAL;
    }
    public void realizarMantenimiento(){
        this.desgaste=0;
    }
    public boolean enEstadoOperativo(){
        //si entendí, el motor debe estar en estado disponible? -> no encontré nada
        //return !requiereMantenimiento() && getMotor().getNombreEstado(motor)=="disponible";
        return !requiereMantenimiento();
    }

    //--- Subsección de costos simultáneos -> Asistente de Comando debe tener equivalentes y delegarlos a estos.
    // tener en cuenta que son costos; la recompensa de la misión se puede hacer con cargarEnergia(), sino debería cancelar una misión porque no puedo guardar la recompensa, suena ilógico
    public boolean puedeAplicar(int combustible, int desgaste, int energia){
        return (combustible>=0 && combustible<=this.combustible
                && energia>=0  && energia<=this.energia
                && desgaste>=0 && desgaste<=DESGASTE_MAX-this.desgaste);
    }
    public void aplicarCostos(int combustible, int desgaste, int energia){
        if(puedeAplicar(combustible,desgaste,energia)){
            this.combustible-=combustible;
            this.energia-=energia;
            this.desgaste+=desgaste;
        }else{
            //...
        }
    }

    //- Sección de tripulación
    public ArrayList<Tripulante> getTripulacion(){
        return tripulacion;
    }
    public void asignarTripulante(Tripulante t) {
        if (t == null) {
            //...
        }
        if (tripulacion.contains(t)) {
            //,,,
        }
        // duda: si el enunciado SÍ limita a un solo capitán,
        // debería verificarlo acá -> no encontré nada,
        // excepto que el capitán parece tener cierta funcionalidad,
        // que no se detalla, sobre decidir ciertas cosas,
        // y ahí debería ser único (creo)
        tripulacion.add(t);
    }
    public void eliminarTripulante(Tripulante t) {
        if (!tripulacion.remove(t)) {
            //...
        }
    }
    public boolean verificaTripulacionMinima() {
        boolean hayCapitan = false;

        for (Tripulante t : this.getTripulacion()) {
            if ("capitan".equals(t.getCargo())) { // problema: Tripulante no tiene getCargo() -> consultar a Ramiro -> también alcanza con boolean esCapitan()
                hayCapitan = true;
                break;
            }
        }

        return hayCapitan && this.getTripulacion().size() >= 5;
    }
}