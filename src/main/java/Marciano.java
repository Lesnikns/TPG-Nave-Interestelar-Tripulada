
public class Marciano extends Persona {

	public Marciano(String nombre) {
		super(nombre);
	}
	
	@Override
	public String getOrigen() {
		return "Marciano";
	}
	
	@Override
	public double getSueldo() {
		return 18;
	}
}
