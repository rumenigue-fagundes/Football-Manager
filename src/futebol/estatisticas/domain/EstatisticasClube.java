package futebol.estatisticas.domain;

import futebol.clube.domain.Clube;

public class EstatisticasClube {
    private int vitorias;
    private int derrotas;
    private int empates;
    private int golsMarcados;
    private int golsSofridos;
    private final Clube clube;


    public EstatisticasClube(Clube clube) {
        if (clube == null) {
            throw new IllegalArgumentException("Clube inválido.");
        }
        this.clube = clube;
    }

    public void registrarPartida(int golsMarcados, int golsSofridos) {
        if (golsMarcados < 0 || golsSofridos < 0) {
            throw new IllegalArgumentException("Quantidade de gols inválida.");
        }
        this.golsMarcados += golsMarcados;
        this.golsSofridos += golsSofridos;
        if (golsMarcados > golsSofridos) {
            this.vitorias++;
        } else if (golsMarcados < golsSofridos) {
            this.derrotas++;
        } else {
            this.empates++;
        }
    }

    public int getVitorias() {
        return vitorias;
    }

    public int getDerrotas() {
        return derrotas;
    }

    public int getEmpates() {
        return empates;
    }

    public int getGolsMarcados() {
        return golsMarcados;
    }

    public int getGolsSofridos() {
        return golsSofridos;
    }

    public int getPontos() {
        return (this.getVitorias()*3 + this.getEmpates());
    }

    public Clube getClube() {
        return clube;
    }
}
