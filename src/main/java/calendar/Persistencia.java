package calendar;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.*;

import java.time.LocalDateTime;
public class Persistencia {
    public void serializacion(List<Recordatorio> recordatorios) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimePersistencia());
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        String json = gson.toJson(recordatorios);

        FileWriter archivo = new FileWriter("./src/main/calendario.json");
        PrintWriter out = new PrintWriter(archivo);
        out.write(json);
        archivo.close();
    }

    public List<Recordatorio> deserializacion() throws IOException {
        Path archivo = Path.of("./src/main/calendario.json");
        Reader lector = Files.newBufferedReader(archivo);
        Gson gson = new Gson();
        JsonArray recordatoriosJson = gson.fromJson(lector, JsonArray.class);

        List<Recordatorio> recordatorios = new ArrayList<>();

        for (JsonElement recordatorioJson : recordatoriosJson) {

            JsonObject recordatorio = recordatorioJson.getAsJsonObject();

            String nombre = recordatorio.get("nombre").getAsString();
            String descripcion = recordatorio.get("descripcion").getAsString();
            LocalDateTime inicio = LocalDateTime.parse(recordatorio.get("inicio").getAsString());
            Integer horas = recordatorio.get("horas").getAsInt();
            Integer minutos = recordatorio.get("minutos").getAsInt();
            int id = recordatorio.get("id").getAsInt();
            String tipoRecordatorio = recordatorio.get("tipo").getAsString();
            JsonArray alarmas = recordatorio.get("alarmas").getAsJsonArray();
            String alarmasJson = alarmas.toString();

            Recordatorio recordatorioAct = crearRecordatorio(tipoRecordatorio, nombre, descripcion, horas, minutos, inicio, id, alarmasJson);

            recordatorios.add(recordatorioAct);
        }

        lector.close();
        return recordatorios;
    }

    private void cargarAlarmas(String alarmasJson, Recordatorio recordatorio){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(AlarmaEfectos.class, new AlarmaEfectosDeserializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimePersistencia());
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        Alarma[] alarma = gson.fromJson(alarmasJson, Alarma[].class);
        recordatorio.establecerAlarmas(Arrays.asList(alarma));
    }

    private Recordatorio crearRecordatorio(String tipoRecordatorio, String nombre, String descripcion, Integer horas, Integer minutos, LocalDateTime inicio, int id, String alarmasJson){
        Recordatorio recordatorio = tipoRecordatorio.equals("Evento") ? new Evento(inicio, horas, minutos) : new Tarea(inicio, horas, minutos);
        recordatorio.modificarNombre(nombre);
        recordatorio.modificarDescripcion(descripcion);
        recordatorio.establecerId(id);
        if(!(alarmasJson.isEmpty())) { cargarAlarmas(alarmasJson, recordatorio); }

        return recordatorio;
    }
}
