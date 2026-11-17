package shared.infrastructure;

import shared.application.Repositorio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class RepositorioMemoria<T> implements Repositorio<T> {

    private final List<T> itens = new ArrayList<>();
    private final Function<T, Integer> extratorId;

    public RepositorioMemoria(Function<T, Integer> extratorId) {
        this.extratorId = extratorId;
    }

    @Override
    public Optional<T> buscarPorId(int id) {
        return itens.stream().filter(
                item -> extratorId.apply(item) == id).findFirst();
    }

    @Override
    public void salvar(T objeto) {
        if (objeto == null) {
            throw new IllegalArgumentException("Objeto inválido.");
        }
        int id = extratorId.apply(objeto);
        for (int i = 0; i < itens.size(); i++){
            if (extratorId.apply(itens.get(i)) == id) {
                itens.set(i, objeto);
                return;
            }
        }
        itens.add(objeto);
    }

    @Override
    public List<T> listarTodos() {
        return Collections.unmodifiableList(itens);
    }

    @Override
    public void remover(int id) {
        itens.removeIf(item -> extratorId.apply(item) == id);
    }
}
