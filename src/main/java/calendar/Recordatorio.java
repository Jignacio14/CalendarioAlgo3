package calendar;
import java.time.LocalDateTime;
import java.time.Duration;
abstract class Recordatorio {
    protected String nombre = "Nuevo Evento";
    protected String descripcion = "Sin descripcion disponible";
    protected LocalDateTime inicio;
    protected LocalDateTime fin;

    public Recordatorio(LocalDateTime inicio, String nombre, String descripcion, LocalDateTime fin){
        this.inicio = inicio;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fin = fin;
    }


    public Recordatorio(LocalDateTime inicio, String nombre, String descripcion) {
    }

    public void ModificarNombre(String nuevoNombre){
        this.nombre = nuevoNombre;
    }

    public void ModificarDescripcion(String nuevaDescripcion){
        this.nombre = nuevaDescripcion;
    }

    public long CalcularDuracion(){
        return Duration.between(inicio, fin).toHours();
    }

}
