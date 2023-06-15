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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
    private String rangoAct;
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

    private void cargarInterfaz() {
        List<Recordatorio> recordatorios = this.calendario.obtenerRecordatorios();
        for (Recordatorio recordatorio : recordatorios) {
            if (recordatorio.obtenerTipo().equals("Evento")) {
                crearVistaEvento(recordatorio.obtenerId());
            } else {
                crearVistaTarea(recordatorio.obtenerId());
            }
        }
    }

    public void crearVista(Recordatorio recordatorioAct) {
        String color = recordatorioAct.obtenerTipo().equals("Evento") ? this.colorEvento : this.colorTarea;
        Button botonRecordatorio = new Button();
        botonRecordatorio.setText("");
        botonRecordatorio.setGraphic(datosRecordatorios(recordatorioAct));
        botonRecordatorio.setStyle(color);
        botonRecordatorio.setId(Integer.toString(recordatorioAct.obtenerId()));
        botonRecordatorio.setOnAction(this.escuchaPersonalizarRec);
        contenedorRecordatorios.getChildren().add(botonRecordatorio);
    }

    private int crearRecordatorio(String tipo) {
        LocalDateTime fechaHoraDefault = LocalDateTime.now();
        return tipo.equals("Evento") ? this.calendario.crearEvento(fechaHoraDefault, 0, 0) : this.calendario.crearTarea(fechaHoraDefault, 0, 0);
    }

    private void crearVista(String color, int id) {
        Recordatorio recordatorioAct = this.calendario.obtenerRecordatorio(id);
        Button botonRecordatorio = new Button();
        botonRecordatorio.setText("");
        botonRecordatorio.setGraphic(datosRecordatorios(recordatorioAct));
        botonRecordatorio.setStyle(color);
        botonRecordatorio.setId(Integer.toString(recordatorioAct.obtenerId()));
        botonRecordatorio.setOnAction(this.escuchaPersonalizarRec);
        contenedorRecordatorios.getChildren().add(botonRecordatorio);
    }

    public String vistaAgregarEfecto() {
        TextInputDialog td = new TextInputDialog();
        td.setHeaderText("Elige un efecto entre los que se ven en pantalla - 'Notificacion', 'Sonido', 'Email' ");
        td.showAndWait();

        return td.getResult();
    }

    public Object vistaAgregarIntervalo() {
        String[] opcionesRec = {"Min", "Horas", "Dias", "Semanas"};
        var personalizarRecIntervalo = new ChoiceDialog("selecciona", opcionesRec);

        personalizarRecIntervalo.setTitle("Agregar intervalo");
        personalizarRecIntervalo.setHeaderText("Elegi el intervalo deseado");
        personalizarRecIntervalo.setContentText("Intervalos");
        personalizarRecIntervalo.showAndWait();

        return personalizarRecIntervalo.getSelectedItem();
    }

    public MenuItem obtenerRecAgregado(ActionEvent recSeleccionado) {
        return (MenuItem) recSeleccionado.getSource();
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

    public Object vistaPersonalizarRec(Recordatorio recordatorioAct) {
        String[] opcionesRec = opcionesModificarRec(recordatorioAct);
        var personalizarRec = new ChoiceDialog("seleccionar", opcionesRec);
        personalizarRec.setTitle("Personalizar Recordatorio");
        personalizarRec.setContentText("Modificar");
        Label label = new Label();
        label.setGraphic(datosCompletosRecordatorio(recordatorioAct));
        personalizarRec.setHeaderText(null);
        personalizarRec.getDialogPane().setHeader(label);
        personalizarRec.getDialogPane().setPrefWidth(300);
        personalizarRec.showAndWait();
        Object eleccionUsuario = personalizarRec.getResult();

        return eleccionUsuario == null || eleccionUsuario.equals("selecciona") ? null : eleccionUsuario;
    }

    public String[] opcionesModificarRec(Recordatorio recordatorio) {
        String opcionesRec = "Titulo,Descripcion,Todo el dia,Fecha de inicio o fin,Agregar alarma,";
        return recordatorio.obtenerTipo().equals("Evento") ? opcionesRec.concat("Agregar repeticion").split(",")
                : opcionesRec.concat("Cambiar estado").split(",");
    }

    public void crearVistaEvento(int id) {
        crearVista("-fx-background-color:pink;", id);
    }

    public void crearVistaTarea(int id) {
        crearVista("-fx-background-color:skyBlue;", id);
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
        info.getChildren().addAll(descripcion, infoAlarmas);
        return info;
    }

    private String[] mostrarAlarmas(List<Alarma> alarmas) {
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
                LocalDateTime tiempoAct = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
                calendario.obtenerRecordatorios().stream()
                        .flatMap(recordatorio -> recordatorio.obtenerAlarmas().stream())
                        .filter(alarma -> !alarma.yaSono() && alarma.obtenerfechaHora().equals(tiempoAct))
                        .forEach(Vista::lanzarAlarma);
            }
        };
        timer.start();
    }

    public Object vistaAgregarFechaIniYFin(Recordatorio recordatorio) {
        String opcionesRec = "Fecha de inicio,";

        var personalizarAlarmaIntervalo = new ChoiceDialog("seleccionar", recordatorio.obtenerTipo().equals("Evento") ? opcionesRec.concat("Agregar duracion").split(",")
                : opcionesRec.concat("Fecha de inicio").split(","));

        personalizarAlarmaIntervalo.setTitle("Modificar fecha de inicio y fin/duracion");
        personalizarAlarmaIntervalo.setHeaderText("Elegi la fecha a modificar");
        personalizarAlarmaIntervalo.setContentText("Opciones");
        personalizarAlarmaIntervalo.showAndWait();

        Object eleccionUsuario = personalizarAlarmaIntervalo.getResult();

        return eleccionUsuario==null || eleccionUsuario.equals("selecciona")
                ? null : eleccionUsuario;
    }

    public void verEventosPorRangoFechas(Map<LocalDateTime, HashSet<Integer>> codigosOrdenados){
        contenedorRecordatorios.getChildren().clear();
        for (Map.Entry<LocalDateTime, HashSet<Integer>> entry : codigosOrdenados.entrySet()) {
            HashSet<Integer> codigos = entry.getValue();
            for (var codigo: codigos){
                crearVista(calendario.obtenerRecordatorio(codigo));
            }
        }
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

}
