
public abstract class Persona implements Tripulante{
	protected String nombre;
	
	public Persona(String nombre) {
		this.nombre = nombre;
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
