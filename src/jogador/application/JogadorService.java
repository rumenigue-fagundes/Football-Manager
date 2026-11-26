package jogador.application;

import clube.domain.Clube;
import jogador.domain.Goleiro;
import jogador.domain.Jogador;
import jogador.domain.JogadorLinha;
import jogador.domain.PosicaoLinha;
import shared.application.Repositorio;

import java.util.List;

/**
 * Casos de uso de jogadores (Application): criar goleiros e jogadores de linha, buscar, editar e excluir.
 * Gera os ids e coordena o repositório; as validações ficam no Domain.
 */
public class JogadorService {

    private final Repositorio<Jogador> repositorio;

    public JogadorService(Repositorio<Jogador> repositorio) {
        this.repositorio = repositorio;
    }

    /** Cria um jogador de linha com id gerado automaticamente. */
    public JogadorLinha criarLinha(String nome, int idade,
                                   int numeroCamisa, PosicaoLinha posicao) {
        JogadorLinha jogador = new JogadorLinha(proximoId(), nome, idade, numeroCamisa, posicao);
        repositorio.salvar(jogador);
        return jogador;
    }

    public Goleiro criarGoleiro(String nome, int idade, int numeroCamisa) {
        Goleiro goleiro = new Goleiro(proximoId(), nome, idade, numeroCamisa);
        repositorio.salvar(goleiro);
        return goleiro;
    }

    public Jogador buscar(int id) {
        return repositorio.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException(
                "Jogador não encontrado."
        ));
    }

    public List<Jogador> listar(){
        return repositorio.listarTodos();
    }

    /**
     * Atualiza os dados do jogador. A posição só se aplica a jogador de linha;
     * para goleiro o valor é ignorado.
     */
    public void editar(int id, String nome, int idade, int numeroCamisa, PosicaoLinha posicao){
        Jogador jogador = buscar(id);

        jogador.setNome(nome);
        jogador.setIdade(idade);
        jogador.setNumeroCamisa(numeroCamisa);

        if (jogador instanceof JogadorLinha linha){
            linha.setPosicao(posicao);
        }

        repositorio.salvar(jogador);
    }

    /** Desvincula o jogador do clube antes de removê-lo; senão o clube continuaria com ele no elenco. */
    public void excluir(int id) {
        Jogador jogador = buscar(id);

        Clube clube = jogador.getClube();

        if (clube != null) {
            clube.removerJogador(jogador);
        }

        repositorio.remover(id);
    }

    /** Maior id existente + 1. */
    private int proximoId() {
        return repositorio.listarTodos().stream().mapToInt(
                Jogador::getId).max().orElse(0) + 1;
    }
}
