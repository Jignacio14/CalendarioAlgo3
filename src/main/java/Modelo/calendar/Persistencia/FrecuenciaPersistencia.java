package Modelo.calendar.Persistencia;
import Modelo.calendar.Frecuencia;
import com.google.gson.*;

import java.lang.reflect.Type;


public class FrecuenciaPersistencia extends Deserealizador implements JsonSerializer<Frecuencia>, JsonDeserializer<Frecuencia> {

    @Override
    public Enum<?>[] listar(){
        return Frecuencia.values();
    }

    @Override
    public Frecuencia deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject jsonObject = json.getAsJsonObject();
        String frecuenciaTipo = jsonObject.get("tipoFrecuencia").getAsString();
        int intervalo = jsonObject.get("intervalo").getAsInt();

        Frecuencia frecuencia = (Frecuencia) super.deserialize(frecuenciaTipo, typeOfT, context);
        if (frecuencia==null){return null;}
        frecuencia.setIntervalo(intervalo);

        return frecuencia;
    }

    @Override
    public JsonElement serialize(Frecuencia frecuencia, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("tipoFrecuencia", frecuencia.name());
        jsonObject.addProperty("intervalo", frecuencia.obtenerIntervalo().toString());
        return jsonObject;
    }
}
