package calendar;

public enum AlarmaEfectos {
    EMAIL("Email"),
    NOTIFICACION("Notificacion"),
    SONIDO("Sonido");

    private final String efecto;

    AlarmaEfectos(String efecto){
        this.efecto = efecto;
    }

    public String lanzarEfecto() {
        return efecto;
    }
}
