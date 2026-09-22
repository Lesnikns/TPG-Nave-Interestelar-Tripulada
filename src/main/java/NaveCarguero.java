import java.util.ArrayList;

public class NaveCarguero extends Nave{
    final private static int COMBUSTIBLE_INICIAL = 100;
    final private static int ENERGIA_INICIAL = 60;
    final private static int DESGASTE_INICIAL = 0;

    public NaveCarguero() {
        String tipo= "carguero";

        super(tipo, COMBUSTIBLE_INICIAL, ENERGIA_INICIAL, DESGASTE_INICIAL);
    }
}