package futebol.jogador.application;

import futebol.clube.domain.Clube;
import futebol.jogador.domain.Goleiro;
import futebol.jogador.domain.Jogador;
import futebol.jogador.domain.JogadorLinha;
import futebol.jogador.domain.PosicaoLinha;
import futebol.shared.application.Repositorio;

import java.util.List;

public class JogadorService {

    private final Repositorio<Jogador> repositorio;

    public JogadorService(Repositorio<Jogador> repositorio) {
        this.repositorio = repositorio;
    }

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

    public void editar(int id, String nome, int idade) {
        Jogador jogador = buscar(id);

        jogador.setNome(nome);
        jogador.setIdade(idade);

        repositorio.salvar(jogador);
    }

    public void excluir(int id) {
        Jogador jogador = buscar(id);

        Clube clube = jogador.getClube();

        if (clube != null) {
            clube.removerJogador(jogador);
        }

        repositorio.remover(id);
    }

    private int proximoId() {
        return repositorio.listarTodos().stream().mapToInt(
                Jogador::getId).max().orElse(0) + 1;
    }
}
