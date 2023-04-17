import calendar.Calendario;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CalendarioTest {

    @Test
    public void TestCalendarioVacio() {
        var calendario = new Calendario();

        var calendarioVacio = calendario.verCalendario();

        assertNull(calendarioVacio);
    }

    @Test
    public void TestVerCalendarioDias() {
    }

    @Test
    public void TestVerCalendarioSemana() {
    }

    @Test
    public void TestVerCalendarioMes() {
    }

    @Test
    public void TestVerCalendarioAnio() {
    }


}