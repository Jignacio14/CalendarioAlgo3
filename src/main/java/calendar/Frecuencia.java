package calendar;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public enum Frecuencia{


    Diaria {
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
    Semanal {
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
    Mensual{
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
    Anual{
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


    public abstract void setDiasSemana(Set<DayOfWeek> dias);
    public abstract List<LocalDateTime> obtenerRepeticiones(Limite limite, LocalDateTime fecha, LocalDateTime inicio);
    public void setIntervalo(Integer intervalo){
        this.intervalo = intervalo;
    };
}