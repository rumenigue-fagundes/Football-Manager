package presentation.factory;

import presentation.gui.GUIFactory;
import presentation.textual.TextualFactory;

/** Classe estática: concentra a configuração que escolhe a interface do sistema. */
public final class ConfiguracaoSistema {

    // Para trocar a interface do sistema, altere SOMENTE esta constante (o Main não muda):
    // true abre as janelas (Swing); false usa o console.
    private static final boolean USAR_GUI = true;

    private ConfiguracaoSistema() {
    }

    public static ApresentacaoFactory obterFactory() {
        return USAR_GUI ? new GUIFactory() : new TextualFactory();
    }
}
