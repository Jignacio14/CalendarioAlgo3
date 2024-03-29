package Modelo.calendar.Persistencia;
import Modelo.calendar.Limite;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

public class LimiteDeserializer extends Deserealizador implements JsonDeserializer<Limite> {
    public Enum<?>[] listar(){
        return Limite.values();
    }

    @Override
    public Limite deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        return (Limite) super.deserialize(json, typeOfT, context);
    }

}
