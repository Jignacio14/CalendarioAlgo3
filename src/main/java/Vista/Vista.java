package Vista;

import Modelo.calendar.*;
import Modelo.calendar.Persistencia.PersistorJSON;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class Vista {
    @FXML
    private TilePane contenedorRecordatorios;
    @FXML
    private MenuItem agregarEvento;
    @FXML
    private MenuItem agregarTarea;

    private final Calendario calendario;
    //private Button botonRecordatorio;
    private String datoNuevo;
    private EventHandler<ActionEvent> escuchaEvento;

    public Vista(Stage stage, Calendario calendario) throws IOException {
        stage.setTitle("Calendario");

        FXMLLoader loader = new FXMLLoader(getClass().
                getResource("../estructura.fxml"));
        loader.setController(this);
        VBox contenedorCalendario = loader.load();


        this.calendario = calendario;
       /* if (!this.calendario.calendarioVacio()) {
            cargarInterfaz();
        }*/

        agregarEvento.setOnAction(evento -> {
            int id = crearRecordatorio("Evento");
            crearVistaEvento(id);
        });

        agregarTarea.setOnAction(evento -> {
            int id = crearRecordatorio("Tarea");
            crearVistaTarea(id);
        });


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


        Scene scene = new Scene(contenedorCalendario, 600, 480);
        stage.setScene(scene);
        stage.show();
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

    /*
    private void cargarInterfaz() {
        List<Recordatorio> recordatorios = this.calendario.obtenerRecordatorios();
        for (Recordatorio recordatorio : recordatorios) {
            if (recordatorio.obtenerTipo().equals("Evento")){
                crearVistaEvento(recordatorio.obtenerId());
            }else {
                crearVistaTarea(recordatorio.obtenerId());
            }
        }
    }*/

    private void guardarCalendario() throws IOException {
        if ( !this.calendario.calendarioVacio() ){
            var persistor = new PersistorJSON("./src/main/pruebaSerializador.json");
            this.calendario.guardar(persistor);
        }
    }

    private int crearRecordatorio(String tipo) {
        LocalDateTime fechaHoraDefault = LocalDateTime.now();
        return tipo.equals("Evento") ? this.calendario.crearEvento(fechaHoraDefault, 0, 0) : this.calendario.crearTarea(fechaHoraDefault, 0, 0);
    }

    private void crearVista(String color, int id) {
        Recordatorio recordatorioAct = this.calendario.obtenerRecordatorio(id);
        Button botonRecordatorio = new Button(datosRecordatorios(recordatorioAct));
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
        recordatorioAct.setText(datosRecordatorios(recordatorio));
    }

    public Object vistaPersonalizarRec(Recordatorio recordatorioAct) {
        String[] opcionesRec = opcionesModificarRec(recordatorioAct);
        var personalizarRec = new ChoiceDialog("selecciona", opcionesRec);

        personalizarRec.setTitle("Personalizar Recordatorio");
        personalizarRec.setHeaderText(datosCompletosRecordatorio(recordatorioAct));
        personalizarRec.setContentText("Modificar");
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
        this.datoNuevo = modificar.getResult();

        return modificar.getResult();
    }

    public void crearVistaEvento(int id) {
        crearVista("-fx-background-color:pink;", id);
    }

    public void crearVistaTarea(int id) {
        crearVista("-fx-background-color:skyBlue;", id);
    }

    public String datosRecordatorios(Recordatorio recordatorio){
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm:ss");
        return recordatorio.obtenerTipo() + "\n" +
                recordatorio.obtenerNombre() + "\n" +
                "Inicio: " + formato.format(recordatorio.obtenerInicio()) + "\n" +
                "Fin: " + formato.format(recordatorio.verFinal()) + " " + ((recordatorio.verficarDiaCompleto()) ? " -- Dia completo --" : "") + "\n" +
                (recordatorio.obtenerTipo().equals("Tarea") ? verificarCompletada(recordatorio.verificarCompletada()) : "");
    }

    private String datosCompletosRecordatorio(Recordatorio recordatorio){
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm:ss");
        return recordatorio.obtenerTipo() + "\n" +
                recordatorio.obtenerNombre() + "\n" +
                "Inicio: " + formato.format(recordatorio.obtenerInicio()) + "\n" +
                "Fin: " + formato.format(recordatorio.verFinal()) + " " + ((recordatorio.verficarDiaCompleto()) ? " -- Dia completo --" : "") + "\n" +
                recordatorio.obtenerDescripcion() + "\n" +
                "Alarmas: " + Arrays.toString(alarmas(recordatorio)) + "\n" +
                (recordatorio.obtenerTipo().equals("Tarea") ? verificarCompletada(recordatorio.verificarCompletada()) : "");
    }

    private String[] alarmas(Recordatorio recordatorio){
        String[] alarmas = new String[recordatorio.obtenerAlarmas().size()];
        for (int i = 0; i < recordatorio.obtenerAlarmas().size(); i++) {
            alarmas[i] = recordatorio.obtenerAlarma(i).toString();
        }
        return alarmas;
    }

    private String verificarCompletada(Boolean tareaCompletada){
        if (tareaCompletada){
            return "Completada";
        }
        return "Sin completar";
    }
}

