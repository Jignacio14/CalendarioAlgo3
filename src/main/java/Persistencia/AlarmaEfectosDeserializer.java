package Persistencia;

import calendar.AlarmaEfectos;
public class AlarmaEfectosDeserializer extends Deserealizador {

    @Override
    public Enum<?>[] listar(){
        return AlarmaEfectos.values();
    }


}
