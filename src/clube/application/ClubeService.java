package clube.application;

import clube.domain.Clube;
import jogador.domain.Jogador;
import shared.application.Repositorio;

import java.util.ArrayList;
import java.util.List;

/**
 * Casos de uso de clubes (Application): criar, buscar, editar, excluir e gerenciar o elenco.
 * Coordena os repositórios; as regras de negócio continuam no Domain (Clube).
 */
public class ClubeService {
    private final Repositorio<Clube> repositorio;
    private final Repositorio<Jogador> repositorioJogadores;

    public ClubeService(Repositorio<Clube> repositorio, Repositorio<Jogador> repositorioJogadores) {
        this.repositorio = repositorio;
        this.repositorioJogadores = repositorioJogadores;
    }

    /** Gera o id automaticamente e salva o novo clube. */
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

    /** Altera nome e cidade (validados pelo Clube) e salva. */
    public void editar(int id, String nome, String cidade) {
        Clube clube = buscar(id);

        clube.setNome(nome);
        clube.setCidade(cidade);

        repositorio.salvar(clube);
    }

    /**
     * Libera os jogadores do elenco (ficam sem clube) antes de remover o clube.
     * Percorre uma cópia da lista porque removerJogador altera o elenco original.
     */
    public void excluir(int id) {
        Clube clube = buscar(id);

        for (Jogador j : new ArrayList<>((clube.getElenco()))) {
            clube.removerJogador(j);
        }

        repositorio.remover(id);
    }

    /** Maior id existente + 1; continua correto depois de carregar dados do arquivo. */
    private int proximoId() {
        return repositorio.listarTodos().stream().mapToInt(
                Clube::getId).max().orElse(0) + 1;
    }

    /** Busca clube e jogador pelos ids; a regra do vínculo fica por conta do Clube. */
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

    /**
     * Move o jogador para outro clube (ou dá o primeiro clube a quem não tem).
     * Se o destino recusar, devolve o jogador à origem
     * para ele não ficar sem clube, e repassa o erro.
     */
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
