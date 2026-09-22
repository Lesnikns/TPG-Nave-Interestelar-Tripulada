public class MotorWarp{
	private EstadoMotor estado;

	public MotorWarp(EstadoMotor estado) {
		super();
		this.estado = new Disponible();
	}
	
	protected EstadoMotor getEstado() {
		return estado;
	}
	protected void setEstado(EstadoMotor estado) {
		this.estado = estado;
	}

	protected void pedirDisponibilidad() {
		estado.dejarDisponible(this);
	}
	protected void pedirPrepararSalto() {
		estado.prepararSalto(this);
	}
	protected void pedirWarpeo() {
		estado.warpear(this);
	}
	protected void pedirEnfriamiento() {
		estado.enfriar(this);
	}
}
