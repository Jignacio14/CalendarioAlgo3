package calendar;

import java.time.LocalDateTime;
import java.util.*;

public class Calendario {

    private final Set<Recordatorio> recordatorios = new HashSet<Recordatorio>();

    public Set<Recordatorio> verCalendario(){
        return this.recordatorios;
    }

    public void crearEvento(LocalDateTime inicio, Integer horas, Integer minutos){
        var evento = new Evento(inicio, horas, minutos);
        recordatorios.add(evento);
    }

    public void crearTarea(LocalDateTime inicio, Integer horas, Integer minutos){
        var evento = new Tarea(inicio, horas, minutos);
        recordatorios.add(evento);
    }

    public void modificarNombre(Recordatorio recordatorio, String nombreNuevo){
       recordatorio.modificarNombre(nombreNuevo);
    }

    public void modificarDescripcion(Recordatorio recordatorio, String descripcionNueva){
        recordatorio.modificarDescripcion(descripcionNueva);
    }

    public void modificarInicio(Recordatorio recordatorio, LocalDateTime inicioNuevo){
        recordatorio.modificarInicio(inicioNuevo);
    }

    public void establecerDiaCompleto(Recordatorio recordatorio){
        recordatorio.establecerDiaCompleto();
    }

    public void agregarAlarma(Recordatorio recordatorio){
        var alarma = new Alarma(recordatorio.obtenerNombre(), recordatorio.obtenerDescripcion(), recordatorio.obtenerInicio());
        recordatorio.agregarAlarma(alarma);
    }

    public void modificarAlarmaFechaHoraAbs(Recordatorio recordatorio, Integer idAlarma, LocalDateTime fechaHoraAbs){
        recordatorio.modificarAlarmaFechaHoraAbs(idAlarma, fechaHoraAbs);
    }

    public void modificarAlarmaIntervalo(Recordatorio recordatorio, Integer idAlarma, Integer min, Integer horas, Integer dias, Integer semanas){
        recordatorio.modificarAlarmaIntervalo(idAlarma, min, horas, dias, semanas);
    }

    public void modificarAlarmaEfecto(Recordatorio recordatorio, Integer idAlarma, AlarmaEfectos efecto){
        recordatorio.modificarAlarmaEfecto(idAlarma, efecto);
    }

    public void eliminarRecordatorio(Recordatorio recordatorio){
        recordatorios.remove(recordatorio);
    }

}
