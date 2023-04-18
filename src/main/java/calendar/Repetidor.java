package calendar;

import java.time.LocalDateTime;

public class Repetidor {

    private final Integer cantIteraciones;
    private final Frecuencia frecuencia;
    private final LocalDateTime fechaLimite;

    public Repetidor(Integer cantIteraciones, Frecuencia frecuencia, LocalDateTime fechaLimite) {
        this.cantIteraciones = cantIteraciones;
        this.frecuencia = frecuencia;
        this.fechaLimite = fechaLimite;
    }
}
