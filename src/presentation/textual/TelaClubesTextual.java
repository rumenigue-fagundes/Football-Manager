package presentation.textual;

import clube.application.ClubeService;
import presentation.produtos.TelaClubes;

import java.util.Scanner;

/** Tela de clubes no console. */
class TelaClubesTextual implements TelaClubes {

    private final ClubeService clubeService;
    private final Scanner entrada;

    TelaClubesTextual(ClubeService clubeService, Scanner entrada) {
        this.clubeService = clubeService;
        this.entrada = entrada;
    }

    @Override
    public void exibir() {
        // TODO: listar, criar, editar e excluir clubes
        System.out.println("(tela de clubes em construção)");
    }
}
