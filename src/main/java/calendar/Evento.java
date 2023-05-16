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
        this.tipo = "Evento";
        this.ultRepeticion = super.inicio;
    }

    @Override
    public boolean verificarRepeticion() { return repetidor != null; }

    public boolean verificarHayProximaRepeticion() { return repetidor.verificarHayRepeticiones(); }

    public void configurarRepeticion(Frecuencia frecuencia, Limite limite) {
        this.repetidor = new Repetidor(limite, frecuencia);
    }

    public void configurarFechaLimite(LocalDateTime fechaLimite) { this.repetidor.configurarFechaLimite(fechaLimite); }

    public void configurarIteracion(Integer iteraciones) { this.repetidor.configurarIteraciones(iteraciones); }

    public void configurarIntervalo(Integer intervalo) { this.repetidor.configurarIntervalo(intervalo); }

    public void configurarDias(Set<DayOfWeek> dias) { this.repetidor.configurarDias(dias); }

    public List<LocalDateTime> verRepeticiones(LocalDateTime hasta) {
        var consultaFechas = repetidor.verFuturasRepeticiones(ultRepeticion, hasta);
        ultRepeticion = consultaFechas.get(consultaFechas.size() - 1);
        return consultaFechas;
    }

    public void establecerRepetidor(Repetidor repetidor){ this.repetidor = repetidor; }

    public void establecerUltRepeticion(LocalDateTime ultRepeticion){ this.ultRepeticion = ultRepeticion; }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass()) {
            return false;
        }

        Evento eventoAComparar = (Evento) obj;
        return super.equals(obj) &&
                this.ultRepeticion.equals(eventoAComparar.ultRepeticion) &&
                compararRepetidor(eventoAComparar.repetidor);
    }

    private boolean compararRepetidor(Repetidor repetidorAcomparar){
        if (this.repetidor==null || repetidorAcomparar==null){
            return this.repetidor == repetidorAcomparar;
        }
        return this.repetidor.equals(repetidorAcomparar);
    }
}
