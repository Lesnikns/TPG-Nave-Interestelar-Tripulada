public class Consejero extends CargoDecorator {
	protected int cantConsejos;
	
	public Consejero(Tripulante t, int antiguedad, int cantConsejos) {
		super(t, antiguedad);
		this.cantConsejos = cantConsejos;
	}
	
	public Consejero(Tripulante t, int antiguedad) {
		super(t, antiguedad);
		this.cantConsejos = 0;
	}
	
	public Consejero(Tripulante t) {
		super(t);
		this.cantConsejos = 0;
	}
	
	public void setCantConsejos(int cant) {
		cantConsejos = cant;
	}
	
	public void sumaConsejo() {
		cantConsejos += 1;
	}
	
	public void sumaConsejo(int cant) {
		cantConsejos += cant;
	}
	
	@Override
	public double getAporte() {
		return 600 + (600 * antiguedad * 0.05) + (2 * cantConsejos);
	}
	
	@Override
	public String getCargo() {
		return "Consejero";
	}
}