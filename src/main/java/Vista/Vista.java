package Vista;

import Modelo.calendar.*;
import Modelo.calendar.Persistencia.PersistorJSON;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import java.util.Arrays;
import java.util.List;

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
    //private Button botonRecordatorio;
    private String datoNuevo;
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

       /* seria algo como el metodo por demanda de los recordatorios pero con las alarmas
       a ese metodo en vez de mandarle ver los recordatorios de "x" dia/semana/mes
       se le manda ver las alarmas de dia y hora actual

        Label label = new Label();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                LocalDateTime tiempo = LocalDateTime.now();
                Instant startTime = Instant.now();
                label.setText("Tiempo: " + startTime );
            }
        };
        timer.start();
        contenedorCalendario.getChildren().add(label);*/


        Scene scene = new Scene(contenedorCalendario, 800, 600);
        stage.setScene(scene);
        stage.show();
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

    public void registrarEscucha(EventHandler<ActionEvent> escucha) {
        this.escuchaEvento = escucha;
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

    /*
    //esto va en calendario
    private List<Alarma> pedirAlarmas(LocalDateTime tiempoActual) {
        List<Alarma> alarmasASonar = new ArrayList<>();

        for (Recordatorio recordatorio: recordatorios) {
            if (recordatorio)
        }
    }*/

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

    /*public void personalizarRec(Object opcionUsuario){
    }*/

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

    public Integer vistaCantIntervalo() {
        var modificarCant = new TextInputDialog();
        modificarCant.setHeaderText("Coloca la cantidad de intervalo a aplicar: ");
        modificarCant.showAndWait();

        return Integer.parseInt(modificarCant.getResult());
    }

    /*public void establecerRecSeleccionado(ActionEvent recSeleccionado){
        this.idRecordatorioAct =  ((Button) recSeleccionado.getSource()).getId();
        this.recordatorioAct = (Button) recSeleccionado.getSource();
    }*/

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
        return recordatorio.obtenerTipo().equals("Evento") ? new String[]{"Titulo", "Descripcion", "Agregar alarma", "Todo el dia" , "Agregar repeticion"}
                : new String[]{"Titulo", "Descripcion", "Agregar alarma", "Todo el dia", "Tarea Completada"};
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
}

