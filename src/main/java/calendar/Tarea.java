package calendar;

import java.time.LocalDateTime;

public class Tarea extends Recordatorio {
    private boolean completada = false;

    public Tarea(LocalDateTime inicio, Integer horas, Integer minutos) {
        super(inicio, horas, minutos);
    }

    public void cambiarCompletada() { this.completada = !completada; }

    public boolean verificarEstarVencida(LocalDateTime fecha) { return super.inicio.isAfter(fecha); }

    public boolean verCompletada() { return completada; }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass()) {
            return false;
        }

        Tarea tareaAComparar = (Tarea) obj;
        return super.equals(obj) &&
                this.completada == tareaAComparar.completada;
    }
}


