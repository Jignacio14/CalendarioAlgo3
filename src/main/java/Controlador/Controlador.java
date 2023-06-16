package Controlador;

import Modelo.calendar.*;
import Modelo.calendar.Persistencia.PersistorJSON;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;

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

    @Override
    public void start(Stage stage) throws Exception {
        this.calendario = new Calendario();
        this.guia = Avanzador.Diario; // Vista por defecto es un dia
        //cargarCalendario();
        this.vista = new Vista(stage, this.calendario, escuchaPersonalizarRec(), eventoAvanzarAtrasar(), eventoVerPorRango(), escuchaAgregarRec());
        var fechasDefecto = descomponerFechaRangoDia(LocalDateTime.now());
        auxiliarGestionConsultas(fechasDefecto);
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
            System.out.println(idRecordatorioAct);
            System.out.println(this.calendario.obtenerRecordatorios());
            Recordatorio recordatorioAct = calendario.obtenerRecordatorio(idRecordatorioAct);
            if (recordatorioAct!=null){
                Object opcionUsuario = vista.vistaPersonalizarRec(recordatorioAct);
                establecerDatosRec(opcionUsuario, recordatorioAct);
                vista.actualizarVistaRec(vista.obtenerRecSeleccionado(actionEvent), recordatorioAct);
            }
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
        opcionesModRec = Arrays.stream(vista.opcionesModificarRec(recordatorio))
                .filter(opcion -> !Objects.equals(opcion, "Eliminar"))
                .toArray(String[]::new);

        for (String modificar: opcionesModRec) {
            establecerDatosRec(modificar, recordatorio);
        }
        vista.crearVista(recordatorio);
    }

    private int crearRecordatorio(String tipo) {
        LocalDateTime fechaHoraDefault = LocalDateTime.now();
        return tipo.equals("Evento") ? this.calendario.crearEvento(fechaHoraDefault, 0, 0)
                : this.calendario.crearTarea(fechaHoraDefault, 0, 0);
    }

    private String verificarDatoNuevo(String datoNuevo, String datoAnt) {
        return (datoNuevo!=null && !datoNuevo.isEmpty()) ? datoNuevo : datoAnt;
    }

    private void modificarFecha(String fechaAMod, Recordatorio recordatorioAct) {
        vista.establecerFechaAMod(fechaAMod);
        var fechaNueva = verificarDatoUsuarioInt(Vista::vistaModificarFecha);
        if (fechaNueva == null) {
            return;
        }
        if (fechaAMod.equals("inicio")) {
            calendario.modificarInicio(recordatorioAct, (LocalDateTime) fechaNueva);
            return;
        }
        recordatorioAct.modificarFin((LocalDateTime) fechaNueva);
    }

    public EventHandler<ActionEvent> eventoVerPorRango(){
        return actionEvent -> {
            LocalDateTime fechaActual = LocalDateTime.now();
            String origen = vista.obtenerOrigenVerRango(actionEvent.getSource());
            gestionarConsulta(origen, fechaActual);
        };
    }

    public EventHandler<ActionEvent> eventoAvanzarAtrasar() {
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
            default -> {}
        }
        auxiliarGestionConsultas(nuevoLimites);
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
        auxiliarGestionConsultas(inicioFin);
    }

    private void auxiliarGestionConsultas(LocalDateTime[] fechas){
        desde = fechas[0];
        hasta = fechas[1];
        calendario.organizarRecordatorios(desde, hasta);
        Map<LocalDateTime, HashSet<Integer>> codigos = calendario.verRecordatoriosOrdenados(desde, hasta);
        vista.verEventosPorRangoFechas(codigos);
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


    public void agregarAlarma(Recordatorio recordatorio, Object efecto, Object tipoIntervalo) {
        if (tipoIntervalo == null) {
            return;
        }
        var intervalo = verificarDatoUsuarioInt(Vista::vistaCantIntervalo);
        if (intervalo == null) {
            return;
        }
        int id = this.calendario.agregarAlarma(recordatorio);
        establecerEfecto(efecto, recordatorio, id);
        establecerIntervaloAlarma( (Integer)intervalo, tipoIntervalo, recordatorio, id);
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

    public void crearAlarma(Recordatorio recordatorio){
        Object efecto = vista.vistaAgregarEfecto();
        if (efecto == null) {
            return;
        }
        Object tipoIntervalo = vista.vistaAgregarIntervalo();
        agregarAlarma(recordatorio, efecto, tipoIntervalo);
    }
    private void establecerDatosRec(Object opcionUsuario, Recordatorio recordatorioAct) {
        Function<String, String> pedirDatoUsuario = Vista::vistaModificarDato;
        String datoNuevo;

        if (opcionUsuario == null) {
            return;
        }
        switch ((String) opcionUsuario) {
            case "Titulo" -> {
                datoNuevo = verificarDatoNuevo(pedirDatoUsuario.apply("Modificar titulo"), recordatorioAct.obtenerNombre());
                recordatorioAct.modificarNombre(datoNuevo);
            }
            case "Descripcion" -> {
                datoNuevo = verificarDatoNuevo(pedirDatoUsuario.apply("Modificar descripcion"), recordatorioAct.obtenerDescripcion());
                recordatorioAct.modificarDescripcion(datoNuevo);
            }
            case "Agregar alarma" -> crearAlarma(recordatorioAct);
            case "Cambiar estado" -> recordatorioAct.cambiarCompletada();
            case "Agregar repeticion" -> {
                Evento repetible = (Evento) recordatorioAct;
                agregarRepeticion(repetible, pedirDatoUsuario);
            }
            case "Todo el dia" -> {
                if (vista.msjConfirmacion("¿Quiere que dure todo el dia?\nSi quiere que deje de durar todo el dia debe modificar la fecha de inicio")) {
                    recordatorioAct.establecerDiaCompleto();
                }
            }
            case "Fecha de inicio o fin" -> {
                if (vista.msjConfirmacion("Quiere agregar una fecha de inicio y fin\n(Si elige 'ACEPTAR' el evento o tarea que es de dia completo va a dejar de serlo")) {
                    establecerInicioyFinRec(recordatorioAct, pedirDatoUsuario);
                }
            }
            case "Eliminar" -> {
                vista.eliminarRecordatorio(recordatorioAct.obtenerId());
                this.calendario.eliminarRecordatorio(recordatorioAct);
            }
        }
    }

    private void agregarRepeticion(Evento recordatorioAct, Function<String,String> pedirDatoUsuario) {
        var opcionUsuario = vista.vistaAgregarRepeticion();
        if (opcionUsuario == null){
            return;
        }

        switch ((String) opcionUsuario) {
            case "Sin repeticion" -> {
                calendario.eliminarRepeticiones(recordatorioAct);
            }
            case "Repeticion diaria" -> {
                Supplier<Object> intervaloRepUsuario = () -> (Integer.parseInt(verificarDatoNuevo((pedirDatoUsuario
                        .apply("Intervalo - despues de cuantos dias queres que se repita el evento -")), "0")));
                var intervaloRep = verificarDatoUsuarioInt(intervaloRepUsuario);
                agregarLimiteRepDiaria(recordatorioAct, (Integer) intervaloRep, pedirDatoUsuario);
            }
        }
    }

    private void agregarLimiteRepDiaria(Recordatorio recordatorioAct, Integer intervaloRep, Function<String,String> pedirDatoUsuario) {
        var opcionUsuario = vista.vistaConsultarTipoLimiteRepDiaria();
        if (opcionUsuario == null){
            return;
        }
        var evento = (Evento)recordatorioAct;
        switch ((String) opcionUsuario) {
            case "Una fecha limite" -> {
                vista.establecerFechaAMod("limite de la repeticion");
                var fechaLimite = verificarDatoUsuarioInt(Vista::vistaModificarFecha);
                calendario.agregarRepeticiones(evento, Frecuencia.Diaria, Limite.FechaMax);
                calendario.modificarRepeticionesFechaLimite(evento, (LocalDateTime)fechaLimite);
            }
            case "despues de cierta cantidad de repeticiones" -> {
                Supplier<Object> iteracionesUsuario = () -> (Integer.parseInt(verificarDatoNuevo((pedirDatoUsuario
                        .apply("Coloca la cantidad de repeticiones deseadas")), "0")));
                var iteracionesRep = verificarDatoUsuarioInt(iteracionesUsuario);
                calendario.agregarRepeticiones(evento, Frecuencia.Diaria, Limite.Iteraciones);
                calendario.modificarRepeticionesIteraciones(evento, (Integer)iteracionesRep);
            }
        }
        calendario.modificarRepeticionesIntervalo(evento, intervaloRep);
    }

    private void establecerInicioyFinRec(Recordatorio recordatorioAct, Function<String,String> pedirDatoUsuario) {
        do {
            var opcionUsuario = vista.vistaAgregarFechaIniYFin(recordatorioAct);
            if (opcionUsuario == null) {
                return;
            }
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
        } while(vista.msjConfirmacion("¿Quiere modificar algo mas?"));
    }

}