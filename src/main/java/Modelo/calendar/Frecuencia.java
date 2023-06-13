package Modelo.calendar;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;



public enum Frecuencia {
    Diaria {},
    Semanal{
        @Override
        public List<LocalDateTime> obtenerRepeticiones(Limite limite, LocalDateTime tope, LocalDateTime inicio) {
            var guia = inicio;
            var lista = new ArrayList<LocalDateTime>();
            do {
                if (diasSemana.contains(guia.getDayOfWeek())) {
                    lista.add(guia);
                    limite.ajustarIteracion();
                }
                guia = guia.plusDays(1);
            } while (limite.verificarProximasIteraciones(guia) & guia.isBefore(tope));
            return lista;
        }
    },
    Mensual{
        @Override
        public LocalDateTime incrementarFecha(LocalDateTime fecha) { return fecha.plusMonths(intervalo); }
    },
    Anual {
        @Override
        public LocalDateTime incrementarFecha(LocalDateTime fecha) { return fecha.plusYears(intervalo); }
    },
    ;
    protected Set<DayOfWeek> diasSemana;
    protected Integer intervalo;

    Frecuencia() {
        this.intervalo = intervaloDefecto;
        this.diasSemana = diaDefecto;
    }

    public void setIntervalo(Integer intervalo) { this.intervalo = intervalo; }

    public List<LocalDateTime> obtenerRepeticiones(Limite limite, LocalDateTime tope, LocalDateTime inicio) {
        var repeticiones = new ArrayList<LocalDateTime>();
        repeticiones.add(inicio);
        var guia = inicio;
        do {
            guia = incrementarFecha(guia);
            repeticiones.add(guia);
            limite.ajustarIteracion();
        } while (limite.verificarProximasIteraciones(guia) & guia.isBefore(tope));
        return repeticiones;
    }

    public LocalDateTime incrementarFecha(LocalDateTime fecha){
        return fecha.plusDays(intervalo);
    }
    public void setDiasSemana(Set<DayOfWeek> dias) { diasSemana = dias; }

    final Integer intervaloDefecto = 1;
    final Set<DayOfWeek> diaDefecto = Set.of(DayOfWeek.MONDAY);
}
