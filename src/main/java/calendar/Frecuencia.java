package calendar;
import java.time.LocalDateTime;

public enum Frecuencia {
    Diario{
        @Override
        public LocalDateTime obtenerRepeticiones(LocalDateTime fecha, Integer iteracion){
            return fecha.plusDays(iteracion);
        }
    },

    Semanal{
        @Override
        public LocalDateTime obtenerRepeticiones(LocalDateTime fecha, Integer iteracion){
            return fecha.plusWeeks(iteracion);
        }
    },

    Mensual{
        @Override
        public LocalDateTime obtenerRepeticiones(LocalDateTime fecha, Integer iteracion){
            return fecha.plusMonths(iteracion);
        }
    },

    Anual{
        @Override
        public LocalDateTime obtenerRepeticiones(LocalDateTime fecha, Integer iteracion){
            return fecha.plusYears(iteracion);
        }
    };

    public abstract LocalDateTime obtenerRepeticiones(LocalDateTime fecha, Integer iteracion);
}
