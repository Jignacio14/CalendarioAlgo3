package calendar;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;

public class Evento extends Recordatorio {
    private Periodicidad repetidor;
    public Evento(LocalDateTime inicio, Integer horas, Integer minutos) {
        super(inicio, horas, minutos);

    }

    public boolean tieneRepeticion(){
        return periodicidad != null;
    }

    //private final ArrayList<LocalDateTime> repeticiones = new ArrayList<>();

    // -cantRepeticiones: la cantidad de veces ue se va a repetir (sea con un tipoFrecuencia o despues de "x" repeticiones)
    // -tipoFrencuencia:  Diario, Semanal, Mensual, Anual
    // -fechaLimite: fecha(se indico que termina en una fecha en especifico), nunca (algo que indique que es infinito),
    // -ocurrencias: si es 0 significa que termina nunca o en una fecha especifica, >0 significa que termina despues de "x" apariciones
    public void agregarPeriodicidad (Integer cantRepeticiones, Frecuencia tipoFrecuencia, LocalDateTime fechaLimite, Integer ocurrencias){
        this.periodicidad = new Periodicidad(cantRepeticiones, tipoFrecuencia, fechaLimite, ocurrencias);
    }

    public void verRepeticiones(LocalDateTime fechaInicio, LocalDateTime fechaFin){
        periodicidad.generarRepeticiones(fechaInicio, fechaFin);
    }

    // GETTERS
    public LocalDateTime obtenerFechaLimite(){
        return periodicidad.obtenerFechaLimite();
    }
}
