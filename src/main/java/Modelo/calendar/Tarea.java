package Modelo.calendar;

import Vista.Actividad;
import Vista.RecordatorioVisitor;

import java.time.LocalDateTime;

public class Tarea extends Recordatorio {
    private boolean completada = true;

    public Tarea(LocalDateTime inicio, Integer horas, Integer minutos) {
        super(inicio, horas, minutos);
        this.tipo = "Tarea";
    }

    public void aceptar(RecordatorioVisitor visitor) {
        visitor.visitarTarea(this);
    }

    @Override
    public void cambiarCompletada() { this.completada = !completada; }

    public boolean verificarEstarVencida(LocalDateTime fecha) { return super.inicio.isAfter(fecha); }

    @Override
    public boolean verificarCompletada() { return completada; }

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


