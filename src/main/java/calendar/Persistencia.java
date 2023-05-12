package calendar;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.*;

import java.time.LocalDateTime;
public class Persistencia {
    public void serializacion(List<Recordatorio> recordatorios) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        String json = gson.toJson(recordatorios);

        FileWriter archivo = new FileWriter("./src/main/calendario.json");
        PrintWriter out = new PrintWriter(archivo);
        out.write(json);
        archivo.close();
    }

    public List<Recordatorio> deserializacion() throws IOException {
        FileReader archivo = new FileReader("./src/main/calendario.json");
        BufferedReader lector = new BufferedReader(archivo);
        String linea = lector.readLine();
        String texto = "";

        while (linea != null) {
            texto = texto + linea; //ver clase prcatica donde se hablaba de esta alerta
            linea = lector.readLine();
        }

        List<Recordatorio> recordatorios = new ArrayList<>();

        JsonArray listaRecordatoriosJson = JsonParser.parseString(texto).getAsJsonArray();

        for (JsonElement recordatorio : listaRecordatoriosJson) {

            JsonObject recordatorioJson = recordatorio.getAsJsonObject();

            String nombre = recordatorioJson.get("nombre").getAsString();
            String descripcion = recordatorioJson.get("descripcion").getAsString();
            LocalDateTime inicio = LocalDateTime.parse(recordatorioJson.get("inicio").getAsString());
            Integer horas = recordatorioJson.get("horas").getAsInt();
            Integer minutos = recordatorioJson.get("minutos").getAsInt();
            int id = recordatorioJson.get("id").getAsInt();
            String tipoRecordatorio = recordatorioJson.get("tipo").getAsString();
            JsonArray alarmasJson = recordatorioJson.get("alarmas").getAsJsonArray();

            //Recordatorio recordatorioAct = tipoRecordatorio.equals("Evento") ? crearEvento(nombre, descripcion, horas, minutos, inicio, id, alarmasJson) : crearTarea(nombre, descripcion, horas, minutos, inicio, id, alarmasJson);

            Recordatorio recordatorioAct = crearRecordatorio(tipoRecordatorio, nombre, descripcion, horas, minutos, inicio, id, alarmasJson);

            recordatorios.add(recordatorioAct);
        }

        lector.close();
        return recordatorios;
    }

    private void cargarAlarmas(JsonArray alarmasJson, Recordatorio recordatorio){
        for (JsonElement alarma : alarmasJson) {
            JsonObject alarmaJson = alarma.getAsJsonObject();
            String nombre = alarmaJson.get("nombre").getAsString();
            String descripcion = alarmaJson.get("descripcion").getAsString();
            LocalDateTime fechaHora = LocalDateTime.parse(alarmaJson.get("fechaHora").getAsString());
            //String efecto = alarmaJson.get("efecto").getAsString();
            Alarma alarmaNueva = new Alarma(nombre, descripcion, fechaHora);
            //alarmaNueva.establecerEfecto(efecto);
            recordatorio.agregarAlarma(alarmaNueva);
        }
    }

    private Recordatorio crearRecordatorio(String tipoRecordatorio, String nombre, String descripcion, Integer horas, Integer minutos, LocalDateTime inicio, int id, JsonArray alarmasJson){
        Recordatorio recordatorio = tipoRecordatorio.equals("Evento") ? new Evento(inicio, horas, minutos) : new Tarea(inicio, horas, minutos);
        recordatorio.modificarNombre(nombre);
        recordatorio.modificarDescripcion(descripcion);
        recordatorio.establecerId(id);
        if(!(alarmasJson.isEmpty())) { cargarAlarmas(alarmasJson, recordatorio); }

        return recordatorio;
    }

    /*private Recordatorio crearEvento(String nombre, String descripcion, Integer horas, Integer minutos, LocalDateTime inicio, int id, JsonArray alarmasJson){
        Recordatorio evento = new Evento(inicio, horas, minutos);
        evento.modificarNombre(nombre);
        evento.modificarDescripcion(descripcion);
        evento.establecerId(id);
        if(!(alarmasJson.isEmpty())) { cargarAlarmas(alarmasJson, evento); }

        return evento;
    }

    private Recordatorio crearTarea(String nombre, String descripcion, Integer horas, Integer minutos, LocalDateTime inicio, int id, JsonArray alarmasJson){
        Recordatorio tarea = new Tarea(inicio, horas, minutos);
        tarea.modificarNombre(nombre);
        tarea.modificarDescripcion(descripcion);
        tarea.establecerId(id);
        if(!(alarmasJson.isEmpty())) { cargarAlarmas(alarmasJson, tarea); }

        return tarea;
    }*/
}
