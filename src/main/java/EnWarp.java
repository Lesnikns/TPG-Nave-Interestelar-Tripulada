public class EnWarp implements EstadoMotor {
	private Nave nave;
	private AsistenteDeComando asistente;

	@Override
	public void cerrar(MotorWarp motor) {
		motor.setEstado(new Disponible(this.nave));
		motor.setNombreEstado("EnWarp");
		asistente.escribeBitacora("transicion a estado: Preparacion de salto","Transicion");
	}
	@Override
	public void preparar(MotorWarp motor) {
		asistente.escribeBitacora("No permitida transicion a estado: Disponible","Error");
	}
	@Override
	public void saltar(MotorWarp motor) {
		asistente.escribeBitacora("No permitida transicion a estado: Disponible","Error");
	}
	
	public EnWarp(Nave nave) {
		super();
		this.nave = nave;
		this.asistente = nave.getAsistente();
	}
}