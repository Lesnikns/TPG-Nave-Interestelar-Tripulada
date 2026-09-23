public class Alferez extends CargoDecorator {
	
	public Alferez(Tripulante t, int antiguedad) {
		super(t, antiguedad);
	}
	
	public Alferez(Tripulante t) {
		super(t);
	}
	
	@Override
	public double getAporte() {
		return 200 + (200 * antiguedad * 0.005);
	}
	
	@Override
	public String getCargo() {
		return "Alférez";
	}
	
}