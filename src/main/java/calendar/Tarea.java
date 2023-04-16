package calendar;

import java.time.LocalDateTime;

public class Tarea extends Recordatorio{
    private boolean completada;

    public Tarea(LocalDateTime inicio, Integer horas, Integer minutos, String nombre, String descripcion) {
        super(inicio, horas, minutos, nombre, descripcion);
        this.completada = false;
    }

    public void CambiarCompletada(){
        this.completada = !completada;
    }



}


