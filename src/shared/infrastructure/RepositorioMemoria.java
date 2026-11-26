package shared.infrastructure;

import shared.application.Repositorio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Repositório que guarda os objetos numa lista na memória; é a base do RepositorioArquivo.
 * O extrator de id ensina como obter o id de um objeto, já que o tipo T é genérico.
 */
public class RepositorioMemoria<T> implements Repositorio<T> {

    private final List<T> itens;
    private final Function<T, Integer> extratorId;

    public RepositorioMemoria(Function<T, Integer> extratorId) {
        this(new ArrayList<>(), extratorId);
    }

    // Sem public: só o pacote infrastructure usa, para compartilhar uma lista já existente.
    RepositorioMemoria(List<T> itens, Function<T, Integer> extratorId){
        this.itens = itens;
        this.extratorId = extratorId;
    }

    /** Devolve Optional para obrigar quem chama a tratar o caso "não encontrado". */
    @Override
    public Optional<T> buscarPorId(int id) {
        return itens.stream().filter(
                item -> extratorId.apply(item) == id).findFirst();
    }

    /** Se já existe objeto com o mesmo id, substitui; senão adiciona (assim editar não duplica). */
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

    /** Devolve uma visão não modificável da lista interna. */
    @Override
    public List<T> listarTodos() {
        return Collections.unmodifiableList(itens);
    }

    @Override
    public void remover(int id) {
        itens.removeIf(item -> extratorId.apply(item) == id);
    }

    /** Dados só na memória: não há o que gravar. */
    @Override
    public void gravar() {

    }

    /** Dados só na memória: não há o que carregar. */
    @Override
    public void carregar() {

    }
}
