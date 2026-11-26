package presentation.gui;

import clube.application.ClubeService;
import clube.domain.Clube;
import presentation.produtos.TelaClubes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Janela modal (JDialog) de clubes: tabela com os clubes e botões Novo, Editar, Excluir e Fechar.
 * Não tem regra de negócio: só lê dados do usuário e chama o ClubeService.
 */
class TelaClubesGUI extends JDialog implements TelaClubes {

    private final ClubeService clubeService;
    private DefaultTableModel modelo;
    private JTable tabela;

    TelaClubesGUI(ClubeService clubeService) {
        super((Frame) null, "Clubes", true);
        this.clubeService = clubeService;
        setSize(600, 400);
        setLocationRelativeTo(null);
        montarTela();

    }

    /** Monta a janela: tabela no centro e botões embaixo. */
    private void montarTela() {
        setLayout(new BorderLayout());

        modelo = new ModeloSomenteLeitura(new Object[]{"Id", "Nome", "Cidade", "Jogadores"});
        tabela = new JTable(modelo);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        add(criarPainelBotoes(), BorderLayout.SOUTH);
    }

    /** Recarrega a tabela a partir do serviço (ao abrir a janela e após cada alteração). */
    private void atualizarTabela() {
        modelo.setRowCount(0);
        for (Clube c : clubeService.listar()) {
            modelo.addRow(new Object[]{c.getId(), c.getNome(), c.getCidade(), c.getElenco().size()});
        }
    }

    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel();

        JButton botaoNovo = new JButton("Novo");
        botaoNovo.addActionListener(e -> novo());
        painel.add(botaoNovo);

        JButton botaoEditar = new JButton("Editar");
        botaoEditar.addActionListener(e -> editar());
        painel.add(botaoEditar);

        JButton botaoExcluir = new JButton("Excluir");
        botaoExcluir.addActionListener(e -> excluir());
        painel.add(botaoExcluir);

        JButton botaoFechar = new JButton("Fechar");
        botaoFechar.addActionListener(e -> dispose());
        painel.add(botaoFechar);

        return painel;
    }

    /** Exclui o clube selecionado depois de pedir confirmação. */
    private void excluir() {
        try {
            int id = idSelecionado();
            int resposta = JOptionPane.showConfirmDialog(this,
                    "Excluir o clube?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (resposta != JOptionPane.YES_OPTION) {
                return;
            }
            clubeService.excluir(id);
            atualizarTabela();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Edita o clube selecionado; as caixas já vêm preenchidas com os valores atuais. */
    private void editar() {
        try {
            int id = idSelecionado();

            int linha = tabela.getSelectedRow();
            String nomeAtual = String.valueOf(modelo.getValueAt(linha, 1));
            String nome = JOptionPane.showInputDialog(this, "Nome do clube:", nomeAtual);
            if (nome == null) {
                return;
            }

            String cidadeAtual = String.valueOf(modelo.getValueAt(linha, 2));
            String cidade = JOptionPane.showInputDialog(this, "Cidade:", cidadeAtual);
            if (cidade == null) {
                return;
            }
            clubeService.editar(id, nome.trim(), cidade.trim());
            atualizarTabela();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Pede nome e cidade; erros do Domain aparecem numa caixa de erro. */
    private void novo() {
        try {
            String nome = JOptionPane.showInputDialog(this, "Nome do clube:");
            if (nome == null) {
                return;
            }
            String cidade = JOptionPane.showInputDialog(this, "Cidade:");
            if (cidade == null) {
                return;
            }
            clubeService.criar(nome.trim(), cidade.trim());
            atualizarTabela();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Id da linha selecionada (coluna 0); sem seleção, lança a exceção cuja mensagem é mostrada ao usuário. */
    private int idSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            throw new IllegalArgumentException("Selecione um clube na tabela.");
        }
        return (Integer) modelo.getValueAt(linha, 0);
    }


    /** Recarrega os dados e abre a janela; como é modal, só retorna quando ela fecha. */
    @Override
    public void exibir() {
        atualizarTabela();
        setVisible(true);
    }
}
