package Controlador;

import Modelo.calendar.Calendario;
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


public class Controlador extends Application {

    @FXML
    private TilePane contenedorRecordatorios;
    @FXML
    private MenuItem agregarEvento;
    @FXML
    private MenuItem agregarTarea;

    @Override
    public void start(Stage stage) throws IOException {

        Calendario calendario = new Calendario();

        FXMLLoader loader = new FXMLLoader(getClass().
                getResource("../estructura.fxml"));
        loader.setController(this);
        VBox contenedorCalendario = loader.load();

        stage.setTitle("Calendario");

        agregarEvento.setOnAction(evento -> {
            LocalDateTime fechaHoraDefault = LocalDateTime.now();
            int id = calendario.crearEvento(fechaHoraDefault, fechaHoraDefault.getHour(), fechaHoraDefault.getMinute());
            Recordatorio recNuevo = calendario.obtenerRecordatorio(id);
          crearVistaEvento(recNuevo);
        });

        agregarTarea.setOnAction(evento -> {
            LocalDateTime fechaHoraDefault = LocalDateTime.now();
            int id = calendario.crearTarea(fechaHoraDefault, fechaHoraDefault.getHour(), fechaHoraDefault.getMinute());
            Recordatorio recNuevo = calendario.obtenerRecordatorio(id);
            crearVistaTarea(recNuevo);
        });

        Scene scene = new Scene(contenedorCalendario, 600, 480);
        stage.setScene(scene);
        stage.show();
    }

    private void crearVista(String color, Recordatorio recordatorio){
        Button botonRecNuevo = new Button(datosRecordatorios(recordatorio));
        botonRecNuevo.setStyle(color);
        botonRecNuevo.setId(Integer.toString(recordatorio.obtenerId()));
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                String[] opcionesRec = { "Titulo", "Descripcion", "Agregar Alarma" };
                var modificarOpciones = new ChoiceDialog("selecciona", opcionesRec);

                modificarOpciones.setTitle("Personalizar Recordatorio");
                modificarOpciones.setHeaderText(datosCompletosRecordatorio(recordatorio));
                modificarOpciones.setContentText("Modificar");
                modificarOpciones.showAndWait();

                if (modificarOpciones.getSelectedItem().equals("Titulo")){
                    String datoNuevo = modificarDato(recordatorio, botonRecNuevo, "Modificar titulo");
                    if (datoNuevo != null && !datoNuevo.isEmpty()){
                        recordatorio.modificarNombre(datoNuevo);
                        botonRecNuevo.setText(datosRecordatorios(recordatorio));
                    }
                } else if (modificarOpciones.getSelectedItem().equals("Descripcion")) {
                    String datoNuevo = modificarDato(recordatorio, botonRecNuevo, "Modificar descripcion");
                    if (datoNuevo != null && !datoNuevo.isEmpty()){
                        recordatorio.modificarDescripcion(datoNuevo);
                        botonRecNuevo.setText(datosRecordatorios(recordatorio));
                    }
                }
            }
        };
        botonRecNuevo.setOnAction(event);
        contenedorRecordatorios.getChildren().add(botonRecNuevo);
    }

    private String modificarDato(Recordatorio recordatorio, Button boton, String datoAModificar){
        var modificar = new TextInputDialog();
        modificar.setHeaderText(datoAModificar);
        modificar.showAndWait();
        return modificar.getResult();
    }

    private void crearVistaEvento(Recordatorio recordatorio){
        crearVista("-fx-background-color:orange;", recordatorio);
    }

    private void crearVistaTarea(Recordatorio recordatorio){
        crearVista("-fx-background-color:violet;", recordatorio);
    }

    private String datosRecordatorios(Recordatorio recordatorio){
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm:ss");
        return recordatorio.obtenerTipo() + "\n" +
                recordatorio.obtenerNombre() + "\n" +
                "Inicio: " + formato.format(recordatorio.obtenerInicio()) + "\n" +
                (recordatorio.obtenerTipo().equals("Tarea") ? verificarCompletada(recordatorio.verificarCompletada()) : "");
    }

    private String datosCompletosRecordatorio(Recordatorio recordatorio){
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm:ss");
        return recordatorio.obtenerTipo() + "\n" +
                recordatorio.obtenerNombre() + "\n" +
                "Inicio: " + formato.format(recordatorio.obtenerInicio()) + "\n" +
                recordatorio.obtenerDescripcion() + "\n" +
                "Alarmas: " + recordatorio.obtenerAlarmas().toString() + "\n" +
                (recordatorio.obtenerTipo().equals("Tarea") ? verificarCompletada(recordatorio.verificarCompletada()) : "");
    }

    private String verificarCompletada(Boolean tareaCompletada){
        if (tareaCompletada){
            return "Completada";
        }
        return "Sin completar";
    }
}
