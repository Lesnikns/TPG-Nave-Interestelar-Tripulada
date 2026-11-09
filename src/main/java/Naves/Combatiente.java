package Naves;

public interface Combatiente {
    public enum ResultadoCombate { GANA, PAZ, PIERDE }
    ResultadoCombate versus(Combatiente o);
    ResultadoCombate resultadoParaCarguero();
    ResultadoCombate resultadoParaExploradora();
    ResultadoCombate resultadoParaDeCombate();

}
