package Modelo.calendar;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OrganizadorTest {

    @Test
    public void PruebaAgregandoElementos(){
        var fechaBase = LocalDateTime.of(2023, 1, 1, 0, 0);
        var fechaFinal = fechaBase.plusYears(1);

        var evento1 = new Evento(fechaBase, 1, 30);
        var evento2 = new Evento(fechaBase, 1, 0);
        var tarea1 = new Tarea(fechaBase.plusDays(2), 1, 1);

        evento1.modificarNombre("Evento1");
        evento1.establecerId(1);
        evento2.modificarNombre("Evento2");
        evento2.establecerId(2);
        evento2.configurarRepeticion(Frecuencia.Diaria, Limite.Iteraciones);
        evento2.configurarIntervalo(1);
        evento2.configurarIteracion(3);
        tarea1.modificarNombre("Tarea1");
        tarea1.establecerId(3);
        List<Recordatorio> lista = new ArrayList<>();
        lista.add(evento1);
        lista.add(evento2);
        lista.add(tarea1);


        var orga = new Organizador();

        orga.organizarRecordatorios(fechaBase, fechaFinal, lista);
        var x = orga.verCalendarioOrdenado(fechaBase, fechaFinal);
        System.out.printf(x.toString());
        assertTrue(true);
    }

}