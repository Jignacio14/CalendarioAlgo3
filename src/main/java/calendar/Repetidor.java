package calendar;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class Repetidor {

    private final Limite limite;
    private final Frecuencia frecuencia;
    private LocalDateTime ultConsulta = LocalDateTime.now();
    public Repetidor(Limite limite, Frecuencia frecuencia) {
        this.limite = limite;
        this.frecuencia = frecuencia;
    }

    public void configurarFechaLimite(LocalDateTime fechaLimite) {
        this.limite.setFechaLimite(fechaLimite);
    }

    public void configurarIteraciones(Integer iteraciones){
        this.limite.setIteraciones(iteraciones);
    }
    public void configurarIntervalo(Integer intervalo) {
        this.frecuencia.setIntervalo(intervalo);
    }

    public  void configurarDias(Set<DayOfWeek> dias){
        this.frecuencia.setDiasSemana(dias);
    }
    public boolean verificarHayRepeticiones() {
        return limite.verificarProximasIteraciones(this.ultConsulta);
    }

    public List<LocalDateTime> verFuturasRepeticiones(LocalDateTime desde, LocalDateTime hasta) {
        if (ultConsulta.isBefore(hasta)){
            ultConsulta = hasta;
        }
        return frecuencia.obtenerRepeticiones(limite, hasta, desde);
    }

}