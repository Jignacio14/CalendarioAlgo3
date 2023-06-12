package Controlador;

import Modelo.calendar.Calendario;
import Modelo.calendar.Persistencia.PersistorJSON;

import java.io.IOException;
import Vista.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class Controlador extends Application {

    private Vista vista;
    private Calendario calendario;

    //  VA A RECIBIR LOS EVENTOS Y VA A REACCIONAR A ESOS EVENTOS
    // SABE QUE EXISTE MODELO Y VISTA

    @Override
    public void start(Stage stage) throws Exception {
        this.calendario = new Calendario();
        this.vista = new Vista(stage, calendario);
    }

    private void cargarCalendario() {
        try {
            var persistor = new PersistorJSON("./src/main/pruebaSerializador.json");
            this.calendario.cargar(persistor);
        }catch (IOException ignore){
        }
    }
}