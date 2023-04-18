package calendar;

import java.time.LocalDateTime;

public class Alarma {
    private LocalDateTime fechaHora;
    private AlarmaEfectos efecto;

    /*public enum Efecto {Email, Notificacion, Sonido};*/

    public Alarma(LocalDateTime fechaHora, AlarmaEfectos efecto){
        this.fechaHora = fechaHora;
        this.efecto = efecto;
    }

    public void modificar(LocalDateTime fechaHoraNueva, AlarmaEfectos efectoNuevo){
        this.fechaHora = fechaHoraNueva;
        this.efecto = efectoNuevo;
    }

    public LocalDateTime obtenerfechaHora(){ return this.fechaHora; }
    public AlarmaEfectos obtenerEfecto(){ return this.efecto; }

}
