import java.util.ArrayList;

public class NaveExploradora extends Nave{
    final private static int COMBUSTIBLE_INICIAL = 60;
    final private static int ENERGIA_INICIAL = 80;
    final private static int DESGASTE_INICIAL = 0;

    public NaveExploradora() {
        String tipo= "exploradora";

        super(tipo, COMBUSTIBLE_INICIAL, ENERGIA_INICIAL, DESGASTE_INICIAL);
    }
}