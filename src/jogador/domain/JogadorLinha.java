package jogador.domain;

/** Jogador de linha: especialização de Jogador que acrescenta a posição em campo (PosicaoLinha). */
public class JogadorLinha extends Jogador {
    private static final long serialVersionUID = 1L;
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

    /** Altera a posição; recusa valor nulo. */
    public void setPosicao(PosicaoLinha posicao) {
        if (posicao == null) {
            throw new IllegalArgumentException("Posição inválida.");
        }
        this.posicao = posicao;
    }
}
