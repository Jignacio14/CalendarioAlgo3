package calendar;
import java.time.LocalDateTime;
import java.util.ArrayList;

abstract class Recordatorio {

    protected String nombre = "Nuevo recordatorio";
    protected String descripcion = "Sin descripcion";
    protected LocalDateTime inicio;
    protected Integer horas;
    protected Integer minutos;
    protected ArrayList<Alarma> alarmas = new ArrayList<>();

    public Recordatorio(LocalDateTime inicio,  Integer horas, Integer minutos){
        this.inicio = inicio;
        this.horas = horas;
        this.minutos = minutos;
    }

    public void modificarNombre(String nuevoNombre){
        this.nombre = nuevoNombre;
    }

    public void modificarDescripcion(String nuevaDescripcion){
        this.nombre = nuevaDescripcion;
    }

    public LocalDateTime verFinal() {
        return this.inicio.plusHours(horas).plusMinutes(minutos);
    }
    public boolean verficarDiaCompleto(){
        return (inicio.getHour() == 0) && (horas == 24);
    }

    public boolean verificarRepeticion(){
        return false;
    }

    public void agregarAlarma(LocalDateTime fechaHora, Alarma.Efecto efecto, int intervalo, Alarma.TipoIntervalo tipoIntervalo){
        Alarma alarmaNueva = new Alarma(fechaHora, efecto, intervalo, tipoIntervalo, this.inicio);
        alarmas.add(alarmaNueva);
    }

    public void modificarAlarma(int idAlarma, LocalDateTime fechaHoraNueva, Alarma.Efecto efectoNuevo, int intervaloNuevo, Alarma.TipoIntervalo tipoIntervaloNuevo){
        alarmas.get(idAlarma).modificar(fechaHoraNueva, efectoNuevo, intervaloNuevo, tipoIntervaloNuevo);
    }

    public ArrayList<Alarma> obtenerAlarmas(){ return alarmas; }

    public Alarma obtenerAlarmaDeseada(int idAlarma){ return  alarmas.get(idAlarma); }

    public void eliminarAlarmaDeseada(int idAlarma) { alarmas.remove(idAlarma); }

    public void eliminarAlarmas(){ alarmas.clear(); }
}
