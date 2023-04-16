import calendar.Calendario;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CalendarioTest {

    @Test
    public void calendarioVacio() {

        // arrange
        var calendario = new Calendario();

        // act
        var calendarioVacio = calendario.verCalendario();

        // assert
        assertEquals(null, calendarioVacio);
    }




}