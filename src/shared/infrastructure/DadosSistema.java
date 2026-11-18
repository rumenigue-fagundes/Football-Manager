package shared.infrastructure;

import clube.domain.Clube;
import jogador.domain.Jogador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DadosSistema implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Clube> clubes = new ArrayList<>();
    private final List<Jogador> jogadores = new ArrayList<>();

    public List<Clube> getClubes() {
        return clubes;
    }

    public List<Jogador> getJogadores() {
        return jogadores;
    }
}
