import calendar.Calendario;
import calendar.Evento;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.*;

public class EventoTest {

    //prueba de terminar el recordatorio en un dia en especifico
    @Test
    public void CalcularFechaFin() {
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

    //prueba de terminar el recordatorio en un dia en especifico
    //por ejemplo: empieza el 20 de abril y se indica que se repite cada x cantidad de dias o semanas o mes o año
    // (cantidad que supera el rango entre el inicio y fin) pero se pone que termina el 25 de abril,
    // solo va a durar el inicio o los dias que esten dentro del rango que cumpla la condicion de (repeticion cada: ...)
    public void cantidadDeRepeticionesMayorADuracionDelRecordatorio(){
        // arrange

        // act

        // assert

    }

}