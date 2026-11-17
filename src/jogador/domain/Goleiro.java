package jogador.domain;

public class Goleiro extends Jogador {

    public Goleiro(int id, String nome, int idade, int numeroCamisa) {
        super(id, nome, idade, numeroCamisa);
    }

    @Override
    public String getPosicaoPrincipal() {
        return "Goleiro";
    }

}
