package presentation.gui;

import presentation.produtos.MenuPrincipal;
import presentation.produtos.TelaClubes;
import presentation.produtos.TelaJogadores;
import shared.application.PersistenciaService;

public class MenuPrincipalGUI implements MenuPrincipal {

    private final TelaClubes telaClubes;
    private final TelaJogadores telaJogadores;
    private final PersistenciaService persistenciaService;

    public MenuPrincipalGUI(TelaClubes telaClubes, TelaJogadores telaJogadores,
                            PersistenciaService persistenciaService) {
        this.telaClubes = telaClubes;
        this.telaJogadores = telaJogadores;
        this.persistenciaService = persistenciaService;
    }

    @Override
    public void exibir() {

    }
}
