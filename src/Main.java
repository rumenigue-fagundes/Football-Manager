import clube.application.ClubeService;
import clube.domain.Clube;
import jogador.application.JogadorService;
import jogador.domain.Jogador;
import presentation.factory.ApresentacaoFactory;
import presentation.factory.ConfiguracaoSistema;
import presentation.produtos.MenuPrincipal;
import presentation.produtos.TelaClubes;
import presentation.produtos.TelaJogadores;
import shared.application.PersistenciaService;
import shared.application.Repositorio;
import shared.infrastructure.DadosSistema;
import shared.infrastructure.RepositorioArquivo;

import java.nio.file.Path;
import java.nio.file.Paths;

/** Ponto de entrada: monta as camadas e abre a interface escolhida em ConfiguracaoSistema. */
public class Main {

    public static void main(String[] args) {
        Path arquivo = Paths.get("dados", "futebol.dat");
        DadosSistema dados = new DadosSistema();
        Repositorio<Clube> repositorioClubes =
                new RepositorioArquivo<>(dados, DadosSistema::getClubes, Clube::getId, arquivo);
        Repositorio<Jogador> repositorioJogadores =
                new RepositorioArquivo<>(dados, DadosSistema::getJogadores, Jogador::getId, arquivo);

        ClubeService clubeService = new ClubeService(repositorioClubes, repositorioJogadores);
        JogadorService jogadorService = new JogadorService(repositorioJogadores);
        PersistenciaService persistenciaService = new PersistenciaService(repositorioClubes, repositorioJogadores);

        ApresentacaoFactory factory = ConfiguracaoSistema.obterFactory();
        TelaClubes telaClubes = factory.criarTelaClubes(clubeService);
        TelaJogadores telaJogadores = factory.criarTelaJogadores(jogadorService, clubeService);
        MenuPrincipal menu = factory.criarMenu(telaClubes, telaJogadores, persistenciaService);
        menu.exibir();
    }
}
