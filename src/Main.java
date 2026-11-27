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
        // Arquivo onde os dados são gravados (a pasta "dados" é criada ao salvar).
        Path arquivo = Paths.get("dados", "futebol.dat");
        // Os dois repositórios compartilham os mesmos dados, para clubes e jogadores serem gravados juntos.
        DadosSistema dados = new DadosSistema();
        // Os dados só são lidos quando o usuário escolhe Carregar no menu.
        Repositorio<Clube> repositorioClubes =
                new RepositorioArquivo<>(dados, DadosSistema::getClubes, Clube::getId, arquivo);
        Repositorio<Jogador> repositorioJogadores =
                new RepositorioArquivo<>(dados, DadosSistema::getJogadores, Jogador::getId, arquivo);

        // Serviços da Application: recebem apenas a interface Repositorio.
        ClubeService clubeService = new ClubeService(repositorioClubes, repositorioJogadores);
        JogadorService jogadorService = new JogadorService(repositorioJogadores);
        PersistenciaService persistenciaService = new PersistenciaService(repositorioClubes, repositorioJogadores);

        // A fábrica definida em ConfiguracaoSistema cria as telas (textuais ou gráficas); o Main não sabe qual.
        ApresentacaoFactory factory = ConfiguracaoSistema.obterFactory();
        TelaClubes telaClubes = factory.criarTelaClubes(clubeService);
        TelaJogadores telaJogadores = factory.criarTelaJogadores(jogadorService, clubeService);
        MenuPrincipal menu = factory.criarMenu(telaClubes, telaJogadores, persistenciaService);
        menu.exibir();
    }
}
