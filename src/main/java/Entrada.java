import java.util.Date;

public class Entrada {
	private String mensaje;
	private Date fecha;
	private String tipo;
	
	protected String getMensaje() {
		return this.mensaje;
	}
	protected Date getFecha() {
		return this.fecha;
	}
	protected String getTipo() {
		return this.tipo;
	}

	public Entrada(String mensaje, String tipo) {
		super();
		this.mensaje = mensaje;
		this.tipo = tipo;
		this.fecha = new Date();
	}
}
