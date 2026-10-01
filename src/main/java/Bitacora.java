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
		String log = "";
		for  (Entrada entrada : this.entradas)
			log = log + entrada.toString() + "\n"; //funcionara?
		return log;
	}

	public Bitacora() {
		super();
		this.entradas = new ArrayList<>();
	}
}
