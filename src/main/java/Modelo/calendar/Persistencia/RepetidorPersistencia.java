package Modelo.calendar.Persistencia;

import Modelo.calendar.Frecuencia;
import Modelo.calendar.Limite;
import Modelo.calendar.Repetidor;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class RepetidorPersistencia implements JsonSerializer<Repetidor>, JsonDeserializer<Repetidor> {
    @Override
    public Repetidor deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Limite limite = new LimitePersistencia().deserialize(jsonObject.get("Limite").getAsJsonObject(), type, context) ;
        Frecuencia frecuencia = new FrecuenciaPersistencia().deserialize(jsonObject.get("Frecuencia").getAsJsonObject(), type, context) ;
        LocalDateTime ultConsulta = LocalDateTime.parse(jsonObject.get("ultConsulta").getAsString());

        Repetidor repetidor = new Repetidor(limite, frecuencia);
        repetidor.establecerUltConsulta(ultConsulta);
        return repetidor;
    }

    @Override
    public JsonElement serialize(Repetidor repetidor, Type type, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("ultConsulta", repetidor.obtenerUltConsulta().toString());

        jsonObject.add("Limite", new LimitePersistencia().serialize(repetidor.obtenerLimite(), type, context));
        jsonObject.add("Frecuencia", new FrecuenciaPersistencia().serialize(repetidor.obtenerFrecuencia(), type, context));

        return jsonObject;
    }
}
