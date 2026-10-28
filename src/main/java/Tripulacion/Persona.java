package Tripulacion;

public abstract class Persona implements Tripulante{
	protected String nombre;
	
	
	
	/**
	 * Constructor de una persona sin cargo.
	 * 
	 * @pre nombre != null
	 * @post Crea una nueva persona que será utilizada para aplicarle el decorator CargoDecorator.
	 * 
	 * @param nombre El nombre que tendrá la persona.
	 */
	public Persona(String nombre) {
		assert nombre != null && !nombre.trim().isEmpty() : "El nombre no puede ser nulo ni vacío";
		
		this.nombre = nombre;
	}

	@Override
	public String getCargo() {
		return "Sin cargo";
	}
	public String getNombre() {
		return nombre;
	}
	
	
	public String getDescripcion() {
		return "Origen (" + this.getOrigen() + "): " + this.nombre + "\n";
	}
	
	
	abstract public String getOrigen();
	abstract public double getSueldo();
}
