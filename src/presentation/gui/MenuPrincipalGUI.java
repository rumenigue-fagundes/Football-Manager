package presentation.gui;

import presentation.produtos.MenuPrincipal;
import presentation.produtos.TelaClubes;
import presentation.produtos.TelaJogadores;
import shared.application.PersistenciaService;

import javax.swing.*;
import java.awt.*;

class MenuPrincipalGUI extends JFrame implements MenuPrincipal {

    private final TelaClubes telaClubes;
    private final TelaJogadores telaJogadores;
    private final PersistenciaService persistenciaService;

    MenuPrincipalGUI(TelaClubes telaClubes, TelaJogadores telaJogadores,
                            PersistenciaService persistenciaService) {
        this.telaClubes = telaClubes;
        this.telaJogadores = telaJogadores;
        this.persistenciaService = persistenciaService;
        setTitle("Sistema de futebol");
        setSize(300, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1, 10, 10));

        JButton botaoClubes = new JButton("Clubes");
        botaoClubes.addActionListener(e -> telaClubes.exibir());
        add(botaoClubes);

        JButton botaoJogadores = new JButton("Jogadores");
        botaoJogadores.addActionListener(e-> telaJogadores.exibir());
        add(botaoJogadores);
    }

    @Override
    public void exibir() {
        SwingUtilities.invokeLater(()->setVisible(true));
    }
}
