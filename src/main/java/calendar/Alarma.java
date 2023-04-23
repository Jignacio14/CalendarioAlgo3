package calendar;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class Alarma implements Repetible {
    private LocalDateTime horallamada;

    private enum efecto {Email, Notificacion, Sonido}

    public Alarma() {
    }

    public boolean verificarRepeticion() {
        return false;
    }

    public boolean verificarHayProximaRepeticion() {
        return false;
    }

    public void configurarRepeticion(Frecuencia frecuencia, Limite limite) {

    }

    public void configurarFechaLimite(LocalDateTime fechaLimite) {

    }

    public void configurarIteracion(Integer iteraciones) {

    }

    public void configurarIntervalo(Integer intervalo) {

    }

    public void configurarDias(Set<DayOfWeek> dias) {

    }

    public List<LocalDateTime> verRepeticiones(LocalDateTime hasta) {
        return null;
    }
}
