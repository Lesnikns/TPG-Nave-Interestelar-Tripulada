

public class Enfriamiento implements EstadoMotor {

	@Override
	public void dejarDisponible(MotorWarp motor) {
		motor.setEstado(new Disponible());
		//llamada transicion a bitacora
	}
	@Override
	public void prepararSalto(MotorWarp motor) {
		//registro llamada invalida en bitacora
	}
	@Override
	public void warpear(MotorWarp motor) {
	
	}
	@Override
	public void enfriar(MotorWarp motor) {
		
	}
	
	public Enfriamiento() {
		super();
	}
}