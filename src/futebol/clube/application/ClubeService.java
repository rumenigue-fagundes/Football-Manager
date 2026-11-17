package futebol.clube.application;

import futebol.clube.domain.Clube;
import futebol.jogador.domain.Jogador;
import futebol.shared.application.Repositorio;

import java.util.ArrayList;
import java.util.List;

public class ClubeService {
    private final Repositorio<Clube> repositorio;
    private final Repositorio<Jogador> repositorioJogadores;

    public ClubeService(Repositorio<Clube> repositorio, Repositorio<Jogador> repositorioJogadores) {
        this.repositorio = repositorio;
        this.repositorioJogadores = repositorioJogadores;
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

    public void adicionarJogador(int clubeId, int jogadorId) {
        Clube clube = buscar(clubeId);
        Jogador jogador = repositorioJogadores.buscarPorId(jogadorId)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Jogador não encontrado."
                        ));

        clube.adicionarJogador(jogador);
    }

    public void removerJogador(int clubeId, int jogadorId) {
        Clube clube = buscar(clubeId);
        Jogador jogador = repositorioJogadores.buscarPorId(jogadorId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Jogador não encontrado."
                ));

        clube.removerJogador(jogador);
    }

    public List<Jogador> listarElenco(int clubeId){
        Clube clube = buscar(clubeId);

        return clube.getElenco();
    }

    public void transferir(int jogadorId, int clubeDestinoId){
        Jogador jogador = repositorioJogadores.buscarPorId(jogadorId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Jogador não encontrado"
                ));
        Clube destino = buscar(clubeDestinoId);
        Clube origem = jogador.getClube();

        if (origem == destino) {
            throw new IllegalArgumentException("Jogador já está nesse clube.");
        }

        if (origem != null) {
            origem.removerJogador(jogador);
        }

        try {
            destino.adicionarJogador(jogador);
        } catch (IllegalArgumentException e) {
            if (origem != null) {
                origem.adicionarJogador(jogador); //desfaz a saída
            }
            throw e; //avisa quem chamou
        }
    }

}
