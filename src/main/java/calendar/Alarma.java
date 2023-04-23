package calendar;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Alarma implements Repetible{
    private LocalDateTime fechaHora;
    private AlarmaEfectos efecto;
    private String nombre;
    private String descripcion;
    private final LocalDateTime fechaHoraRecordatorio;
    private Integer diferenciaHoraria;
    private Repetidor repetidor;
    private LocalDateTime ultRepeticion;
    private Integer id;

    public Alarma(String nombre, String descripcion, LocalDateTime fechaHora){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaHoraRecordatorio = fechaHora;
        this.fechaHora = fechaHora;
        this.ultRepeticion = fechaHora;
        this.id = null;
    }

    public void modificarNombre(String nuevoNombre){
        this.nombre = nuevoNombre;
    }

    public void modificarDescripcion(String nuevaDescripcion){
        this.descripcion = nuevaDescripcion;
    }

    /*
    - VER COMO HACER CON LA ALARMA CUANDO SE CAMBIA LA FECHA Y HORA DE INICIO DEL EVENTO/TAREA A QUIEN PERTENECE
    - POR AHORA HICE QUE CUANDO SE CAMBIA LA FECHA Y HORA DE UN EVENTO/TAREA LAS ALARMAS QUE TENIAN SE ELIMINAN TODAS Y HAY QUE VOLVER A CREARLAS
    public void modificarInicio(LocalDateTime nuevoInicio){
        this.fechaHoraRecordatorio = nuevoInicio;
        this.fechaHora = nuevoInicio;
        this.ultRepeticion = nuevoInicio;
    }*/

    /* ____ SETTERS ____ */

    public void establecerIntervalo(Integer min, Integer horas, Integer dias, Integer semanas, boolean esDiaCompleto){
        this.fechaHora = (esDiaCompleto ? this.fechaHora.plusMinutes(min).plusHours(horas).minusDays(dias).minusWeeks(semanas) : this.fechaHora.minusMinutes(min).minusHours(horas).minusDays(dias).minusWeeks(semanas));
        this.ultRepeticion = fechaHora;
    }

    public void establecerFechaHoraAbs(LocalDateTime fechaHoraAbs){
        this.fechaHora = fechaHoraAbs;
    }

    public void establecerFechaHoraAbsRepeticiones(LocalDateTime fechaHoraAbs){
        this.establecerFechaHoraAbs(fechaHoraAbs);
        this.diferenciaHoraria = (fechaHoraRecordatorio.getDayOfMonth() - fechaHoraAbs.getDayOfMonth());
        this.ultRepeticion = LocalDateTime.of(fechaHoraRecordatorio.getYear(), fechaHoraRecordatorio.getMonthValue(), fechaHoraRecordatorio.getDayOfMonth(), fechaHoraAbs.getHour(), fechaHoraAbs.getMinute());
    }

    public void establecerEfecto(AlarmaEfectos efecto){
        this.efecto = efecto;
    }

    public void establecerId(int id){ this.id = id; }

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
        return (diferenciaHoraria == null ? consultaFechas : descontarDiferenciaHoraria(consultaFechas));
    }

    private List<LocalDateTime> descontarDiferenciaHoraria(List<LocalDateTime> consultaFechas){
        return consultaFechas.stream().map(fecha -> fecha.minusDays(diferenciaHoraria)).collect(Collectors.toList());
    }
}
