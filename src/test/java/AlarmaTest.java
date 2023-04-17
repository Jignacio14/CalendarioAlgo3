import calendar.Alarma;
import calendar.Evento;
import calendar.Tarea;
import org.junit.Test;

import java.time.LocalDateTime;


import static org.junit.Assert.*;

public class AlarmaTest {

    @Test
    public void TestRecordatorioSinAlarmas() {
        var inicioEvento = LocalDateTime.of(2023, 4, 16, 0, 0);
        var evento = new Evento(inicioEvento, 0 ,0);

        assertTrue(evento.obtenerAlarmas().isEmpty());
    }

    @Test
    public void TestRecordatorioConAlarmas() {
        var inicioEvento = LocalDateTime.of(2023, 4, 16, 0, 0);
        var evento = new Evento(inicioEvento, 0 ,0);
        
        evento.agregarAlarma(null, Alarma.Efecto.Notificacion, 4, Alarma.TipoIntervalo.Minutos);

        assertFalse(evento.obtenerAlarmas().isEmpty());
    }

    @Test
    public void TestAgregarAlarmaConIntervaloMinutos() {
        var fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        var evento = new Evento(fecha, 0 ,0);
        evento.agregarAlarma(null, Alarma.Efecto.Notificacion, 30, Alarma.TipoIntervalo.Minutos);

        var idAlarma = 0;
        var fechaHoraAlarmaEsperada = LocalDateTime.of(2023, 4, 20, 10, 00);

        assertEquals(fechaHoraAlarmaEsperada, evento.obtenerAlarmaDeseada(idAlarma).obtenerfechaHora());
    }

    @Test
    public void TestAgregarAlarmaConIntervaloHoras() {
        var fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        var evento = new Evento(fecha, 0 ,0);
        evento.agregarAlarma(null, Alarma.Efecto.Notificacion, 2, Alarma.TipoIntervalo.Horas);

        var idAlarma = 0;
        var fechaHoraAlarmaEsperada = LocalDateTime.of(2023, 4, 20, 8, 30);

        assertEquals(fechaHoraAlarmaEsperada, evento.obtenerAlarmaDeseada(idAlarma).obtenerfechaHora());
    }

    @Test
    public void TestAgregarAlarmaConIntervaloDias() {
        var fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        var evento = new Evento(fecha, 0 ,0);
        evento.agregarAlarma(null, Alarma.Efecto.Notificacion, 2, Alarma.TipoIntervalo.Dias);

        var idAlarma = 0;
        var fechaHoraAlarmaEsperada = LocalDateTime.of(2023, 4, 18, 10, 30);

        assertEquals(fechaHoraAlarmaEsperada, evento.obtenerAlarmaDeseada(idAlarma).obtenerfechaHora());
    }

    @Test
    public void TestAgregarAlarmaConIntervaloSemanas() {
        var fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        var evento = new Evento(fecha, 0 ,0);
        evento.agregarAlarma(null, Alarma.Efecto.Notificacion, 1, Alarma.TipoIntervalo.Semanas);

        System.out.println("semanas: " + evento.obtenerAlarmaDeseada(0).obtenerfechaHora());

        var idAlarma = 0;
        var fechaHoraAlarmaEsperada = LocalDateTime.of(2023, 4, 13, 10, 30);

        assertEquals(fechaHoraAlarmaEsperada, evento.obtenerAlarmaDeseada(idAlarma).obtenerfechaHora());
    }

    @Test
    public void TestEliminarTodasLasAlarmas() {
        var fechaHora = LocalDateTime.of(2023, 4, 16, 0, 0);
        var tarea = new Tarea(fechaHora, 0 ,0);
        tarea.agregarAlarma(null, Alarma.Efecto.Notificacion, 30, Alarma.TipoIntervalo.Minutos);
        tarea.agregarAlarma(fechaHora, Alarma.Efecto.Email, 0, null);
        tarea.agregarAlarma(null, Alarma.Efecto.Sonido, 38, Alarma.TipoIntervalo.Minutos);

        tarea.eliminarAlarmas();

        assertTrue(tarea.obtenerAlarmas().isEmpty());
    }

    @Test
    public void TestModificarAlarmaConFechaHoraAbs() {
        var inicioEvento = LocalDateTime.of(2023, 4, 16, 0, 0);
        var evento = new Evento(inicioEvento, 0 ,0);
        evento.agregarAlarma(null, Alarma.Efecto.Notificacion, 30, Alarma.TipoIntervalo.Minutos);

        var idAlarmaModificada = 0;
        var fechaHoraNueva = LocalDateTime.now();
        var efectoNuevo = Alarma.Efecto.Sonido;

        evento.modificarAlarma(idAlarmaModificada, fechaHoraNueva, efectoNuevo, 0, null);

        assertEquals(fechaHoraNueva, evento.obtenerAlarmaDeseada(idAlarmaModificada).obtenerfechaHora());
        assertEquals(efectoNuevo, evento.obtenerAlarmaDeseada(idAlarmaModificada).obtenerEfecto());
    }

    /*  @Test
    public void TestAlarmaConIntervaloTieneFechaHoraDeNotificacionCorrecta() {
    } */

    /* @Test
    public void TestAlarmaProximaEsLaCorrecta() {
        LocalDateTime fechaHoraAct = LocalDateTime.now();
    } */

    /* HAY QUE VERIFICAR ESTO?? 
    @Test
    public void TestEliminarAlarmaDeseada() {
        var fechaHora = LocalDateTime.of(2023, 4, 16, 0, 0);
        var tarea = new Tarea(fechaHora, 0 ,0);
        tarea.agregarAlarma(null, 30, Alarma.Efecto.Notificacion);
        tarea.agregarAlarma(fechaHora, 0, Alarma.Efecto.Email);
        tarea.agregarAlarma(null, 8, Alarma.Efecto.Sonido);

        var idAlarmaEliminar = 0;

        var alarmaEliminada = tarea.eliminarAlarmaDeseada(idAlarmaEliminar);

        assertTrue(tarea.obtenerAlarmas().isEmpty());
    } */

    
}