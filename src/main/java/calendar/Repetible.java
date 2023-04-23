package calendar;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface Repetible {

     boolean verificarRepeticion();

     boolean verificarHayProximaRepeticion();

     void configurarRepeticion(Frecuencia frecuencia, Limite limite);

     void configurarFechaLimite(LocalDateTime fechaLimite);

     void configurarIteracion(Integer iteraciones);

     void configurarIntervalo(Integer intervalo);
     void configurarDias(Set<DayOfWeek> dias);
     List<LocalDateTime> verRepeticiones(LocalDateTime hasta);
}
