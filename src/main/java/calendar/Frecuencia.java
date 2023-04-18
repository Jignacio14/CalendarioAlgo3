package calendar;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public enum Frecuencia {
    Diario {
        @Override
        public LocalDateTime obtenerRepeticiones(LocalDateTime fecha, Integer iteracion){
            return fecha.plusDays(iteracion);
        }

        @Override
        public void setDiasSemana(Set<DayOfWeek> dias) {

        }

        @Override
        public ArrayList<LocalDateTime> obtenerRepeticiones(LocalDateTime inicio, LocalDateTime fin) {
            return null;
        }

    },

    Semanal {
        @Override
        public LocalDateTime obtenerRepeticiones(LocalDateTime fecha, Integer iteracion) {
            return null;
        }

        @Override
        public void setDiasSemana(Set<DayOfWeek> dias) {
            this.dias = dias;
        }

        @Override
        public ArrayList<LocalDateTime> obtenerRepeticiones(LocalDateTime inicio, LocalDateTime fin) {
            var guia = inicio;
            var lista = new ArrayList<LocalDateTime>();
            do {
                if (dias.contains(guia.getDayOfWeek())){
                    lista.add(guia);
                }
                guia = guia.plusDays(1);
            } while (guia.isBefore(fin));
            return lista;
        }

    },

    Mensual{
        @Override
        public LocalDateTime obtenerRepeticiones(LocalDateTime fecha, Integer iteracion){
            return fecha.plusMonths(iteracion);
        }

        @Override
        public void setDiasSemana(Set<DayOfWeek> dias) {}

        @Override
        public ArrayList<LocalDateTime> obtenerRepeticiones(LocalDateTime inicio, LocalDateTime fin) {
            return null;
        }
    },

    Anual{
        @Override
        public LocalDateTime obtenerRepeticiones(LocalDateTime fecha, Integer iteracion){
            return fecha.plusYears(iteracion);
        }
        @Override
        public void setDiasSemana(Set<DayOfWeek> dias) {}

        @Override
        public ArrayList<LocalDateTime> obtenerRepeticiones(LocalDateTime inicio, LocalDateTime fin) {
            return null;
        }

    };

    protected Set<DayOfWeek> dias;
    public abstract LocalDateTime obtenerRepeticiones(LocalDateTime fecha, Integer iteracion);
    public abstract void setDiasSemana(Set<DayOfWeek> dias);
    public abstract ArrayList<LocalDateTime> obtenerRepeticiones(LocalDateTime inicio, LocalDateTime fin);
}
