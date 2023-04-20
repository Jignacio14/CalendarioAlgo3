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

    public Alarma crearAlarma(){
        return new Alarma(this.nombre, this.descripcion, this.inicio);
    }

    public void establecerDiaCompleto(){
        this.inicio = LocalDateTime.of(inicio.getYear(), inicio.getMonthValue(), inicio.getDayOfMonth(), 0, 0);
        this.horas = 24;
        this.minutos = 0;
        //lo que hace calendar: si era un evento que no es dia completo y tenia una alarma cuando se lo establece como dia completo se elimina la alarma puesta y se debe volver a crear una alarma
    }
}
