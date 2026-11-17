package futebol.partida.domain;

import futebol.clube.domain.Clube;
import futebol.clube.domain.GerenciadorJogadores;
import futebol.jogador.domain.Jogador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Escalacao implements GerenciadorJogadores {

    private Clube clube;
    private List<Participacao> participacoes;
    private boolean fechada;

    public Escalacao(Clube clube) {
        if (clube == null) {
            throw new IllegalArgumentException("Clube inválido.");
        }
        this.clube = clube;
        this.participacoes = new ArrayList<>();
    }

    void fechar(){
        this.fechada = true;
        for (Participacao p : participacoes) {
            p.fechar();
        }
    }

    @Override
    public void adicionarJogador(Jogador jogador) {

        if (fechada) {
            throw new IllegalStateException("Partida finalizada.");
        }

        if (jogador == null) {
            throw new IllegalArgumentException("Jogador inválido.");
        }

        if (participacoes.size() >= 11) {
            throw new IllegalStateException("Escalação completa.");
        }

        if (!clube.getElenco().contains(jogador)) {
            throw new IllegalArgumentException("Jogador não pertence ao clube.");
        }

        if (participacoes.stream().anyMatch(p -> p.getJogador().equals(jogador))) {
            throw new IllegalArgumentException("Jogador já está escalado.");
        }

        participacoes.add(new Participacao(jogador));
    }

    @Override
    public void removerJogador(Jogador jogador) {
        if (fechada) {
            throw new IllegalStateException("Partida finalizada.");
        }
        participacoes.removeIf(p -> p.getJogador().equals(jogador));
    }

    public List<Participacao> getParticipacoes() {
        return Collections.unmodifiableList(participacoes);
    }

    public Clube getClube() {
        return clube;
    }
}
