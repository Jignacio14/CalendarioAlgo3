import calendar.Frecuencia;
import calendar.Limite;
import org.junit.Test;

import static org.junit.Assert.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

public class FrecuenciaTest {
    @Test
    public void TestFrecuenciaDiariaIteracion() {
        var x = Frecuencia.Diaria;
        x.setIntervalo(3);
        var fecha = LocalDateTime.of(2023, 1, 1, 0, 0);

        var y = Limite.Iteraciones;
        y.setFechaLimite(fecha.plusYears(1));
        y.setIteraciones(3);

        var limite = fecha.plusYears(1);
        var resultadoEsperado = new ArrayList<LocalDateTime>();
        resultadoEsperado.add(fecha);
        resultadoEsperado.add(fecha.plusDays(3));
        resultadoEsperado.add(fecha.plusDays(6));
        resultadoEsperado.add(fecha.plusDays(9));
        var z = x.obtenerRepeticiones(y, limite, fecha);

        for (int i = 0; i < resultadoEsperado.size(); i++) {
            assertEquals(resultadoEsperado.get(i), z.get(i));
        }
    }

    @Test
    public void TestFrecuenciaSemanalIteracion() {
        var x = Frecuencia.Semanal;
        var diasSemana = Set.of(DayOfWeek.THURSDAY, DayOfWeek.TUESDAY);
        x.setDiasSemana(diasSemana);

        var fecha = LocalDateTime.of(2023, 1, 3, 0, 0);

        var y = Limite.Iteraciones;
        y.setIteraciones(4);

        var limite = fecha.plusYears(1);
        var resultadoEsperado = new ArrayList<LocalDateTime>();

        resultadoEsperado.add(fecha);
        resultadoEsperado.add(fecha.plusDays(2));
        resultadoEsperado.add(fecha.plusDays(7));
        resultadoEsperado.add(fecha.plusDays(9));
        var z = x.obtenerRepeticiones(y, limite, fecha);

        for (int i = 0; i < resultadoEsperado.size(); i++) {
            assertEquals(resultadoEsperado.get(i), z.get(i));
        }

    }

    @Test
    public void TestFrecuenciaMensualIteracion() {
        var x = Frecuencia.Mensual;
        x.setIntervalo(2);
        var fecha = LocalDateTime.of(2023, 1, 1, 0, 0);

        var y = Limite.Iteraciones;
        y.setIteraciones(3);

        var limite = fecha.plusYears(1);
        var resultadoEsperado = new ArrayList<LocalDateTime>();
        resultadoEsperado.add(fecha);
        resultadoEsperado.add(fecha.plusMonths(2));
        resultadoEsperado.add(fecha.plusMonths(4));
        resultadoEsperado.add(fecha.plusMonths(6));
        var z = x.obtenerRepeticiones(y, limite, fecha);

        for (int i = 0; i < resultadoEsperado.size(); i++) {
            assertEquals(resultadoEsperado.get(i), z.get(i));
        }

    }

    @Test
    public void TestFrecuenciaAnualIteracion() {
        var x = Frecuencia.Anual;
        x.setIntervalo(1);
        var fecha = LocalDateTime.of(2023, 1, 1, 0, 0);

        var y = Limite.Iteraciones;
        y.setIteraciones(4);

        var limite = fecha.plusYears(6);
        var resultadoEsperado = new ArrayList<LocalDateTime>();
        resultadoEsperado.add(fecha);
        resultadoEsperado.add(fecha.plusYears(1));
        resultadoEsperado.add(fecha.plusYears(2));
        resultadoEsperado.add(fecha.plusYears(3));
        var z = x.obtenerRepeticiones(y, limite, fecha);

        for (int i = 0; i < resultadoEsperado.size(); i++) {
            assertEquals(resultadoEsperado.get(i), z.get(i));
        }
    }

    @Test
    public void TestFrecuenciaDiariaHasta() {
        var inicio = LocalDateTime.of(2023, 1, 1, 0, 0);
        var x = Frecuencia.Diaria;
        x.setIntervalo(2);

        var y = Limite.FechaMax;
        y.setFechaLimite(inicio.plusDays(10));

        var resultadoEsperado = new ArrayList<LocalDateTime>();
        resultadoEsperado.add(inicio);
        resultadoEsperado.add(inicio.plusDays(2));
        resultadoEsperado.add(inicio.plusDays(4));
        resultadoEsperado.add(inicio.plusDays(6));
        resultadoEsperado.add(inicio.plusDays(8));

        var resultado = x.obtenerRepeticiones(y, inicio.plusWeeks(2), inicio);

        for (int i = 0; i < resultadoEsperado.size(); i++) {
            assertEquals(resultadoEsperado.get(i), resultado.get(i));
        }
    }

    @Test
    public void TestFrecuenciaSemanalHasta() {
        var inicio = LocalDateTime.of(2023, 1, 2, 0, 0);
        var frecuencia = Frecuencia.Semanal;
        frecuencia.setDiasSemana(Set.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY));

        var limite = Limite.FechaMax;
        limite.setFechaLimite(inicio.plusWeeks(2));

        var fechaMax = inicio.plusYears(2);

        var resultadoEsperado = new ArrayList<LocalDateTime>();
        resultadoEsperado.add(inicio);
        resultadoEsperado.add(inicio.plusDays(4));
        resultadoEsperado.add(inicio.plusDays(7));
        resultadoEsperado.add(inicio.plusDays(11));

        var resultado = frecuencia.obtenerRepeticiones(limite, fechaMax, inicio);

        for (int i = 0; i < resultadoEsperado.size(); i++) {
            assertEquals(resultadoEsperado.get(i), resultado.get(i));
        }
    }

    @Test
    public void TestFrecuenciaMensualHasta() {
        var inicio = LocalDateTime.of(2023, 1, 2, 0, 0);
        var frecuencia = Frecuencia.Mensual;
        frecuencia.setIntervalo(3);

        var limite = Limite.FechaMax;
        limite.setFechaLimite(inicio.plusYears(1));

        var fechaMax = inicio.plusYears(2);

        var resultadoEsperado = new ArrayList<LocalDateTime>();
        resultadoEsperado.add(inicio);
        resultadoEsperado.add(inicio.plusMonths(3));
        resultadoEsperado.add(inicio.plusMonths(6));
        resultadoEsperado.add(inicio.plusMonths(9));

        var resultado = frecuencia.obtenerRepeticiones(limite, fechaMax, inicio);

        for (int i = 0; i < resultadoEsperado.size(); i++) {
            assertEquals(resultadoEsperado.get(i), resultado.get(i));
        }

    }

    @Test
    public void TestFrecuenciaAnualHasta() {
        var inicio = LocalDateTime.of(2023, 1, 2, 0, 0);
        var frecuencia = Frecuencia.Anual;
        frecuencia.setIntervalo(1);

        var limite = Limite.FechaMax;
        limite.setFechaLimite(inicio.plusYears(5));

        var fechaMax = inicio.plusYears(3);

        var resultadoEsperado = new ArrayList<LocalDateTime>();
        resultadoEsperado.add(inicio);
        resultadoEsperado.add(inicio.plusYears(1));
        resultadoEsperado.add(inicio.plusYears(2));


        var resultado = frecuencia.obtenerRepeticiones(limite, fechaMax, inicio);

        for (int i = 0; i < resultadoEsperado.size(); i++) {
            assertEquals(resultadoEsperado.get(i), resultado.get(i));
        }
    }

    @Test
    public void TestFrecuenciaDiariaSinLimite() {
        var inicio = LocalDateTime.of(2023, 1, 1, 0, 0);
        var x = Frecuencia.Diaria;
        x.setIntervalo(2);

        var limite = Limite.SinLimite;

        var resultadoEsperado = new ArrayList<LocalDateTime>();
        resultadoEsperado.add(inicio);
        resultadoEsperado.add(inicio.plusDays(2));
        resultadoEsperado.add(inicio.plusDays(4));
        resultadoEsperado.add(inicio.plusDays(6));
        resultadoEsperado.add(inicio.plusDays(8));

        var resultado = x.obtenerRepeticiones(limite, inicio.plusWeeks(2), inicio);

        for (int i = 0; i < resultadoEsperado.size(); i++) {
            assertEquals(resultadoEsperado.get(i), resultado.get(i));
        }
    }

    @Test
    public void TestFrecuenciaSemanalSinLimite() {
        var x = Frecuencia.Semanal;
        var diasSemana = Set.of(DayOfWeek.THURSDAY, DayOfWeek.TUESDAY);
        x.setDiasSemana(diasSemana);

        var fecha = LocalDateTime.of(2023, 1, 3, 0, 0);

        var y = Limite.Iteraciones;
        y.setIteraciones(4);

        var limite = fecha.plusWeeks(2);
        var resultadoEsperado = new ArrayList<LocalDateTime>();

        resultadoEsperado.add(fecha);
        resultadoEsperado.add(fecha.plusDays(2));
        resultadoEsperado.add(fecha.plusDays(7));
        resultadoEsperado.add(fecha.plusDays(9));
        var z = x.obtenerRepeticiones(y, limite, fecha);

        for (int i = 0; i < z.size(); i++) {
            assertEquals(resultadoEsperado.get(i), z.get(i));
        }

    }
}