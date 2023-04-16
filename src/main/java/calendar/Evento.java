package calendar;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;

public class Evento extends Recordatorio {
    public Evento(LocalDateTime inicio, Integer horas, Integer minutos) {
        super(inicio, horas, minutos);
    }


    //private final ArrayList<LocalDateTime> repeticiones = new ArrayList<>();

    //private boolean repetible; ???



}
