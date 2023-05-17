package calendar;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

public class FrecuenciaDeserializer implements JsonDeserializer<Frecuencia> {
    @Override
    public Frecuencia deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context){
        Frecuencia[] frecuencias = Frecuencia.values();
        for (Frecuencia frecuencia: frecuencias) {
            if (frecuencia.name().equals(json.getAsString())){
                return frecuencia;
            }
        }
        return null;
    }
}
