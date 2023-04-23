package calendar;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Periodicidad {

    private final Integer cantIteraciones;
    private final Frecuencia frecuencia;
    private final LocalDateTime fechaLimite;
    //private final Integer ocurrencias;

    public Periodicidad(Integer cantIteraciones, Frecuencia frecuencia, LocalDateTime fechaLimite, Integer ocurrencias) {
        this.cantIteraciones = cantIteraciones;
        this.frecuencia = frecuencia;
        this.fechaLimite = (fechaLimite == null ? calcularFechaLimite(ocurrencias) : fechaLimite);
    }

    private LocalDateTime calcularFechaLimite(Integer ocurrencias){
        return null;
    }

    // se genera la cantidad de eventos repetidos que esten entre el rango fechaInicio y fechaFin
    // fechaFin < fechaLimite: se genera eventos hasta fecheFin
    // fechaFin > fechaLimite: se genera eventos hasta fechaLimite
    public Evento generarRepeticiones(LocalDateTime fechaInicio, LocalDateTime fechaFin){
        return null;
    }

    // GETTERS
    public LocalDateTime obtenerFechaLimite(){
        return this.fechaLimite;
    }


}
