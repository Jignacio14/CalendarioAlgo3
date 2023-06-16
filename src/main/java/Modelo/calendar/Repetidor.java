package Modelo.calendar;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class Repetidor {

    private final Limite limite;
    private final Frecuencia frecuencia;
    private LocalDateTime ultConsulta = LocalDateTime.now();

    public Repetidor(Limite limite, Frecuencia frecuencia) {
        this.limite = limite;
        this.frecuencia = frecuencia;
    }

    public void configurarFechaLimite(LocalDateTime fechaLimite) { this.limite.setFechaLimite(fechaLimite); }

    public void configurarIteraciones(Integer iteraciones) { this.limite.setIteraciones(iteraciones); }

    public void configurarIntervalo(Integer intervalo) { this.frecuencia.setIntervalo(intervalo); }

    public void configurarDias(Set<DayOfWeek> dias) { this.frecuencia.setDiasSemana(dias); }

    public boolean verificarHayRepeticiones() { return limite.verificarProximasIteraciones(this.ultConsulta); }

    public void establecerUltConsulta(LocalDateTime ultConsulta) { this.ultConsulta = ultConsulta; }

    public List<LocalDateTime> verFuturasRepeticiones(LocalDateTime desde, LocalDateTime hasta) {
        return frecuencia.obtenerRepeticiones(limite, hasta, desde);
    }

    public Limite obtenerLimite() { return this.limite; }

    public Frecuencia obtenerFrecuencia() { return this.frecuencia; }

    public LocalDateTime obtenerUltConsulta() { return this.ultConsulta; }

    @Override
    public String toString() {
        return limite.obtenerIteraciones().toString() + " " + limite.obtenerFechaLimite().toString() + " " + ultConsulta.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass()) {
            return false;
        }

        Repetidor repetidorAComparar = (Repetidor) obj;
        return  this.limite.equals(repetidorAComparar.limite) &&
                this.frecuencia.equals(repetidorAComparar.frecuencia) &&
                this.ultConsulta.equals(repetidorAComparar.ultConsulta);
    }

}