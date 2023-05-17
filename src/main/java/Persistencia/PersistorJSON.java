package Persistencia;

import calendar.*;
import com.google.gson.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PersistorJSON implements Persistor {

    public PersistorJSON(){}

    public void serializar(List<Recordatorio> recordatorios) throws IOException {
        persistirArchivo(crearSerializado(recordatorios));
    }

    private String crearSerializado(List<Recordatorio> recordatorios){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimePersistencia());
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        return gson.toJson(recordatorios);
    }

    private void persistirArchivo(String json) throws IOException {
        FileWriter archivo = new FileWriter("./src/main/calendarioNuevo.json");
        PrintWriter out = new PrintWriter(archivo);
        out.write(json);
        archivo.close();
    }

    public List<Recordatorio> deserealizar() throws IOException {
        Path archivo = Path.of("./src/main/calendario.json");
        Reader lector = Files.newBufferedReader(archivo);
        Gson gson = new Gson();
        JsonArray recordatoriosJson = gson.fromJson(lector, JsonArray.class);

        List<Recordatorio> recordatorios = new ArrayList<>();

        cargarCalendario(recordatoriosJson, recordatorios);

        lector.close();
        return recordatorios;
    }

    private void cargarCalendario(JsonArray recordatoriosJson, List<Recordatorio> recordatorios){
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

            Recordatorio recordatorioAct;
            if (tipoRecordatorio.equals("Evento")){
                String repetidorJson = recordatorio.get("repetidor") != null ? recordatorio.get("repetidor").getAsJsonObject().toString() : null;
                Repetidor repetidor = crearRepetidor(repetidorJson);
                LocalDateTime ultRepeticion = LocalDateTime.parse(recordatorio.get("ultRepeticion").getAsString());
                recordatorioAct = crearEvento(nombre, descripcion, horas, minutos, inicio, id, alarmasJson, repetidor, ultRepeticion);
            }else {
                recordatorioAct = crearTarea(nombre, descripcion, horas, minutos, inicio, id, alarmasJson);
            }

            recordatorios.add(recordatorioAct);
        }
    }

    private Repetidor crearRepetidor(String repetidorJson){

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Limite.class, new LimiteDeserializer());
        gsonBuilder.registerTypeAdapter(Frecuencia.class, new FrecuenciaDeserializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimePersistencia());
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        return gson.fromJson(repetidorJson, Repetidor.class);
    }

    private void cargarAlarmas(String alarmasJson, Recordatorio recordatorio){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(AlarmaEfectos.class, new AlarmaEfectosDeserializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimePersistencia());
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        Alarma[] alarma = gson.fromJson(alarmasJson, Alarma[].class);
        recordatorio.establecerAlarmas(Arrays.asList(alarma));
    }

    private Evento crearEvento(String nombre, String descripcion, Integer horas, Integer minutos, LocalDateTime inicio, int id, String alarmasJson, Repetidor repetidor, LocalDateTime ultRepeticion){
        Evento evento = new Evento(inicio, horas, minutos);
        evento.establecerRepetidor(repetidor);
        evento.establecerUltRepeticion(ultRepeticion);
        cargarDatosRecordatorio(evento, nombre, descripcion, id, alarmasJson);
        return evento;
    }

    private Tarea crearTarea(String nombre, String descripcion, Integer horas, Integer minutos, LocalDateTime inicio, int id, String alarmasJson){
        Tarea tarea = new Tarea(inicio, horas, minutos);
        cargarDatosRecordatorio(tarea, nombre, descripcion, id, alarmasJson);
        return tarea;
    }

    private void cargarDatosRecordatorio(Recordatorio recordatorio, String nombre, String descripcion, int id, String alarmasJson){
        recordatorio.modificarNombre(nombre);
        recordatorio.modificarDescripcion(descripcion);
        recordatorio.establecerId(id);
        if(!(alarmasJson.isEmpty())) { cargarAlarmas(alarmasJson, recordatorio); }
    }

    private Recordatorio crearRecordatorio(){
    return null;
    }
}


