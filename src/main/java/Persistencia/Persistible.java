package Persistencia;

import java.io.IOException;


public interface Persistible {
    void guardar(Persistor persistor) throws IOException;
    void cargar(Persistor persistor) throws IOException;
}
