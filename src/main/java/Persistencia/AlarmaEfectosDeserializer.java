package Persistencia;

import calendar.AlarmaEfectos;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

public class AlarmaEfectosDeserializer extends Deserealizador {

    @Override
    public Enum<?>[] listar(){
        return AlarmaEfectos.values();
    }

    @Override
    public AlarmaEfectos deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        return (AlarmaEfectos) super.deserialize(json, typeOfT, context);
    }
}
