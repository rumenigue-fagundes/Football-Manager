package futebol.clube.application;

import futebol.clube.domain.Clube;
import futebol.jogador.domain.Jogador;
import futebol.shared.application.Repositorio;

import java.util.ArrayList;
import java.util.List;

public class ClubeService {
    private final Repositorio<Clube> repositorio;

    public ClubeService(Repositorio<Clube> repositorio) {
        this.repositorio = repositorio;
    }

    public Clube criar(String nome, String cidade) {
        Clube clube = new Clube(proximoId(), nome, cidade);
        repositorio.salvar(clube);
        return clube;
    }

    public Clube buscar(int id) {
        return repositorio.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException(
                "Clube não encontrado."
        ));
    }

    public List<Clube> listar(){
        return repositorio.listarTodos();
    }

    public void editar(int id, String nome, String cidade) {
        Clube clube = buscar(id);

        clube.setNome(nome);
        clube.setCidade(cidade);

        repositorio.salvar(clube);
    }

    public void excluir(int id) {
        Clube clube = buscar(id);

        for (Jogador j : new ArrayList<>((clube.getElenco()))) {
            clube.removerJogador(j);
        }

        repositorio.remover(id);
    }

    private int proximoId() {
        return repositorio.listarTodos().stream().mapToInt(
                Clube::getId).max().orElse(0) + 1;
    }

    /* public void adicionarJogador(clubeId, jogadorId) {
        Clube clube = buscar(clubeId);
        Jogador jogador = buscar(jogadorId);

        clube.adicionarJogador(jogador);
    }
    */ //TODO
}
