package Persistencia;

import calendar.Frecuencia;


public class FrecuenciaDeserializer extends Deserealizador{


    public Enum<?>[] listar(){
        return Frecuencia.values();
    }




}
