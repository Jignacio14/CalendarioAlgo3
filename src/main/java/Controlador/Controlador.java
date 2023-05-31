package Controlador;

import Modelo.calendar.AlarmaEfectos;
import Modelo.calendar.Calendario;
import Modelo.calendar.Persistencia.PersistorJSON;
import Modelo.calendar.Recordatorio;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;


public class Controlador extends Application {

    //  VA A RECIBIR LOS EVENTOS Y VA A REACCIONAR A ESOS EVENTOS
    // SABE QUE EXISTE MODELO Y VISTA

    @FXML
    private TilePane contenedorRecordatorios;
    @FXML
    private MenuItem agregarEvento;
    @FXML
    private MenuItem agregarTarea;

    @Override
    public void start(Stage stage) throws IOException {

        Calendario calendario = new Calendario();
        cargarCalendario(calendario);

        FXMLLoader loader = new FXMLLoader(getClass().
                getResource("../estructura.fxml"));
        loader.setController(this);
        VBox contenedorCalendario = loader.load();

        stage.setTitle("Calendario");

        agregarEvento.setOnAction( evento -> {
            int id = crearRecordatorio("Evento", calendario);
            crearVistaEvento(calendario, id);
        });

        agregarTarea.setOnAction( evento -> {
            int id = crearRecordatorio("Tarea", calendario);
            crearVistaTarea(calendario, id);
        });

        Scene scene = new Scene(contenedorCalendario, 600, 480);
        stage.setScene(scene);
        stage.show();
        guardarCalendario(calendario);
    }

    private int crearRecordatorio(String tipo, Calendario calendario){
        LocalDateTime fechaHoraDefault = LocalDateTime.now();
        return tipo.equals("Evento") ? calendario.crearEvento(fechaHoraDefault, 0, 0) : calendario.crearTarea(fechaHoraDefault, 0, 0);
    }

    private void cargarCalendario(Calendario calendario) {
        try {
            var persistor = new PersistorJSON("./src/main/pruebaSerializador.json");
            calendario.cargar(persistor);
        }catch (IOException ignore){
        }
    }

    private void guardarCalendario(Calendario calendario) throws IOException {
        if ( !calendario.calendarioVacio() ){
            var persistor = new PersistorJSON("./src/main/pruebaSerializador.json");
            calendario.guardar(persistor);
        }
    }

    private void crearVista(String color, Calendario calendario, int id){
        Recordatorio recordatorio = calendario.obtenerRecordatorio(id);
        Button botonRecNuevo = new Button(datosRecordatorios(recordatorio));
        botonRecNuevo.setStyle(color);
        botonRecNuevo.setId(Integer.toString(recordatorio.obtenerId()));
        EventHandler<ActionEvent> event = e -> {
            String[] opcionesRec = opcionesModificarRec(recordatorio);
            var personalizarRec = new ChoiceDialog("selecciona", opcionesRec);

            personalizarRec.setTitle("Personalizar Recordatorio");
            personalizarRec.setHeaderText(datosCompletosRecordatorio(recordatorio));
            personalizarRec.setContentText("Modificar");
            personalizarRec.showAndWait();

            if (personalizarRec.getSelectedItem().equals("Titulo")){
                String datoNuevo = modificarDato("Modificar titulo");
                if (datoNuevo != null && !datoNuevo.isEmpty()){
                    recordatorio.modificarNombre(datoNuevo);
                }
            } else if (personalizarRec.getSelectedItem().equals("Descripcion")) {
                String datoNuevo = modificarDato("Modificar descripcion");
                if (datoNuevo != null && !datoNuevo.isEmpty()){
                    recordatorio.modificarDescripcion(datoNuevo);
                }
            } else if (personalizarRec.getSelectedItem().equals("Agregar alarma")){
                agregarAlarma(calendario, recordatorio);
            } else if (personalizarRec.getSelectedItem().equals("Tarea Completada")) {
                if (!recordatorio.verificarCompletada()){
                    recordatorio.cambiarCompletada();
                }
            } else if (personalizarRec.getSelectedItem().equals("Agregar repeticion")) {
                //algo
            } else if (personalizarRec.getSelectedItem().equals("Todo el dia")) {
                recordatorio.establecerDiaCompleto();
            }
            botonRecNuevo.setText(datosRecordatorios(recordatorio));
        };
        botonRecNuevo.setOnAction(event);
        contenedorRecordatorios.getChildren().add(botonRecNuevo);
    }

    private void agregarAlarma(Calendario calendario, Recordatorio recordatorio){
        int id = calendario.agregarAlarma(recordatorio);
        agregarEfecto(calendario, recordatorio, id);
        agregarIntervalo(calendario, recordatorio, id);
    }

    void agregarEfecto(Calendario calendario, Recordatorio recordatorio, int id){
        TextInputDialog td = new TextInputDialog();
        td.setHeaderText("Elige un efecto entre los que se ven en pantalla - 'Notificacion', 'Sonido', 'Email' ");
        td.showAndWait();

        String opcionElegida = td.getResult();
        if (opcionElegida.equals("Notificacion")){
            calendario.modificarAlarmaEfecto(recordatorio, id, AlarmaEfectos.NOTIFICACION);
        }
    }

    void agregarIntervalo(Calendario calendario, Recordatorio recordatorio, int id){
        String[] opcionesRec = {"Min", "Horas", "Dias", "Semanas"};
        var personalizarRec = new ChoiceDialog("selecciona", opcionesRec);

        personalizarRec.setTitle("Agregar intervalo");
        personalizarRec.setHeaderText("Elegi el intervalo deseado");
        personalizarRec.setContentText("Intervalos");
        personalizarRec.showAndWait();

        if (personalizarRec.getSelectedItem().equals("Min")){
            var modificar = new TextInputDialog();
            modificar.setHeaderText("Min");
            modificar.showAndWait();
            Integer intervalo = Integer.parseInt(modificar.getResult());
            calendario.modificarAlarmaIntervalo(recordatorio, id, intervalo, 0,0,0);
        } else if (personalizarRec.getSelectedItem().equals("Horas")) {
            var modificar = new TextInputDialog();
            modificar.setHeaderText("Horas");
            modificar.showAndWait();
            Integer intervalo = Integer.parseInt(modificar.getResult());
            calendario.modificarAlarmaIntervalo(recordatorio, id, 0, intervalo,0,0);
        } else if (personalizarRec.getSelectedItem().equals("Dias")){
            var modificar = new TextInputDialog();
            modificar.setHeaderText("Dias");
            modificar.showAndWait();
            Integer intervalo = Integer.parseInt(modificar.getResult());
            calendario.modificarAlarmaIntervalo(recordatorio, id, 0, 0,intervalo,0);
        } else if (personalizarRec.getSelectedItem().equals("Semanas")) {
            var modificar = new TextInputDialog();
            modificar.setHeaderText("Semanas");
            modificar.showAndWait();
            Integer intervalo = Integer.parseInt(modificar.getResult());
            calendario.modificarAlarmaIntervalo(recordatorio, id, 0, 0,0,intervalo);
        }
    }

    private String[] opcionesModificarRec(Recordatorio recordatorio) {
        return recordatorio.obtenerTipo().equals("Evento") ? new String[]{"Titulo", "Descripcion", "Agregar alarma", "Todo el dia" , "Agregar repeticion"} : new String[]{"Titulo", "Descripcion", "Agregar alarma", "Todo el dia", "Tarea Completada"};
    }

    private String modificarDato(String datoAModificar){
        var modificar = new TextInputDialog();
        modificar.setHeaderText(datoAModificar);
        modificar.showAndWait();
        return modificar.getResult();
    }

    private void crearVistaEvento(Calendario calendario, int id){
        crearVista("-fx-background-color:orange;", calendario, id);
    }

    private void crearVistaTarea(Calendario calendario, int id){
        crearVista("-fx-background-color:violet;", calendario, id);
    }

    private String datosRecordatorios(Recordatorio recordatorio){
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm:ss");
        return recordatorio.obtenerTipo() + "\n" +
                recordatorio.obtenerNombre() + "\n" +
                "Inicio: " + formato.format(recordatorio.obtenerInicio()) + "\n" +
                "Fin: " + formato.format(recordatorio.verFinal()) + " " + ((recordatorio.verficarDiaCompleto()) ? " -- Dia completo --" : "") + "\n" +
                (recordatorio.obtenerTipo().equals("Tarea") ? verificarCompletada(recordatorio.verificarCompletada()) : "");
    }

    private String datosCompletosRecordatorio(Recordatorio recordatorio){
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm:ss");
        return recordatorio.obtenerTipo() + "\n" +
                recordatorio.obtenerNombre() + "\n" +
                "Inicio: " + formato.format(recordatorio.obtenerInicio()) + "\n" +
                "Fin: " + formato.format(recordatorio.verFinal()) + " " + ((recordatorio.verficarDiaCompleto()) ? " -- Dia completo --" : "") + "\n" +
                recordatorio.obtenerDescripcion() + "\n" +
                "Alarmas: " + Arrays.toString(alarmas(recordatorio)) + "\n" +
                (recordatorio.obtenerTipo().equals("Tarea") ? verificarCompletada(recordatorio.verificarCompletada()) : "");
    }

    private String[] alarmas(Recordatorio recordatorio){
        String[] alarmas = new String[recordatorio.obtenerAlarmas().size()];
        for (int i = 0; i < recordatorio.obtenerAlarmas().size(); i++) {
            alarmas[i] = recordatorio.obtenerAlarma(i).toString();
        }
        return alarmas;
    }

    private String verificarCompletada(Boolean tareaCompletada){
        if (tareaCompletada){
            return "Completada";
        }
        return "Sin completar";
    }
}
