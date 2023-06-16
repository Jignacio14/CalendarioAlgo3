package Vista;

import Modelo.calendar.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.application.Platform;
import javafx.animation.AnimationTimer;


public class Vista {
    @FXML
    private TilePane contenedorRecordatorios;
    @FXML
    private MenuItem agregarEvento;
    @FXML
    private MenuItem agregarTarea;
    @FXML
    private MenuItem rangoDia;
    @FXML
    private MenuItem rangoSemana;
    @FXML
    private MenuItem rangoMes;

    @FXML
    private Button antRango;
    @FXML
    private Button sigRango;

    private final Calendario calendario;
    private final EventHandler<ActionEvent> escuchaPersonalizarRec;
    private final EventHandler<ActionEvent> escuchaAgregarRec;

    private final EventHandler<ActionEvent> verPorRango;
    private EventHandler<ActionEvent> antSig;
    final String colorEvento = "-fx-background-color:pink;";
    final String colorTarea = "-fx-background-color:skyBlue;";

    private static String fechaAMod;



    public Vista(Stage stage, Calendario calendario, EventHandler<ActionEvent> escuchaPersonalizarRec, EventHandler<ActionEvent> generarAntSig, EventHandler<ActionEvent> verPorRango, EventHandler<ActionEvent> escuchaAgregarRec) throws IOException {
        stage.setTitle("Calendario");
        FXMLLoader loader = new FXMLLoader(getClass().
                getResource("../estructura.fxml"));
        loader.setController(this);
        VBox contenedorCalendario = loader.load();

        this.verPorRango = verPorRango;
        this.antSig = generarAntSig;

        this.calendario = calendario;
        this.escuchaPersonalizarRec = escuchaPersonalizarRec;
        this.escuchaAgregarRec = escuchaAgregarRec;

        if (!this.calendario.calendarioVacio()) {
            cargarInterfaz();
        }

        agregarRecordatorios();
        verCalendarioPorRango();
        verificarProximasAlarmas();

        Scene scene = new Scene(contenedorCalendario, 800, 600);
        stage.setScene(scene);
        stage.show();

    }


    private void agregarRecordatorios() {
        agregarEvento.setOnAction(this.escuchaAgregarRec);
        agregarTarea.setOnAction(this.escuchaAgregarRec);
    }

    private void verCalendarioPorRango() {
        rangoDia.setOnAction(verPorRango);
        rangoSemana.setOnAction(verPorRango);
        rangoMes.setOnAction(verPorRango);
        verRangoAntSig();
    }

    public String obtenerOrigenVerRango(Object menuItem) {
        MenuItem menu = (MenuItem) menuItem;
        return menu.getId();
    }

    private void verRangoAntSig() {
        antRango.setOnAction(antSig);
        sigRango.setOnAction(antSig);
    }

    public String obtenerOrigenAntSig(Object button) {
        Button botonAntSig = (Button) button;
        return botonAntSig.getId();
    }

    public void cargarInterfaz() {
        List<Recordatorio> recordatorios = (this.calendario.obtenerRecordatorios()).stream()
                .filter(Objects::nonNull).toList();
        for (Recordatorio recordatorio : recordatorios) {
            if (recordatorio.obtenerTipo().equals("Evento") && ((Evento)recordatorio).verificarRepeticion()){
                crearVistaRepeticiones(recordatorio, recordatorio.obtenerInicio());
            } else {
                crearVista(recordatorio);
            }
        }
    }

    public void eliminarRecordatorio(int recordatorioEliminado){
        contenedorRecordatorios.getChildren().remove(recordatorioEliminado);
    }

    public void crearVistaRepeticiones(Recordatorio evento, LocalDateTime anioOriginalRec){

    }

    public void crearVista(Recordatorio recordatorioAct) {
        if (recordatorioAct==null){return;}
        String color = recordatorioAct.obtenerTipo().equals("Evento") ? this.colorEvento : this.colorTarea;
        Button botonRecordatorio = new Button();
        botonRecordatorio.setText("");
        botonRecordatorio.setGraphic(datosRecordatorios(recordatorioAct));
        botonRecordatorio.setStyle(color);
        botonRecordatorio.setId(Integer.toString(recordatorioAct.obtenerId()));
        botonRecordatorio.setOnAction(this.escuchaPersonalizarRec);
        contenedorRecordatorios.getChildren().add(botonRecordatorio);
    }


    public static Integer vistaCantIntervalo() {
        var modificarCant = new TextInputDialog();
        modificarCant.setHeaderText("Coloca la cantidad de intervalo a aplicar: ");
        modificarCant.showAndWait();
        return Integer.parseInt(modificarCant.getResult());
    }

    public Button obtenerRecSeleccionado(ActionEvent recSeleccionado) {
        return (Button) recSeleccionado.getSource();
    }

    public void actualizarVistaRec(Button recordatorioAct, Recordatorio recordatorio) {
        recordatorioAct.setGraphic(datosRecordatorios(recordatorio));
    }

    public MenuItem obtenerRecAgregado(ActionEvent recSeleccionado) {
        return (MenuItem) recSeleccionado.getSource();
    }

    public String[] opcionesModificarRec(Recordatorio recordatorio) {
        String opcionesRec = "Titulo,Descripcion,Todo el dia,Fecha de inicio o fin,Agregar alarma,Eliminar,";
        return recordatorio.obtenerTipo().equals("Evento") ? opcionesRec.concat("Agregar repeticion").split(",")
                : opcionesRec.concat("Cambiar estado").split(",");
    }

    public TextFlow datosRecordatorios(Recordatorio recordatorio) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm:ss");
        Text infoRec = new Text();
        infoRec.setText(recordatorio.obtenerTipo() + "\n" +
                recordatorio.obtenerNombre() + "\n" +
                "Inicio: " + formato.format(recordatorio.obtenerInicio()) + "\n" +
                "Fin: " + formato.format(recordatorio.verFinal()) + "\n");

        Text infoTarCom = ((recordatorio.obtenerTipo().equals("Tarea")) ? verificarCompletada(recordatorio.verificarCompletada()) : new Text(""));
        infoTarCom.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        Text infoDiaCom = new Text(((recordatorio.verficarDiaCompleto()) ? "\n-- Dia completo --\n" : ""));
        infoDiaCom.setFill(Color.BLUEVIOLET);
        infoDiaCom.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        var info = new TextFlow();
        info.setTextAlignment(TextAlignment.CENTER);
        info.getChildren().addAll(infoRec, infoDiaCom, infoTarCom);
        info.setPrefHeight(100);
        return info;
    }

    private TextFlow datosCompletosRecordatorio(Recordatorio recordatorio) {
        var info = datosRecordatorios(recordatorio);
        info.setPrefHeight(300);
        info.setPrefWidth(300);
        Text descripcion = new Text("\n" + recordatorio.obtenerDescripcion());
        Text infoAlarmas = new Text("\n  Alarmas: " + ((recordatorio.obtenerAlarmas().isEmpty()) ? "Sin alarmas" : Arrays.toString(mostrarAlarmas(recordatorio.obtenerAlarmas()))) + "\n");
        Text infoRepe = ((recordatorio.obtenerTipo().equals("Evento")) ? msjRepeticiones(((Evento)recordatorio).verificarRepeticion(), recordatorio) : new Text(""));
        infoRepe.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        info.getChildren().addAll(descripcion, infoAlarmas, infoRepe);
        return info;
    }

    private Text msjRepeticiones(boolean hayRepeticiones, Recordatorio recordatorio) {
        Text infoRep = new Text();
        LocalDateTime todasRep = LocalDateTime.now().plusYears(1);
        String textoRep = (hayRepeticiones ?  ("Cantidad total de repeticiones: " + (((Evento)recordatorio).verRepeticiones(todasRep).size())) : "Sin Repeticiones");
        infoRep.setText(textoRep);
        return infoRep;
    }

    private String[] mostrarAlarmas(List<Alarma> alarmas){

        String[] alarmasAMostrar = new String[alarmas.size()];
        for (int i = 0; i < alarmas.size(); i++) {
            alarmasAMostrar[i] = alarmas.get(i).toString();
        }
        return alarmasAMostrar;
    }

    private Text verificarCompletada(Boolean tareaCompletada) {
        Text infoCom = new Text();
        if (tareaCompletada) {
            infoCom.setText("\nCompletada\n");
            infoCom.setFill(Color.GREEN);

        } else {
            infoCom.setText("\nSin completar\n");
            infoCom.setFill(Color.RED);
        }
        return infoCom;
    }

    public static String vistaModificarDato(String datoAModificar){
        var modificar = new TextInputDialog();
        modificar.setHeaderText(datoAModificar);
        modificar.showAndWait();
        return modificar.getResult();
    }

    public static LocalDateTime vistaModificarFecha() {
        String opcionesInicio = "año,mes,dia,hora,minutos";
        String[] opcionesModInic = Arrays.stream(opcionesInicio.split(","))
                .map(s -> "Ingrese el/la " + s.toUpperCase() + " de " + fechaAMod.toUpperCase())
                .toArray(String[]::new);

        int[] opcionesUsuario = new int[5];
        for (int i = 0; i < 5; i++) {
            var opcionUsuario = vistaModificarDato(opcionesModInic[i]);
            if (opcionUsuario == null) {
                return null;
            }
            opcionesUsuario[i] = Integer.parseInt(opcionUsuario);
        }

        return LocalDateTime.of(opcionesUsuario[0], opcionesUsuario[1], opcionesUsuario[2], opcionesUsuario[3], opcionesUsuario[4]);
    }



    public void establecerFechaAMod(String fechaAMod) {
        Vista.fechaAMod = fechaAMod;
    }

    public void mensajeError() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Dato ingresado incorrecto");
            alert.setContentText("Por favor ingrese el dato correcto segun lo solicitado");

            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK);
        });
    }

    public boolean msjConfirmacion(String msjConfirmacion) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar operacion");
        alert.setHeaderText(msjConfirmacion);
        alert.showAndWait();
        return alert.getResult() == ButtonType.OK;
    }


    private void verificarProximasAlarmas() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Function<Recordatorio, Stream<Alarma>> pedirAlarmas = recordatorio -> {
                    if (recordatorio!=null){
                        return recordatorio.obtenerAlarmas().stream();
                    }
                    return Stream.empty();
                };

                LocalDateTime tiempoAct = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
                calendario.obtenerRecordatorios().stream()
                        .flatMap(pedirAlarmas)
                        .filter(alarma -> !alarma.yaSono() && alarma.obtenerfechaHora().equals(tiempoAct))
                        .forEach(Vista::lanzarAlarma);
            }
        };
        timer.start();
    }

    public void verEventosPorRangoFechas(Map<LocalDateTime, HashSet<Integer>> codigosOrdenados){
        contenedorRecordatorios.getChildren().clear();
        for (Map.Entry<LocalDateTime, HashSet<Integer>> entry : codigosOrdenados.entrySet()) {
            HashSet<Integer> codigos = entry.getValue();
            for (var codigo: codigos){
                crearVista(calendario.obtenerRecordatorio(codigo), entry.getKey());
            }
        }
    }

    public void crearVista(Recordatorio recordatorioAct, LocalDateTime inicio) {
        String color = recordatorioAct.obtenerTipo().equals("Evento") ? this.colorEvento : this.colorTarea;
        Button botonRecordatorio = new Button();
        botonRecordatorio.setText("");
        botonRecordatorio.setGraphic(datosRecordatorios(recordatorioAct, inicio));
        botonRecordatorio.setStyle(color);
        botonRecordatorio.setId(Integer.toString(recordatorioAct.obtenerId()));
        botonRecordatorio.setOnAction(this.escuchaPersonalizarRec);
        contenedorRecordatorios.getChildren().add(botonRecordatorio);
    }

    public TextFlow datosRecordatorios(Recordatorio recordatorio, LocalDateTime inicio) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm:ss");
        Text infoRec = new Text();
        infoRec.setText(recordatorio.obtenerTipo() + "\n" +
                recordatorio.obtenerNombre() + "\n" +
                "Inicio: " + formato.format(inicio) + "\n" +
                "Fin: " +formato.format(recordatorio.verFinal(inicio))+ "\n");

        Text infoTarCom = ((recordatorio.obtenerTipo().equals("Tarea")) ? verificarCompletada(recordatorio.verificarCompletada()) : new Text(""));
        infoTarCom.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        Text repe = null;
        if (recordatorio.verificarRepeticion()){
            repe = new Text("Se repite");
        }
        Text infoDiaCom = new Text(((recordatorio.verficarDiaCompleto()) ? "\n-- Dia completo --\n" : ""));
        infoDiaCom.setFill(Color.BLUEVIOLET);
        infoDiaCom.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        var info = new TextFlow();
        info.setTextAlignment(TextAlignment.CENTER);
        info.getChildren().addAll(infoRec, infoDiaCom, infoTarCom, repe);
        info.setPrefHeight(100);
        return info;
    }

    private static void lanzarAlarma(Alarma alarma) {
        Platform.runLater(() -> {
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm:ss");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alarma " + alarma.obtenerTipoRec());
            alert.setHeaderText("Titulo: " + alarma.obtenerNombre());
            alert.setContentText("Descripcion: " + alarma.obtenerDescripcion() + "\n" +
                    "Fecha y Hora: " + formato.format(alarma.obtenerfechaHora()));

            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK);
        });
        alarma.actualizarSono();
    }

    public Object vistaAgregarEfecto() {
        String[] opcionesRec = {"Notificacion", "Sonido", "Email"};

        return vistaChoiceDialog(opcionesRec, "Crear Alarma", "Elegi el efecto deseado", null, null,0);
    }

    public Object vistaAgregarIntervalo() {
        String[] opcionesRec = {"Min", "Horas", "Dias", "Semanas"};

        return vistaChoiceDialog(opcionesRec, "Crear Alarma", "Elegi el intervalo deseado", null, null,0);
    }

    public Object vistaAgregarRepeticion() {
        String[] opcionesRepe = new String[]{"Sin repeticion", "Repeticion diaria"};

        return vistaChoiceDialog(opcionesRepe, "Agregar repeticion", "Elegi el tipo de repeticion deseado", null, null,0);
    }

    public Object vistaConsultarTipoLimiteRepDiaria() {
        String[] opcionesRepe = new String[]{"Una fecha limite"
                , "despues de cierta cantidad de repeticiones"};

        return vistaChoiceDialog(opcionesRepe, "Repetir evento hasta:", "Elegi el tipo de limite de la repeticion", null, null,0);
    }

    public Object vistaAgregarFechaIniYFin(Recordatorio recordatorio) {
        String opcionesRec = "Fecha de inicio,";
        String[] opciones = recordatorio.obtenerTipo().equals("Evento") ? opcionesRec.concat("Agregar duracion").split(",")
                : opcionesRec.concat("Fecha de inicio").split(",");

        return vistaChoiceDialog(opciones, "Modificar fecha de inicio y fin/duracion", "Elegi la fecha a modificar", null, null,0);
    }

    public Object vistaPersonalizarRec(Recordatorio recordatorioAct) {
        String[] opcionesRec = opcionesModificarRec(recordatorioAct);

        Label label = new Label();
        label.setGraphic(datosCompletosRecordatorio(recordatorioAct));

        return vistaChoiceDialog(opcionesRec, "Personalizar Recordatorio", null, "Modificar", label,300);
    }

    private Object vistaChoiceDialog(String[] opciones, String titulo, String descripcion, String tipoOpciones, Label datoDecorado, double ancho){
        var personalizarRec = new ChoiceDialog("seleccionar", opciones);

        personalizarRec.setTitle(titulo);
        personalizarRec.setHeaderText(descripcion);
        personalizarRec.setContentText(tipoOpciones);

        personalizarRec.getDialogPane().setHeader(datoDecorado);
        personalizarRec.getDialogPane().setPrefWidth(ancho);

        personalizarRec.showAndWait();

        Object eleccionUsuario = personalizarRec.getResult();

        return eleccionUsuario==null || eleccionUsuario.equals("selecciona")
                ? null : eleccionUsuario;
    }

}
