public class EnWarp implements EstadoMotor {

	@Override
	public void dejarDisponible(MotorWarp motor) {
		//bitacora invalido
	}
	@Override
	public void prepararSalto(MotorWarp motor) {
		//bitacora
	}
	@Override
	public void warpear(MotorWarp motor) {
		//bitacora
	}
	@Override
	public void enfriar(MotorWarp motor) {
		motor.setEstado(new Enfriamiento());
	}
	
	public EnWarp() {
		super();
	}
}