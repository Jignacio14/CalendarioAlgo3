import calendar.Evento;
import calendar.Frecuencia;
import calendar.Limite;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

import static org.junit.Assert.*;

public class EventoTest {

    @Test
    public void TestEventoCrear() {
        var fecha = LocalDateTime.now();
        var evento = new Evento(fecha, 1, 1);
        assertNotNull(evento);
    }

    @Test
    public void TestEventoNombrePorDefecto() {
        var fecha = LocalDateTime.now();
        var evento = new Evento(fecha, 1, 1);
        assertEquals("Sin titulo", evento.obtenerNombre());
    }

    @Test
    public void TestEventoDetallePorDefecto() {
        var fecha = LocalDateTime.now();
        var evento = new Evento(fecha, 1, 1);
        assertEquals(evento.obtenerDescripcion(), "Sin descripcion");
    }

    @Test
    public void TestEventoSeRepiteEsFalso() {
        var fecha = LocalDateTime.now();
        var evento = new Evento(fecha, 1, 1);
        assertFalse(evento.verificarRepeticion());
    }

    @Test
    public void TestEventoCambiarNombre() {
        var fecha = LocalDateTime.now();
        var evento = new Evento(fecha, 1, 0);
        var nombre = "Entrega del TP";
        evento.modificarNombre("Entrega del TP");
        assertEquals(nombre, evento.obtenerNombre());
    }


    @Test
    public void TestEventoCambiarDescripcion() {
        var fecha = LocalDateTime.now();
        var evento = new Evento(fecha, 1, 0);
        var descripcion = "Todo lo relacionado al TP";
        evento.modificarDescripcion(descripcion);
        assertEquals(descripcion, evento.obtenerDescripcion());
    }

    @Test
    public void TestEventoDiaCompleto() {
        var fecha = LocalDateTime.now();
        var evento = new Evento(fecha, 1, 0);
        assertFalse(evento.verficarDiaCompleto());
        evento.establecerDiaCompleto();
        assertTrue(evento.verficarDiaCompleto());
    }

    @Test
    public void TestEventoRepeticionDiaria() {
        var fecha = LocalDateTime.now();
        var evento = new Evento(fecha, 1, 0);
        evento.configurarRepeticion(Frecuencia.Diaria, Limite.Iteraciones);
        evento.configurarIntervalo(4);
        evento.configurarIteracion(4);

        var resultadoEsperado = new ArrayList<LocalDateTime>();
        resultadoEsperado.add(fecha);
        resultadoEsperado.add(fecha.plusDays(4));
        resultadoEsperado.add(fecha.plusDays(8));
        resultadoEsperado.add(fecha.plusDays(12));

        assertTrue(evento.verificarRepeticion());

        var resultado = evento.verRepeticiones(fecha.plusYears(1));

        for (int i = 0; i < resultadoEsperado.size(); i++) {
            assertEquals(resultadoEsperado.get(i), resultado.get(i));
        }

        assertFalse(evento.verificarHayProximaRepeticion());
    }

    @Test
    public void TestEventoRepeticionSemanal() {
        var fecha = LocalDateTime.of(2023, 4, 18, 0, 0);
        var evento = new Evento(fecha, 1, 0);

        assertFalse(evento.verificarRepeticion());

        evento.configurarRepeticion(Frecuencia.Semanal, Limite.FechaMax);
        evento.configurarDias(Set.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY));
        evento.configurarFechaLimite(fecha.plusWeeks(2));

        assertTrue(evento.verificarRepeticion());

        var resultadoEsperado = new ArrayList<LocalDateTime>();
        resultadoEsperado.add(fecha);
        resultadoEsperado.add(fecha.plusDays(2));
        resultadoEsperado.add(fecha.plusDays(7));
        resultadoEsperado.add(fecha.plusDays(9));

        assertTrue(evento.verificarRepeticion());

        var resultado = evento.verRepeticiones(fecha.plusYears(1));

        for (int i = 0; i < resultadoEsperado.size(); i++) {
            assertEquals(resultadoEsperado.get(i), resultado.get(i));
        }
        assertFalse(evento.verificarHayProximaRepeticion());
    }

    @Test
    public void TestEventoRepeticionMensual() {
        var fecha = LocalDateTime.of(2023, 4, 18, 0, 0);
        var evento = new Evento(fecha, 1, 0);
        evento.configurarRepeticion(Frecuencia.Mensual, Limite.SinLimite);
        evento.configurarIntervalo(3);

        assertTrue(evento.verificarRepeticion());

        var resultadoEsperado = new ArrayList<LocalDateTime>();
        resultadoEsperado.add(fecha);
        resultadoEsperado.add(fecha.plusMonths(3));
        resultadoEsperado.add(fecha.plusMonths(6));
        resultadoEsperado.add(fecha.plusMonths(9));
        resultadoEsperado.add(fecha.plusMonths(12));

        var resultado = evento.verRepeticiones(fecha.plusYears(1));

        for (int i = 0; i < resultado.size(); i++) {
            assertEquals(resultadoEsperado.get(i), resultado.get(i));
        }
        assertTrue(evento.verificarHayProximaRepeticion());


    }

    @Test
    public void TestEventoRepeticionAnual() {
        var fecha = LocalDateTime.of(2023, 4, 18, 0, 0);
        var evento = new Evento(fecha, 1, 0);
        evento.configurarRepeticion(Frecuencia.Anual, Limite.SinLimite);

        var resultadoEsperado = new ArrayList<LocalDateTime>();
        resultadoEsperado.add(fecha);
        resultadoEsperado.add(fecha.plusYears(1));
        resultadoEsperado.add(fecha.plusYears(2));
        resultadoEsperado.add(fecha.plusYears(3));
        resultadoEsperado.add(fecha.plusYears(4));

        var resultado = evento.verRepeticiones(fecha.plusYears(4));

        for (int i = 0; i < resultado.size(); i++) {
            assertEquals(resultadoEsperado.get(i), resultado.get(i));
        }
        assertTrue(evento.verificarHayProximaRepeticion());

        var nuevoResultadoEsperado = new ArrayList<LocalDateTime>();

        nuevoResultadoEsperado.add(fecha.plusYears(4));
        nuevoResultadoEsperado.add(fecha.plusYears(5));
        nuevoResultadoEsperado.add(fecha.plusYears(6));
        nuevoResultadoEsperado.add(fecha.plusYears(7));

        resultado = evento.verRepeticiones(fecha.plusYears(7));

        for (int i = 0; i < resultado.size(); i++) {
            assertEquals(nuevoResultadoEsperado.get(i), resultado.get(i));
        }

        assertTrue(evento.verificarHayProximaRepeticion());
    }


}