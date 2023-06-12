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
            HashSet<Integer> ids = organizador.get(repe);
            if (ids == null || !organizador.containsKey(repe)){
                continue;
            }
            ids.remove(evento.obtenerId());
            if (ids.isEmpty()){
                organizador.remove(repe);
            }
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

}
