package presentation.factory;

import clube.application.ClubeService;
import jogador.application.JogadorService;
import presentation.produtos.MenuPrincipal;
import presentation.produtos.TelaClubes;
import presentation.produtos.TelaJogadores;
import shared.application.PersistenciaService;

/** Abstract Factory: cria a família de telas de uma mesma interface (textual ou gráfica). */
public interface ApresentacaoFactory {
    TelaClubes criarTelaClubes(ClubeService clubeService);

    TelaJogadores criarTelaJogadores(JogadorService jogadorService, ClubeService clubeService);

    MenuPrincipal criarMenu(TelaClubes telaClubes, TelaJogadores telaJogadores,
                            PersistenciaService persistenciaService);
}
