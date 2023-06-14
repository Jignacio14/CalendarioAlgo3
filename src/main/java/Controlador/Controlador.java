package Controlador;

import Modelo.calendar.AlarmaEfectos;
import Modelo.calendar.Calendario;
import Modelo.calendar.Persistencia.PersistorJSON;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
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
        this.vista = new Vista(stage, this.calendario, escuchaPersonalizarRec(), escuchaAgregarRec());

        stage.setOnCloseRequest(windowEvent -> guardarCalendario());
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

    public EventHandler<ActionEvent> escuchaPersonalizarRec(){
        return (actionEvent -> {
            int idRecordatorioAct = Integer.parseInt((vista.obtenerRecSeleccionado(actionEvent)).getId());
            Recordatorio recordatorioAct = calendario.obtenerRecordatorio(idRecordatorioAct);
            Object opcionUsuario = vista.vistaPersonalizarRec(recordatorioAct);
            establecerDatosRec(opcionUsuario, recordatorioAct);

            vista.actualizarVistaRec(vista.obtenerRecSeleccionado(actionEvent), recordatorioAct);
        });
    }

    public EventHandler<ActionEvent> escuchaAgregarRec(){
        return (actionEvent -> {
            String idAgregarRec = (vista.obtenerRecAgregado(actionEvent)).getId();
            if (idAgregarRec.equals("agregarEvento")) {
                agregarRecordatorio("Evento");
            }else {
                agregarRecordatorio("Tarea");
            }
        });
    }

    private void agregarRecordatorio(String recAgregar){
        String[] opcionesModRec;
        Recordatorio recordatorio;
        recordatorio = calendario.obtenerRecordatorio(crearRecordatorio(recAgregar));
        opcionesModRec = vista.opcionesModificarRec(recordatorio);

        for (String modificar: opcionesModRec) {
            establecerDatosRec(modificar, recordatorio);
        }

        vista.crearVista(recordatorio);
    }

    private int crearRecordatorio(String tipo) {
        LocalDateTime fechaHoraDefault = LocalDateTime.now();
        return tipo.equals("Evento") ? this.calendario.crearEvento(fechaHoraDefault, 0, 0) : this.calendario.crearTarea(fechaHoraDefault, 0, 0);
    }

    private void establecerDatosRec(Object opcionUsuario, Recordatorio recordatorioAct) {
        Function<String, String> pedirDatoUsuario = Vista::vistaModificarDato;
        String datoNuevo;

        if (opcionUsuario != null && opcionUsuario.equals("Titulo")) {
            datoNuevo = verificarDatoNuevo(pedirDatoUsuario.apply("Modificar titulo"), recordatorioAct.obtenerNombre());
            recordatorioAct.modificarNombre(datoNuevo);

        } else if (opcionUsuario != null && opcionUsuario.equals("Descripcion")) {
            datoNuevo = verificarDatoNuevo(pedirDatoUsuario.apply("Modificar descripcion"), recordatorioAct.obtenerDescripcion());
            recordatorioAct.modificarDescripcion(datoNuevo);

        } else if (opcionUsuario != null && opcionUsuario.equals("Agregar alarma")) {
            crearAlarma(recordatorioAct);

        } else if (opcionUsuario != null && opcionUsuario.equals("Cambiar estado")) {
            recordatorioAct.cambiarCompletada();

        } else if (opcionUsuario != null && opcionUsuario.equals("Agregar repeticion")) {
            //algo

        } else if (opcionUsuario != null && opcionUsuario.equals("Todo el dia")) {
            recordatorioAct.establecerDiaCompleto();
        } else if (opcionUsuario != null && opcionUsuario.equals("Fecha de inicio")) {
            var fechaInicioNueva = verificarDatoUsuarioInt(Vista::vistaModificarInicio);
            if (fechaInicioNueva!=null) {
                recordatorioAct.modificarInicio((LocalDateTime) fechaInicioNueva);
            }
        }
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
            var intervalo = verificarDatoUsuarioInt(Vista::vistaCantIntervalo);
            if (intervalo!=null){
                int id = this.calendario.agregarAlarma(recordatorio);
                establecerEfecto(efecto, recordatorio, id);
                establecerIntervaloAlarma( (Integer)intervalo, tipoIntervalo, recordatorio, id);
            }
        }
    }

    public Object verificarDatoUsuarioInt(Supplier<Object> metodoAVerificar) {
        while (true){
            try {
                return metodoAVerificar.get();
            }catch (NumberFormatException | DateTimeException e){
                vista.mensajeError();
            }
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