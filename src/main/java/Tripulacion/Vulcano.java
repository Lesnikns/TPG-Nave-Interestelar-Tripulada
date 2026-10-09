package Tripulacion;

public class Vulcano extends Persona{
	
	public Vulcano(String nombre) {
		super(nombre);
	}
	
	@Override
	public String getOrigen() {
		return "Tripulación.Vulcano";
	}
	
	@Override
	public double getSueldo() {
		return 30;
	}
}
