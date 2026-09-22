import java.util.ArrayList;
import java.util.Comparator;

public class Bitacora {
	protected ArrayList<Entrada> entradas;

	protected ArrayList<Entrada> getEntradas() {
		return entradas;
	}
	
	protected void addEntrada(Entrada entrada) {
		assert entrada != null : "la entrada no puede estar vacia/ser nula";
		this.entradas.add(entrada);
		this.entradas.sort(Comparator.comparing(Entrada::getNumEnt));
		//orden segun entrada en bitacora seria correcto? o ordenar por fecha?
	}

	public Bitacora(ArrayList<Entrada> entradas) {
		super();
		this.entradas = new ArrayList<>();
	}
}
