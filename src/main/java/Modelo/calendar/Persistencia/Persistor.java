package Modelo.calendar.Persistencia;

import Modelo.calendar.Recordatorio;

import java.io.IOException;
import java.util.List;

public interface Persistor {

     void serializar(List<Recordatorio> recordatorios) throws IOException;
     List<Recordatorio> deserealizar() throws IOException;
}
