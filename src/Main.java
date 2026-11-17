import clube.application.ClubeService;
import clube.domain.Clube;
import jogador.application.JogadorService;
import jogador.domain.Goleiro;
import jogador.domain.Jogador;
import jogador.domain.JogadorLinha;
import jogador.domain.PosicaoLinha;
import shared.infrastructure.RepositorioMemoria;

/**
 * Main PROVISÓRIO: valida Domain e Application pelo console.
 * Será substituído pelo Main definitivo (Abstract Factory) antes da entrega.
 */
public class Main {

    private static int aprovados = 0;
    private static int falhas = 0;

    public static void main(String[] args) {
        RepositorioMemoria<Clube> repositorioClubes = new RepositorioMemoria<>(Clube::getId);
        RepositorioMemoria<Jogador> repositorioJogadores = new RepositorioMemoria<>(Jogador::getId);

        ClubeService clubeService = new ClubeService(repositorioClubes, repositorioJogadores);
        JogadorService jogadorService = new JogadorService(repositorioJogadores);

        // ---------------------------------------------------------------
        titulo("1. CRIAÇÃO");
        Clube gremio = clubeService.criar("Grêmio", "Porto Alegre");
        Clube inter = clubeService.criar("Inter", "Porto Alegre");
        verificar("ids dos clubes são gerados em sequência",
                gremio.getId() == 1 && inter.getId() == 2);

        JogadorLinha renato = jogadorService.criarLinha(
                "Renato Portaluppi", 21, 7, PosicaoLinha.ATACANTE);
        Goleiro grohe = jogadorService.criarGoleiro(
                "Marcelo Grohe", 31, 1);
        JogadorLinha kannemann = jogadorService.criarLinha(
                "Kannemann", 35, 4, PosicaoLinha.ZAGUEIRO);

        JogadorLinha falcao = jogadorService.criarLinha(
                "Falcão", 27, 7, PosicaoLinha.ATACANTE);
        Goleiro alisson = jogadorService.criarGoleiro(
                "Alisson Becker", 29, 1);
        JogadorLinha dale = jogadorService.criarLinha(
                "D'Alessandro", 34, 10, PosicaoLinha.MEIO_CAMPO);

        JogadorLinha extra = jogadorService.criarLinha(
                "Extra", 20, 7, PosicaoLinha.ZAGUEIRO);
        JogadorLinha livre = jogadorService.criarLinha(
                "Livre", 25, 11, PosicaoLinha.MEIO_CAMPO);
        verificar("8 jogadores cadastrados",
                jogadorService.listar().size() == 8);
        verificar("jogador novo não tem clube",
                livre.getClube() == null);

        esperarErro("criar clube com nome vazio", () -> clubeService.criar(" ", "Porto Alegre"));
        esperarErro("criar jogador com idade inválida",
                () -> jogadorService.criarLinha("Fulano", 0, 5, PosicaoLinha.LATERAL));

        // ---------------------------------------------------------------
        titulo("2. VÍNCULO JOGADOR-CLUBE");
        clubeService.adicionarJogador(gremio.getId(), renato.getId());
        clubeService.adicionarJogador(gremio.getId(), grohe.getId());
        clubeService.adicionarJogador(gremio.getId(), kannemann.getId());
        clubeService.adicionarJogador(inter.getId(), falcao.getId());
        clubeService.adicionarJogador(inter.getId(), alisson.getId());
        clubeService.adicionarJogador(inter.getId(), dale.getId());

        verificar("Grêmio tem 3 jogadores", clubeService.listarElenco(gremio.getId()).size() == 3);
        verificar("Inter tem 3 jogadores", clubeService.listarElenco(inter.getId()).size() == 3);
        verificar("o jogador conhece o seu clube",
                renato.getClube() == gremio);

        esperarErro("adicionar jogador que já tem clube",
                () -> clubeService.adicionarJogador(inter.getId(), renato.getId()));
        esperarErro("adicionar jogador com camisa repetida",
                () -> clubeService.adicionarJogador(gremio.getId(), extra.getId()));
        esperarErro("remover jogador de um clube que não é o dele",
                () -> clubeService.removerJogador(inter.getId(), renato.getId()));
        esperarErro("buscar jogador inexistente",
                () -> jogadorService.buscar(999));
        esperarErro("buscar clube inexistente",
                () -> clubeService.buscar(999));

        // ---------------------------------------------------------------
        titulo("3. EDIÇÃO");
        jogadorService.editar(renato.getId(),
                "Renato Gaúcho", 22, 9, PosicaoLinha.MEIO_CAMPO);
        verificar("jogador de linha: nome, idade, camisa e posição alterados",
                renato.getNome().equals("Renato Gaúcho") && renato.getIdade() == 22
                        && renato.getNumeroCamisa() == 9
                        && renato.getPosicaoPrincipal().equals("MEIO_CAMPO"));

        jogadorService.editar(grohe.getId(),
                "Marcelo Grohe Jr.", 32, 1, null);
        verificar("goleiro: posição ignorada, mantém a própria camisa",
                grohe.getIdade() == 32 && grohe.getNumeroCamisa() == 1
                        && grohe.getPosicaoPrincipal().equals("Goleiro"));

        esperarErro("editar camisa para número já usado no elenco",
                () -> jogadorService.editar(falcao.getId(), "Falcão", 27, 1, PosicaoLinha.ATACANTE));
        verificar("camisa do Falcão continua 7 após a falha", falcao.getNumeroCamisa() == 7);

        clubeService.editar(gremio.getId(), "Grêmio FBPA", "Porto Alegre");
        verificar("clube editado", gremio.getNome().equals("Grêmio FBPA"));
        esperarErro("editar clube com nome vazio", () -> clubeService.editar(gremio.getId(), " ", "X"));

        // ---------------------------------------------------------------
        titulo("4. TRANSFERÊNCIA");
        clubeService.transferir(kannemann.getId(), inter.getId());
        verificar("a) transferência válida: jogador no destino", kannemann.getClube() == inter);
        verificar("a) Grêmio ficou com 2 e Inter com 4",
                clubeService.listarElenco(gremio.getId()).size() == 2
                        && clubeService.listarElenco(inter.getId()).size() == 4);

        esperarErro("b) transferência recusada (camisa 1 já usada no destino)",
                () -> clubeService.transferir(alisson.getId(), gremio.getId()));
        verificar("b) Alisson voltou para o Inter (retorno funcionou)", alisson.getClube() == inter);
        verificar("b) elencos intactos após a falha",
                clubeService.listarElenco(gremio.getId()).size() == 2
                        && clubeService.listarElenco(inter.getId()).size() == 4);

        esperarErro("c) transferir para o clube onde já está",
                () -> clubeService.transferir(grohe.getId(), gremio.getId()));

        clubeService.transferir(livre.getId(), gremio.getId());
        verificar("d) jogador sem clube recebe um clube", livre.getClube() == gremio);
        verificar("d) Grêmio voltou a ter 3", clubeService.listarElenco(gremio.getId()).size() == 3);

        // ---------------------------------------------------------------
        titulo("5. EXCLUSÃO");
        jogadorService.excluir(livre.getId());
        verificar("jogador excluído sai do repositório", repositorioJogadores.buscarPorId(livre.getId()).isEmpty());
        verificar("jogador excluído sai do elenco", clubeService.listarElenco(gremio.getId()).size() == 2);
        verificar("jogador excluído fica sem clube", livre.getClube() == null);

        jogadorService.excluir(extra.getId());
        verificar("jogador sem clube também pode ser excluído", jogadorService.listar().size() == 6);

        clubeService.excluir(inter.getId());
        verificar("clube excluído sai do repositório", clubeService.listar().size() == 1);
        verificar("jogadores do clube excluído continuam existindo", jogadorService.listar().size() == 6);
        verificar("jogadores do clube excluído ficaram sem clube",
                falcao.getClube() == null && alisson.getClube() == null
                        && dale.getClube() == null && kannemann.getClube() == null);

        // ---------------------------------------------------------------
        System.out.println();
        System.out.println("RESULTADO: " + aprovados + " verificações OK, " + falhas + " falhas.");
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("=== " + texto + " ===");
    }

    private static void verificar(String descricao, boolean condicao) {
        if (condicao) {
            aprovados++;
            System.out.println("[OK]     " + descricao);
        } else {
            falhas++;
            System.out.println("[FALHOU] " + descricao);
        }
    }

    /** A ação DEVE lançar IllegalArgumentException; mostra a mensagem recebida. */
    private static void esperarErro(String descricao, Runnable acao) {
        try {
            acao.run();
            verificar(descricao + " (deveria ter lançado exceção)", false);
        } catch (IllegalArgumentException e) {
            verificar(descricao + " -> \"" + e.getMessage() + "\"", true);
        }
    }
}