package calendar;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Set;


public class Evento extends Recordatorio {
    private Repetidor repetidor;
    public Evento(LocalDateTime inicio, Integer horas, Integer minutos) {
        super(inicio, horas, minutos);
        super.nombre = "Nuevo evento";
    }

    @Override
    public boolean verificarRepeticion(){
        return repetidor != null;
    }

    public void configurarRepeticion(Frecuencia frecuencia, Limite limite){}

    public void configurarFechaLimite(LocalDateTime fechaLimite){}

    public void configurarIteracion(Integer iteraciones){}

    public void configurarDias(Set<DayOfWeek> dias){}





}
