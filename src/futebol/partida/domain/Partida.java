package futebol.partida.domain;

import java.time.LocalDateTime;

public class Partida {

    private int id;
    private Escalacao visitante;
    private Escalacao mandante;
    private LocalDateTime data;
    private int golsMandante;
    private int golsVisitante;
    private StatusPartida status;

    public Partida(int id, LocalDateTime data, Escalacao mandante, Escalacao visitante) {
        if (mandante == null || visitante == null) {
            throw new IllegalArgumentException("Escalações inválidas.");
        }
        if (mandante.getClube().equals(visitante.getClube())) {
            throw new IllegalArgumentException("Um clube não pode jogar contra si mesmo.");
        }

        if (data == null) {
            throw new IllegalArgumentException("Data inválida.");
        }

        this.id = id;
        this.data = data;
        this.mandante = mandante;
        this.visitante = visitante;
        this.golsMandante = 0;
        this.golsVisitante = 0;
        this.status = StatusPartida.AGENDADA;
    }

    public void finalizar(){
        if (status == StatusPartida.FINALIZADA) {
            throw new IllegalStateException("A partida já foi finalizada.");
        }
        this.status = StatusPartida.FINALIZADA;

        mandante.fechar();
        visitante.fechar();
    }

    public void definirResultado(int golsMandante, int golsVisitante){

        if (golsMandante < 0 || golsVisitante < 0) {
            throw new IllegalArgumentException("O resultado não pode ser negativo.");
        }

        if (status == StatusPartida.FINALIZADA) {
            throw new IllegalStateException("A partida já foi finalizada.");
        }

        this.golsMandante = golsMandante;
        this.golsVisitante = golsVisitante;
    }

    public LocalDateTime getData() {
        return data;
    }

    public int getGolsMandante() {
        return golsMandante;
    }

    public int getGolsVisitante() {
        return golsVisitante;
    }

    public StatusPartida getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public Escalacao getVisitante() {
        return visitante;
    }

    public Escalacao getMandante() {
        return mandante;
    }
}
