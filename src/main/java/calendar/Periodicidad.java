package calendar;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Periodicidad {

    private final Integer cantIteraciones;
    private final Frecuencia frecuencia;
    private final LocalDateTime fechaLimite;

    public Periodicidad(Integer cantIteraciones, Frecuencia frecuencia, LocalDateTime fechaLimite) {
        this.cantIteraciones = cantIteraciones;
        this.frecuencia = frecuencia;
        this.fechaLimite = fechaLimite;
    }
}
