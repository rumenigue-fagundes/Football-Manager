package presentation.gui;

import clube.application.ClubeService;
import clube.domain.Clube;
import jogador.application.JogadorService;
import jogador.domain.Jogador;
import jogador.domain.JogadorLinha;
import jogador.domain.PosicaoLinha;
import presentation.produtos.TelaJogadores;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.util.List;

/**
 * Janela modal (JDialog) de jogadores: tabela com os jogadores e botões Novo, Editar, Excluir,
 * Transferir e Fechar. Não tem regra de negócio: só lê dados do usuário e chama os serviços.
 */
class TelaJogadoresGUI extends JDialog implements TelaJogadores {

    private final JogadorService jogadorService;
    private final ClubeService clubeService;
    private DefaultTableModel modelo;
    private JTable tabela;

    TelaJogadoresGUI(JogadorService jogadorService, ClubeService clubeService) {
        super((Frame) null, "Jogadores", true);
        this.jogadorService = jogadorService;
        this.clubeService = clubeService;
        setSize(600, 400);
        setLocationRelativeTo(null);
        montarTela();

    }

    @Override
    public void exibir() {
        atualizarTabela();
        setVisible(true);
    }

    /** Monta a janela: tabela no centro e botões embaixo. */
    private void montarTela() {
        setLayout(new BorderLayout());

        modelo = new ModeloSomenteLeitura(new Object[]{"Id", "Nome", "Idade",
                "Número camisa", "Posição", "Clube"});
        tabela = new JTable(modelo);

        DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);
        tabela.getColumnModel().getColumn(0).setCellRenderer(centralizado);
        tabela.getColumnModel().getColumn(2).setCellRenderer(centralizado);
        tabela.getColumnModel().getColumn(3).setCellRenderer(centralizado);

        add(new JScrollPane(tabela), BorderLayout.CENTER);

        add(criarPainelBotoes(), BorderLayout.SOUTH);
    }

    /** Recarrega a tabela a partir do serviço (ao abrir a janela e após cada alteração). */
    private void atualizarTabela() {
        modelo.setRowCount(0);

        for (Jogador j : jogadorService.listar()) {
            String nomeClube = j.getClube() == null
                    ? "Sem clube"
                    : j.getClube().getNome();

            modelo.addRow(new Object[]{j.getId(), j.getNome(), j.getIdade(), j.getNumeroCamisa(),
                    j.getPosicaoPrincipal(), nomeClube});
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

        JButton botaoTransferir = new JButton("Transferir");
        botaoTransferir.addActionListener(e -> transferir());
        painel.add(botaoTransferir);

        JButton botaoFechar = new JButton("Fechar");
        botaoFechar.addActionListener(e -> dispose());
        painel.add(botaoFechar);

        return painel;
    }

    /** Escolhe o clube de destino num combo; o ClubeService decide se aceita (camisa repetida, por exemplo). */
    private void transferir() {
        try {
            int id = idSelecionado();
            Jogador jogador = jogadorService.buscar(id);

            List<Clube> clubes = clubeService.listar();

            if (clubes.isEmpty()) {
                throw new IllegalArgumentException("Não existem clubes cadastrados");
            }

            JComboBox<Clube> comboClubes = new JComboBox<>(
                    clubes.toArray(new Clube[0])
            );

            int resposta = JOptionPane.showConfirmDialog(
                    this,
                    comboClubes,
                    "Selecione o clube de destino",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (resposta != JOptionPane.OK_OPTION) {
                return;
            }

            Clube destino = (Clube) comboClubes.getSelectedItem();

            if (destino == null) {
                return;
            }

            clubeService.transferir(jogador.getId(), destino.getId());

            atualizarTabela();

            JOptionPane.showMessageDialog(
                    this,
                    jogador.getNome() + " foi transferido para " + destino.getNome() + "."
            );

        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Exclui o jogador selecionado depois de pedir confirmação. */
    private void excluir() {
        try {
            int id = idSelecionado();
            int resposta = JOptionPane.showConfirmDialog(this,
                    "Excluir o jogador?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (resposta != JOptionPane.YES_OPTION) {
                return;
            }
            jogadorService.excluir(id);
            atualizarTabela();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Edita o jogador selecionado; a posição só é pedida para jogador de linha. */
    /** Formulário único; se o serviço recusar os dados, ele reaparece com os campos como o usuário deixou. */
    private void novo() {
        JTextField campoNome = new JTextField(20);
        JTextField campoIdade = new JTextField(20);
        JTextField campoCamisa = new JTextField(20);
        JComboBox<String> comboTipo = new JComboBox<>(new String[]{"Jogador de linha", "Goleiro"});
        JComboBox<PosicaoLinha> comboPosicao = new JComboBox<>(PosicaoLinha.values());
        // Goleiro não tem posição: o combo só fica habilitado para jogador de linha.
        comboTipo.addActionListener(e -> comboPosicao.setEnabled(comboTipo.getSelectedIndex() == 0));

        JPanel formulario = new JPanel(new GridLayout(0, 2, 5, 5));
        formulario.add(new JLabel("Nome:"));
        formulario.add(campoNome);
        formulario.add(new JLabel("Idade:"));
        formulario.add(campoIdade);
        formulario.add(new JLabel("Número da camisa:"));
        formulario.add(campoCamisa);
        formulario.add(new JLabel("Tipo:"));
        formulario.add(comboTipo);
        formulario.add(new JLabel("Posição:"));
        formulario.add(comboPosicao);

        while (true) {
            int resposta = JOptionPane.showConfirmDialog(this, formulario, "Novo jogador",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (resposta != JOptionPane.OK_OPTION) {
                return;                                   // o usuário cancelou
            }
            try {
                String nome = campoNome.getText().trim();
                int idade = lerNumero(campoIdade.getText(), "Idade");
                int numCamisa = lerNumero(campoCamisa.getText(), "Número da camisa");
                if (comboTipo.getSelectedIndex() == 1) {
                    jogadorService.criarGoleiro(nome, idade, numCamisa);
                } else {
                    jogadorService.criarLinha(nome, idade, numCamisa,
                            (PosicaoLinha) comboPosicao.getSelectedItem());
                }
                atualizarTabela();
                return;                                   // deu certo
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                // o laço repete: o formulário volta com os campos como o usuário deixou
            }
        }
    }

    /** Formulário com os valores atuais; a posição só aparece para jogador de linha. Erros reabrem o formulário. */
    private void editar() {
        Jogador jogador;
        try {
            jogador = jogadorService.buscar(idSelecionado());
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField campoNome = new JTextField(jogador.getNome(), 20);
        JTextField campoIdade = new JTextField(String.valueOf(jogador.getIdade()), 20);
        JTextField campoCamisa = new JTextField(String.valueOf(jogador.getNumeroCamisa()), 20);

        JPanel formulario = new JPanel(new GridLayout(0, 2, 5, 5));
        formulario.add(new JLabel("Nome:"));
        formulario.add(campoNome);
        formulario.add(new JLabel("Idade:"));
        formulario.add(campoIdade);
        formulario.add(new JLabel("Número da camisa:"));
        formulario.add(campoCamisa);

        JComboBox<PosicaoLinha> comboPosicao = null;     // continua null para goleiro
        if (jogador instanceof JogadorLinha linha) {
            comboPosicao = new JComboBox<>(PosicaoLinha.values());
            comboPosicao.setSelectedItem(linha.getPosicao());
            formulario.add(new JLabel("Posição:"));
            formulario.add(comboPosicao);
        }

        while (true) {
            int resposta = JOptionPane.showConfirmDialog(this, formulario, "Editar jogador",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (resposta != JOptionPane.OK_OPTION) {
                return;                                   // o usuário cancelou
            }
            try {
                String nome = campoNome.getText().trim();
                int idade = lerNumero(campoIdade.getText(), "Idade");
                int numCamisa = lerNumero(campoCamisa.getText(), "Número da camisa");
                PosicaoLinha posicao = comboPosicao == null
                        ? null
                        : (PosicaoLinha) comboPosicao.getSelectedItem();
                jogadorService.editar(jogador.getId(), nome, idade, numCamisa, posicao);
                atualizarTabela();
                return;                                   // deu certo
            } catch (RuntimeException ex) {
                atualizarTabela();                        // a tela mostra o que realmente foi alterado
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /** Id da linha selecionada (coluna 0); sem seleção, lança a exceção cuja mensagem é mostrada ao usuário. */
    private int idSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            throw new IllegalArgumentException("Selecione um jogador na tabela.");
        }
        return (Integer) modelo.getValueAt(linha, 0);
    }

    /** Converte o texto em número; texto inválido vira uma mensagem clara em vez do erro do Java. */
    private int lerNumero(String texto, String campo) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(campo + " deve ser um número.");
        }
    }
}
