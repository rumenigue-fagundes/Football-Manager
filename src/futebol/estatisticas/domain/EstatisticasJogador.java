package futebol.estatisticas.domain;

import futebol.jogador.domain.Jogador;
import futebol.partida.domain.Participacao;

public class EstatisticasJogador {
    private int partidasDisputadas;
    private int gols;
    private int assistencias;
    private int cartoesAmarelos;
    private int cartoesVermelhos;
    private final Jogador jogador;

    public EstatisticasJogador(Jogador jogador) {
        if (jogador == null) {
            throw new IllegalArgumentException("Jogador inválido.");
        }
        this.jogador = jogador;
    }

    public void registrarParticipacao(Participacao p) {
        if (p == null) {
            throw new IllegalArgumentException("Participação inválida.");
        }
        if (!p.getJogador().equals(jogador)) {
            throw new IllegalArgumentException("A participação não pertence a este jogador.");
        }
        partidasDisputadas++;
        this.gols += p.getGols();
        this.assistencias += p.getAssistencias();
        this.cartoesAmarelos += p.getCartoesAmarelos();
        this.cartoesVermelhos += p.getCartoesVermelhos();
    }

    public int getPartidasDisputadas() {
        return partidasDisputadas;
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
}
