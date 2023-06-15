package Modelo.calendar;

import java.time.LocalDateTime;
import java.util.*;

public class Organizador {
    private final TreeMap<LocalDateTime, HashSet<Integer>> organizador;
    public Organizador(){
        this.organizador = new TreeMap<>();
    }
    public void organizarRecordatorios(LocalDateTime desde, LocalDateTime hasta, List<Recordatorio> recordatorios){
        for (var recordatorio: recordatorios){
            if (recordatorio.verificarRepeticion()){
                agregarRepetidos(recordatorio.verRepeticiones(hasta), recordatorio.obtenerId());
            }else {
                agregarElemento(recordatorio.obtenerInicio(), recordatorio.obtenerId());
            }
        }
    }


    public void eliminarRepeticiones(Evento evento){
        var repeticionesPrevias = evento.verRepeticiones(evento.obtenerInicio(), organizador.lastKey().plusDays(1));
        repeticionesPrevias.remove(0);
        for (var repe: repeticionesPrevias){
            auxiliarEliminacion(repe, evento.obtenerId());
        }
    }

    public void actualizarRepeticiones(Recordatorio recordatorio){
        if (recordatorio.verificarRepeticion()){
            List<LocalDateTime> fechas = recordatorio.verRepeticiones(recordatorio.obtenerInicio(), organizador.lastKey());
            agregarRepetidos(fechas, recordatorio.obtenerId());
        }
        agregarElemento(recordatorio.obtenerInicio(), recordatorio.obtenerId());
    }

    private void agregarElemento(LocalDateTime fecha, Integer id){
        if (organizador.get(fecha) != null){
            organizador.get(fecha).add(id);
            return;
        }
        var conjunto = new HashSet<Integer>();
        conjunto.add(id);
        organizador.put(fecha, conjunto);
    }

    private void agregarRepetidos(List<LocalDateTime> fechas, Integer id){
        for (var fecha: fechas){
            agregarElemento(fecha, id);
        }
    }

    public SortedMap<LocalDateTime, HashSet<Integer>> verCalendarioOrdenado(LocalDateTime desde, LocalDateTime hasta){
        return organizador.subMap(desde, hasta);
    }

    public boolean organizarEstaVacio(){
        return organizador.isEmpty();
    }

    public void eliminarRepeticiones(Recordatorio recordatorio){
        auxiliarEliminacion(recordatorio.obtenerInicio(), recordatorio.obtenerId());
    }

    private void auxiliarEliminacion(LocalDateTime fecha, Integer id){
        if (organizador.isEmpty()){
            return;
        }
        HashSet<Integer> ids = organizador.get(fecha);
       if (ids == null){
           return;
       }
       ids.remove(id);
       if (ids.isEmpty()){
           organizador.remove(fecha);
       }
    }


}
