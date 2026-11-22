package presentation.textual;

import clube.application.ClubeService;
import jogador.application.JogadorService;
import presentation.produtos.TelaJogadores;

import java.util.Scanner;

/** Tela de jogadores no console. */
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
        // TODO: listar, criar, editar, excluir, vincular e transferir jogadores
        System.out.println("(tela de jogadores em construção)");
    }
}
