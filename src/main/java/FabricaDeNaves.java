public abstract class FabricaDeNaves {
    public static Nave getNave(String tipo) {
        return switch (tipo) {
            case "carguero" -> new NaveCarguero();
            case "combate" -> new NaveDeCombate();
            case "exploradora" -> new NaveExploradora();
            default -> null; //debería lanzar excepción
        };
    }
    //Para agregar un nuevo tipo de Nave debería modificar este factory, lo que violaría sOlid ...
    // es un defecto del patron simple Factory, que es lo que estoy seguro de que exige el enunciado, pero hay algunos enfoques que pueden solucionarlo con ciertas consecuencias.
}
