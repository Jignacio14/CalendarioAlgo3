package calendar;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class Alarma implements Repetible{
    private LocalDateTime fechaHora;
    private AlarmaEfectos efecto;
    private final String nombre;
    private final String descripcion;
    private Integer diferenciaHoraria;
    private Repetidor repetidor;
    private LocalDateTime ultRepeticion;

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

    /* ____ ALARMA CON REPETICIONES ____ */

    public boolean verificarRepeticion(){
        return repetidor != null ;
    }

    public boolean verificarHayProximaRepeticion(){
        return repetidor.verificarHayRepeticiones();
    }

    public void configurarRepeticion(Frecuencia frecuencia, Limite limite){
        this.repetidor = new Repetidor(limite, frecuencia);
    }

    public void configurarFechaLimite(LocalDateTime fechaLimite){
        this.repetidor.configurarFechaLimite(fechaLimite);
    }

    public void configurarIteracion(Integer iteraciones){
        this.repetidor.configurarIteraciones(iteraciones);
    }

    public void configurarIntervalo(Integer intervalo){
        this.repetidor.configurarIntervalo(intervalo);
    }

    public void configurarDias(Set<DayOfWeek> dias){
        this.repetidor.configurarDias(dias);
    }

    public List<LocalDateTime> verRepeticiones(LocalDateTime hasta){
        var consultaFechas = repetidor.verFuturasRepeticiones(ultRepeticion, hasta);
        ultRepeticion = consultaFechas.get(consultaFechas.size()-1);
        return consultaFechas;
    }
}
