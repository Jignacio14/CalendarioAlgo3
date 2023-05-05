
import calendar.AlarmaEfectos;
import calendar.Calendario;

import java.time.LocalDateTime;

import calendar.Recordatorio;
import org.junit.Test;

import static org.junit.Assert.*;


public class CalendarioTest {

    @Test
    public void TestCalendarioCrearEvento() {
        LocalDateTime fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        var calendario = new Calendario();
        int idEvento = calendario.crearEvento(fecha, 1, 30);

        Recordatorio eventoCreado = calendario.verCalendario(idEvento);

        assertEquals("Sin titulo", eventoCreado.obtenerNombre());
        assertEquals("Sin descripcion", eventoCreado.obtenerDescripcion());
        assertEquals(fecha, eventoCreado.obtenerInicio());
    }

    @Test
    public void TestCalendarioCrearTarea() {
        LocalDateTime fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        var calendario = new Calendario();
        int idTarea = calendario.crearTarea(fecha, 0, 0);

        Recordatorio tareaCreada = calendario.verCalendario(idTarea);

        assertEquals("Sin titulo", tareaCreada.obtenerNombre());
        assertEquals("Sin descripcion", tareaCreada.obtenerDescripcion());
        assertEquals(fecha, tareaCreada.obtenerInicio());
    }

    @Test
    public void TestCalendarioModificarEvento() {
        LocalDateTime fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        var calendario = new Calendario();
        int idEvento1 = calendario.crearEvento(fecha, 1, 30);
        int idEvento2 = calendario.crearEvento(fecha, 1, 30);
        int idTarea3 = calendario.crearTarea(fecha.plusDays(5), 2, 0);

        Recordatorio eventoCreado1 = calendario.verCalendario(idEvento1);
        Recordatorio eventoCreado2 = calendario.verCalendario(idEvento2);
        Recordatorio TareaCreado3 = calendario.verCalendario(idTarea3);

        assertEquals("Sin titulo", eventoCreado2.obtenerNombre());
        assertEquals("Sin descripcion", eventoCreado2.obtenerDescripcion());
        assertEquals(fecha, eventoCreado2.obtenerInicio());

        calendario.modificarNombre(eventoCreado2, "titulo prueba");
        calendario.modificarDescripcion(eventoCreado2, "descripcion prueba");
        calendario.modificarInicio(eventoCreado2, fecha.plusDays(7));

        assertEquals("titulo prueba", eventoCreado2.obtenerNombre());
        assertEquals("descripcion prueba", eventoCreado2.obtenerDescripcion());
        assertEquals(fecha.plusDays(7), eventoCreado2.obtenerInicio());

        assertEquals("Sin titulo", eventoCreado1.obtenerNombre());
        assertEquals("Sin descripcion", eventoCreado1.obtenerDescripcion());
        assertEquals(fecha, eventoCreado1.obtenerInicio());

        assertEquals("Sin titulo", TareaCreado3.obtenerNombre());
        assertEquals("Sin descripcion", TareaCreado3.obtenerDescripcion());
        assertEquals(fecha.plusDays(5), TareaCreado3.obtenerInicio());
    }

    @Test
    public void TestCalendarioModificarTarea() {
        LocalDateTime fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        var calendario = new Calendario();
        int idEvento1 = calendario.crearEvento(fecha, 1, 30);
        int idEvento2 = calendario.crearEvento(fecha, 1, 30);
        int idTarea3 = calendario.crearTarea(fecha.plusDays(5), 2, 0);

        Recordatorio eventoCreado1 = calendario.verCalendario(idEvento1);
        Recordatorio eventoCreado2 = calendario.verCalendario(idEvento2);
        Recordatorio TareaCreado3 = calendario.verCalendario(idTarea3);

        assertEquals("Sin titulo", eventoCreado2.obtenerNombre());
        assertEquals("Sin descripcion", eventoCreado2.obtenerDescripcion());
        assertEquals(fecha, eventoCreado2.obtenerInicio());

        assertEquals("Sin titulo", eventoCreado1.obtenerNombre());
        assertEquals("Sin descripcion", eventoCreado1.obtenerDescripcion());
        assertEquals(fecha, eventoCreado1.obtenerInicio());

        assertEquals("Sin titulo", TareaCreado3.obtenerNombre());
        assertEquals("Sin descripcion", TareaCreado3.obtenerDescripcion());
        assertEquals(fecha.plusDays(5), TareaCreado3.obtenerInicio());

        calendario.modificarNombre(TareaCreado3, "titulo prueba");
        calendario.modificarDescripcion(TareaCreado3, "descripcion prueba");
        calendario.modificarInicio(TareaCreado3, fecha.plusDays(7));

        assertEquals("titulo prueba", TareaCreado3.obtenerNombre());
        assertEquals("descripcion prueba", TareaCreado3.obtenerDescripcion());
        assertEquals(fecha.plusDays(7), TareaCreado3.obtenerInicio());
    }

    @Test
    public void TestCalendarioEstablecerDiaCompletoEvento() {
        LocalDateTime fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        var calendario = new Calendario();
        int idEvento = calendario.crearEvento(fecha, 1, 30);

        Recordatorio eventoCreado = calendario.verCalendario(idEvento);
        assertFalse(eventoCreado.verficarDiaCompleto());

        calendario.establecerDiaCompleto(eventoCreado);
        assertTrue(eventoCreado.verficarDiaCompleto());
    }

    @Test
    public void TestCalendarioEstablecerDiaCompletoTarea() {
        LocalDateTime fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        var calendario = new Calendario();
        int idTarea = calendario.crearEvento(fecha, 1, 30);

        Recordatorio tareaCreado = calendario.verCalendario(idTarea);
        assertFalse(tareaCreado.verficarDiaCompleto());

        calendario.establecerDiaCompleto(tareaCreado);
        assertTrue(tareaCreado.verficarDiaCompleto());
    }

    @Test
    public void TestCalendarioEliminarRecordatorio() {
        LocalDateTime fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        var calendario = new Calendario();
        int idEvento1 = calendario.crearEvento(fecha, 1, 30);
        int idTarea2 = calendario.crearTarea(fecha.plusDays(5), 2, 0);
        int idEvento3 = calendario.crearEvento(fecha.plusDays(2), 1, 30);
        int idTarea4 = calendario.crearTarea(fecha, 1, 30);

        assertNotNull(calendario.verCalendario(idEvento3));

        var eventoAeliminar = calendario.verCalendario(idEvento3);
        calendario.eliminarRecordatorio(eventoAeliminar);

        assertNull(calendario.verCalendario(idEvento3));
        assertNotNull(calendario.verCalendario(idEvento1));
        assertNotNull(calendario.verCalendario(idTarea2));
        assertNotNull(calendario.verCalendario(idTarea4));
    }

    @Test
    public void TestCalendarioAgregarAlarmaEvento() {
        LocalDateTime fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        var calendario = new Calendario();
        int idEvento = calendario.crearEvento(fecha, 1, 30);
        Recordatorio eventoCreado = calendario.verCalendario(idEvento);

        var idAlarma = calendario.agregarAlarma(eventoCreado);

        assertEquals("Sin titulo", eventoCreado.obtenerAlarma(idAlarma).obtenerNombre());
        assertEquals("Sin descripcion", eventoCreado.obtenerAlarma(idAlarma).obtenerDescripcion());
        assertEquals(fecha, eventoCreado.obtenerAlarma(idAlarma).obtenerfechaHora());
        assertNull(eventoCreado.obtenerAlarma(idAlarma).obtenerEfecto());
    }

    @Test
    public void TestCalendarioAgregarAlarmaTarea() {
        LocalDateTime fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        var calendario = new Calendario();
        int idTarea = calendario.crearTarea(fecha, 1, 30);
        Recordatorio tareaCreada = calendario.verCalendario(idTarea);

        var idAlarma = calendario.agregarAlarma(tareaCreada);

        assertEquals("Sin titulo", tareaCreada.obtenerAlarma(idAlarma).obtenerNombre());
        assertEquals("Sin descripcion", tareaCreada.obtenerAlarma(idAlarma).obtenerDescripcion());
        assertEquals(fecha, tareaCreada.obtenerAlarma(idAlarma).obtenerfechaHora());
        assertNull(tareaCreada.obtenerAlarma(idAlarma).obtenerEfecto());
    }

    @Test
    public void TestCalendarioModificarAlarmaEventoIntervalo() {
        LocalDateTime fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        var calendario = new Calendario();
        int idEvento = calendario.crearEvento(fecha, 1, 30);
        Recordatorio eventoCreado = calendario.verCalendario(idEvento);

        var idAlarma = calendario.agregarAlarma(eventoCreado);

        assertEquals("Sin titulo", eventoCreado.obtenerAlarma(idAlarma).obtenerNombre());
        assertEquals("Sin descripcion", eventoCreado.obtenerAlarma(idAlarma).obtenerDescripcion());
        assertEquals(fecha, eventoCreado.obtenerAlarma(idAlarma).obtenerfechaHora());
        assertNull(eventoCreado.obtenerAlarma(idAlarma).obtenerEfecto());

        calendario.modificarNombre(eventoCreado, "Titulo prueba");
        calendario.modificarDescripcion(eventoCreado, "Descripcion Prueba");
        calendario.modificarAlarmaIntervalo(eventoCreado, idAlarma, 30, 0, 0, 0);
        calendario.modificarAlarmaEfecto(eventoCreado, idAlarma, AlarmaEfectos.SONIDO);

        assertEquals("Titulo prueba", eventoCreado.obtenerAlarma(idAlarma).obtenerNombre());
        assertEquals("Descripcion Prueba", eventoCreado.obtenerAlarma(idAlarma).obtenerDescripcion());
        assertEquals(fecha.minusMinutes(30), eventoCreado.obtenerAlarma(idAlarma).obtenerfechaHora());
        assertEquals(AlarmaEfectos.SONIDO, eventoCreado.obtenerAlarma(idAlarma).obtenerEfecto());
    }

    @Test
    public void TestCalendarioModificarAlarmaEventoFechaHoraAbs() {
        LocalDateTime fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        var calendario = new Calendario();
        int idEvento = calendario.crearEvento(fecha, 1, 30);
        Recordatorio eventoCreado = calendario.verCalendario(idEvento);

        var idAlarma = calendario.agregarAlarma(eventoCreado);

        assertEquals("Sin titulo", eventoCreado.obtenerAlarma(idAlarma).obtenerNombre());
        assertEquals("Sin descripcion", eventoCreado.obtenerAlarma(idAlarma).obtenerDescripcion());
        assertEquals(fecha, eventoCreado.obtenerAlarma(idAlarma).obtenerfechaHora());
        assertNull(eventoCreado.obtenerAlarma(idAlarma).obtenerEfecto());

        calendario.modificarNombre(eventoCreado, "Titulo prueba");
        calendario.modificarDescripcion(eventoCreado, "Descripcion Prueba");
        calendario.modificarAlarmaFechaHoraAbs(eventoCreado, idAlarma, fecha.minusDays(3).minusHours(3));
        calendario.modificarAlarmaEfecto(eventoCreado, idAlarma, AlarmaEfectos.SONIDO);

        assertEquals("Titulo prueba", eventoCreado.obtenerAlarma(idAlarma).obtenerNombre());
        assertEquals("Descripcion Prueba", eventoCreado.obtenerAlarma(idAlarma).obtenerDescripcion());
        assertEquals(fecha.minusDays(3).minusHours(3), eventoCreado.obtenerAlarma(idAlarma).obtenerfechaHora());
        assertEquals(AlarmaEfectos.SONIDO, eventoCreado.obtenerAlarma(idAlarma).obtenerEfecto());
    }

    @Test
    public void TestCalendarioEliminarAlarma() {
        LocalDateTime fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        var calendario = new Calendario();
        int idEvento1 = calendario.crearEvento(fecha, 1, 30);

        Recordatorio eventoCreado = calendario.verCalendario(idEvento1);

        var idAlarma1 = calendario.agregarAlarma(eventoCreado);
        var idAlarma2 = calendario.agregarAlarma(eventoCreado);
        var idAlarma3 = calendario.agregarAlarma(eventoCreado);

        assertNotNull(eventoCreado.obtenerAlarma(idAlarma2));

        var alarmaAeliminar = eventoCreado.obtenerAlarma(idAlarma2);
        calendario.eliminarAlarma(eventoCreado, alarmaAeliminar);

        assertNull(eventoCreado.obtenerAlarma(idAlarma2));
        assertNotNull(eventoCreado.obtenerAlarma(idAlarma1));
        assertNotNull(eventoCreado.obtenerAlarma(idAlarma3));
    }

    @Test
    public void TestCalendarioEventoConMasDeUnaAlarma() {
        LocalDateTime fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        var calendario = new Calendario();
        int idEvento = calendario.crearEvento(fecha, 1, 30);
        int cantidadAlarmasAgregadas = 4;

        Recordatorio eventoCreado = calendario.verCalendario(idEvento);

        calendario.agregarAlarma(eventoCreado);
        calendario.agregarAlarma(eventoCreado);
        calendario.agregarAlarma(eventoCreado);
        calendario.agregarAlarma(eventoCreado);

        for (int i = 0; i < cantidadAlarmasAgregadas; i++) {
            assertNotNull(eventoCreado.obtenerAlarma(i));
        }
    }

    @Test
    public void TestCalendarioTareaConMasDeUnaAlarma() {
        LocalDateTime fecha = LocalDateTime.of(2023, 4, 20, 10, 30);
        var calendario = new Calendario();
        int idTarea = calendario.crearTarea(fecha, 1, 30);
        int cantidadAlarmasAgregadas = 4;

        Recordatorio tareaCreada = calendario.verCalendario(idTarea);

        calendario.agregarAlarma(tareaCreada);
        calendario.agregarAlarma(tareaCreada);
        calendario.agregarAlarma(tareaCreada);
        calendario.agregarAlarma(tareaCreada);

        for (int i = 0; i < cantidadAlarmasAgregadas; i++) {
            assertNotNull(tareaCreada.obtenerAlarma(i));
        }
    }
}