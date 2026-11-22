package presentation.factory;

import presentation.gui.GUIFactory;
import presentation.textual.TextualFactory;

/** Classe estática: concentra a configuração que escolhe a interface do sistema. */
public final class ConfiguracaoSistema {

    //true usa janelas, false usa o console.
    private static final boolean USAR_GUI = true;

    private ConfiguracaoSistema() {
    }

    public static ApresentacaoFactory obterFactory() {
        // TODO GUI: return USAR_GUI ? new GUIFactory() : new TextualFactory();
        return USAR_GUI ? new GUIFactory() : new TextualFactory();
    }
}
