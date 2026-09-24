public class MotorWarp{
	private EstadoMotor estado;
	private String nombreEstado;
	private Nave nave;

	protected String getNombreEstado() {
		return this.nombreEstado;
	}
	public void setNombreEstado(String nombreEstado) {
		this.nombreEstado = nombreEstado;
	}
	public void setEstado(EstadoMotor estado) {
		this.estado = estado;
	}

	protected void aplicarCostos(int combustible, int desgaste, int energia ){
		this.nave.consumirCombustible(combustible);
		this.nave.aumentarDesgaste(desgaste);
		this.nave.cargarEnergia(energia);
	}

	protected boolean disponibleParaSaltar(){
		return this.nombreEstado.equals("Disponible");
	}

	protected void pedirDisponibilidad() {
		estado.cerrar(this);
	}
	protected void pedirPrepararSalto() { estado.preparar(this); }
	protected void pedirWarp() {
		estado.saltar(this);
	}
	//protected void pedirEnfriamiento() { estado.enfriar(this); }

	public MotorWarp(Nave nave) {
		this.estado = new Disponible(nave);
		this.nave = nave;
		this.nombreEstado = "Disponible";
	}
}
