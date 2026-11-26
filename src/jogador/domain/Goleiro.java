package jogador.domain;

/** Jogador que atua no gol: especialização de Jogador sem posição de linha. */
public class Goleiro extends Jogador {

    public Goleiro(int id, String nome, int idade, int numeroCamisa) {
        super(id, nome, idade, numeroCamisa);
    }

    @Override
    public String getPosicaoPrincipal() {
        return "Goleiro";
    }

}
