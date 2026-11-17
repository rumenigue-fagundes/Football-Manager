package jogador.domain;

import clube.domain.Clube;

import java.io.Serializable;

public abstract class Jogador implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private String nome;
    private int idade;
    private int numeroCamisa;
    private Clube clube;

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
