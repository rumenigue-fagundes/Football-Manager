package jogador.domain;

import clube.domain.Clube;

import java.io.Serializable;

/**
 * Jogador de futebol (classe abstrata): guarda os dados comuns a todos os jogadores.
 * Cada tipo de jogador informa a sua posição em getPosicaoPrincipal().
 * É Serializable para poder ser gravado em arquivo.
 */
public abstract class Jogador implements Serializable {

    // Identifica a versão da classe no arquivo de dados (serialização).
    private static final long serialVersionUID = 1L;
    private int id;
    private String nome;
    private int idade;
    private int numeroCamisa;
    private Clube clube;

    /** Cria o jogador validando nome, idade e número da camisa. */
    public Jogador(int id, String nome, int idade, int numeroCamisa) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome inválido.");
        }
        if (idade <= 0) {
            throw new IllegalArgumentException("Idade inválida.");
        }
        if (numeroCamisa < 0) {
            throw new IllegalArgumentException("Número de camisa inválido.");
        }
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.numeroCamisa = numeroCamisa;
    }

    /** Cada subclasse devolve a sua posição (polimorfismo). */
    public abstract String getPosicaoPrincipal();

    public Clube getClube() {
        return clube;
    }
    // Chamado apenas por Clube, para manter o vínculo consistente.
    public void setClube(Clube clube) {
        this.clube = clube;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome inválido.");
        }
        this.nome = nome;
    }

    public void setIdade(int idade) {
        if (idade <= 0) {
            throw new IllegalArgumentException("Idade inválida.");
        }
        this.idade = idade;
    }

    public int getNumeroCamisa() {
        return numeroCamisa;
    }

    /** Altera a camisa; se o jogador tem clube, recusa número já usado por outro jogador do elenco. */
    public void setNumeroCamisa(int numeroCamisa) {
        if (numeroCamisa < 0) {
            throw new IllegalArgumentException("Número de camisa inválido.");
        }

        if (clube != null && clube.getElenco().stream()
                .anyMatch(j -> j != this && j.getNumeroCamisa() == numeroCamisa)) {
            throw new IllegalArgumentException("Número de camisa já utilizado.");
        }
        this.numeroCamisa = numeroCamisa;
    }
}
