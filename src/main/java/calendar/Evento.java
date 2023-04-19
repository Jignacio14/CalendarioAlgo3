package calendar;

import java.time.LocalDateTime;


public class Evento extends Recordatorio {
    private Repetidor repetidor;
    public Evento(LocalDateTime inicio, Integer horas, Integer minutos) {
        super(inicio, horas, minutos);
        super.nombre = "Nuevo evento";
    }

    @Override
    public boolean verificarRepeticion(){
        return repetidor != null;
    }



    //private final ArrayList<LocalDateTime> repeticiones = new ArrayList<>();

    //private boolean repetible; ???



}
