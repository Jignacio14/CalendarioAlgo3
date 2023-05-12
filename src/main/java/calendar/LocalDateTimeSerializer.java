package calendar;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {
    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type srcType, JsonSerializationContext context) {
        return new JsonPrimitive(localDateTime.toString());
    }

    //@Override
    // implements , JsonDeserializer<LocalDateTime>
    //public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
    //    return LocalDateTime.parse(json.getAsString());
    //}
}