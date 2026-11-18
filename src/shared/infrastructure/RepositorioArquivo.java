package shared.infrastructure;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

public class RepositorioArquivo<T> extends RepositorioMemoria {

    private final DadosSistema dados;
    private final Path arquivo;

    public RepositorioArquivo(DadosSistema dados,
                              Function<DadosSistema, List<T>> listaRepositorio,
                              Function<T, Integer> extratorId,
                              Path arquivo) {
        super(listaRepositorio.apply(dados), extratorId);
        this.dados = dados;
        this.arquivo = arquivo;
    }

    @Override
    public void gravar() {
        try {
            Path pasta = arquivo.getParent();
            if (pasta != null) {
                Files.createDirectories(pasta);
            }
            try (ObjectOutputStream saida =
                         new ObjectOutputStream(Files.newOutputStream(arquivo))){
                saida.writeObject(dados);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Não foi possivel gravar os dados", e);
        }
    }

    @Override
    public void carregar() {
        if (Files.notExists(arquivo)) {
            return;
        }
        try (ObjectInputStream entrada = new ObjectInputStream(Files.newInputStream(arquivo))){
            DadosSistema lidos = (DadosSistema) entrada.readObject();

            dados.getClubes().clear();
            dados.getClubes().addAll(lidos.getClubes());
            dados.getJogadores().clear();
            dados.getJogadores().addAll(lidos.getJogadores());
        } catch (IOException e) {
            throw new UncheckedIOException("Não foi possível carregar os dados.", e);
        } catch (ClassNotFoundException e){
            throw new IllegalStateException("Arquivo de dados incompatível com o sistema");
        }


    }
}
