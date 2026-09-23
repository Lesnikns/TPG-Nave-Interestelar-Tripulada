public abstract class CargoDecorator implements Tripulante {
	private Tripulante tripulante;
	protected int antiguedad;
	
	public CargoDecorator(Tripulante t, int antiguedad) {
		this.tripulante = t;
		this.antiguedad = antiguedad;
	}
	
	public CargoDecorator(Tripulante t) {
		this.tripulante = t;
		this.antiguedad = 0;
	}
	
	public Tripulante getTripulante() {
		return tripulante;
	}
	
	public void setTripulante(Tripulante t) {
		tripulante = t;
	}
	
	@Override
	public String getOrigen() {
		return getTripulante().getOrigen();
	}
	
	@Override
	public String getNombre() {
		return getTripulante().getNombre();
	}
	
	@Override
	public double getSueldo() {
		return getTripulante().getSueldo() + this.getAporte();
	}
	
	public int getAntiguedad() {
		return antiguedad;
	}
	
	
	@Override
	public String getDescripcion() {
		return getTripulante().getDescripcion() 
			+ "Cargo: " + this.getCargo() 
			+ " | Antigüedad: " + antiguedad + " años" ;
	}
	
	public abstract double getAporte();
	
	abstract public String getCargo();
}