import calendar.*;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

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
        Alarma alarmaNueva = new Alarma("titulo prueba", "descripcion prueba", fecha);
        var fechaHoraAlarmaEsperada = LocalDateTime.of(2023, 4, 15, 10, 0);

        alarmaNueva.establecerFechaHoraAbs(fechaHoraAlarmaEsperada);
        alarmaNueva.establecerEfecto(AlarmaEfectos.SONIDO);

        assertEquals(fechaHoraAlarmaEsperada, alarmaNueva.obtenerfechaHora());
    }


    /* ___________________ TESTS ALARMA CON INTERVALO ___________________ */

    @Test
    public void TestAgregarAlarmaConIntervaloMinutos() {
        var fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        Alarma alarmaNueva = new Alarma("titulo prueba", "descripcion prueba", fecha);

        alarmaNueva.establecerIntervalo(30, 0, 0, 0, false);
        alarmaNueva.establecerEfecto(AlarmaEfectos.SONIDO);

        var fechaHoraAlarmaEsperada = LocalDateTime.of(2023, 4, 20, 10, 0);

        assertEquals(fechaHoraAlarmaEsperada, alarmaNueva.obtenerfechaHora());
    }

    @Test
    public void TestAgregarAlarmaConIntervaloRecordatorioDiaCompleto() {
        var fecha = LocalDateTime.of(2023, 4, 20, 0, 0);
        Alarma alarmaNueva = new Alarma("titulo prueba", "descripcion prueba", fecha);

        alarmaNueva.establecerIntervalo(30, 9, 3, 0, true);
        alarmaNueva.establecerEfecto(AlarmaEfectos.SONIDO);

        var fechaHoraAlarmaEsperada = LocalDateTime.of(2023, 4, 17, 9, 30);

        assertEquals(fechaHoraAlarmaEsperada, alarmaNueva.obtenerfechaHora());
    }

    /* ___________________ TESTS DE EFECTOS ___________________ */

    @Test
    public void TestEfectoAlarmaSonido() {
        var fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        Alarma alarmaNueva = new Alarma("titulo prueba", "descripcion prueba", fecha);

        alarmaNueva.establecerIntervalo(30, 0, 0, 0, false);
        alarmaNueva.establecerEfecto(AlarmaEfectos.SONIDO);

        assertEquals("Sonido", alarmaNueva.obtenerEfecto().lanzarEfecto());
    }

    @Test
    public void TestEfectoAlarmaEmail() {
        var fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        Alarma alarmaNueva = new Alarma("titulo prueba", "descripcion prueba", fecha);

        alarmaNueva.establecerIntervalo(30, 0, 0, 0, false);
        alarmaNueva.establecerEfecto(AlarmaEfectos.EMAIL);

        assertEquals("Email", alarmaNueva.obtenerEfecto().lanzarEfecto());
    }

    @Test
    public void TestEfectoAlarmaNotificacion() {
        var fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        Alarma alarmaNueva = new Alarma("titulo prueba", "descripcion prueba", fecha);

        alarmaNueva.establecerIntervalo(30, 0, 0, 0, false);
        alarmaNueva.establecerEfecto(AlarmaEfectos.NOTIFICACION);

        assertEquals("Notificacion", alarmaNueva.obtenerEfecto().lanzarEfecto());
    }

    /* ___________________ TESTS DE DATOS DE ALARMA ___________________ */

    @Test
    public void TestTituloAlarma() {
        var fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        Alarma alarmaNueva = new Alarma("titulo prueba", "descripcion prueba", fecha);

        alarmaNueva.establecerIntervalo(30, 0, 0, 0, false);
        alarmaNueva.establecerEfecto(AlarmaEfectos.EMAIL);

        assertEquals("titulo prueba", alarmaNueva.obtenerNombre());
    }

    @Test
    public void TestDescripcionAlarma() {
        var fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        Alarma alarmaNueva = new Alarma("titulo prueba", "descripcion prueba", fecha);

        alarmaNueva.establecerIntervalo(30, 0, 0, 0, false);
        alarmaNueva.establecerEfecto(AlarmaEfectos.EMAIL);

        assertEquals("descripcion prueba", alarmaNueva.obtenerDescripcion());
    }

    /* ___________________ TESTS DE ALARMA CON REPETICIONES ___________________ */

    @Test
    public void TestAlarmaRepeticionDiaria(){
        var fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        Alarma alarmaNueva = new Alarma("titulo prueba", "descripcion prueba", fecha);
        alarmaNueva.establecerIntervalo(30, 0, 0, 0, false);
        alarmaNueva.establecerEfecto(AlarmaEfectos.SONIDO);

        alarmaNueva.configurarRepeticion(Frecuencia.Diaria, Limite.Iteraciones);
        alarmaNueva.configurarIntervalo(4);
        alarmaNueva.configurarIteracion(4);

        var resultadoEsperado = new ArrayList<LocalDateTime>();
        resultadoEsperado.add(fecha);
        resultadoEsperado.add(fecha.plusDays(4));
        resultadoEsperado.add(fecha.plusDays(8));
        resultadoEsperado.add(fecha.plusDays(12));

        assertTrue(alarmaNueva.verificarRepeticion());

        var resultado = alarmaNueva.verRepeticiones(fecha.plusYears(1));

        for (int i = 0; i < resultadoEsperado.size(); i++){
            assertEquals(resultadoEsperado.get(i), resultado.get(i));
        }

        assertFalse(alarmaNueva.verificarHayProximaRepeticion());
    }

    /*@Test
    public void TestEventoRepeticionSemanal(){
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

        for (int i = 0; i < resultadoEsperado.size(); i++){assertEquals(resultadoEsperado.get(i), resultado.get(i));}
        assertFalse(evento.verificarHayProximaRepeticion());
    }

    @Test
    public void TestEventoRepeticionMensual(){
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

        for (int i = 0; i < resultado.size(); i++){assertEquals(resultadoEsperado.get(i), resultado.get(i));}
        assertTrue(evento.verificarHayProximaRepeticion());


    }

    @Test
    public void TestEventoRepeticionAnual(){
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

        for (int i = 0; i < resultado.size(); i++){assertEquals(resultadoEsperado.get(i), resultado.get(i));}
        assertTrue(evento.verificarHayProximaRepeticion());

        var nuevoResultadoEsperado =  new ArrayList<LocalDateTime>();

        nuevoResultadoEsperado.add(fecha.plusYears(4));
        nuevoResultadoEsperado.add(fecha.plusYears(5));
        nuevoResultadoEsperado.add(fecha.plusYears(6));
        nuevoResultadoEsperado.add(fecha.plusYears(7));

        resultado = evento.verRepeticiones(fecha.plusYears(7));

        for (int i = 0; i < resultado.size(); i++){assertEquals(nuevoResultadoEsperado.get(i), resultado.get(i));}

        assertTrue(evento.verificarHayProximaRepeticion());
    }*/

}