package calendar;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


public class Evento extends Recordatorio {
    private Repetidor repetidor;
    private LocalDateTime ultRepeticion;
    public Evento(LocalDateTime inicio, Integer horas, Integer minutos) {
        super(inicio, horas, minutos);
        super.nombre = "Nuevo evento";
    }

    @Override
    public boolean verificarRepeticion(){
        return repetidor == null ;
    }

    public boolean verificarHayProximaRepeticion(){
        return repetidor.verificarHayRepeticiones();
    }

    public void configurarRepeticion(Frecuencia frecuencia, Limite limite){
        this.repetidor = new Repetidor(limite, frecuencia);
    }

    public void configurarFechaLimite(LocalDateTime fechaLimite){
        this.repetidor.configurarFechaLimite(fechaLimite);
    }

    public void configurarIteracion(Integer iteraciones){
        this.repetidor.configurarIteraciones(iteraciones);
    }

    public void configurarIntervalo(Integer intervalo){
        this.repetidor.configurarIntervalo(intervalo);
    }

    public void configurarDias(Set<DayOfWeek> dias){
        this.repetidor.configurarDias(dias);
    }

    public List<LocalDateTime> verRepeticiones(LocalDateTime hasta){
        var consultaFechas = repetidor.verFuturasRepeticiones(ultRepeticion, hasta);
        ultRepeticion = consultaFechas.get(consultaFechas.size()-1);
        return consultaFechas;
    }



}
