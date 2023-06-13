package Modelo.calendar;

import Vista.RecordatorioVisitor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Recordatorio {

    protected String tipo;
    protected String nombre = "Sin titulo";
    protected String descripcion = "Sin descripcion";
    protected LocalDateTime inicio;
    protected Integer horas;
    protected Integer minutos;
    protected int id;
    protected List<Alarma> alarmas = new ArrayList<>();

    public Recordatorio(LocalDateTime inicio, Integer horas, Integer minutos) {
        this.inicio = inicio;
        this.horas = horas;
        this.minutos = minutos;
    }

    public abstract void aceptar(RecordatorioVisitor visitor);

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
        int idAlarma = alarmas.lastIndexOf(alarma);
        alarmas.get(idAlarma).establecerId(idAlarma);
        return idAlarma;
    }

    public void establecerAlarmas(List<Alarma> alarmas){
        this.alarmas.addAll(alarmas);
    }

    private void modificarDatosDeAlarmas(String nombre, String descripcion) {
        for (Alarma alarma : alarmas) {
            alarma.modificarNombre(nombre);
            alarma.modificarDescripcion(descripcion);
        }
    }

    public void modificarAlarmaFechaHoraAbs(int idAlarma, LocalDateTime fechaHoraAbs) {
        if (verificarRepeticion()) {
            alarmas.get(idAlarma).establecerFechaHoraAbsRepeticiones(fechaHoraAbs);
        } else {
            alarmas.get(idAlarma).establecerFechaHoraAbs(fechaHoraAbs);
        }
    }

    public void modificarAlarmaIntervalo(int idAlarma, Integer min, Integer horas, Integer dias, Integer semanas) {
        alarmas.get(idAlarma).establecerIntervalo(min, horas, dias, semanas, verficarDiaCompleto());
    }

    public void modificarAlarmaEfecto(int idAlarma, AlarmaEfectos efecto) {
        alarmas.get(idAlarma).establecerEfecto(efecto);
    }

    public void eliminarAlarma(Alarma alarma) {
        var idAlarma = alarma.obtenerId();
        this.alarmas.set(idAlarma, null);
    }

    public void cambiarCompletada(){}

    public boolean verificarCompletada(){return false;}

    /* _________ GETTERS _________ */

    public String obtenerNombre() { return nombre; }

    public String obtenerDescripcion() { return descripcion; }

    public LocalDateTime obtenerInicio() { return inicio; }

    public Alarma obtenerAlarma(int idAlarma) { return alarmas.get(idAlarma); }

    public int obtenerId() { return this.id; }

    public String obtenerTipo() { return this.tipo; }

    public List<Alarma> obtenerAlarmas() { return this.alarmas; }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass()) {
            return false;
        }

        Recordatorio recordatorioAComparar = (Recordatorio) obj;
        return this.inicio.equals(recordatorioAComparar.inicio) &&
                this.horas.equals(recordatorioAComparar.horas) &&
                this.minutos.equals(recordatorioAComparar.minutos) &&
                this.nombre.equals(recordatorioAComparar.nombre) &&
                this.descripcion.equals(recordatorioAComparar.descripcion) &&
                this.id == recordatorioAComparar.id &&
                this.alarmas.equals(recordatorioAComparar.alarmas);
    }
}
