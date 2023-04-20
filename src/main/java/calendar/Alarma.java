package calendar;

import java.time.LocalDateTime;

public class Alarma {
    private LocalDateTime fechaHora;
    private AlarmaEfectos efecto;
    private final String nombre;
    private final String descripcion;

    public Alarma(String nombre, String descripcion, LocalDateTime fechaHora){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaHora = fechaHora;
    }

    /* ____ SETTERS ____ */

    public void establecerIntervalo(Integer min, Integer horas, Integer dias, Integer semanas, boolean esDiaCompleto){
        this.fechaHora = (esDiaCompleto ? this.fechaHora.plusMinutes(min).plusHours(horas).minusDays(dias).minusWeeks(semanas) : this.fechaHora.minusMinutes(min).minusHours(horas).minusDays(dias).minusWeeks(semanas));
    }

    public void establecerFechaHoraAbs(LocalDateTime fechaHoraAbs){
        this.fechaHora = fechaHoraAbs;
    }

    public void establecerEfecto(AlarmaEfectos efecto){
        this.efecto = efecto;
    }

    /* ____ GETTERS ____ */

    public LocalDateTime obtenerfechaHora(){ return this.fechaHora; }
    public AlarmaEfectos obtenerEfecto(){ return this.efecto; }
    public String obtenerNombre(){ return this.nombre; }
    public String obtenerDescripcion(){ return this.descripcion; }
}
