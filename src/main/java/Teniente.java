public class Teniente extends CargoDecorator {
	
	public Teniente(Tripulante t, int antiguedad) {
		super(t, antiguedad);
	}
	
	public Teniente(Tripulante t) {
		super(t);
	}
	
	@Override
	public double getAporte() {
		return 400 + (400 * antiguedad * 0.03);
	}
	
	
	@Override
	public String getCargo() {
		return "Teniente";
	}
	
	
}
