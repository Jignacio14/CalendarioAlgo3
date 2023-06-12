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


    public void actualizarRepeticiones(Recordatorio recordatorio){
        if (recordatorio.verificarRepeticion()){
            agregarRepetidos(recordatorio.verRepeticiones(organizador.lastKey()), recordatorio.obtenerId());
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
