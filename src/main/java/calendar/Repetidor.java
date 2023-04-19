package calendar;

import java.time.LocalDateTime;
import java.util.List;

public class Repetidor {

    private final Limite limite;
    private final Frecuencia frecuencia;
    private LocalDateTime ultConsulta;

    public Repetidor(Limite limite, Frecuencia frecuencia) {
        this.limite = limite;
        this.frecuencia = frecuencia;
    }

    public List<LocalDateTime> FuturasRepeticiones(LocalDateTime consulta){
        return null;
    }


    public Limite getLimite() {
        return limite;
    }
}
