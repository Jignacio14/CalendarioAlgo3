package calendar;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public enum Frecuencia{


    Diaria (1, Set.of(DayOfWeek.MONDAY)){
        @Override
        public void setDiasSemana(Set<DayOfWeek> dias) {}

        @Override
        public List<LocalDateTime> obtenerRepeticiones(Limite limite, LocalDateTime tope, LocalDateTime inicio) {
            var repeticiones = new ArrayList<LocalDateTime>();
            repeticiones.add(inicio);
            var guia = inicio;
            do {
                guia = guia.plusDays(this.intervalo);
                repeticiones.add(guia);
                limite.ajustarIteracion();
            } while (limite.verificarProximasIteraciones(guia) & guia.isBefore(tope));
            return repeticiones;
        }
    },
    Semanal (1, Set.of(DayOfWeek.MONDAY)){
        @Override
        public void setDiasSemana(Set<DayOfWeek> dias) {diasSemana = dias;}

        @Override
        public List<LocalDateTime> obtenerRepeticiones(Limite limite, LocalDateTime tope, LocalDateTime inicio) {
            var guia = inicio;
            var lista = new ArrayList<LocalDateTime>();
            do {
                if (diasSemana.contains(guia.getDayOfWeek())){
                    lista.add(guia);
                }
                guia = guia.plusDays(1);
            } while (limite.verificarProximasIteraciones(guia) & guia.isBefore(tope));
            return lista;
        }
    },
    Mensual(1, Set.of(DayOfWeek.MONDAY)){
        @Override
        public void setDiasSemana(Set<DayOfWeek> dias) {}

        @Override
        public List<LocalDateTime> obtenerRepeticiones(Limite limite, LocalDateTime tope, LocalDateTime inicio) {
            var repeticiones = new ArrayList<LocalDateTime>();
            repeticiones.add(inicio);
            var guia = inicio;
            do {
                guia = guia.plusMonths(this.intervalo);
                repeticiones.add(guia);
                limite.ajustarIteracion();
            } while (limite.verificarProximasIteraciones(guia) & guia.isBefore(tope));
            return repeticiones;
        }
    },
    Anual(1, Set.of(DayOfWeek.MONDAY)){
        @Override
        public void setDiasSemana(Set<DayOfWeek> dias) {}

        @Override
        public List<LocalDateTime> obtenerRepeticiones(Limite limite, LocalDateTime tope, LocalDateTime inicio) {
            var repeticiones = new ArrayList<LocalDateTime>();
            repeticiones.add(inicio);
            var actual = inicio;
            do {
                actual = actual.plusYears(this.intervalo);
                repeticiones.add(actual);
                limite.ajustarIteracion();
            } while (limite.verificarProximasIteraciones(actual) & actual.isBefore(tope));
            return repeticiones;
        }

    },
    ;
    protected Set<DayOfWeek> diasSemana;
    protected Integer intervalo;

    Frecuencia(Integer intervalo, Set<DayOfWeek> diasSemana){
        this.intervalo = intervalo;
        this.diasSemana = diasSemana;
    }
    public abstract void setDiasSemana(Set<DayOfWeek> dias);
    public abstract List<LocalDateTime> obtenerRepeticiones(Limite limite, LocalDateTime fecha, LocalDateTime inicio);
    public void setIntervalo(Integer intervalo){
        this.intervalo = intervalo;
    };
}