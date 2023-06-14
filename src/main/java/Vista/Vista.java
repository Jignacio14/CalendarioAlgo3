package Vista;

import Modelo.calendar.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

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
    private EventHandler<ActionEvent> escuchaEvento;
    private String rangoAct;

    public Vista(Stage stage, Calendario calendario, EventHandler<ActionEvent> escucha) throws IOException {
        stage.setTitle("Calendario");

        FXMLLoader loader = new FXMLLoader(getClass().
                getResource("../estructura.fxml"));
        loader.setController(this);
        VBox contenedorCalendario = loader.load();

        this.escuchaEvento = escucha;
        this.calendario = calendario;

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

    private void agregarRecordatorios() {
        agregarEvento.setOnAction(evento -> {
            int id = crearRecordatorio("Evento");
            crearVistaEvento(id);
        });

        agregarTarea.setOnAction(evento -> {
            int id = crearRecordatorio("Tarea");
            crearVistaTarea(id);
        });
    }

    private void verCalendarioPorRango() {

        rangoDia.setOnAction(evento->{
            System.out.println("ver por dia");
            this.rangoAct = "dia";
        });
        rangoSemana.setOnAction(evento->{
            System.out.println("ver por semana");
            this.rangoAct = "semana";
        });
        rangoMes.setOnAction(evento->{
            System.out.println("ver por mes");
            this.rangoAct = "mes";
        });

        verRangoAntSig();
    }

    private void verRangoAntSig() {
        antRango.setOnAction(event -> {
            System.out.println("Ant");
            System.out.println(this.rangoAct);
        });

        sigRango.setOnAction(event -> {
            System.out.println("Sig");
            System.out.println(this.rangoAct);
        });
    }

    /*
    public void Vista(List<Recordatorio> recordatorios) {
        for (var recordatorio : recordatorios) {
            recordatorio.aceptar( new RecordatorioVisitor() {
                @Override void visitarEvento(Evento e) {
                    this.children().add(new VistaDeEvento(e));
                }
                @Override void visitarTarea(Tarea t) {
                    this.children().add(new VistaDeTarea(t));
                }
            });
        }
    } */

    private void cargarInterfaz() {
        List<Recordatorio> recordatorios = this.calendario.obtenerRecordatorios();
        for (Recordatorio recordatorio : recordatorios) {
            if (recordatorio.obtenerTipo().equals("Evento")){
                crearVistaEvento(recordatorio.obtenerId());
            }else {
                crearVistaTarea(recordatorio.obtenerId());
            }
        }
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
        botonRecordatorio.setOnAction(this.escuchaEvento);
        contenedorRecordatorios.getChildren().add(botonRecordatorio);
    }

    public Object vistaAgregarEfecto() {
        String[] opcionesRec = {"Notificacion", "Sonido", "Email"};
        var personalizarAlarmaEfecto = new ChoiceDialog("selecciona", opcionesRec);

        personalizarAlarmaEfecto.setTitle("Agregar efecto");
        personalizarAlarmaEfecto.setHeaderText("Elegi el efecto deseado");
        personalizarAlarmaEfecto.setContentText("Efectos");

        personalizarAlarmaEfecto.showAndWait();

        Object eleccionUsuario = personalizarAlarmaEfecto.getResult();

        return eleccionUsuario==null || eleccionUsuario.equals("selecciona")
                ? null : eleccionUsuario;
    }

    public Object vistaAgregarIntervalo() {
        String[] opcionesRec = {"Min", "Horas", "Dias", "Semanas"};
        var personalizarAlarmaIntervalo = new ChoiceDialog("selecciona", opcionesRec);

        personalizarAlarmaIntervalo.setTitle("Agregar intervalo");
        personalizarAlarmaIntervalo.setHeaderText("Elegi el intervalo deseado");
        personalizarAlarmaIntervalo.setContentText("Intervalos");
        personalizarAlarmaIntervalo.showAndWait();

        Object eleccionUsuario = personalizarAlarmaIntervalo.getResult();

        return eleccionUsuario==null || eleccionUsuario.equals("selecciona")
                ? null : eleccionUsuario;
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

    public void actualizarVistaRec(Button recordatorioAct, Recordatorio recordatorio){
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

        return personalizarRec.getSelectedItem();
    }

    private String[] opcionesModificarRec(Recordatorio recordatorio) {
        String opcionesRec = "Fecha de inicio,Fecha de fin,Titulo,Descripcion,Agregar alarma,Todo el dia";
        return recordatorio.obtenerTipo().equals("Evento") ? opcionesRec.concat(",Agregar repeticion").split(",")
                : opcionesRec.concat(",Tarea Completada").split(",");
    }

    public String vistaModificarDato(String datoAModificar){
        var modificar = new TextInputDialog();
        modificar.setHeaderText(datoAModificar);
        modificar.showAndWait();

        return modificar.getResult();
    }

    public void crearVistaEvento(int id) {
        crearVista("-fx-background-color:pink;", id);
    }

    public void crearVistaTarea(int id) {
        crearVista("-fx-background-color:skyBlue;", id);
    }

    public TextFlow datosRecordatorios(Recordatorio recordatorio){
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

        var info =  new TextFlow();
        info.setTextAlignment(TextAlignment.CENTER);
        info.getChildren().addAll(infoRec, infoDiaCom, infoTarCom);
        info.setPrefHeight(100);
        return info;
    }

    private TextFlow datosCompletosRecordatorio(Recordatorio recordatorio){
        var info = datosRecordatorios(recordatorio);
        info.setPrefHeight(300);
        info.setPrefWidth(300);
        Text descripcion = new Text("\n" + recordatorio.obtenerDescripcion());
        Text infoAlarmas = new Text("\n  Alarmas: " + ((recordatorio.obtenerAlarmas().isEmpty()) ? "Sin alarmas" : Arrays.toString(mostrarAlarmas(recordatorio.obtenerAlarmas()))) + "\n");
        info.getChildren().addAll(descripcion, infoAlarmas);
        return info;
    }

    private String[] mostrarAlarmas(List<Alarma> alarmas){
        String[] alarmasAMostrar = new String[alarmas.size()];
        for (int i = 0; i < alarmas.size(); i++) {
            alarmasAMostrar[i] = alarmas.get(i).toString();
        }
        return alarmasAMostrar;
    }

    private Text verificarCompletada(Boolean tareaCompletada){
        Text infoCom = new Text();
        if (tareaCompletada){
            infoCom.setText( "\nCompletada\n");
            infoCom.setFill(Color.GREEN);
            return infoCom;
        }
        infoCom.setText( "\nSin completar\n");
        infoCom.setFill(Color.RED);
        return infoCom;
    }

    public LocalDateTime vistaModificarInicio() {
        String opcionesInicio = "año,mes,dia,hora,minutos";
        String[] opcionesModInic = Arrays.stream(opcionesInicio.split(","))
                .map(s -> "Ingrese el/la nuevo/a " + s)
                .toArray(String[]::new);;

        int[] opcionesUsuario = new int[5];
        for (int i = 0; i < 5; i++) {
            opcionesUsuario[i] = Integer.parseInt(vistaModificarDato(opcionesModInic[i])) ;
        }

        return LocalDateTime.of(opcionesUsuario[0], opcionesUsuario[1], opcionesUsuario[2], opcionesUsuario[3], opcionesUsuario[4]);
    }
}

