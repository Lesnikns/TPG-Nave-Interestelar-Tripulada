package Tripulacion;

public abstract class CargoDecorator implements Tripulante {
	private Tripulante tripulante;
	protected int antiguedad;
	
	/**
	 * Crea un tripulante con cargo a partir de uno que no tenía. 
	 * 
	 * @pre t != null, t.getCargo() == "Sin cargo", antiguedad >= 0 o nulo.
	 * @post Se crea un tripulante decorado con cargo y antiguedad correctamente.
	 * 
	 * @param t El tripulante al cual se le va a aplicar el decorator (no debe tener cargo)
	 * @param antiguedad La antiguedad en años del tripulante.
	 */
	public CargoDecorator(Tripulante t, int antiguedad) {
		assert t.getCargo() == "Sin cargo": "El tripulante no puede tener 2 cargos.";
		assert t != null : "El tripulante no puede ser nulo";
	    assert antiguedad >= 0 : "La antiguedad no puede ser negativa";
	    
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