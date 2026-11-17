package futebol.clube.domain;

import futebol.jogador.domain.Jogador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Clube implements GerenciadorJogadores {

    private int id;
    private String nome;
    private String cidade;
    private List<Jogador> elenco;

    public Clube(int id, String nome, String cidade) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome inválido.");
        }
        if (cidade == null || cidade.isBlank()) {
            throw new IllegalArgumentException("Cidade inválida.");
        }
        this.id = id;
        this.nome = nome;
        this.cidade = cidade;
        this.elenco = new ArrayList<>();
    }

    @Override
    public void adicionarJogador(Jogador jogador) {
        if (jogador == null) {
            throw new IllegalArgumentException("Jogador inválido.");
        }

        if (elenco.contains(jogador)) {
            throw new IllegalArgumentException("O jogador já pertence ao elenco.");
        }

        if (jogador.getClube() != null) {
            throw new IllegalArgumentException("Jogador já pertence a outro clube.");
        }

        if (elenco.stream().anyMatch(j -> j.getNumeroCamisa() == jogador.getNumeroCamisa())) {
            throw new IllegalArgumentException("Número de camisa já utilizado.");
        }

        elenco.add(jogador);
        jogador.setClube(this);
    }

    @Override
    public void removerJogador(Jogador jogador) {
        if (jogador == null) {
            throw new IllegalArgumentException("Jogador inválido.");
        }

        if (elenco.remove(jogador)) {
            jogador.setClube(null);
        }
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCidade() {
        return cidade;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome inválido.");
        }
        this.nome = nome;
    }

    public void setCidade(String cidade) {
        if (cidade == null || cidade.isBlank()) {
            throw new IllegalArgumentException("Cidade inválida.");
        }
        this.cidade = cidade;
    }

    public List<Jogador> getElenco() {
        return Collections.unmodifiableList(elenco);
    }
}
