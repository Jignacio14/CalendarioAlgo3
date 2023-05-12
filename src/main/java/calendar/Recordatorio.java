package calendar;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Recordatorio {

    protected String nombre = "Sin titulo";
    protected String descripcion = "Sin descripcion";
    protected LocalDateTime inicio;
    protected Integer horas;
    protected Integer minutos;
    protected int id;
    private final List<Alarma> alarmas = new ArrayList<>();

    public Recordatorio(LocalDateTime inicio, Integer horas, Integer minutos) {
        this.inicio = inicio;
        this.horas = horas;
        this.minutos = minutos;
    }

    public void modificarNombre(String nuevoNombre) {
        this.nombre = nuevoNombre;
        if (!(alarmas.isEmpty())) {
            modificarDatosDeAlarmas(nuevoNombre, this.descripcion);
        }
    }

    public void modificarDescripcion(String nuevaDescripcion) {
        this.descripcion = nuevaDescripcion;
        if (!(alarmas.isEmpty())) {
            modificarDatosDeAlarmas(this.nombre, nuevaDescripcion);
        }
    }

    public void modificarInicio(LocalDateTime nuevoInicio) {
        this.inicio = nuevoInicio;
        alarmas.clear();
    }

    public LocalDateTime verFinal() { return this.inicio.plusHours(horas).plusMinutes(minutos); }

    public boolean verficarDiaCompleto() { return (inicio.getHour() == 0) && (horas == 24) && (minutos == 0); }

    public boolean verificarRepeticion() { return false; }

    public void establecerDiaCompleto() {
        this.inicio = LocalDateTime.of(inicio.getYear(), inicio.getMonthValue(), inicio.getDayOfMonth(), 0, 0);
        this.horas = 24;
        this.minutos = 0;
    }

    public void establecerId(int id) { this.id = id; }

    /* _________ ALARMA _________ */

    public void almacenarAlarma(Alarma alarma) {
        int posicionVacia = this.alarmas.indexOf(null);
        if (this.alarmas.isEmpty() || posicionVacia < 0) {
            this.alarmas.add(alarma);
        } else {
            this.alarmas.set(posicionVacia, alarma);
        }
    }

    public int agregarAlarma(Alarma alarma) {
        almacenarAlarma(alarma);
        int idAlarma = alarmas.indexOf(alarma);
        alarmas.get(idAlarma).establecerId(idAlarma);
        return idAlarma;
    }

    private void modificarDatosDeAlarmas(String nombre, String descripcion) {
        for (Alarma alarma : alarmas) {
            alarma.modificarNombre(nombre);
            alarma.modificarDescripcion(descripcion);
        }
    }

    public void modificarAlarmaFechaHoraAbs(Integer idAlarma, LocalDateTime fechaHoraAbs) {
        if (verificarRepeticion()) {
            alarmas.get(idAlarma).establecerFechaHoraAbsRepeticiones(fechaHoraAbs);
        } else {
            alarmas.get(idAlarma).establecerFechaHoraAbs(fechaHoraAbs);
        }
    }

    public void modificarAlarmaIntervalo(Integer idAlarma, Integer min, Integer horas, Integer dias, Integer semanas) {
        alarmas.get(idAlarma).establecerIntervalo(min, horas, dias, semanas, verficarDiaCompleto());
    }

    public void modificarAlarmaEfecto(Integer idAlarma, AlarmaEfectos efecto) {
        alarmas.get(idAlarma).establecerEfecto(efecto);
    }

    public void eliminarAlarma(Alarma alarma) {
        var idAlarma = alarma.obtenerId();
        this.alarmas.set(idAlarma, null);
    }

    /* _________ GETTERS _________ */

    public String obtenerNombre() { return nombre; }

    public String obtenerDescripcion() { return descripcion; }

    public LocalDateTime obtenerInicio() { return inicio; }

    public Alarma obtenerAlarma(Integer idAlarma) { return alarmas.get(idAlarma); }

    public List<Alarma> obtenerAlarmas() { return alarmas; }

    public int obtenerId(){ return this.id; }
}
