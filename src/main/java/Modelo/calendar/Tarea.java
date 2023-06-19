package Modelo.calendar;


import java.time.LocalDateTime;

public class Tarea extends Recordatorio {
    private boolean completada = false;

    public Tarea(LocalDateTime inicio, Integer horas, Integer minutos) {
        super(inicio, horas, minutos);
        this.tipo = "Tarea";
    }

    @Override
    public void cambiarCompletada() { this.completada = !completada; }

    public boolean verificarEstarVencida(LocalDateTime fecha) { return super.inicio.isAfter(fecha); }

    @Override
    public boolean verificarCompletada() { return completada; }

    @Override
    public boolean equals(Object obj) {

        if (obj == null){
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        Tarea tareaAComparar = (Tarea) obj;
        return super.equals(obj) &&
                this.completada == tareaAComparar.completada;
    }
}


