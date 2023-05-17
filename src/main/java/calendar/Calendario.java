package calendar;
import Persistencia.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class Calendario implements Persistible{

    private List<Recordatorio> recordatorios = new ArrayList<>();
    private final Persistencia persistencia = new Persistencia();

    public Recordatorio obtenerRecordatorio(int idRecordatorio) { return this.recordatorios.get(idRecordatorio); }

    private void agregarRecordatorio(Recordatorio recordatorio) {
        int posicionVacia = this.recordatorios.indexOf(null);
        if (this.recordatorios.isEmpty() || posicionVacia < 0) {
            this.recordatorios.add(recordatorio);
        } else {
            this.recordatorios.set(posicionVacia, recordatorio);
        }
    }

    public int crearEvento(LocalDateTime inicio, Integer horas, Integer minutos) {
        var evento = new Evento(inicio, horas, minutos);
        agregarRecordatorio(evento);
        int idEvento = this.recordatorios.lastIndexOf(evento);
        this.recordatorios.get(idEvento).establecerId(idEvento); //se viola polk
        return idEvento;
    }

    public int crearTarea(LocalDateTime inicio, Integer horas, Integer minutos) {
        var tarea = new Tarea(inicio, horas, minutos);
        agregarRecordatorio(tarea);
        int idTarea = this.recordatorios.lastIndexOf(tarea);
        this.recordatorios.get(idTarea).establecerId(idTarea);//se viola polk
        return idTarea;
    }

    public void eliminarRecordatorio(Recordatorio recordatorio) {
        var idRecordatorio = recordatorio.obtenerId();
        this.recordatorios.set(idRecordatorio, null);
    }

    public void modificarNombre(Recordatorio recordatorio, String nombreNuevo) {
        recordatorio.modificarNombre(nombreNuevo);
    }

    public void modificarDescripcion(Recordatorio recordatorio, String descripcionNueva) {
        recordatorio.modificarDescripcion(descripcionNueva);
    }

    public void modificarInicio(Recordatorio recordatorio, LocalDateTime inicioNuevo) {
        recordatorio.modificarInicio(inicioNuevo);
    }

    public void establecerDiaCompleto(Recordatorio recordatorio) { recordatorio.establecerDiaCompleto(); }

    public int agregarAlarma(Recordatorio recordatorio) {
        var alarma = new Alarma(recordatorio.obtenerNombre(), recordatorio.obtenerDescripcion(), recordatorio.obtenerInicio());
        return recordatorio.agregarAlarma(alarma);
    }

    public void modificarAlarmaFechaHoraAbs(Recordatorio recordatorio, int idAlarma, LocalDateTime fechaHoraAbs) {
        recordatorio.modificarAlarmaFechaHoraAbs(idAlarma, fechaHoraAbs);
    }

    public void modificarAlarmaIntervalo(Recordatorio recordatorio, int idAlarma, Integer min, Integer horas, Integer dias, Integer semanas) {
        recordatorio.modificarAlarmaIntervalo(idAlarma, min, horas, dias, semanas);
    }

    public void modificarAlarmaEfecto(Recordatorio recordatorio, int idAlarma, AlarmaEfectos efecto) {
        recordatorio.modificarAlarmaEfecto(idAlarma, efecto);
    }

    public void eliminarAlarma(Recordatorio recordatorio, Alarma alarma){ recordatorio.eliminarAlarma(alarma); }


    public void guardar(Persistor persistor) throws IOException{
        try{
        persistor.serializar(recordatorios);
        }
        catch (IOException e ){
        // manejar lo que pueda ocurrir
        }
    }

    public void cargar(Persistor persistor) throws IOException{
        try{
        recordatorios = persistor.deserealizar();
        }
        catch (IOException e ){
        // manejar lo que pueda ocurrir
        }
    }

    public void guardar() throws IOException {
        persistencia.serializacion(recordatorios);
    }

    public List<Recordatorio> cargar() throws IOException {
       this.recordatorios = persistencia.deserializacion();
       return this.recordatorios;
    }
}
