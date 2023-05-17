package Persistencia;

import calendar.AlarmaEfectos;
public class AlarmaEfectoDeserializer extends Deserealizador {

    @Override
    public Enum<?>[] listar(){
        return AlarmaEfectos.values();
    }


}
