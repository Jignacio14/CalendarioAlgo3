package Modelo.calendar.Persistencia;
import Modelo.calendar.Limite;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class LimitePersistencia extends Deserealizador implements JsonSerializer<Limite>, JsonDeserializer<Limite> {

    @Override
    public Enum<?>[] listar(){
        return Limite.values();
    }

    @Override
    public Limite deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject jsonObject = json.getAsJsonObject();
        String limiteTipo = jsonObject.get("tipoLimite").getAsString();
        LocalDateTime fechaLimite = LocalDateTime.parse(jsonObject.get("fechaLimite").getAsString());
        int iteraciones = jsonObject.get("iteraciones").getAsInt();

        Limite limite = (Limite) super.deserialize(limiteTipo, typeOfT, context);
        if (limite==null){return null;}
        limite.setIteraciones(iteraciones);
        limite.setFechaLimite(fechaLimite);

        return limite;
    }

    @Override
    public JsonElement serialize(Limite limite, Type type, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        System.out.println(limite.name() + "  " + limite.obtenerFechaLimite().toString() + "  " + limite.obtenerIteraciones().toString());
        jsonObject.addProperty("tipoLimite", limite.name());
        jsonObject.addProperty("fechaLimite", limite.obtenerFechaLimite().toString());
        jsonObject.addProperty("iteraciones", limite.obtenerIteraciones().toString());
        return jsonObject;
    }
}
