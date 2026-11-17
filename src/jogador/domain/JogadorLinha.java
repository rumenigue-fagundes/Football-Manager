package jogador.domain;

public class JogadorLinha extends Jogador {
    private PosicaoLinha posicao;

    public JogadorLinha(int id, String nome, int idade, int numeroCamisa, PosicaoLinha posicao) {
        super(id, nome, idade, numeroCamisa);
        if (posicao == null) {
            throw new IllegalArgumentException("Posição inválida.");
        }
        this.posicao = posicao;
    }

    @Override
    public String getPosicaoPrincipal() {
        return String.valueOf(this.posicao);
    }

    public PosicaoLinha getPosicao() {
        return posicao;
    }

    public void setPosicao(PosicaoLinha posicao) {
        if (posicao == null) {
            throw new IllegalArgumentException("Posição inválida.");
        }
        this.posicao = posicao;
    }
}
