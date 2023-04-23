import calendar.Tarea;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class TareaTest {

    @Test
    public void TestTareaCrear() {
        var fecha = LocalDateTime.of(2023, 4, 16, 0, 0);
        var tarea = new Tarea(fecha, 0, 0);
        assertNotNull(tarea);
    }

    @Test
    public void TestTareaNombreDefecto() {
        var fecha = LocalDateTime.of(2023, 4, 15, 0, 0);
        var tarea = new Tarea(fecha, 0, 0);
        String porDefecto = "Sin titulo";
        var nombreDefecto = tarea.obtenerNombre();
        assertEquals(nombreDefecto, porDefecto);
    }

    @Test
    public void TestTareaDescripcionDefecto() {
        var fecha = LocalDateTime.of(2023, 4, 15, 0, 0);
        var tarea = new Tarea(fecha, 0, 0);
        String porDefecto = "Sin descripcion";
        var nombreDefecto = tarea.obtenerDescripcion();
        assertEquals(nombreDefecto, porDefecto);
    }

    @Test
    public void TestTareaCompletadaPorDefecto() {
        var fecha = LocalDateTime.of(2023, 4, 15, 0, 0);
        var tarea = new Tarea(fecha, 0, 0);
        assertFalse(tarea.verCompletada());
    }

    @Test
    public void TestTareaCambiarNombre() {
        var fecha = LocalDateTime.of(2023, 4, 15, 0, 0);
        var tarea = new Tarea(fecha, 0, 0);
        var nuevoNombre = "Hacer TP algo III";
        tarea.modificarNombre(nuevoNombre);
        assertEquals(nuevoNombre, tarea.obtenerNombre());
    }

    @Test
    public void TestTareaCambiarDescripcion() {
        var fecha = LocalDateTime.of(2023, 4, 15, 0, 0);
        var tarea = new Tarea(fecha, 0, 0);
        var nuevaDescripcion = "Recuerda hacer el TP porque repruebas";
        tarea.modificarDescripcion(nuevaDescripcion);
        assertEquals(nuevaDescripcion, tarea.obtenerDescripcion());
    }

    @Test
    public void TestTareaCompletar() {
        var fecha = LocalDateTime.of(2023, 4, 15, 0, 0);
        var tarea = new Tarea(fecha, 0, 0);
        tarea.cambiarCompletada();
        assertTrue(tarea.verCompletada());
        tarea.cambiarCompletada();
        assertFalse(tarea.verCompletada());
    }

    @Test
    public void TestTareaNoVencida() {
        var fecha = LocalDateTime.now();
        var fechaPosterior = fecha.plusHours(1);
        var tarea = new Tarea(fecha, 0, 0);
        assertFalse(tarea.verificarEstarVencida(fechaPosterior));
    }

    @Test
    public void TestTareaVencida() {
        var fecha = LocalDateTime.now();
        var fechaAnterior = fecha.minusHours(1);
        var tarea = new Tarea(fecha, 0, 0);
        assertTrue(tarea.verificarEstarVencida(fechaAnterior));
    }

    @Test
    public void TestTareaDiaCompleto() {
        var fecha = LocalDateTime.of(2023, 4, 16, 0, 0);
        var tarea = new Tarea(fecha, 24, 0);

        var fecha2 = LocalDateTime.now();
        var tarea2 = new Tarea(fecha2, 0, 0);

        assertTrue(tarea.verficarDiaCompleto());
        assertFalse(tarea2.verficarDiaCompleto());
    }

    @Test
    public void TestTareaCambiarInicio() {
        var fechaActual = LocalDateTime.now();
        var tarea = new Tarea(fechaActual, 0, 0);
        var fechaPosterior = fechaActual.plusHours(5);
        assertFalse(tarea.verificarEstarVencida(fechaPosterior));

        tarea.modificarInicio(fechaPosterior);

        assertTrue(tarea.verificarEstarVencida(fechaActual));
    }

}