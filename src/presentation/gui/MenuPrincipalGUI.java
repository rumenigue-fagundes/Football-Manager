package presentation.gui;

import presentation.produtos.MenuPrincipal;
import presentation.produtos.TelaClubes;
import presentation.produtos.TelaJogadores;
import shared.application.PersistenciaService;

import javax.swing.*;
import java.awt.*;

/**
 * Janela principal (JFrame) com os botões Clubes, Jogadores, Salvar e Carregar.
 * Ao fechá-la, o programa termina.
 */
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
        botaoJogadores.addActionListener(e -> telaJogadores.exibir());
        add(botaoJogadores);

        JButton botaoSalvar = new JButton("Salvar");
        botaoSalvar.addActionListener(e -> salvar());
        add(botaoSalvar);

        JButton botaoCarregar = new JButton("Carregar");
        botaoCarregar.addActionListener(e -> carregar());
        add(botaoCarregar);
    }

    /**
     * Só mostra a janela (na thread do Swing); quem mantém o programa vivo é o laço de eventos.
     */
    @Override
    public void exibir() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }

    /**
     * Salva os dados e mostra uma mensagem de sucesso ou o erro ocorrido.
     */
    private void salvar() {
        int resposta = JOptionPane.showConfirmDialog(this,
                "Isso substitui o arquivo salvo anteriormente. Continuar?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (resposta != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            persistenciaService.salvar();
            JOptionPane.showMessageDialog(this, "Dados salvos.");

        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carrega os dados do arquivo e mostra uma mensagem de sucesso ou o erro ocorrido.
     */
    private void carregar() {
        int resposta = JOptionPane.showConfirmDialog(this,
                "Os dados não salvos serão perdidos. Continuar?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (resposta != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            persistenciaService.carregar();
            JOptionPane.showMessageDialog(this, "Dados carregados.");
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
