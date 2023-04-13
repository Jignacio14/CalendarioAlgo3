package calendar;

import java.time.LocalDateTime;

public class Tarea extends Recordatorio{

    private boolean completada = false;
    public Tarea(LocalDateTime inicio, String nombre, String descripcion, LocalDateTime fin){
        //Construir para dia completo o hora absoluta
        super(inicio, nombre, descripcion, fin);
    }

    public void Completar(){
        this.completada = true;
    }

}
