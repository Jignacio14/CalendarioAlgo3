import calendar.Alarma;
import calendar.AlarmaEfectos;
import calendar.Evento;
import org.junit.Test;

import java.time.LocalDateTime;

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

}