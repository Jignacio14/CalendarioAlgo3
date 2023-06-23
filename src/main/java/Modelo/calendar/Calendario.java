package Modelo.calendar;
import Modelo.calendar.Persistencia.Persistible;
import Modelo.calendar.Persistencia.Persistor;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;

public class Calendario implements Persistible {

    private List<Recordatorio> recordatorios = new ArrayList<>();
    private final Organizador organizador = new Organizador();

    public Recordatorio obtenerRecordatorio(int idRecordatorio) {
        return this.recordatorios.get(idRecordatorio);
    }

    public Boolean calendarioVacio(){ return recordatorios.isEmpty(); }

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
        this.recordatorios.get(idEvento).establecerId(idEvento);
        organizador.actualizarRepeticiones(evento);
        return idEvento;
    }

    public int crearTarea(LocalDateTime inicio, Integer horas, Integer minutos) {
        var tarea = new Tarea(inicio, horas, minutos);
        agregarRecordatorio(tarea);
        int idTarea = this.recordatorios.lastIndexOf(tarea);
        this.recordatorios.get(idTarea).establecerId(idTarea);
        // TO DO -> agregar al ordenador
        organizador.actualizarRepeticiones(tarea);
        return idTarea;
    }

    public void eliminarRecordatorio(Recordatorio recordatorio) {
        var idRecordatorio = recordatorio.obtenerId();
        organizador.eliminarRepeticiones(recordatorio);
        this.recordatorios.set(idRecordatorio, null);
    }

    public void modificarNombre(Recordatorio recordatorio, String nombreNuevo) {
        recordatorio.modificarNombre(nombreNuevo);
    }

    public void modificarDescripcion(Recordatorio recordatorio, String descripcionNueva) {
        recordatorio.modificarDescripcion(descripcionNueva);
    }

    public void modificarInicio(Recordatorio recordatorio, LocalDateTime inicioNuevo) {
        organizador.eliminarRepeticiones(recordatorio);
        recordatorio.modificarInicio(inicioNuevo);
        organizador.actualizarRepeticiones(recordatorio);
    }

    public void establecerDiaCompleto(Recordatorio recordatorio) {
        organizador.eliminarRepeticiones(recordatorio);
        recordatorio.establecerDiaCompleto();
        organizador.actualizarRepeticiones(recordatorio);
    }

    public void modificarCompletada(Recordatorio recordatorio){ recordatorio.cambiarCompletada();}

    public int agregarAlarma(Recordatorio recordatorio) {
        var alarma = new Alarma(recordatorio.obtenerNombre(), recordatorio.obtenerDescripcion(), recordatorio.obtenerInicio(), recordatorio.obtenerTipo());
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

    public Alarma obtenerAlarma(Recordatorio recordatorio, int idAlarma){
        return recordatorio.obtenerAlarma(idAlarma);
    }

    //--- metodos para persistir
    public void guardar(Persistor persistor) throws IOException{
        persistor.serializar(recordatorios);
    }

    public void cargar(Persistor persistor) throws IOException{
        recordatorios = persistor.deserealizar();
    }

    public List<Recordatorio> obtenerRecordatorios(){
        return this.recordatorios;
    }


    // metodos para hacer consultas de fechas

    public Map<LocalDateTime, HashSet<Integer>> verRecordatoriosOrdenados(LocalDateTime desde, LocalDateTime hasta){
        return organizador.verCalendarioOrdenado(desde, hasta);
    }
    public void agregarRepeticiones(Evento evento, Frecuencia frecuencia, Limite limite){
        evento.configurarRepeticion(frecuencia, limite);
        organizador.actualizarRepeticiones(evento);
    }

    public void modificarRepeticionesFechaLimite(Evento evento, LocalDateTime hasta){
        organizador.eliminarRepeticiones(evento);
        evento.configurarFechaLimite(hasta);
        organizador.actualizarRepeticiones(evento);
    }

    public void modificarRepeticionesIteraciones(Evento evento, Integer iteraciones){
        organizador.eliminarRepeticiones(evento);
        evento.configurarIteracion(iteraciones);
        organizador.actualizarRepeticiones(evento);
    }

    public void modificarRepeticionesIntervalo(Evento evento, Integer intervalo){
        organizador.eliminarRepeticiones(evento);
        evento.configurarIntervalo(intervalo);
        organizador.actualizarRepeticiones(evento);
    }

    public void mofificarRepeticionesDias(Evento evento, Set<DayOfWeek> dias){
        organizador.eliminarRepeticiones(evento);
        evento.configurarDias(dias);
        organizador.actualizarRepeticiones(evento);
    }

    public void eliminarRepeticiones(Evento evento){
        organizador.eliminarRepeticiones(evento);
        evento.eliminarRepeticiones();
        organizador.actualizarRepeticiones(evento);
    }

    public void organizarRecordatorios(LocalDateTime desde, LocalDateTime hasta){
        // si el mapa en la consulta no incluye el hasta, por eso la extiendo 1 dia
        organizador.organizarRecordatorios(desde, hasta.plusDays(1), recordatorios);
    }

    public boolean verificarHayRepeticionesGeneradas(){
        return organizador.organizarEstaVacio();
    }

    // metodos para hacer pruebas
    private boolean compararRecordatorios(Object obj) {
        Calendario aComparar = (Calendario) obj;
        if (this.recordatorios.size() != aComparar.recordatorios.size()){
            return false;
        }
        for (int i = 0; i < this.recordatorios.size(); i++){
            if (! recordatorios.get(i).equals(aComparar.recordatorios.get(i))){
                return false;
            }
        }
        return true;
    }
    @Override
    public boolean equals(Object obj){
        if (getClass() != obj.getClass()) {
            return false;
        }

        return compararRecordatorios(obj);
    }

}
