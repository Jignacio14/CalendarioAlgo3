package Modelo.calendar.Persistencia;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

public abstract class Deserealizador {

    public abstract Enum<?>[] listar();

    public Enum<?> deserialize(String json, Type typeOfT, JsonDeserializationContext context){
        var listado = listar();
        for (Enum<?> esperado: listado){
            if(esperado.name().equals(json)) {
                return esperado;
            }
        }
        return null;
    }
}
