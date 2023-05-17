package calendar;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

public class AlarmaEfectosDeserializer implements JsonDeserializer<AlarmaEfectos> {
    @Override
    public AlarmaEfectos deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context){
        AlarmaEfectos[] efectos = AlarmaEfectos.values();
        for (AlarmaEfectos efecto: efectos) {
            if (efecto.name().equals(json.getAsString())){
                return efecto;
            }
        }
        return null;
    }
}
