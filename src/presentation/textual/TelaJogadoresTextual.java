package presentation.textual;

import clube.application.ClubeService;

import jogador.application.JogadorService;
import jogador.domain.Jogador;
import jogador.domain.JogadorLinha;
import jogador.domain.PosicaoLinha;
import presentation.produtos.TelaJogadores;

import java.util.Scanner;

/**
 * Tela de jogadores no console.
 */
class TelaJogadoresTextual implements TelaJogadores {

    private final JogadorService jogadorService;
    private final ClubeService clubeService;
    private final Scanner entrada;

    TelaJogadoresTextual(JogadorService jogadorService, ClubeService clubeService, Scanner entrada) {
        this.jogadorService = jogadorService;
        this.clubeService = clubeService;
        this.entrada = entrada;
    }

    @Override
    public void exibir() {
        while (true) {
            System.out.println();
            System.out.println("=== Jogadores ===");
            System.out.println("1. Listar jogadores");
            System.out.println("2. Criar jogador");
            System.out.println("3. Editar jogador");
            System.out.println("4. Excluir jogador");
            System.out.println("5. Transferir jogador");
            System.out.println("0. Voltar");
            String op = ler("Opção");

            try {
                switch (op) {
                    case "1":
                        listar();
                        break;
                    case "2":
                        criar();
                        break;
                    case "3":
                        editar();
                        break;
                    case "4":
                        excluir();
                        break;
                    case "5":
                        transferir();
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (RuntimeException e) {
                System.out.println("ERRO: " + e.getMessage());
            }
        }
    }

    private void listar() {
        if (jogadorService.listar().isEmpty()) {
            System.out.println("Nenhum jogador cadastrado.");
            return;
        }
        for (Jogador j : jogadorService.listar()) {
            String clube = j.getClube() == null ? "Sem clube" : j.getClube().getNome();
            System.out.println(j.getId() + " - " + j.getNome() + " - " + j.getIdade() + " anos - camisa "
                    + j.getNumeroCamisa() + " - " + j.getPosicaoPrincipal() + " - " + clube);
        }
    }

    private void criar() {
        String tipo = ler("Jogador de linha ou goleiro? [L/G]").toUpperCase();
        if (!tipo.equals("L") && !tipo.equals("G")) {
            throw new IllegalArgumentException("Tipo inválido, digite L ou G.");
        }

        String nome = ler("Nome");
        int idade = lerInteiro("Idade");
        int numCamisa = lerInteiro("Número da camisa");

        if (tipo.equals("L")) {
            PosicaoLinha posicao = lerPosicao();
            jogadorService.criarLinha(nome, idade, numCamisa, posicao);
        } else if (tipo.equals("G")) {
            jogadorService.criarGoleiro(nome, idade, numCamisa);
        }
        System.out.println("Jogador criado.");
    }

    private void editar() {
        int id = lerInteiro("Id");
        Jogador jogador = jogadorService.buscar(id);
        String nome = ler("Nome");
        int idade = lerInteiro("Idade");
        int numCamisa = lerInteiro("Número da camisa");

        PosicaoLinha posicao = null;
        if (jogador instanceof JogadorLinha) {
            posicao = lerPosicao();
        }

        jogadorService.editar(id, nome, idade, numCamisa, posicao);
        System.out.println("Jogador editado.");
    }

    private void excluir() {
        int id = lerInteiro("Id");
        jogadorService.excluir(id);
        System.out.println("Jogador excluído.");
    }

    private void transferir() {
        int jogadorId = lerInteiro("Id do jogador escolhido");
        int clubeDestinoId = lerInteiro("Id do clube de destino");
        clubeService.transferir(jogadorId, clubeDestinoId);
        System.out.println("Jogador transferido.");
    }

    private String ler(String rotulo) {
        System.out.print(rotulo + ": ");
        return entrada.nextLine().trim();
    }

    private int lerInteiro(String rotulo) {
        try {
            return Integer.parseInt(ler(rotulo));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Digite um número válido.");
        }
    }

    private PosicaoLinha lerPosicao() {
        String texto = ler("Posição (ZAGUEIRO, LATERAL, MEIO_CAMPO, ATACANTE)").toUpperCase();
        try {
            return PosicaoLinha.valueOf(texto);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Posição inválida. Use ZAGUEIRO, LATERAL, MEIO_CAMPO ou ATACANTE.");
        }
    }

}
