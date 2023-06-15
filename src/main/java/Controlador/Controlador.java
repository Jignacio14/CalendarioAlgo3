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

        if (opcionUsuario!=null){
            switch ((String) opcionUsuario) {
                case "Titulo":
                    datoNuevo = verificarDatoNuevo(pedirDatoUsuario.apply("Modificar titulo"), recordatorioAct.obtenerNombre());
                    recordatorioAct.modificarNombre(datoNuevo);
                    break;
                case "Descripcion":
                    datoNuevo = verificarDatoNuevo(pedirDatoUsuario.apply("Modificar descripcion"), recordatorioAct.obtenerDescripcion());
                    recordatorioAct.modificarDescripcion(datoNuevo);
                    break;
                case "Agregar alarma":
                    crearAlarma(recordatorioAct);
                    break;
                case "Cambiar estado":
                    recordatorioAct.cambiarCompletada();
                    break;
                case "Agregar repeticion":
                    //algo
                    break;
                case "Todo el dia":
                    if (vista.msjConfirmacion("¿Quiere que dure todo el dia?\nSi quiere que deje de durar todo el dia debe modificar la fecha de inicio")){recordatorioAct.establecerDiaCompleto();}
                    break;
                case "Fecha de inicio o fin":
                    if (vista.msjConfirmacion("Quiere agregar una fecha de inicio y fin\n(Si elige 'ACEPTAR' el evento o tarea que es de dia completo va a dejar de serlo")){
                        establecerInicioyFinRec(recordatorioAct, pedirDatoUsuario);
                    }
                    break;
            }
        }
    }

    private void establecerInicioyFinRec(Recordatorio recordatorioAct, Function<String,String> pedirDatoUsuario) {
        do {
            var opcionUsuario = vista.vistaAgregarFechaIniYFin(recordatorioAct);
            if (opcionUsuario!=null){
                switch ((String) opcionUsuario) {
                    case "Fecha de inicio" -> modificarFecha("inicio", recordatorioAct);
                    case "Agregar duracion" -> {
                        Supplier<Object> duracionHorasUsuario = () -> (Integer.parseInt(verificarDatoNuevo(pedirDatoUsuario.apply("Ingrese las horas que quiere que dure el evento"), "0")));
                        var duracionHs = verificarDatoUsuarioInt(duracionHorasUsuario);
                        Supplier<Object> duracionMinUsuario = () -> (Integer.parseInt(verificarDatoNuevo(pedirDatoUsuario.apply("Ingrese los minutos que quiere que dure el evento"), "0")));
                        var duracionMin = verificarDatoUsuarioInt(duracionMinUsuario);
                        recordatorioAct.modificarFin(recordatorioAct.obtenerInicio().plusHours((Integer) duracionHs).plusMinutes((Integer) duracionMin));
                    }
                    case "Fecha de fin" -> modificarFecha("fin", recordatorioAct);
                }
            }
        } while(vista.msjConfirmacion("¿Quiere modificar algo mas?"));
    }

    private void modificarFecha(String fechaAMod, Recordatorio recordatorioAct) {
        vista.establecerFechaAMod(fechaAMod);
        var fechaNueva = verificarDatoUsuarioInt(Vista::vistaModificarFecha);
        if (fechaNueva!=null) {
            if (fechaAMod.equals("inicio")) {recordatorioAct.modificarInicio((LocalDateTime) fechaNueva);}else {
                recordatorioAct.modificarFin((LocalDateTime) fechaNueva);
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
        switch ((String) efecto) {
            case "Notificacion" -> this.calendario.modificarAlarmaEfecto(recordatorio, id, AlarmaEfectos.NOTIFICACION);
            case "Sonido" -> this.calendario.modificarAlarmaEfecto(recordatorio, id, AlarmaEfectos.SONIDO);
            case "Email" -> this.calendario.modificarAlarmaEfecto(recordatorio, id, AlarmaEfectos.EMAIL);
        }
    }

    private void establecerIntervaloAlarma(Integer intervalo, Object opcionUsuario, Recordatorio recordatorio, int id) {
        switch ((String) opcionUsuario) {
            case "Min" -> this.calendario.modificarAlarmaIntervalo(recordatorio, id, intervalo, 0, 0, 0);
            case "Horas" -> this.calendario.modificarAlarmaIntervalo(recordatorio, id, 0, intervalo, 0, 0);
            case "Dias" -> this.calendario.modificarAlarmaIntervalo(recordatorio, id, 0, 0, intervalo, 0);
            case "Semanas" -> this.calendario.modificarAlarmaIntervalo(recordatorio, id, 0, 0, 0, intervalo);
        }
    }

}