package Controlador;

import Modelo.calendar.Calendario;
import Modelo.calendar.Persistencia.PersistorJSON;

import java.io.IOException;
import java.util.List;

import Modelo.calendar.Recordatorio;
import Vista.*;
import javafx.application.Application;
import javafx.stage.Stage;


public class Controlador extends Application {

    private Vista vista;
    private Calendario calendario;

    //  VA A RECIBIR LOS EVENTOS Y VA A REACCIONAR A ESOS EVENTOS
    // SABE QUE EXISTE MODELO Y VISTA

    @Override
    public void start(Stage stage) throws IOException {
        this.calendario = new Calendario();
        cargarCalendario();
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