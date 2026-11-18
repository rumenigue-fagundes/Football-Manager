package shared.application;

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
