package Persistencia;
import calendar.Frecuencia;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;


public class FrecuenciaDeserializer extends Deserealizador implements JsonDeserializer<Frecuencia> {


    public Enum<?>[] listar(){
        return Frecuencia.values();
    }

    @Override
    public Frecuencia deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        return (Frecuencia) super.deserialize(json, typeOfT, context);
    }
}
