package calendar;

import java.time.LocalDateTime;

public class Tarea extends Recordatorio{
    private boolean completada;

    public Tarea(LocalDateTime inicio, Integer horas, Integer minutos) {
        super(inicio, horas, minutos);
    }


    public void CambiarCompletada(){
        this.completada = !completada;
    }



}


