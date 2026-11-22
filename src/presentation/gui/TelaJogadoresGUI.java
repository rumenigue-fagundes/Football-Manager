package presentation.gui;

import clube.application.ClubeService;
import clube.domain.Clube;
import jogador.application.JogadorService;
import jogador.domain.Jogador;
import jogador.domain.JogadorLinha;
import jogador.domain.PosicaoLinha;
import presentation.produtos.TelaJogadores;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.util.List;

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

    private void montarTela() {
        setLayout(new BorderLayout());

        modelo = new ModeloSomenteLeitura(new Object[]{"Id", "Nome", "Idade",
                "Número camisa", "Posição", "Clube"});
        tabela = new JTable(modelo);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        add(criarPainelBotoes(), BorderLayout.SOUTH);
    }

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

    private void editar() {
        try {
            int id = idSelecionado();
            Jogador jogador = jogadorService.buscar(id);

            String nome = JOptionPane.showInputDialog(
                    this, "Nome do jogador:", jogador.getNome());
            if (nome == null) {
                return;
            }

            String idadeTexto = JOptionPane.showInputDialog(
                    this, "Idade:", jogador.getIdade());
            if (idadeTexto == null) {
                return;
            }
            int idade = lerNumero(idadeTexto, "Idade");

            String camisaTexto = JOptionPane.showInputDialog(
                    this, "Número da camisa:", jogador.getNumeroCamisa()
            );
            if (camisaTexto == null) {
                return;
            }
            int numCamisa = lerNumero(camisaTexto, "Número da camisa");

            PosicaoLinha posicao = null;

            if (jogador instanceof JogadorLinha linha) {
                posicao = (PosicaoLinha) JOptionPane.showInputDialog(
                        this, "Selecione a posição:",
                        "Posição do jogador", JOptionPane.QUESTION_MESSAGE, null,
                        PosicaoLinha.values(), linha.getPosicao()
                );
                if (posicao == null) {
                    return;
                }
            }

            jogadorService.editar(id, nome.trim(), idade, numCamisa, posicao);
            atualizarTabela();

        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void novo() {
        try {

            String nome = JOptionPane.showInputDialog(
                    this, "Nome do jogador:");
            if (nome == null) {
                return;
            }

            String idadeTexto = JOptionPane.showInputDialog(
                    this, "Idade:");
            if (idadeTexto == null) {
                return;
            }
            int idade = lerNumero(idadeTexto, "Idade");

            String camisaTexto = JOptionPane.showInputDialog(
                    this, "Número da camisa:");
            if (camisaTexto == null) {
                return;
            }
            int numCamisa = lerNumero(camisaTexto, "Número da camisa");

            String[] tipos = {"Goleiro", "Jogador de linha"};

            String tipo = (String) JOptionPane.showInputDialog(
                    this, "Selecione o tipo do jogador:",
                    "Tipo de jogador", JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]
            );

            if (tipo == null) {
                return;
            }

            if (tipo.equals("Goleiro")) {
                jogadorService.criarGoleiro(nome.trim(), idade, numCamisa);
            } else {
                PosicaoLinha posicao = (PosicaoLinha) JOptionPane.showInputDialog(
                        this,
                        "Selecione a posição:",
                        "Posição do jogador",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        PosicaoLinha.values(),
                        PosicaoLinha.ATACANTE
                );
                if (posicao == null) {
                    return;
                }

                jogadorService.criarLinha(nome.trim(), idade, numCamisa, posicao);
            }

            atualizarTabela();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int idSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            throw new IllegalArgumentException("Selecione um jogador na tabela.");
        }
        return (Integer) modelo.getValueAt(linha, 0);
    }

    private int lerNumero(String texto, String campo) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(campo + " deve ser um número.");
        }
    }
}
