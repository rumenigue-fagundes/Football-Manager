package presentation.gui;

import clube.application.ClubeService;
import clube.domain.Clube;
import jogador.domain.Jogador;
import presentation.produtos.TelaClubes;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

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

        DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);
        tabela.getColumnModel().getColumn(0).setCellRenderer(centralizado);
        tabela.getColumnModel().getColumn(3).setCellRenderer(centralizado);

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

        JButton botaoElenco = new JButton("Ver elenco");
        botaoElenco.addActionListener(e -> verElenco());
        painel.add(botaoElenco);

        JButton botaoFechar = new JButton("Fechar");
        botaoFechar.addActionListener(e -> dispose());
        painel.add(botaoFechar);

        return painel;
    }

    /** Mostra os jogadores do clube selecionado (camisa, nome e posição). */
    private void verElenco() {
        try {
            int id = idSelecionado();
            Clube clube = clubeService.buscar(id);
            List<Jogador> elenco = clubeService.listarElenco(id);
            if (elenco.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O clube não tem jogadores.",
                        clube.getNome(), JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            StringBuilder texto = new StringBuilder();
            for (Jogador j : elenco) {
                texto.append(j.getNumeroCamisa()).append(" - ").append(j.getNome())
                        .append(" (").append(j.getPosicaoPrincipal()).append(")\n");
            }
            JOptionPane.showMessageDialog(this, texto.toString(),
                    "Elenco - " + clube.getNome(), JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
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
    /** Formulário com os valores atuais do clube; se o serviço recusar os dados, o formulário reaparece. */
    private void editar() {
        Clube clube;
        try {
            clube = clubeService.buscar(idSelecionado());
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField campoNome = new JTextField(clube.getNome(), 20);
        JTextField campoCidade = new JTextField(clube.getCidade(), 20);
        JPanel formulario = new JPanel(new GridLayout(0, 2, 5, 5));
        formulario.add(new JLabel("Nome:"));
        formulario.add(campoNome);
        formulario.add(new JLabel("Cidade:"));
        formulario.add(campoCidade);

        while (true) {
            int resposta = JOptionPane.showConfirmDialog(this, formulario, "Editar clube",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (resposta != JOptionPane.OK_OPTION) {
                return;                                   // o usuário cancelou
            }
            try {
                clubeService.editar(clube.getId(), campoNome.getText().trim(), campoCidade.getText().trim());
                atualizarTabela();
                return;                                   // deu certo
            } catch (RuntimeException ex) {
                atualizarTabela();                        // a tela mostra o que realmente foi alterado
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /** Pede nome e cidade; erros do Domain aparecem numa caixa de erro. */
    private void novo() {
        JTextField campoNome = new JTextField(20);
        JTextField campoCidade = new JTextField(20);
        JPanel formulario = new JPanel(new GridLayout(0, 2, 5, 5));
        formulario.add(new JLabel("Nome:"));
        formulario.add(campoNome);
        formulario.add(new JLabel("Cidade:"));
        formulario.add(campoCidade);

        while (true) {
            int resposta = JOptionPane.showConfirmDialog(this, formulario, "Novo clube",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (resposta != JOptionPane.OK_OPTION) {
                return;                                   // o usuário cancelou
            }
            try {
                clubeService.criar(campoNome.getText().trim(), campoCidade.getText().trim());
                atualizarTabela();
                return;                                   // deu certo
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                // o laço repete: o formulário volta com os campos como o usuário deixou
            }
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
