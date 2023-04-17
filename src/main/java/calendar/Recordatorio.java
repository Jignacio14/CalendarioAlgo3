package calendar;
import java.time.LocalDateTime;

abstract class Recordatorio {

    protected String nombre;
    protected String descripcion = "Sin descripcion";
    protected LocalDateTime inicio;
    protected Integer horas;
    protected Integer minutos;

    public Recordatorio(LocalDateTime inicio,  Integer horas, Integer minutos){
        this.inicio = inicio;
        this.horas = horas;
        this.minutos = minutos;
    }

    public void cambiarNombre(String nuevoNombre){
        this.nombre = nuevoNombre;
    }

    public void cambiarDescripcion(String nuevaDescripcion){
        this.descripcion = nuevaDescripcion;
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

    public String obtenerNombre(){
        return nombre;
    }
    public String obtenerDescripcion(){
        return descripcion;
    }

    public void cambiarInicio(LocalDateTime nuevoInicio){
        this.inicio = nuevoInicio;
    }
}
