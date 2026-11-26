package shared.application;

/**
 * Casos de uso Salvar e Carregar: aciona todos os repositórios do sistema.
 * A Presentation só conhece esta classe, nunca os detalhes de arquivo.
 */
public class PersistenciaService {
    private final Repositorio<?>[] repositorios;

    public PersistenciaService(Repositorio<?>... repositorios) {
        this.repositorios = repositorios;
    }

    public void salvar() {
        for (Repositorio<?> repositorio : repositorios){
            repositorio.gravar();
        }
    }

    public void carregar() {
        for (Repositorio<?> repositorio : repositorios){
            repositorio.carregar();
        }
    }
}
