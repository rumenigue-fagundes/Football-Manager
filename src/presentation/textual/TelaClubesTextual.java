package presentation.textual;

import clube.application.ClubeService;
import clube.domain.Clube;
import jogador.domain.Jogador;
import presentation.produtos.TelaClubes;

import java.util.List;
import java.util.Scanner;

/**
 * Tela de clubes no console.
 */
class TelaClubesTextual implements TelaClubes {

    private final ClubeService clubeService;
    private final Scanner entrada;

    TelaClubesTextual(ClubeService clubeService, Scanner entrada) {
        this.clubeService = clubeService;
        this.entrada = entrada;
    }

    /** Laço do submenu: repete até o usuário escolher 0 (Voltar); erros do Domain aparecem como ERRO. */
    @Override
    public void exibir() {
        while (true) {
            System.out.println();
            System.out.println("=== Clubes ===");
            System.out.println("1. Listar clubes");
            System.out.println("2. Criar clube");
            System.out.println("3. Editar clube");
            System.out.println("4. Excluir Clube");
            System.out.println("5. Visualizar elenco");
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
                        verElenco();
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

    /** Mostra um clube por linha, com o tamanho do elenco. */
    private void listar() {
        if (clubeService.listar().isEmpty()) {
            System.out.println("Nenhum clube cadastrado.");
            return;
        }
        for (Clube c : clubeService.listar()) {
            System.out.println(c.getId() + " - " + c.getNome() + " (" + c.getCidade() + ") - "
                    + c.getElenco().size() + " jogadores");
        }
    }

    private void criar() {
        String nome = ler("Nome");
        String cidade = ler("Cidade");
        clubeService.criar(nome, cidade);
        System.out.println("Clube criado.");
    }

    private void editar() {
        int id = lerInteiro("Id");
        String nome = ler("Nome");
        String cidade = ler("Cidade");
        clubeService.editar(id, nome, cidade);
        System.out.println("Clube editado.");
    }

    private void excluir() {
        int id = lerInteiro("Id");
        clubeService.excluir(id);
        System.out.println("Clube excluído.");
    }

    private void verElenco() {
        int id = lerInteiro("Id do clube");
        List<Jogador> elenco = clubeService.listarElenco(id);
        if (elenco.isEmpty()) {
            System.out.println("O clube não tem jogadores.");
            return;
        }
        for (Jogador j : elenco) {
            System.out.println(j.getNumeroCamisa() + " - " + j.getNome() + " (" + j.getPosicaoPrincipal() + ")");
        }
    }

    /** Mostra o rótulo e lê uma linha digitada. */
    private String ler(String rotulo) {
        System.out.print(rotulo + ": ");
        return entrada.nextLine().trim();
    }

    /** Converte a linha lida em número; texto inválido vira uma mensagem clara. */
    private int lerInteiro(String rotulo) {
        try {
            return Integer.parseInt(ler(rotulo));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Digite um número válido.");
        }
    }
}
