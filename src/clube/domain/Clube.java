package clube.domain;

import jogador.domain.Jogador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clube de futebol: guarda os dados do clube e o seu elenco (lista de jogadores).
 * As regras do vínculo clube-jogador ficam aqui: um jogador só pode estar em um clube
 * e o número da camisa não pode se repetir no elenco.
 * É Serializable para poder ser gravado em arquivo junto com o elenco.
 */
public class Clube implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private String nome;
    private String cidade;
    // Só é alterada por adicionarJogador e removerJogador.
    private List<Jogador> elenco;

    /** Cria o clube; nome e cidade são validados (não podem ser vazios). */
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

    /**
     * Coloca o jogador no elenco e atualiza o clube dele. Recusa jogador nulo,
     * que já está no elenco, que já pertence a outro clube ou com camisa repetida.
     */
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

    /** Tira o jogador do elenco e desfaz o vínculo; recusa quem não pertence ao clube. */
    public void removerJogador(Jogador jogador) {
        if (jogador == null) {
            throw new IllegalArgumentException("Jogador inválido.");
        }

        if (!elenco.contains(jogador)) {
            throw new IllegalArgumentException(
                    "Jogador não pertence a esse clube."
            );
        }

        elenco.remove(jogador);
        jogador.setClube(null);
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

    /** Devolve uma lista não modificável: o elenco só muda pelos métodos acima. */
    public List<Jogador> getElenco() {
        return Collections.unmodifiableList(elenco);
    }

    /** Texto usado nas telas (por exemplo, a lista de clubes na transferência): só o nome. */
    @Override
    public String toString() {
        return nome;
    }
}
