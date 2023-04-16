package calendar;
import java.time.LocalDateTime;

abstract class Recordatorio {

    protected String nombre; // despues hacer un metodo donde si es "null" que muestre "Sin Titulo"
    protected String descripcion; // despues hacer un metodo donde si es "null" que muestre "Sin Descripcion"
    protected LocalDateTime inicio;
    protected Integer horas;
    protected Integer minutos;

    public Recordatorio(LocalDateTime inicio,  Integer horas, Integer minutos){
        this.inicio = inicio;
        this.horas = horas;
        this.minutos = minutos;
        this.nombre = "Nueva Tarea";
        this.descripcion = "Sin descripcion";
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
