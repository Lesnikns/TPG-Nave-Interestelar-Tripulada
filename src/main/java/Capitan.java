
public class Capitan extends CargoDecorator {
	
	public Capitan(Tripulante t, int antiguedad) {
		super(t, antiguedad);
	}
	
	public Capitan(Tripulante t) {
		super(t);
	}
	
	@Override
	public double getAporte() {
		return 1000 + (1000 * antiguedad * 0.20);
	}
	
	@Override
	public String getCargo() {
		return "Capitán";
	}
}
