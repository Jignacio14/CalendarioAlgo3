package calendar;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

public class LimiteDeserializer implements JsonDeserializer<Limite> {
    @Override
    public Limite deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context){
        Limite[] limites = Limite.values();
        for (Limite limite: limites) {
            if (limite.name().equals(json.getAsString())){
                return limite;
            }
        }
        return null;
    }
}
