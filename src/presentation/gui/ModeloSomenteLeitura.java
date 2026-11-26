package presentation.gui;

import javax.swing.table.DefaultTableModel;

/**
 * Modelo de tabela cujas células não podem ser editadas: os dados só mudam pelos botões,
 * que passam pelo serviço e pelas validações do Domain.
 */
class ModeloSomenteLeitura extends DefaultTableModel {

    ModeloSomenteLeitura(Object[] colunas) {
        super(colunas, 0);
    }

    @Override
    public boolean isCellEditable(int linha, int coluna) {
        return false;
    }
}
