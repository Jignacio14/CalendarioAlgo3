package Controlador;

import Modelo.calendar.AlarmaEfectos;
import Modelo.calendar.Calendario;
import Modelo.calendar.Persistencia.PersistorJSON;

import java.io.IOException;

import Modelo.calendar.Recordatorio;
import Vista.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
        this.vista = new Vista(stage, this.calendario, registrarEscuchaEnVista());

        stage.setOnCloseRequest(windowEvent -> {
            guardarCalendario();
        });
    }

    private void cargarCalendario() {
        try {
            var persistor = new PersistorJSON("./src/main/pruebaSerializador.json");
            this.calendario.cargar(persistor);
        }catch (IOException ignore){
        }
    }

    private void guardarCalendario() {
        try {
            if ( !this.calendario.calendarioVacio() ){
                var persistor = new PersistorJSON("./src/main/pruebaSerializador.json");
                this.calendario.guardar(persistor);
            }
        }catch (IOException ignore){
        }
    }

    public EventHandler<ActionEvent> registrarEscuchaEnVista(){
        return (new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                int idRecordatorioAct = Integer.parseInt((vista.obtenerRecSeleccionado(actionEvent)).getId());
                Recordatorio recordatorioAct = calendario.obtenerRecordatorio(idRecordatorioAct);

                Object opcionUsuario = vista.vistaPersonalizarRec(recordatorioAct);

                if (opcionUsuario.equals("Titulo")){
                    String datoNuevo = vista.vistaModificarDato("Modificar titulo");
                    if (datoNuevo != null && !datoNuevo.isEmpty()){
                        recordatorioAct.modificarNombre(datoNuevo);
                    }

                } else if (opcionUsuario.equals("Descripcion")) {
                    String datoNuevo = vista.vistaModificarDato("Modificar descripcion");
                    if (datoNuevo != null && !datoNuevo.isEmpty()){
                        recordatorioAct.modificarDescripcion(datoNuevo);
                    }

                } else if (opcionUsuario.equals("Agregar alarma")){
                    agregarAlarma(recordatorioAct);

                } else if (opcionUsuario.equals("Tarea Completada")) {
                    if (!recordatorioAct.verificarCompletada()){
                        recordatorioAct.cambiarCompletada();
                    }

                } else if (opcionUsuario.equals("Agregar repeticion")) {
                    //algo

                } else if (opcionUsuario.equals("Todo el dia")) {
                    recordatorioAct.establecerDiaCompleto();
                }

                vista.actualizarVistaRec(vista.obtenerRecSeleccionado(actionEvent), recordatorioAct);
            }
        });
    }

    /*public void registrarEscuchaEnVista(){
        this.vista.registrarEscucha(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                int idRecordatorioAct = Integer.parseInt((vista.obtenerRecSeleccionado(actionEvent)).getId());
                Recordatorio recordatorioAct = calendario.obtenerRecordatorio(idRecordatorioAct);

                Object opcionUsuario = vista.vistaPersonalizarRec(recordatorioAct);

                if (opcionUsuario.equals("Titulo")){
                    String datoNuevo = vista.vistaModificarDato("Modificar titulo");
                    if (datoNuevo != null && !datoNuevo.isEmpty()){
                        recordatorioAct.modificarNombre(datoNuevo);
                    }

                } else if (opcionUsuario.equals("Descripcion")) {
                    String datoNuevo = vista.vistaModificarDato("Modificar descripcion");
                    if (datoNuevo != null && !datoNuevo.isEmpty()){
                        recordatorioAct.modificarDescripcion(datoNuevo);
                    }

                } else if (opcionUsuario.equals("Agregar alarma")){
                    agregarAlarma(recordatorioAct);

                } else if (opcionUsuario.equals("Tarea Completada")) {
                    if (!recordatorioAct.verificarCompletada()){
                        recordatorioAct.cambiarCompletada();
                    }

                } else if (opcionUsuario.equals("Agregar repeticion")) {
                    //algo

                } else if (opcionUsuario.equals("Todo el dia")) {
                    recordatorioAct.establecerDiaCompleto();
                }

                vista.actualizarVistaRec(vista.obtenerRecSeleccionado(actionEvent), recordatorioAct);
            }
        });
    }*/

    public void agregarAlarma(Recordatorio recordatorio){
        int id = this.calendario.agregarAlarma(recordatorio);
        agregarEfecto(recordatorio, id);
        agregarIntervalo(recordatorio, id);
    }

    public void agregarEfecto(Recordatorio recordatorio, int id){
        String opcionElegida = vista.vistaAgregarEfecto();
        if (opcionElegida.equals("Notificacion")){
            this.calendario.modificarAlarmaEfecto(recordatorio, id, AlarmaEfectos.NOTIFICACION);
        }
    }

    public void agregarIntervalo(Recordatorio recordatorio, int id){
        Object opcionUsuario = vista.vistaAgregarIntervalo();
        Integer intervalo;

        if (opcionUsuario != null ){
            intervalo = vista.vistaCantIntervalo();
            establecerIntervaloAlarma(intervalo, opcionUsuario, recordatorio, id);
        }
    }

    private void establecerIntervaloAlarma(Integer intervalo, Object opcionUsuario, Recordatorio recordatorio, int id) {
        if (opcionUsuario.equals("Min")){
            this.calendario.modificarAlarmaIntervalo(recordatorio, id, intervalo, 0,0,0);
        } else if (opcionUsuario.equals("Horas")) {
            this.calendario.modificarAlarmaIntervalo(recordatorio, id, 0, intervalo,0,0);
        } else if (opcionUsuario.equals("Dias")){
            this.calendario.modificarAlarmaIntervalo(recordatorio, id, 0, 0,intervalo,0);
        } else if (opcionUsuario.equals("Semanas")) {
            this.calendario.modificarAlarmaIntervalo(recordatorio, id, 0, 0,0,intervalo);
        }
    }

    /*private void actualizarDatoCalendario(Consumer<String> modificar, ){
    }*/
}