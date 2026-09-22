public class PreparandoSalto implements EstadoMotor {

	@Override
	public void dejarDisponible(MotorWarp motor) {
		//bitacora
	}
	@Override
	public void prepararSalto(MotorWarp motor) {
		//bitacora
	}
	@Override
	public void warpear(MotorWarp motor) {
		motor.setEstado(new EnWarp());
	}
	@Override
	public void enfriar(MotorWarp motor) {
		//bitacora
	}
	
	public PreparandoSalto() {
		super();
	}
}