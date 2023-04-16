package calendar;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;

abstract class Recordatorio {

    protected String nombre; // despues hacer un metodo donde si es "null" que muestre "Sin Titulo"
    protected String descripcion; // despues hacer un metodo donde si es "null" que muestre "Sin Descripcion"
    protected LocalDateTime inicio;
    protected LocalDateTime fin;
    //protected boolean noSeRepite;
    protected Periodicidad repeticion;
    protected ArrayList<LocalDateTime> repeticiones = new ArrayList<>();

    public Recordatorio(LocalDateTime inicio, LocalDateTime fin, String nombre, String descripcion){
        this.inicio = inicio;
        this.fin = fin;
        this.nombre = nombre;
        this.descripcion = descripcion;
        //this.noSeRepite = noSeRepite;
        //this.repeticion = verificarRepeticiones(noSeRepite);
    }

    public Recordatorio(LocalDateTime inicio, String nombre, String descripcion) {
    }

    public void modificarNombre(String nuevoNombre){
        this.nombre = nuevoNombre;
    }

    public void modificarDescripcion(String nuevaDescripcion){
        this.nombre = nuevaDescripcion;
    }

    //para que calcular la duracion??
    public long calcularDuracion(){
        var duracion = Duration.between(this.inicio, this.fin).toHours();

        return (duracion >= 0 ? duracion : 0);
    }

    public void agregarRepeticion(String tipoRepeticion, int cantidad, String condicionDeFin){
        this.repeticion = new Periodicidad (tipoRepeticion, cantidad, condicionDeFin);
        this.repeticiones = this.repeticion.calcularDiasRepetidos();
    }

    public void modificarRepeticion(boolean noSeRepite, String tipoRepeticion, int cantidad, String condicionDeFin){
        if(noSeRepite){
            this.repeticion = null;
        } else {
            this.repeticion.modificarDatos(tipoRepeticion, cantidad, condicionDeFin);
        }
    }

}
