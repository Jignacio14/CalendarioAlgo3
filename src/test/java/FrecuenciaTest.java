import calendar.Frecuencia;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Set;

public class FrecuenciaTest {
    @Test
    public void TestFrecuenciaDias() {
        var frecuencia = Frecuencia.Diario;
        var fecha = LocalDateTime.now();
        var result = frecuencia.obtenerRepeticiones(fecha, 1);
        assertEquals(fecha.plusDays(1), result);
    }

    @Test
    public void TestFrecuenciaSemanal(){
        var s = Frecuencia.Semanal;
        Set<DayOfWeek> dias = Set.of(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY);
        s.setDiasSemana(dias);
        var fecha1 = LocalDateTime.now().minusDays(1);
        var fecha2 = LocalDateTime.of(2023, 4, 28, 23, 0);
        var lista = s.obtenerRepeticiones(fecha1, fecha2);

    }
}