package presentation.textual;

import clube.application.ClubeService;
import jogador.application.JogadorService;
import presentation.factory.ApresentacaoFactory;
import presentation.produtos.MenuPrincipal;
import presentation.produtos.TelaClubes;
import presentation.produtos.TelaJogadores;
import shared.application.PersistenciaService;

import java.util.Scanner;

/** Fábrica das telas de console. Só ela é pública; as telas concretas ficam neste pacote. */
public class TextualFactory implements ApresentacaoFactory {

    // Um único Scanner para todas as telas, porque cada Scanner guarda um buffer próprio do teclado.
    private final Scanner entrada = new Scanner(System.in);

    @Override
    public TelaClubes criarTelaClubes(ClubeService clubeService) {
        return new TelaClubesTextual(clubeService, entrada);
    }

    @Override
    public TelaJogadores criarTelaJogadores(JogadorService jogadorService, ClubeService clubeService) {
        return new TelaJogadoresTextual(jogadorService, clubeService, entrada);
    }

    @Override
    public MenuPrincipal criarMenu(TelaClubes telaClubes, TelaJogadores telaJogadores,
                                   PersistenciaService persistenciaService) {
        return new MenuPrincipalTextual(telaClubes, telaJogadores, persistenciaService, entrada);
    }
}
