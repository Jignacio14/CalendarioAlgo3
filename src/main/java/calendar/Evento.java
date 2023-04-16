package calendar;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;

public class Evento extends Recordatorio {
    public Evento(LocalDateTime inicio, Integer horas, Integer minutos, String nombre, String descripcion) {
        super(inicio, horas, minutos, nombre, descripcion);
    }

    //private final ArrayList<LocalDateTime> repeticiones = new ArrayList<>();

    //private boolean repetible; ???



}
