package calendar;
import java.time.LocalDateTime;

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

}
