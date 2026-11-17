package futebol.clube.domain;

import futebol.jogador.domain.Jogador;

public interface GerenciadorJogadores {
    void adicionarJogador(Jogador jogador);
    void removerJogador(Jogador jogador);
}
