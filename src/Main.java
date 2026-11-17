import futebol.clube.application.ClubeService;
import futebol.jogador.application.JogadorService;
import futebol.clube.domain.Clube;
import futebol.jogador.domain.Jogador;
import futebol.jogador.domain.PosicaoLinha;
import futebol.shared.infrastructure.RepositorioMemoria;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        RepositorioMemoria<Clube> repositorioClubes =
                new RepositorioMemoria<>(Clube::getId);

        RepositorioMemoria<Jogador> repositorioJogadores =
                new RepositorioMemoria<>(Jogador::getId);

        ClubeService clubeService =
                new ClubeService(repositorioClubes, repositorioJogadores);

        JogadorService jogadorService =
                new JogadorService(repositorioJogadores);

        Clube gremio = clubeService.criar("Gremio", "Porto-Alegre");
        Clube inter = clubeService.criar("Inter", "Porto-Alegre");

        List<Jogador> jogadoresGremio = new ArrayList<>();
        List<Jogador> jogadoresInter = new ArrayList<>();

        jogadoresGremio.add(jogadorService.criarLinha("Renato Portaluppi", 21,
                7, PosicaoLinha.ATACANTE));
        jogadoresGremio.add(jogadorService.criarGoleiro("Marcelo Grohe",
                31, 1));
        jogadoresGremio.add(jogadorService.criarLinha("Kannemann",
                35, 4, PosicaoLinha.ZAGUEIRO));


        jogadoresInter.add(jogadorService.criarLinha("Falcão", 27,
                7, PosicaoLinha.ATACANTE));
        jogadoresInter.add(jogadorService.criarGoleiro("Alisson Becker",
                29, 1));
        jogadoresInter.add(jogadorService.criarLinha("D'Alessandro",
                34, 10, PosicaoLinha.MEIO_CAMPO));

        for (Jogador j : jogadoresGremio) {
            clubeService.adicionarJogador(gremio.getId(), j.getId());
        }

        for (Jogador j : jogadoresInter) {
            clubeService.adicionarJogador(inter.getId(), j.getId());
        }

        for (Clube clube : clubeService.listar()) {
            System.out.println("Elenco do " + clube.getNome() + ":");

            for (Jogador jogador : clubeService.listarElenco(clube.getId())) {
                System.out.println(
                        "Nome: " + jogador.getNome()
                                + " | Idade: " + jogador.getIdade()
                                + " | Camisa: " + jogador.getNumeroCamisa()
                                + " | Posição: " + jogador.getPosicaoPrincipal()
                );
            }
            System.out.println();
        }

        //TESTE TRANSFERÊNCIA
        for (Jogador jogador : jogadoresGremio) {
            if (jogador.getNome().equals("Kannemann")) {
                clubeService.removerJogador(gremio.getId(), jogador.getId());
                clubeService.adicionarJogador(inter.getId(), jogador.getId());
                break;
            }
        }

        // TESTE: número duplicado
        try {
            Jogador jogador = jogadoresInter.get(0);
            jogador.setNumeroCamisa(1); // Alisson já usa a camisa 1
        } catch (IllegalArgumentException e) {
            System.out.println("Erro esperado: " + e.getMessage());
        }

        Jogador jogadorTeste = jogadoresGremio.get(0);
        jogadorTeste.setNumeroCamisa(9);

        for (Clube clube : clubeService.listar()) {
            System.out.println("Elenco do " + clube.getNome() + ":");

            for (Jogador jogador : clubeService.listarElenco(clube.getId())) {
                System.out.println(
                        "Nome: " + jogador.getNome()
                                + " | Idade: " + jogador.getIdade()
                                + " | Camisa: " + jogador.getNumeroCamisa()
                                + " | Posição: " + jogador.getPosicaoPrincipal()
                );
            }
            System.out.println();
        }


        System.out.println("TESTE EXCLUSÃO");
        Jogador jogadorTeste2 = jogadoresGremio.get(2);
        jogadorService.excluir(jogadorTeste2.getId());

        System.out.println("Jogador encontrado: " +
                repositorioJogadores.buscarPorId(jogadorTeste2.getId()).isPresent());

        System.out.println("Clube do jogador: " + jogadorTeste2.getClube());

    }
}