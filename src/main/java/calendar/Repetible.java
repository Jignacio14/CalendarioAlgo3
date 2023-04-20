package calendar;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface Repetible {

    public boolean verificarRepeticion();

    public boolean verificarHayProximaRepeticion();

    public void configurarRepeticion(Frecuencia frecuencia, Limite limite);

    public void configurarFechaLimite(LocalDateTime fechaLimite);

    public void configurarIteracion(Integer iteraciones);

    public void configurarIntervalo(Integer intervalo);
    public void configurarDias(Set<DayOfWeek> dias);
    public List<LocalDateTime> verRepeticiones(LocalDateTime hasta);
}
