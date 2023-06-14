package Controlador;

import Modelo.calendar.AlarmaEfectos;
import Modelo.calendar.Calendario;
import Modelo.calendar.Persistencia.PersistorJSON;

import java.io.IOException;
import java.util.function.Function;
import java.util.function.Supplier;


import Modelo.calendar.Recordatorio;
import Vista.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.stage.Stage;


public class Controlador extends Application {

    private Vista vista;
    private Calendario calendario;

    @Override
    public void start(Stage stage) throws Exception {
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
                Function<String, String> pedirDatoUsuario = modificar -> vista.vistaModificarDato(modificar);
                String datoNuevo;

                int idRecordatorioAct = Integer.parseInt((vista.obtenerRecSeleccionado(actionEvent)).getId());
                Recordatorio recordatorioAct = calendario.obtenerRecordatorio(idRecordatorioAct);

                Object opcionUsuario = vista.vistaPersonalizarRec(recordatorioAct);

                if (opcionUsuario.equals("Titulo")){
                    datoNuevo = verificarDatoNuevo(pedirDatoUsuario.apply("Modificar titulo"), recordatorioAct.obtenerNombre());
                    recordatorioAct.modificarNombre(datoNuevo);

                } else if (opcionUsuario.equals("Descripcion")) {
                    datoNuevo = verificarDatoNuevo(pedirDatoUsuario.apply("Modificar descripcion"), recordatorioAct.obtenerDescripcion());
                    recordatorioAct.modificarDescripcion(datoNuevo);

                } else if (opcionUsuario.equals("Agregar alarma")){
                    crearAlarma(recordatorioAct);

                } else if (opcionUsuario.equals("Tarea Completada")) {
                    if (!recordatorioAct.verificarCompletada()){
                        recordatorioAct.cambiarCompletada();
                    }

                } else if (opcionUsuario.equals("Agregar repeticion")) {
                    //algo

                } else if (opcionUsuario.equals("Todo el dia")) {
                    recordatorioAct.establecerDiaCompleto();
                } else if (opcionUsuario.equals("Fecha de inicio")) {
                    recordatorioAct.modificarInicio(vista.vistaModificarInicio());
                }

                vista.actualizarVistaRec(vista.obtenerRecSeleccionado(actionEvent), recordatorioAct);
            }
        });
    }

    private String verificarDatoNuevo(String datoNuevo, String datoAnt) {
        return (datoNuevo!=null && !datoNuevo.isEmpty()) ? datoNuevo : datoAnt;
    }

    public void crearAlarma(Recordatorio recordatorio){
        Object efecto = vista.vistaAgregarEfecto();
        if (efecto != null) {
            Object tipoIntervalo = vista.vistaAgregarIntervalo();
            agregarAlarma(recordatorio, efecto, tipoIntervalo);
        }
    }

    public void agregarAlarma(Recordatorio recordatorio, Object efecto, Object tipoIntervalo) {
        if (tipoIntervalo!=null){
            Integer intervalo =  verificarDatoUsuario(Vista::vistaCantIntervalo);
            int id = this.calendario.agregarAlarma(recordatorio);
            establecerEfecto(efecto, recordatorio, id);
            establecerIntervaloAlarma(intervalo, tipoIntervalo, recordatorio, id);
        }
    }

    public void establecerEfecto(Object efecto, Recordatorio recordatorio, int id) {
        if (efecto.equals("Notificacion")){
            this.calendario.modificarAlarmaEfecto(recordatorio, id, AlarmaEfectos.NOTIFICACION);
        } else if (efecto.equals("Sonido")) {
            this.calendario.modificarAlarmaEfecto(recordatorio, id, AlarmaEfectos.SONIDO);
        } else if (efecto.equals("Email")){
            this.calendario.modificarAlarmaEfecto(recordatorio, id, AlarmaEfectos.EMAIL);
        }
    }

    public Integer verificarDatoUsuario(Supplier<Integer> metodoAVerificar) {
        Integer dato = 0;
        boolean numeroValido = false;
        while (!numeroValido){
            try {
                dato = metodoAVerificar.get();
                numeroValido = true;
            }catch (NumberFormatException e){
                System.out.println("invalido");
            }
        }
        return dato;
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

}