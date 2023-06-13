import Modelo.calendar.*;
import Modelo.calendar.Persistencia.PersistorJSON;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import static org.junit.Assert.*;


public class CalendarioTest {

    @Test
    public void TestCalendarioCrearEvento() {
        LocalDateTime fecha = LocalDateTime.of(2023, 5, 1 , 19, 40);
        var calendario = new Calendario();
        int idEvento = calendario.crearEvento(fecha, 1, 30);

        Recordatorio eventoCreado = calendario.obtenerRecordatorio(idEvento);

        assertEquals("Sin titulo", eventoCreado.obtenerNombre());
        assertEquals("Sin descripcion", eventoCreado.obtenerDescripcion());
        assertEquals(fecha, eventoCreado.obtenerInicio());
    }

    @Test
    public void TestCalendarioCrearTarea() {
        LocalDateTime fecha = LocalDateTime.of(2023, 5, 1 , 19, 40);
        var calendario = new Calendario();
        int idTarea = calendario.crearTarea(fecha, 0, 0);

        Recordatorio tareaCreada = calendario.obtenerRecordatorio(idTarea);

        assertEquals("Sin titulo", tareaCreada.obtenerNombre());
        assertEquals("Sin descripcion", tareaCreada.obtenerDescripcion());
        assertEquals(fecha, tareaCreada.obtenerInicio());
    }

    @Test
    public void TestCalendarioModificarEvento() {
        LocalDateTime fecha = LocalDateTime.of(2023, 5, 1 , 19, 40);
        var calendario = new Calendario();
        int idEvento1 = calendario.crearEvento(fecha, 1, 30);
        int idEvento2 = calendario.crearEvento(fecha, 1, 30);
        int idTarea3 = calendario.crearTarea(fecha.plusDays(5), 2, 0);

        Recordatorio eventoCreado1 = calendario.obtenerRecordatorio(idEvento1);
        Recordatorio eventoCreado2 = calendario.obtenerRecordatorio(idEvento2);
        Recordatorio TareaCreado3 = calendario.obtenerRecordatorio(idTarea3);

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
        LocalDateTime fecha = LocalDateTime.of(2023, 5, 1 , 19, 40);
        var calendario = new Calendario();
        int idEvento1 = calendario.crearEvento(fecha, 1, 30);
        int idEvento2 = calendario.crearEvento(fecha, 1, 30);
        int idTarea3 = calendario.crearTarea(fecha.plusDays(5), 2, 0);

        Recordatorio eventoCreado1 = calendario.obtenerRecordatorio(idEvento1);
        Recordatorio eventoCreado2 = calendario.obtenerRecordatorio(idEvento2);
        Recordatorio TareaCreado3 = calendario.obtenerRecordatorio(idTarea3);

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
        LocalDateTime fecha = LocalDateTime.of(2023, 5, 1 , 19, 40);
        var calendario = new Calendario();
        int idEvento = calendario.crearEvento(fecha, 1, 30);

        Recordatorio eventoCreado = calendario.obtenerRecordatorio(idEvento);
        assertFalse(eventoCreado.verficarDiaCompleto());

        calendario.establecerDiaCompleto(eventoCreado);
        assertTrue(eventoCreado.verficarDiaCompleto());
    }

    @Test
    public void TestCalendarioEstablecerDiaCompletoTarea() {
        LocalDateTime fecha = LocalDateTime.of(2023, 5, 1 , 19, 40);
        var calendario = new Calendario();
        int idTarea = calendario.crearEvento(fecha, 1, 30);

        Recordatorio tareaCreado = calendario.obtenerRecordatorio(idTarea);
        assertFalse(tareaCreado.verficarDiaCompleto());

        calendario.establecerDiaCompleto(tareaCreado);
        assertTrue(tareaCreado.verficarDiaCompleto());
    }

    @Test
    public void TestCalendarioEliminarRecordatorio() {
        LocalDateTime fecha = LocalDateTime.of(2023, 5, 1 , 19, 40);
        var calendario = new Calendario();
        int idEvento1 = calendario.crearEvento(fecha, 1, 30);
        int idTarea2 = calendario.crearTarea(fecha.plusDays(5), 2, 0);
        int idEvento3 = calendario.crearEvento(fecha.plusDays(2), 1, 30);
        int idTarea4 = calendario.crearTarea(fecha, 1, 30);

        assertNotNull(calendario.obtenerRecordatorio(idEvento3));

        var eventoAeliminar = calendario.obtenerRecordatorio(idEvento3);
        calendario.eliminarRecordatorio(eventoAeliminar);

        assertNull(calendario.obtenerRecordatorio(idEvento3));
        assertNotNull(calendario.obtenerRecordatorio(idEvento1));
        assertNotNull(calendario.obtenerRecordatorio(idTarea2));
        assertNotNull(calendario.obtenerRecordatorio(idTarea4));
    }

    @Test
    public void TestCalendarioAgregarAlarmaEvento() {
        LocalDateTime fecha = LocalDateTime.of(2023, 5, 1 , 19, 40);
        var calendario = new Calendario();
        int idEvento = calendario.crearEvento(fecha, 1, 30);
        Recordatorio eventoCreado = calendario.obtenerRecordatorio(idEvento);

        var idAlarma = calendario.agregarAlarma(eventoCreado);

        assertEquals("Sin titulo", eventoCreado.obtenerAlarma(idAlarma).obtenerNombre());
        assertEquals("Sin descripcion", eventoCreado.obtenerAlarma(idAlarma).obtenerDescripcion());
        assertEquals(fecha, eventoCreado.obtenerAlarma(idAlarma).obtenerfechaHora());
        assertNull(eventoCreado.obtenerAlarma(idAlarma).obtenerEfecto());
    }

    @Test
    public void TestCalendarioAgregarAlarmaTarea() {
        LocalDateTime fecha = LocalDateTime.of(2023, 5, 1 , 19, 40);
        var calendario = new Calendario();
        int idTarea = calendario.crearTarea(fecha, 1, 30);
        Recordatorio tareaCreada = calendario.obtenerRecordatorio(idTarea);

        var idAlarma = calendario.agregarAlarma(tareaCreada);

        assertEquals("Sin titulo", tareaCreada.obtenerAlarma(idAlarma).obtenerNombre());
        assertEquals("Sin descripcion", tareaCreada.obtenerAlarma(idAlarma).obtenerDescripcion());
        assertEquals(fecha, tareaCreada.obtenerAlarma(idAlarma).obtenerfechaHora());
        assertNull(tareaCreada.obtenerAlarma(idAlarma).obtenerEfecto());
    }

    @Test
    public void TestCalendarioModificarAlarmaEventoIntervalo() {
        LocalDateTime fecha = LocalDateTime.of(2023, 5, 1 , 19, 40);
        var calendario = new Calendario();
        int idEvento = calendario.crearEvento(fecha, 1, 30);
        Recordatorio eventoCreado = calendario.obtenerRecordatorio(idEvento);

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
        LocalDateTime fecha = LocalDateTime.of(2023, 5, 1 , 19, 40);
        var calendario = new Calendario();
        int idEvento = calendario.crearEvento(fecha, 1, 30);
        Recordatorio eventoCreado = calendario.obtenerRecordatorio(idEvento);

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
        LocalDateTime fecha = LocalDateTime.of(2023, 5, 1 , 19, 40);
        var calendario = new Calendario();
        int idEvento1 = calendario.crearEvento(fecha, 1, 30);
        calendario.crearEvento(fecha, 1, 30);

        Recordatorio eventoCreado = calendario.obtenerRecordatorio(idEvento1);

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

        Recordatorio eventoCreado = calendario.obtenerRecordatorio(idEvento);

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

        Recordatorio tareaCreada = calendario.obtenerRecordatorio(idTarea);

        calendario.agregarAlarma(tareaCreada);
        calendario.agregarAlarma(tareaCreada);
        calendario.agregarAlarma(tareaCreada);
        calendario.agregarAlarma(tareaCreada);

        for (int i = 0; i < cantidadAlarmasAgregadas; i++) {
            assertNotNull(tareaCreada.obtenerAlarma(i));
        }
    }

    @Test
    public void TestCalendarioPersistenciaCalendarioEvento() throws IOException{
        var calendario = new Calendario();
        var persistor = new PersistorJSON("./src/main/pruebasPersistenciaEvento.json");
        var fecha = LocalDateTime.of(2023, 5, 17, 23, 0);
        var idEvento = calendario.crearEvento(fecha, 2, 0);
        var nombre = "Prueba nombre nuevo";
        var descripcion = "Prueba descripcion nueva";

        calendario.establecerDiaCompleto(calendario.obtenerRecordatorio(idEvento));
        calendario.modificarNombre(calendario.obtenerRecordatorio(idEvento), nombre);
        calendario.modificarDescripcion(calendario.obtenerRecordatorio(idEvento), descripcion);

        calendario.guardar(persistor);

        var nuevoCalendario = new Calendario();
        nuevoCalendario.cargar(persistor);

        assertEquals(calendario, nuevoCalendario);
    }

    @Test
    public void TestCalendarioPersistenciaCalendarioTarea() throws IOException{
        var calendario = new Calendario();
        var persistor = new PersistorJSON("./src/main/pruebasPersistenciaTarea.json");
        var fecha = LocalDateTime.of(2023, 5, 17, 23, 0);
        var idTarea = calendario.crearTarea(fecha, 2, 0);
        var nombre = "Prueba nombre nuevo";
        var descripcion = "Prueba descripcion nueva";

        calendario.establecerDiaCompleto(calendario.obtenerRecordatorio(idTarea));
        calendario.modificarNombre(calendario.obtenerRecordatorio(idTarea), nombre);
        calendario.modificarCompletada(calendario.obtenerRecordatorio(idTarea));
        calendario.modificarDescripcion(calendario.obtenerRecordatorio(idTarea), descripcion);
        calendario.guardar(persistor);

        var nuevoCalendario = new Calendario();
        nuevoCalendario.cargar(persistor);

        assertEquals(calendario, nuevoCalendario);
    }

    @Test
    public void TestCalendarioNuevoSerializador() throws IOException {
        LocalDateTime fecha = LocalDateTime.of(2023, 5, 1 , 19, 40);
        var calendario = new Calendario();
        int idEvento1 = calendario.crearEvento(fecha, 1, 30);
        int idEvento3 = calendario.crearEvento(fecha.plusDays(2), 1, 30);
        int idTarea4 = calendario.crearTarea(fecha, 1, 30);

        Recordatorio recordatorioCreado = calendario.obtenerRecordatorio(idEvento1);
        Recordatorio recordatorioCreado3 = calendario.obtenerRecordatorio(idEvento3);
        Recordatorio recordatorioCreado4 = calendario.obtenerRecordatorio(idTarea4);

        int idAlarma = calendario.agregarAlarma(recordatorioCreado);
        int idAlarma2 = calendario.agregarAlarma(recordatorioCreado);
        int idAlarma3 = calendario.agregarAlarma(recordatorioCreado4);
        calendario.agregarAlarma(recordatorioCreado);
        calendario.agregarAlarma(recordatorioCreado);

        ((Evento)recordatorioCreado3).configurarRepeticion(Frecuencia.Diaria, Limite.SinLimite);

        recordatorioCreado.obtenerAlarma(idAlarma).establecerEfecto(AlarmaEfectos.SONIDO);
        recordatorioCreado.obtenerAlarma(idAlarma2).establecerEfecto(AlarmaEfectos.NOTIFICACION);
        recordatorioCreado4.obtenerAlarma(idAlarma3).establecerEfecto(AlarmaEfectos.EMAIL);

        var persistor = new PersistorJSON("./src/main/pruebaSerializador.json");

        calendario.guardar(persistor);

        var nuevoCalendario = new Calendario();

        nuevoCalendario.cargar(persistor);
        assertEquals(calendario, nuevoCalendario);
    }

    @Test
    public void TestCalendarioDeserealizarArchivoInexistente() {
        LocalDateTime fecha = LocalDateTime.of(2023, 5, 1, 19, 40);
        var calendario = new Calendario();
        int idEvento1 = calendario.crearEvento(fecha, 1, 30);
        int idEvento3 = calendario.crearEvento(fecha.plusDays(2), 1, 30);
        int idTarea4 = calendario.crearTarea(fecha, 1, 30);

        Recordatorio recordatorioCreado = calendario.obtenerRecordatorio(idEvento1);
        Recordatorio recordatorioCreado3 = calendario.obtenerRecordatorio(idEvento3);
        Recordatorio recordatorioCreado4 = calendario.obtenerRecordatorio(idTarea4);

        int idAlarma = calendario.agregarAlarma(recordatorioCreado);
        int idAlarma2 = calendario.agregarAlarma(recordatorioCreado);
        int idAlarma3 = calendario.agregarAlarma(recordatorioCreado4);
        calendario.agregarAlarma(recordatorioCreado);
        calendario.agregarAlarma(recordatorioCreado);

        ((Evento) recordatorioCreado3).configurarRepeticion(Frecuencia.Diaria, Limite.SinLimite);

        recordatorioCreado.obtenerAlarma(idAlarma).establecerEfecto(AlarmaEfectos.SONIDO);
        recordatorioCreado.obtenerAlarma(idAlarma2).establecerEfecto(AlarmaEfectos.NOTIFICACION);
        recordatorioCreado4.obtenerAlarma(idAlarma3).establecerEfecto(AlarmaEfectos.EMAIL);

        var excepcionActual = new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                var persistor = new PersistorJSON("./src/main/pruebaExcepcion.json");
                var nuevoCalendario = new Calendario();
                nuevoCalendario.cargar(persistor);
            }
        };

        var excepcionEsperada = NoSuchFileException.class;

        assertThrows(excepcionEsperada, excepcionActual);
    }

    @Test
    public void TestVerRecordatoriosOrdenadosVacio(){
        var inicio = LocalDateTime.of(2023, 1, 1, 0, 0);
        var fin = inicio.plusYears(1);
        var calendario = new Calendario();
        var result = calendario.verRecordatoriosOrdenados(inicio, fin);
        System.out.printf(result.toString());
    }

    @Test
    public void TestAgregandoEvento(){
        var inicio = LocalDateTime.of(2023, 1, 1, 0, 0);
        var fin = inicio.plusYears(1);
        var calendario = new Calendario();
        var id = calendario.crearEvento(inicio, 1, 30);
        var result = calendario.verRecordatoriosOrdenados(inicio, fin);
        assertTrue(result.containsKey(inicio));
        assertTrue(result.get(inicio).contains(id));
    }

    @Test
    public void TestAgregandoTarea(){
        var inicio = LocalDateTime.of(2023, 1, 1, 0, 0);
        var fin = inicio.plusYears(1);
        var calendario = new Calendario();
        var id = calendario.crearTarea(inicio, 1, 30);
        var result = calendario.verRecordatoriosOrdenados(inicio, fin);
        assertTrue(result.containsKey(inicio));
        assertTrue(result.get(inicio).contains(id));
    }

    @Test
    public void TestAgregandoEventosyTareas(){
        var inicio = LocalDateTime.of(2023, 1, 1, 0, 0);
        var inicio2 = inicio.plusYears(1);
        var fin = inicio2.plusDays(1);
        var calendario = new Calendario();
        var id1 = calendario.crearTarea(inicio, 1, 30);
        var id2 = calendario.crearTarea(inicio2, 0, 0);
        var result = calendario.verRecordatoriosOrdenados(inicio, fin);
        assertTrue(result.containsKey(inicio));
        assertTrue(result.containsKey(inicio2));
        assertTrue(result.get(inicio).contains(id1));
        assertTrue(result.get(inicio2).contains(id2));
    }

    @Test
    public void TestEventoConIteraciones(){
        var inicio = LocalDateTime.of(2023, 1, 1, 0, 0);
        var fin = inicio.plusYears(1);

        /// seteo fecha de inicio del evento y sus futuras repeticiones
        var fechaInicio = LocalDateTime.of(2023, 1, 2, 0, 0);
        var fechaSegundaRepe = LocalDateTime.of(2023, 1, 5, 0, 0);
        var fechaTerceraRepe = LocalDateTime.of(2023, 1, 8, 0, 0);
        var fechaCuartaRepe = LocalDateTime.of(2023, 1, 11, 0, 0);

        /// genero el evento
        var calendario = new Calendario();
        var idEvento = calendario.crearEvento(fechaInicio, 1, 50);
        var evento = (Evento) calendario.obtenerRecordatorio(idEvento);
        calendario.agregarRepeticiones(evento, Frecuencia.Diaria, Limite.Iteraciones);
        calendario.modificarRepeticionesIteraciones(evento, 4);
        calendario.modificarRepeticionesIntervalo(evento, 3);
        calendario.organizarRecordatorios(inicio, fin);

        var result = calendario.verRecordatoriosOrdenados(inicio, fin);

        assertTrue(result.containsKey(fechaInicio));
        assertTrue(result.get(fechaInicio).contains(idEvento));
        assertTrue(result.containsKey(fechaSegundaRepe));
        assertTrue(result.get(fechaSegundaRepe).contains(idEvento));
        assertTrue(result.containsKey(fechaTerceraRepe));
        assertTrue(result.get(fechaTerceraRepe).contains(idEvento));
        assertTrue(result.containsKey(fechaCuartaRepe));
        assertTrue(result.get(fechaCuartaRepe).contains(idEvento));

        System.out.println(result.toString());
    }
}