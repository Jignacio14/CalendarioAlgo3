package calendar;

import java.time.LocalDateTime;


public class Evento extends Recordatorio {
    private Periodicidad repetidor;
    public Evento(LocalDateTime inicio, Integer horas, Integer minutos) {
        super(inicio, horas, minutos);
        super.nombre = "Nuevo evento";
    }

    @Override
    public boolean verificarRepeticion(){
        return repetidor != null;
    }

    public void generarRepeticion(Frecuencia frecuencia, Integer iteraciones){
        this.repetidor = new Periodicidad(iteraciones, frecuencia, inicio);
    }

    //private final ArrayList<LocalDateTime> repeticiones = new ArrayList<>();

    //private boolean repetible; ???



}
