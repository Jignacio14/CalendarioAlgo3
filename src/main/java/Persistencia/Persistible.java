package Persistencia;

import java.io.IOException;
import java.util.List;

import calendar.Recordatorio;

public interface Persistible {

    void guardar(PersistorJSON persitor) throws IOException;

    List<Recordatorio> cargar() throws IOException;
}
