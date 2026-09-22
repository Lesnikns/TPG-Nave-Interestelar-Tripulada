public class Entrada {
	private static int cont = -1;
	protected String tipo;
	protected String fecha; 
	protected int numEnt;
	
	protected String getTipo() {
		return tipo;
	}
	protected String getFecha() {
		return fecha;
	}
	protected int getNumEnt() {
		return numEnt;
	}
	public Entrada(String tipo, String fecha) {
		super();
		cont++;
		this.tipo = tipo;
		this.fecha = fecha;
		this.numEnt = cont;
	}
}
