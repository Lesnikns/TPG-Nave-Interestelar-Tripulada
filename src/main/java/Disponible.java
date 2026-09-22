public class Disponible implements EstadoMotor {

	@Override
	public void dejarDisponible(MotorWarp motor) {
		//recordar mandar transiciones a bitacora para ambas invalidas y validas (sea coleccion recursos, misiones, etc.)
		Entrada e = new Entrada(null, null);
		//la bitacora se supone seria un campo de la nave 
	}
	@Override
	public void prepararSalto(MotorWarp motor) {
		motor.setEstado(new PreparandoSalto());
	}
	@Override
	public void warpear(MotorWarp motor) {
		//devolver entrada erronea
	}
	@Override
	public void enfriar(MotorWarp motor) {
		//idem
	}
	
	public Disponible() {
		super();
	}
}
