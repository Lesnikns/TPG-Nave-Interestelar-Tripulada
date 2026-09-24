import java.util.ArrayList;

public class Bitacora {
	private ArrayList<Entrada> entradas;

	protected ArrayList<Entrada> getEntradas() {
		return entradas;
	}
	
	protected void cargaEntrada(Entrada entrada) {
		assert entrada != null : "la entrada no puede estar vacia/ser nula";
		this.entradas.add(entrada);
	}

	@Override
	public String toString() {
		String log = new String();
		for  (Entrada entrada : this.entradas)
			log += entrada.toString() + "\n";
		return log;
	}

	public Bitacora(ArrayList<Entrada> entradas) {
		super();
		this.entradas = new ArrayList<>();
	}
}
