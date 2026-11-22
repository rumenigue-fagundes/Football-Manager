package presentation.gui;

import clube.application.ClubeService;
import jogador.application.JogadorService;
import presentation.factory.ApresentacaoFactory;
import presentation.produtos.MenuPrincipal;
import presentation.produtos.TelaClubes;
import presentation.produtos.TelaJogadores;
import shared.application.PersistenciaService;

public class GUIFactory implements ApresentacaoFactory {
    @Override
    public TelaClubes criarTelaClubes(ClubeService clubeService) {
        return new TelaClubesGUI(clubeService);
    }

    @Override
    public TelaJogadores criarTelaJogadores(JogadorService jogadorService, ClubeService clubeService) {
        return new TelaJogadoresGUI(jogadorService, clubeService);
    }

    @Override
    public MenuPrincipal criarMenu(TelaClubes telaClubes, TelaJogadores telaJogadores, PersistenciaService persistenciaService) {
        return new MenuPrincipalGUI(telaClubes, telaJogadores, persistenciaService);
    }
}
