package futebol.campeonato.domain;

import futebol.clube.domain.Clube;
import futebol.partida.domain.Partida;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Campeonato {

    private int id;
    private String nome;
    private int temporada;
    private List<Clube> participantes;
    private List<Partida> partidas;

    public Campeonato(int id, String nome, int temporada) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome inválido.");
        }

        if (temporada <= 0) {
            throw new IllegalArgumentException("Temporada inválida.");
        }

        this.id = id;
        this.nome = nome;
        this.temporada = temporada;
        this.participantes = new ArrayList<>();
        this.partidas = new ArrayList<>();
    }

    public void adicionarParticipante(Clube clube) {
        if (clube == null) {
            throw new IllegalArgumentException("Clube inválido.");
        }
        if (participantes.contains(clube)) {
            throw new IllegalArgumentException("Clube já participa do campeonato.");
        }

        participantes.add(clube);
    }

    public void adicionarPartida(Partida partida) {
        if (partida == null) {
            throw new IllegalArgumentException("Partida inválida.");
        }

        if (partidas.contains(partida)) {
            throw new IllegalArgumentException("Partida já cadastrada.");
        }

        if (!participantes.contains(partida.getMandante().getClube())
                || !participantes.contains(partida.getVisitante().getClube())) {
            throw new IllegalArgumentException("Os clubes precisam participar do campeonato.");
        }

        partidas.add(partida);
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getTemporada() {
        return temporada;
    }

    public List<Clube> getParticipantes() {
        return Collections.unmodifiableList(participantes);
    }

    public List<Partida> getPartidas() {
        return Collections.unmodifiableList(partidas);
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome inválido.");
        }
        this.nome = nome;
    }

    public void setTemporada(int temporada) {
        if (temporada <= 0) {
            throw new IllegalArgumentException("Temporada inválida.");
        }
        this.temporada = temporada;
    }
}
