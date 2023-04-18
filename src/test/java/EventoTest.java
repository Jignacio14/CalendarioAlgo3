import calendar.Evento;
import calendar.Frecuencia;
import com.sun.source.tree.AssertTree;
import org.junit.Assert;
import org.junit.Test;
import java.time.LocalDateTime;
import static org.junit.Assert.*;

public class EventoTest {

    @Test
    public void TestEventoCrear(){
        var fecha = LocalDateTime.now();
        var evento = new Evento(fecha, 1, 1);
        assertNotNull(evento);
    }
    
    @Test 
    public void TestEventoNombrePorDefecto(){
        var fecha = LocalDateTime.now();
        var evento = new Evento(fecha, 1, 1);
        assertEquals("Nuevo evento", evento.obtenerNombre());
    }

    @Test
    public void TestEventoDetallePorDefecto(){
        var fecha = LocalDateTime.now();
        var evento = new Evento(fecha, 1, 1);
        assertEquals(evento.obtenerDescripcion(), "Sin descripcion");
    }

    @Test
    public void TestEventoSeRepiteEsFalso(){
        var fecha = LocalDateTime.now();
        var evento = new Evento(fecha, 1, 1);
        assertFalse(evento.verificarRepeticion());
    }

    @Test
    public void TestEventoSeRepiteEsVerdadero(){
        //Agrego pruebas para verificar si se repite o no segun su configuraciòn (por ahora no puedo preguntar como se repite)
        var fecha = LocalDateTime.now();
        var evento = new Evento(fecha, 1, 1);
        evento.generarRepeticion(Frecuencia.Diario, 8 );
        assertTrue(evento.verificarRepeticion());
    }

    @Test
    public void TestEventoEsDeDiaCompleto(){
        var fecha = LocalDateTime.now();
        var evento = new Evento(fecha, 1, 1);
        assertFalse(evento.verficarDiaCompleto());

        fecha = LocalDateTime.of(2023, 12, 5, 0, 0);
        evento = new Evento(fecha, 24, 0);
        assertTrue(evento.verficarDiaCompleto());

        fecha = LocalDateTime.of(2023, 12, 5, 0, 0);
        evento = new Evento(fecha, 26, 0);
        assertFalse(evento.verficarDiaCompleto());

        fecha = LocalDateTime.of(2023, 12, 5, 0, 0);
        evento = new Evento(fecha, 24, 1);
        assertFalse(evento.verficarDiaCompleto());

    }

}