package calendar;

import java.io.*;
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
            JsonArray alarmas = recordatorioJson.get("alarmas").getAsJsonArray();
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
