package Controlador;

import Modelo.calendar.AlarmaEfectos;
import Modelo.calendar.Calendario;
import Modelo.calendar.Persistencia.PersistorJSON;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import Modelo.calendar.Recordatorio;
import Vista.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.stage.Stage;


public class Controlador extends Application {

    private Vista vista;
    private Calendario calendario;
    private Avanzador guia;
    private LocalDateTime desde;
    private LocalDateTime hasta;

    //  VA A RECIBIR LOS EVENTOS Y VA A REACCIONAR A ESOS EVENTOS
    // SABE QUE EXISTE MODELO Y VISTA

    @Override
    public void start(Stage stage) throws Exception {
        this.calendario = new Calendario();
        this.guia = Avanzador.Diario; // Vista por defecto es un dia
        cargarCalendario();
        this.vista = new Vista(stage, this.calendario, registrarEscuchaEnVista(), eventoVerPorRango(), eventoAvanzarAtrasar());
        var fechasDefecto = descomponerFechaRangoDia(LocalDateTime.now());
        desde = fechasDefecto[0];
        hasta = fechasDefecto[1];
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

    public EventHandler<ActionEvent> registrarEscuchaEnVista(){
        return (actionEvent -> {

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

            } else if (opcionUsuario.equals("Todo el dia")) {
                recordatorioAct.establecerDiaCompleto();
            }

            vista.actualizarVistaRec(vista.obtenerRecSeleccionado(actionEvent), recordatorioAct);
        });
    }

    public EventHandler<ActionEvent> eventoVerPorRango(){
        return actionEvent -> {
            LocalDateTime fechaActual = LocalDateTime.now();
            String origen = vista.obtenerOrigenVerRango(actionEvent.getSource());
            gestionarConsulta(origen, fechaActual);
        };
    }

    public EventHandler<ActionEvent> eventoAvanzarAtrasar(){
        return actionEvent -> {
            String origen = vista.obtenerOrigenAntSig(actionEvent.getSource());
            gestionarAntSig(origen);
        };
    }

    private void gestionarAntSig(String origen){
        LocalDateTime[] nuevoLimites = new LocalDateTime[2];
        switch (origen) {
            case "sigRango" -> nuevoLimites = guia.avanzar(desde, hasta);
            case "antRango" -> nuevoLimites = guia.regresar(desde, hasta);
            default -> {
            }
        }
        desde = nuevoLimites[0];
        hasta = nuevoLimites[1];
        calendario.organizarRecordatorios(desde, hasta);
    }

    private void gestionarConsulta(String origen, LocalDateTime fecha){
        LocalDateTime[] inicioFin;
        switch (origen) {
            case "rangoDia" -> {
                inicioFin = descomponerFechaRangoDia(fecha);
                this.guia = Avanzador.Diario;
            }
            case "rangoSemana" -> {
                inicioFin = descomponerFechaRangoSemana(fecha);
                this.guia = Avanzador.Semanal;
            }
            case "rangoMes" -> {
                inicioFin = descomponerFechaRangoMes(fecha);
                this.guia = Avanzador.Mensual;
            }
            default -> {return;}
        }
        desde = inicioFin[0];
        hasta = inicioFin[1];
        calendario.organizarRecordatorios(inicioFin[0], inicioFin[1]);
        System.out.println(calendario.verRecordatoriosOrdenados(inicioFin[0], inicioFin[1]));
    }

    public LocalDateTime[] descomponerFechaRangoDia(LocalDateTime fecha){
        LocalDateTime[] inicioFin = new LocalDateTime[2];
        inicioFin[0]= fecha.truncatedTo(ChronoUnit.DAYS);
        inicioFin[1]  = inicioFin[0].plusHours(23).plusMinutes(59);
        return inicioFin;
    }

    private LocalDateTime[] descomponerFechaRangoSemana(LocalDateTime fecha){
        int diaResultado = fecha.getDayOfMonth() + DayOfWeek.MONDAY.getValue() - fecha.getDayOfWeek().getValue();
        LocalDateTime[] inicioFin = new LocalDateTime[2];
        inicioFin[0] = LocalDateTime.of(fecha.getYear(), fecha.getMonthValue(), diaResultado, 0, 0);
        inicioFin[1] = inicioFin[0].plusDays(6).plusMinutes(59).plusHours(23);
        return inicioFin;
    }
    private LocalDateTime[] descomponerFechaRangoMes(LocalDateTime fecha){
        LocalDateTime[] inicioFin = new LocalDateTime[2];
        inicioFin[0] = LocalDateTime.of(fecha.getYear(), fecha.getMonthValue(), 1, 0, 0);
        inicioFin[1] = LocalDateTime.of(fecha.getYear(), fecha.getMonthValue(), fecha.toLocalDate().lengthOfMonth(), 23, 59);
        return inicioFin;
    }

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

}