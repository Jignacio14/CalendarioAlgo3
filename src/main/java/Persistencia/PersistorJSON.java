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

    private final String fileLocation;
    final Integer cantValoresString = 3;
    final Integer posNombre = 0;
    final Integer posDescripcion = 1;
    final Integer posInicio = 2;
    final Integer cantValoresNumericos = 3;
    final Integer posHoras = 0;
    final Integer posMinutos = 1;
    final Integer posId = 2;


    public PersistorJSON(String fileLocation){
        this.fileLocation = fileLocation;
    }

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
        FileWriter archivo = new FileWriter(fileLocation);
        PrintWriter out = new PrintWriter(archivo);
        out.write(json);
        archivo.close();
    }

    public List<Recordatorio> deserealizar() throws IOException {
        Path archivo = Path.of(fileLocation);
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
            JsonObject recorjson = recordatorioJson.getAsJsonObject();
            Recordatorio recordatorio;
            if (!recorjson.has("completada")){
                recordatorio = agregarElementosEvento(recorjson);
            }else {
                recordatorio = agregarElementosTarea( recorjson);
            }
            recordatorios.add(recordatorio);
        }
    }

    private Tarea agregarElementosTarea( JsonObject json ){
        JsonArray alarmas = json.get("alarmas").getAsJsonArray();
        String alarmasJson = alarmas.toString();
        var valoresString = obtenerValoresString(json);
        var valoresInt = obtenerValoresNumericos(json);
        var tarea = crearTarea(valoresString[0], valoresString[1], valoresInt[0], valoresInt[1], LocalDateTime.parse(valoresString[2]), valoresInt[2], alarmasJson);
        if (json.get("completada").getAsBoolean()) {
            tarea.cambiarCompletada();
        }
        return tarea;
    }


    private Evento agregarElementosEvento(JsonObject json){
        JsonArray alarmas = json.get("alarmas").getAsJsonArray();
        var valoresString = obtenerValoresString(json);
        var valoresNumericos = obtenerValoresNumericos(json);
        String alarmasJson = alarmas.toString();
        String repetidorJson = json.get("repetidor") != null ? json.get("repetidor").getAsJsonObject().toString() : null;
        Repetidor repetidor = crearRepetidor(repetidorJson);
        LocalDateTime ultRepeticion = LocalDateTime.parse(json.get("ultRepeticion").getAsString());
        return crearEvento(valoresString[0], valoresString[1], valoresNumericos[0], valoresNumericos[1], LocalDateTime.parse(valoresString[2]), valoresNumericos[2], alarmasJson, repetidor, ultRepeticion);

    }

    private String[] obtenerValoresString(JsonObject json) {
        String[] valores = new String[cantValoresString];
        valores[posNombre] = json.get("nombre").getAsString();
        valores[posDescripcion] = json.get("descripcion").getAsString();
        valores[posInicio] = json.get("inicio").getAsString();
        return valores;
    }

    private Integer[] obtenerValoresNumericos(JsonObject json){
        Integer[] valores = new Integer[cantValoresNumericos];
        valores[posHoras] = json.get("horas").getAsInt();
        valores[posMinutos] = json.get("minutos").getAsInt();
        valores[posId] = json.get("id").getAsInt();
        return valores;
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

}


