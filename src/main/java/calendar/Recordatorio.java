package calendar;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

abstract class Recordatorio {

    protected String nombre;
    protected String descripcion = "Sin descripcion";
    protected LocalDateTime inicio;
    protected Integer horas;
    protected Integer minutos;
    protected List<Alarma> alarmas = new ArrayList<>();

    public Recordatorio(LocalDateTime inicio,  Integer horas, Integer minutos){
        this.inicio = inicio;
        this.horas = horas;
        this.minutos = minutos;
    }

    public void modificarNombre(String nuevoNombre){
        this.nombre = nuevoNombre;
        modificarDatosDeAlarmas(nuevoNombre, this.descripcion, this.inicio);
    }

    public void modificarDescripcion(String nuevaDescripcion){
        this.descripcion = nuevaDescripcion;
        modificarDatosDeAlarmas(this.nombre, nuevaDescripcion, this.inicio);
    }

    public void modificarInicio(LocalDateTime nuevoInicio){
        this.inicio = nuevoInicio;
        modificarDatosDeAlarmas(this.nombre, this.descripcion, nuevoInicio);
    }

    private void modificarDatosDeAlarmas(String nombre, String descripcion, LocalDateTime inicio){
        for (Alarma alarma : alarmas) {
            alarma.modificarNombre(nombre);
            alarma.modificarDescripcion(descripcion);
            alarma.modificarInicio(inicio);
        }
    }

    public void modificarAlarmaFechaHoraAbs(Integer idAlarma, LocalDateTime fechaHoraAbs){
        if (verificarRepeticion() ){
            alarmas.get(idAlarma).establecerFechaHoraAbsRepeticiones(fechaHoraAbs);
        } else {
            alarmas.get(idAlarma).establecerFechaHoraAbs(fechaHoraAbs);
        }
    }

    public void modificarAlarmaIntervalo(Integer idAlarma, Integer min, Integer horas, Integer dias, Integer semanas){
        alarmas.get(idAlarma).establecerIntervalo(min, horas, dias, semanas, verficarDiaCompleto());
    }

    public void modificarAlarmaEfecto(Integer idAlarma, AlarmaEfectos efecto){
        alarmas.get(idAlarma).establecerEfecto(efecto);
    }

    public LocalDateTime verFinal() {
        return this.inicio.plusHours(horas).plusMinutes(minutos);
    }
    public boolean verficarDiaCompleto(){
        return (inicio.getHour() == 0) && (horas == 24) && (minutos == 0);
    }

    public boolean verificarRepeticion(){
        return false;
    }

    public String obtenerNombre(){
        return nombre;
    }
    public String obtenerDescripcion(){
        return descripcion;
    }
    public LocalDateTime obtenerInicio() { return inicio; }

    public void agregarAlarma(Alarma alarma){
        this.alarmas.add(alarma);
    }

    public Alarma obtenerAlarma(Integer idAlarma){
        return alarmas.get(idAlarma);
    }

    public void establecerDiaCompleto(){
        this.inicio = LocalDateTime.of(inicio.getYear(), inicio.getMonthValue(), inicio.getDayOfMonth(), 0, 0);
        this.horas = 24;
        this.minutos = 0;
        //lo que hace calendar: si era un evento que no es dia completo y tenia una alarma cuando se lo establece como dia completo se elimina la alarma puesta y se debe volver a crear una alarma
    }
}
