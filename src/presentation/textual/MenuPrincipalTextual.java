package presentation.textual;

import presentation.produtos.MenuPrincipal;
import presentation.produtos.TelaClubes;
import presentation.produtos.TelaJogadores;
import shared.application.PersistenciaService;

import java.util.Scanner;

/** Menu de console: encaminha o usuário para as telas e para salvar/carregar. */
class MenuPrincipalTextual implements MenuPrincipal {

    private final TelaClubes telaClubes;
    private final TelaJogadores telaJogadores;
    private final PersistenciaService persistenciaService;
    private final Scanner entrada;

    MenuPrincipalTextual(TelaClubes telaClubes, TelaJogadores telaJogadores,
                         PersistenciaService persistenciaService, Scanner entrada) {
        this.telaClubes = telaClubes;
        this.telaJogadores = telaJogadores;
        this.persistenciaService = persistenciaService;
        this.entrada = entrada;
    }

    /** Laço do menu: repete até o usuário escolher 0 (Sair); erros são mostrados como ERRO. */
    @Override
    public void exibir() {
        while (true) {
            System.out.println();
            System.out.println("=== SISTEMA DE FUTEBOL ===");
            System.out.println("1 - Clubes");
            System.out.println("2 - Jogadores");
            System.out.println("3 - Salvar dados");
            System.out.println("4 - Carregar dados");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");
            String opcao = entrada.nextLine().trim();
            try {
                switch (opcao) {
                    case "1":
                        telaClubes.exibir();
                        break;
                    case "2":
                        telaJogadores.exibir();
                        break;
                    case "3":
                        persistenciaService.salvar();
                        System.out.println("Dados salvos.");
                        break;
                    case "4":
                        persistenciaService.carregar();
                        System.out.println("Dados carregados.");
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
}
