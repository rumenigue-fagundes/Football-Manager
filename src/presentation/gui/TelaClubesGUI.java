package presentation.gui;

import clube.application.ClubeService;
import presentation.produtos.TelaClubes;

public class TelaClubesGUI implements TelaClubes {

    private final ClubeService clubeService;

    public TelaClubesGUI(ClubeService clubeService) {
        this.clubeService = clubeService;
    }

    @Override
    public void exibir() {

    }
}
