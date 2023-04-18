package calendar;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.PriorityQueue;

abstract class Recordatorio {

    protected String nombre = "Nuevo recordatorio";
    protected String descripcion = "Sin descripcion";
    protected LocalDateTime inicio;
    protected Integer horas;
    protected Integer minutos;
    protected ArrayList<Alarma> alarmas = new ArrayList<>();

    public Recordatorio(LocalDateTime inicio,  Integer horas, Integer minutos){
        this.inicio = inicio;
        this.horas = horas;
        this.minutos = minutos;
    }

    public void modificarNombre(String nuevoNombre){
        this.nombre = nuevoNombre;
    }

    public void modificarDescripcion(String nuevaDescripcion){
        this.nombre = nuevaDescripcion;
    }

    public LocalDateTime verFinal() {
        return this.inicio.plusHours(horas).plusMinutes(minutos);
    }
    public boolean verficarDiaCompleto(){
        return (inicio.getHour() == 0) && (horas == 24);
    }

    public boolean verificarRepeticion(){
        return false;
    }

    public void agregarAlarma(Integer min, Integer horas, Integer dias, Integer semanas, LocalDateTime fechaHoraAbs, AlarmaEfectos efecto){
        LocalDateTime fechaHora = (fechaHoraAbs != null ? fechaHoraAbs : agregarIntervalo(min, horas, dias, semanas));
        Alarma alarmaNueva = new Alarma(fechaHora, efecto);
        alarmas.add(alarmaNueva);
    }

    private LocalDateTime agregarIntervalo(Integer min, Integer horas, Integer dias, Integer semanas){
        return (verficarDiaCompleto() ? inicio.plusMinutes(min).plusHours(horas).minusDays(dias).minusWeeks(semanas) : inicio.minusMinutes(min).minusHours(horas).minusDays(dias).minusWeeks(semanas));
    }

    /*public void modificarAlarma(Integer idAlarma, Integer min, Integer horas, Integer dias, Integer semanas, Alarma.Efecto efectoNuevo){
        LocalDateTime fechaHoraNueva = inicio.minusMinutes(min).minusHours(horas).minusDays(dias).minusWeeks(semanas);
        alarmas.get(idAlarma).modificar(fechaHoraNueva, efectoNuevo);
    }*/

    public ArrayList<Alarma> obtenerAlarmas(){ return alarmas; }

    public Alarma obtenerAlarmaDeseada(int idAlarma){ return  alarmas.get(idAlarma); }

    public void eliminarAlarmaDeseada(int idAlarma) { alarmas.remove(idAlarma); }

    public void eliminarAlarmas(){ alarmas.clear(); }
}
