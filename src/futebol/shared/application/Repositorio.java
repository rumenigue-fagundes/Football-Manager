package futebol.shared.application;

import java.util.List;
import java.util.Optional;

public interface Repositorio<T> {
    void salvar(T objeto);
    Optional<T> buscarPorId(int id);
    List<T> listarTodos();
    void remover(int id);
}
