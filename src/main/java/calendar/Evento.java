package calendar;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;

public class Evento extends Recordatorio {

    private final ArrayList<LocalDateTime> repeticiones = new ArrayList<>();
    private boolean repetible;

    public Evento(LocalDateTime inicio, String nombre, String descripcion, LocalDateTime fin){
        //hacer verificaciones que validen repeticion
        super(inicio, nombre, descripcion);
    }
}
