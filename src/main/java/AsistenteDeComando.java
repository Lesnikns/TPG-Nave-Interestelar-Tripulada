import java.util.Date;

public class AsistenteDeComando {
    private Nave nave;
    private Bitacora bitacora;

    public Bitacora getBitacora() {
        return bitacora;
    }

    public Nave getNave() {
        return nave;
    }

    public AsistenteDeComando(Nave nave){
        this.nave = nave;
        this.bitacora = new Bitacora();
    }
    public void coordinarMision(Mision m) throws Exception { // el Asistente comprueba que haya combustuble disponible para realizar la mision
        if (nave.getCombustible() < 4) {
            String mensaje = "Recursos insuficientes: Combustible menor a 4.";
            this.escribeBitacora(mensaje, "ERROR");
            throw new Exception(mensaje);
        }
        m.IniciarMision(this); // si hay combustible arranca, y carga en resultado si se finalizo
    }

    public void escribeBitacora(String mensaje, String tipo){
        Entrada e = new Entrada(mensaje, tipo);
        bitacora.cargaEntrada(e);
    }

}
