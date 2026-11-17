package futebol.partida.domain;

import futebol.jogador.domain.Jogador;

public class Participacao {
    private int gols;
    private int assistencias;
    private int cartoesAmarelos;
    private int cartoesVermelhos;
    private Jogador jogador;
    private boolean fechada;

    public Participacao(Jogador jogador) {
        if (jogador == null) {
            throw new IllegalArgumentException("Jogador inválido.");
        }

        this.gols = 0;
        this.assistencias = 0;
        this.cartoesAmarelos = 0;
        this.cartoesVermelhos = 0;
        this.jogador = jogador;
    }

    void fechar(){
        this.fechada = true;
    }

    public void registrarGol() {
        if (fechada) {
            throw new IllegalStateException("Partida finalizada.");
        }
        gols++;
    }

    public void registrarAssistencia(){
        if (fechada) {
            throw new IllegalStateException("Partida finalizada.");
        }
        assistencias++;
    }

    public void registrarCartaoAmarelo() {
        if (fechada) {
            throw new IllegalStateException("Partida finalizada.");
        }
        cartoesAmarelos++;
    }

    public void registrarCartaoVermelho() {
        if (fechada) {
            throw new IllegalStateException("Partida finalizada.");
        }
        cartoesVermelhos++;
    }

    public int getGols() {
        return gols;
    }

    public int getAssistencias() {
        return assistencias;
    }

    public int getCartoesAmarelos() {
        return cartoesAmarelos;
    }

    public int getCartoesVermelhos() {
        return cartoesVermelhos;
    }

    public Jogador getJogador() {
        return jogador;
    }
}
