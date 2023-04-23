package calendar;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
