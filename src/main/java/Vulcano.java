
public class Vulcano extends Persona{
	
	public Vulcano(String nombre) {
		super(nombre);
	}
	
	@Override
	public String getOrigen() {
		return "Vulcano";
	}
	
	@Override
	public double getSueldo() {
		return 30;
	}
}
