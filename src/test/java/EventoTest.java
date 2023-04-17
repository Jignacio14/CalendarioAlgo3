import calendar.Evento;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.*;

public class EventoTest {

    //prueba de terminar el recordatorio en un dia en especifico
    @Test
    public void TestVerificarFechaFinEsCorrecta() {
        // arrange
        String fechaInicio = "2023-04-20 12:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime inicio = LocalDateTime.parse(fechaInicio, formatter);
        LocalDateTime finalEvento = LocalDateTime.parse(fechaInicio, formatter);
        finalEvento = finalEvento.plusHours(1);
        finalEvento = finalEvento.plusMinutes(30);
        var evento = new Evento(inicio, 1, 30);
        // act
        LocalDateTime duracion = evento.verFinal();

        // assert
        assertEquals(duracion, finalEvento);
    }

    @Test
    public void TestVerificarEventoDiaCompleto(){
        LocalDateTime completa = LocalDateTime.of(2023, 12, 14, 0, 0);
        LocalDateTime parcial = LocalDateTime.of(2023, 9, 14, 19, 0);
        var eventodiacompleto = new Evento(completa, 24, 0);
        var eventoparcial = new Evento(parcial, 4, 30);
        //act
        var prueba1 = eventodiacompleto.verficarDiaCompleto();
        var prueba2 = eventoparcial.verficarDiaCompleto();
        //
        assertTrue(prueba1);
        assertFalse(prueba2);
    }

    @Test
    public void TestVerificarEventoDura24HperoNoEsDiaCompleto(){
        LocalDateTime parcial = LocalDateTime.of(2023, 9, 14, 19, 0);
        var eventoparcial = new Evento(parcial, 24, 0);
        //
        var prueba = eventoparcial.verficarDiaCompleto();
        //
        assertFalse(prueba);
    }

    @Test
    public void TestPruebaCambiarNombre(){
        LocalDateTime parcial = LocalDateTime.of(2023, 9, 14, 19, 0);
        var evento = new Evento(parcial, 24, 0);
        //
        evento.modificarNombre("Examenes fiuba");
        evento.modificarNombre("Examenes fadu2");
        evento.modificarNombre("Examenes fa3");
        evento.modificarNombre("Algo diferente");
    }

    @Test
    public void TestEventoSinPeriodicidad(){
        LocalDateTime parcial = LocalDateTime.of(2023, 9, 14, 19, 0);
        var evento = new Evento(parcial, 24, 0);
        assertFalse(evento.tieneRepeticion());
    }

}