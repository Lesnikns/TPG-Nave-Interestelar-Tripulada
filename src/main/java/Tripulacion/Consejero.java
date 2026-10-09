package Tripulacion;

public class Consejero extends CargoDecorator {
	protected int cantConsejos;
	
	/**
	 * Construye un tripulante con el cargo de consejero.
	 * 
	 *@pre Mismas precondiciones que Tripulación.CargoDecorator, cantConsejos >= 0.
	 *@post Se crea un consejero con la cantidad de consejos indicada por cantConsejos.
	 *
	 * @param cantConsejos La cantidad de consejos brindados por el consejero.
	 */
	public Consejero(Tripulante t, int antiguedad, int cantConsejos) {
		super(t, antiguedad);
		assert cantConsejos >= 0 : "La cantidad de consejos no puede ser negativa";
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
		assert cantConsejos >= 0: "La cantidad de consejos no puede ser negativa.";
		cantConsejos = cant;
	}
	
	
	public void sumaConsejo() {
		cantConsejos += 1;
	}
	
	
	/**
	 * Suma una cantidad de consejos al total.
	 * @pre cant >= 0
	 * @post Se incrementa la cantidad de consejos en la cantidad especificada.
	 * @param cant Cantidad a sumar.
	 */
	public void sumaConsejo(int cant) {
		assert cantConsejos >= 0: "La cantidad de consejos no puede ser negativa.";
		cantConsejos += cant;
	}
	
	@Override
	public double getAporte() {
		return 600 + (600 * antiguedad * 0.05) + (2 * cantConsejos);
	}
	
	@Override
	public String getCargo() {
		return "Tripulación.Consejero";
	}
}