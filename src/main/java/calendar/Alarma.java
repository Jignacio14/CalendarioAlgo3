package calendar;

import java.time.LocalDateTime;

public class Alarma {
    private LocalDateTime fechaHora;
    private Efecto efecto;

    //private int intervalo;

    private final LocalDateTime inicioRecordatorio;

    public enum Efecto {Email, Notificacion, Sonido};
    public enum TipoIntervalo {Minutos, Horas, Dias, Semanas}

    public Alarma(LocalDateTime fechaHora, Efecto efecto, int cantidadIntervalo, TipoIntervalo tipoIntervalo, LocalDateTime inicioRecordatorio){
        this.inicioRecordatorio = inicioRecordatorio;
        this.fechaHora = (tipoIntervalo == null ? fechaHora : calcularFechaHora(cantidadIntervalo, tipoIntervalo));
        //this.intervalo = intervalo;
        this.efecto = efecto;
    }

    private LocalDateTime calcularFechaHora(int intervalo, TipoIntervalo tipoIntervalo){
        switch (tipoIntervalo) {
            case Minutos -> this.fechaHora = inicioRecordatorio.minusMinutes(intervalo);
            case Horas -> this.fechaHora = inicioRecordatorio.minusHours(intervalo);
            case Dias -> this.fechaHora = inicioRecordatorio.minusDays(intervalo);
            case Semanas -> this.fechaHora = inicioRecordatorio.minusWeeks(intervalo);
        }
        return this.fechaHora;
    }

    public void modificar(LocalDateTime fechaHoraNueva, Alarma.Efecto efectoNuevo, int intervaloNuevo, TipoIntervalo tipoIntervaloNuevo){
        this.fechaHora = (tipoIntervaloNuevo == null ? fechaHoraNueva : calcularFechaHora(intervaloNuevo, tipoIntervaloNuevo));
        this.efecto = efectoNuevo;
    }

    public LocalDateTime obtenerfechaHora(){ return this.fechaHora; }
    public Alarma.Efecto obtenerEfecto(){ return this.efecto; }

}
