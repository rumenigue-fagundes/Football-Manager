package presentation.gui;

import javax.swing.table.DefaultTableModel;

class ModeloSomenteLeitura extends DefaultTableModel {

    ModeloSomenteLeitura(Object[] colunas) {
        super(colunas, 0);
    }

    @Override
    public boolean isCellEditable(int linha, int coluna) {
        return false;
    }
}
