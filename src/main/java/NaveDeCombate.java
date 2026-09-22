import java.util.ArrayList;

public class NaveDeCombate extends Nave{
    final private static int COMBUSTIBLE_INICIAL = 80;
    final private static int ENERGIA_INICIAL = 100;
    final private static int DESGASTE_INICIAL = 0;

    public NaveDeCombate() {
        String tipo= "combate";

        super(tipo, COMBUSTIBLE_INICIAL, ENERGIA_INICIAL, DESGASTE_INICIAL);
    }
}

