import Modelo.calendar.*;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

import static org.junit.Assert.*;

public class AlarmaTest {

    /* ___________________ TESTS GENERALES ___________________ */

    /*@Test
    public void TestAlarmaProximaEsLaCorrecta() {
        var fechaHora = LocalDateTime.of(2023, 4, 20, 0, 0);
        var evento = new Evento(fechaHora, 24 ,0);
        var tarea = new Tarea(fechaHora, 24 ,0);

        LocalDateTime fechaHoraAct = LocalDateTime.now();
        *//* 1) se le manda la Fecha y Hora actual
        2) se busca entre las tareas y eventos del calendarios y nos quedamos con
        los que coincide su FechaHora con la fecha y hora actual
        3) nos fijamos si tienen alarma y si tienen ver si una de sus alarmas tiene una
        programada con la fecha y hora actual
        4) se dispara las alarmas que coincidan su fechaHora con la fecha y hora actual *//*

    }*/

    /* ___________________ TESTS ALARMA CON FECHA Y HORA ABSOLUTA ___________________ */

    @Test
    public void TestModificarAlarmaConFechaHoraAbs() {
        var fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        Alarma alarma = new Alarma("titulo prueba", "descripcion prueba", fecha, "prueba");
        var fechaHoraAlarmaEsperada = LocalDateTime.of(2023, 4, 15, 10, 0);

        alarma.establecerFechaHoraAbs(fechaHoraAlarmaEsperada);
        alarma.establecerEfecto(AlarmaEfectos.SONIDO);

        assertEquals(fechaHoraAlarmaEsperada, alarma.obtenerfechaHora());
    }


    /* ___________________ TESTS ALARMA CON INTERVALO ___________________ */

    @Test
    public void TestAgregarAlarmaConIntervaloMinutos() {
        var fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        Alarma alarma = new Alarma("titulo prueba", "descripcion prueba", fecha, "prueba");

        alarma.establecerIntervalo(30, 0, 0, 0, false);
        alarma.establecerEfecto(AlarmaEfectos.SONIDO);

        var fechaHoraAlarmaEsperada = LocalDateTime.of(2023, 4, 20, 10, 0);

        assertEquals(fechaHoraAlarmaEsperada, alarma.obtenerfechaHora());
    }

    @Test
    public void TestAgregarAlarmaConIntervaloRecordatorioDiaCompleto() {
        var fecha = LocalDateTime.of(2023, 4, 20, 0, 0);
        Alarma alarma = new Alarma("titulo prueba", "descripcion prueba", fecha, "prueba");

        alarma.establecerIntervalo(30, 9, 3, 0, true);
        alarma.establecerEfecto(AlarmaEfectos.SONIDO);

        var fechaHoraAlarmaEsperada = LocalDateTime.of(2023, 4, 17, 9, 30);

        assertEquals(fechaHoraAlarmaEsperada, alarma.obtenerfechaHora());
    }

    /* ___________________ TESTS DE EFECTOS ___________________ */

    @Test
    public void TestEfectoAlarmaSonido() {
        var fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        Alarma alarmaNueva = new Alarma("titulo prueba", "descripcion prueba", fecha, "prueba");

        alarmaNueva.establecerIntervalo(30, 0, 0, 0, false);
        alarmaNueva.establecerEfecto(AlarmaEfectos.SONIDO);

        assertEquals("Sonido", alarmaNueva.obtenerEfecto().lanzarEfecto());
    }

    @Test
    public void TestEfectoAlarmaEmail() {
        var fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        Alarma alarmaNueva = new Alarma("titulo prueba", "descripcion prueba", fecha, "prueba");

        alarmaNueva.establecerIntervalo(30, 0, 0, 0, false);
        alarmaNueva.establecerEfecto(AlarmaEfectos.EMAIL);

        assertEquals("Email", alarmaNueva.obtenerEfecto().lanzarEfecto());
    }

    @Test
    public void TestEfectoAlarmaNotificacion() {
        var fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        Alarma alarmaNueva = new Alarma("titulo prueba", "descripcion prueba", fecha, "prueba");

        alarmaNueva.establecerIntervalo(30, 0, 0, 0, false);
        alarmaNueva.establecerEfecto(AlarmaEfectos.NOTIFICACION);

        assertEquals("Notificacion", alarmaNueva.obtenerEfecto().lanzarEfecto());
    }

    /* ___________________ TESTS DE DATOS DE ALARMA ___________________ */

    @Test
    public void TestTituloAlarma() {
        var fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        Alarma alarmaNueva = new Alarma("titulo prueba", "descripcion prueba", fecha, "prueba");

        alarmaNueva.establecerIntervalo(30, 0, 0, 0, false);
        alarmaNueva.establecerEfecto(AlarmaEfectos.EMAIL);

        assertEquals("titulo prueba", alarmaNueva.obtenerNombre());
    }

    @Test
    public void TestDescripcionAlarma() {
        var fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        Alarma alarmaNueva = new Alarma("titulo prueba", "descripcion prueba", fecha, "prueba");

        alarmaNueva.establecerIntervalo(30, 0, 0, 0, false);
        alarmaNueva.establecerEfecto(AlarmaEfectos.EMAIL);

        assertEquals("descripcion prueba", alarmaNueva.obtenerDescripcion());
    }

    /* ___________________ TESTS DE ALARMA CON INTERVALO CON REPETICIONES ___________________ */

    @Test
    public void TestAlarmaRepeticionDiaria(){
        var fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        Alarma alarma = new Alarma("titulo prueba", "descripcion prueba", fecha, "prueba");
        alarma.establecerIntervalo(30, 0, 0, 0, false);
        alarma.establecerEfecto(AlarmaEfectos.SONIDO);

        alarma.configurarRepeticion(Frecuencia.Diaria, Limite.Iteraciones);
        alarma.configurarIntervalo(4);
        alarma.configurarIteracion(4);

        var fechaConIntervalo = alarma.obtenerfechaHora();

        var resultadoEsperado = new ArrayList<LocalDateTime>();
        resultadoEsperado.add(fechaConIntervalo);
        resultadoEsperado.add(fechaConIntervalo.plusDays(4));
        resultadoEsperado.add(fechaConIntervalo.plusDays(8));
        resultadoEsperado.add(fechaConIntervalo.plusDays(12));

        assertTrue(alarma.verificarRepeticion());

        var resultado = alarma.verRepeticiones(fechaConIntervalo.plusYears(1));

        for (int i = 0; i < resultadoEsperado.size(); i++){
            assertEquals(resultadoEsperado.get(i), resultado.get(i));
        }

        assertFalse(alarma.verificarHayProximaRepeticion());
    }

    @Test
    public void TestAlarmaRepeticionSemanal(){
        var fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        Alarma alarma = new Alarma("titulo prueba", "descripcion prueba", fecha, "prueba");
        alarma.establecerIntervalo(30, 0, 0, 0, false);
        alarma.establecerEfecto(AlarmaEfectos.SONIDO);
        var fechaConIntervalo = alarma.obtenerfechaHora();

        assertFalse(alarma.verificarRepeticion());

        alarma.configurarRepeticion(Frecuencia.Semanal, Limite.FechaMax);
        alarma.configurarDias(Set.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY));
        alarma.configurarFechaLimite(fechaConIntervalo.plusWeeks(2));

        assertTrue(alarma.verificarRepeticion());

        var resultadoEsperado = new ArrayList<LocalDateTime>();
        resultadoEsperado.add(fechaConIntervalo);
        resultadoEsperado.add(fechaConIntervalo.plusDays(5));
        resultadoEsperado.add(fechaConIntervalo.plusDays(7));
        resultadoEsperado.add(fechaConIntervalo.plusDays(12));

        assertTrue(alarma.verificarRepeticion());

        var resultado = alarma.verRepeticiones(fechaConIntervalo.plusYears(1));

        for (int i = 0; i < resultadoEsperado.size(); i++){assertEquals(resultadoEsperado.get(i), resultado.get(i));}
        assertFalse(alarma.verificarHayProximaRepeticion());
    }

    @Test
    public void TestAlarmaRepeticionMensual(){
        var fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        Alarma alarma = new Alarma("titulo prueba", "descripcion prueba", fecha, "prueba");
        alarma.establecerIntervalo(30, 0, 0, 0, false);
        alarma.establecerEfecto(AlarmaEfectos.SONIDO);

        alarma.configurarRepeticion(Frecuencia.Mensual, Limite.SinLimite);
        alarma.configurarIntervalo(3);

        assertTrue(alarma.verificarRepeticion());

        var fechaConIntervalo = alarma.obtenerfechaHora();

        var resultadoEsperado = new ArrayList<LocalDateTime>();
        resultadoEsperado.add(fechaConIntervalo);
        resultadoEsperado.add(fechaConIntervalo.plusMonths(3));
        resultadoEsperado.add(fechaConIntervalo.plusMonths(6));
        resultadoEsperado.add(fechaConIntervalo.plusMonths(9));
        resultadoEsperado.add(fechaConIntervalo.plusMonths(12));

        var resultado = alarma.verRepeticiones(fechaConIntervalo.plusYears(1));

        for (int i = 0; i < resultado.size(); i++){assertEquals(resultadoEsperado.get(i), resultado.get(i));}
        assertTrue(alarma.verificarHayProximaRepeticion());
    }

    @Test
    public void TestAlarmaRepeticionAnual(){
        var fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        Alarma alarma = new Alarma("titulo prueba", "descripcion prueba", fecha, "prueba");
        alarma.establecerIntervalo(30, 0, 0, 0, false);
        alarma.establecerEfecto(AlarmaEfectos.SONIDO);

        alarma.configurarRepeticion(Frecuencia.Anual, Limite.SinLimite);

        var fechaConIntervalo = alarma.obtenerfechaHora();

        var resultadoEsperado = new ArrayList<LocalDateTime>();
        resultadoEsperado.add(fechaConIntervalo);
        resultadoEsperado.add(fechaConIntervalo.plusYears(1));
        resultadoEsperado.add(fechaConIntervalo.plusYears(2));
        resultadoEsperado.add(fechaConIntervalo.plusYears(3));
        resultadoEsperado.add(fechaConIntervalo.plusYears(4));

        var resultado = alarma.verRepeticiones(fechaConIntervalo.plusYears(4));

        for (int i = 0; i < resultado.size(); i++){assertEquals(resultadoEsperado.get(i), resultado.get(i));}
        assertTrue(alarma.verificarHayProximaRepeticion());

        var nuevoResultadoEsperado =  new ArrayList<LocalDateTime>();

        nuevoResultadoEsperado.add(fechaConIntervalo.plusYears(4));
        nuevoResultadoEsperado.add(fechaConIntervalo.plusYears(5));
        nuevoResultadoEsperado.add(fechaConIntervalo.plusYears(6));
        nuevoResultadoEsperado.add(fechaConIntervalo.plusYears(7));

        resultado = alarma.verRepeticiones(fechaConIntervalo.plusYears(7));

        for (int i = 0; i < resultado.size(); i++){assertEquals(nuevoResultadoEsperado.get(i), resultado.get(i));}

        assertTrue(alarma.verificarHayProximaRepeticion());
    }

    /* ___________________ TESTS DE ALARMA CON FECHA Y HORA ABS CON REPETICIONES ___________________ */

    @Test
    public void TestAlarmaFechaHoraAbsRepeticionDiaria(){
        var fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        Alarma alarma = new Alarma("titulo prueba", "descripcion prueba", fecha, "prueba");
        alarma.establecerFechaHoraAbsRepeticiones(fecha.minusDays(2).minusHours(1).minusMinutes(30));
        alarma.establecerEfecto(AlarmaEfectos.SONIDO);

        alarma.configurarRepeticion(Frecuencia.Diaria, Limite.Iteraciones);
        alarma.configurarIntervalo(4);
        alarma.configurarIteracion(4);

        var fechaConIntervalo = alarma.obtenerfechaHora();

        var resultadoEsperado = new ArrayList<LocalDateTime>();
        resultadoEsperado.add(fechaConIntervalo);
        resultadoEsperado.add(fechaConIntervalo.plusDays(4));
        resultadoEsperado.add(fechaConIntervalo.plusDays(8));
        resultadoEsperado.add(fechaConIntervalo.plusDays(12));

        assertTrue(alarma.verificarRepeticion());

        var resultado = alarma.verRepeticiones(fechaConIntervalo.plusYears(1));

        for (int i = 0; i < resultadoEsperado.size(); i++){
            assertEquals(resultadoEsperado.get(i), resultado.get(i));
        }

        assertFalse(alarma.verificarHayProximaRepeticion());
    }

    @Test
    public void TestAlarmaFechaHoraAbsRepeticionSemanal(){
        var fecha = LocalDateTime.of(2023, 4, 18, 10, 30);
        Alarma alarma = new Alarma("titulo prueba", "descripcion prueba", fecha, "prueba");
        alarma.establecerFechaHoraAbsRepeticiones(fecha.minusDays(3).minusHours(1).minusMinutes(30));
        alarma.establecerEfecto(AlarmaEfectos.SONIDO);
        var fechaConIntervalo = alarma.obtenerfechaHora();

        assertFalse(alarma.verificarRepeticion());

        alarma.configurarRepeticion(Frecuencia.Semanal, Limite.FechaMax);
        alarma.configurarDias(Set.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY));
        alarma.configurarFechaLimite(fechaConIntervalo.plusWeeks(2));

        assertTrue(alarma.verificarRepeticion());

        var resultadoEsperado = new ArrayList<LocalDateTime>();
        resultadoEsperado.add(fechaConIntervalo);
        resultadoEsperado.add(fechaConIntervalo.plusDays(2));
        resultadoEsperado.add(fechaConIntervalo.plusDays(7));
        resultadoEsperado.add(fechaConIntervalo.plusDays(9));

        assertTrue(alarma.verificarRepeticion());

        var resultado = alarma.verRepeticiones(fechaConIntervalo.plusYears(1));

        for (int i = 0; i < resultadoEsperado.size(); i++){
            assertEquals(resultadoEsperado.get(i), resultado.get(i));
        }
        assertFalse(alarma.verificarHayProximaRepeticion());
    }

    @Test
    public void TestAlarmaFechaHoraAbsRepeticionMensual(){
        var fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        Alarma alarma = new Alarma("titulo prueba", "descripcion prueba", fecha, "prueba");
        alarma.establecerFechaHoraAbsRepeticiones(fecha.minusDays(3).minusHours(1).minusMinutes(30));
        alarma.establecerEfecto(AlarmaEfectos.SONIDO);

        alarma.configurarRepeticion(Frecuencia.Mensual, Limite.SinLimite);
        alarma.configurarIntervalo(3);

        assertTrue(alarma.verificarRepeticion());

        var fechaConIntervalo = alarma.obtenerfechaHora();

        var resultadoEsperado = new ArrayList<LocalDateTime>();
        resultadoEsperado.add(fechaConIntervalo);
        resultadoEsperado.add(fechaConIntervalo.plusMonths(3));
        resultadoEsperado.add(fechaConIntervalo.plusMonths(6));
        resultadoEsperado.add(fechaConIntervalo.plusMonths(9));
        resultadoEsperado.add(fechaConIntervalo.plusMonths(12));

        var resultado = alarma.verRepeticiones(fechaConIntervalo.plusYears(1));

        for (int i = 0; i < resultado.size(); i++){
            assertEquals(resultadoEsperado.get(i), resultado.get(i));
        }
        assertTrue(alarma.verificarHayProximaRepeticion());
    }

    @Test
    public void TestAlarmaFechaHoraAbsRepeticionAnual(){
        var fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        Alarma alarma = new Alarma("titulo prueba", "descripcion prueba", fecha, "prueba");
        alarma.establecerFechaHoraAbsRepeticiones(fecha.minusDays(3).minusHours(1).minusMinutes(30));
        alarma.establecerEfecto(AlarmaEfectos.SONIDO);

        alarma.configurarRepeticion(Frecuencia.Anual, Limite.SinLimite);

        var fechaConIntervalo = alarma.obtenerfechaHora();

        var resultadoEsperado = new ArrayList<LocalDateTime>();
        resultadoEsperado.add(fechaConIntervalo);
        resultadoEsperado.add(fechaConIntervalo.plusYears(1));
        resultadoEsperado.add(fechaConIntervalo.plusYears(2));
        resultadoEsperado.add(fechaConIntervalo.plusYears(3));
        resultadoEsperado.add(fechaConIntervalo.plusYears(4));

        var resultado = alarma.verRepeticiones(fechaConIntervalo.plusYears(4));

        for (int i = 0; i < resultado.size(); i++){
            assertEquals(resultadoEsperado.get(i), resultado.get(i));
        }
        assertTrue(alarma.verificarHayProximaRepeticion());

        var nuevoResultadoEsperado =  new ArrayList<LocalDateTime>();

        nuevoResultadoEsperado.add(fechaConIntervalo.plusYears(4));
        nuevoResultadoEsperado.add(fechaConIntervalo.plusYears(5));
        nuevoResultadoEsperado.add(fechaConIntervalo.plusYears(6));
        nuevoResultadoEsperado.add(fechaConIntervalo.plusYears(7));

        resultado = alarma.verRepeticiones(fechaConIntervalo.plusYears(7));

        for (int i = 0; i < resultado.size(); i++){
            assertEquals(nuevoResultadoEsperado.get(i), resultado.get(i));
        }

        assertTrue(alarma.verificarHayProximaRepeticion());
    }
}




