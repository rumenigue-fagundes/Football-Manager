package presentation.gui;

import clube.application.ClubeService;
import jogador.application.JogadorService;
import presentation.produtos.TelaJogadores;

public class TelaJogadoresGUI implements TelaJogadores {

    private final JogadorService jogadorService;
    private final ClubeService clubeService;

    public TelaJogadoresGUI(JogadorService jogadorService, ClubeService clubeService) {
        this.jogadorService = jogadorService;
        this.clubeService = clubeService;
    }

    @Override
    public void exibir() {

    }
}
