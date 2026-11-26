package shared.infrastructure;

import clube.domain.Clube;
import jogador.domain.Jogador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Raiz dos dados gravados em arquivo: clubes e jogadores ficam juntos para serem gravados
 * no mesmo fluxo, o que preserva as referências entre eles.
 */
public class DadosSistema implements Serializable {
    // Identifica a versão da classe no arquivo de dados (serialização).
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
