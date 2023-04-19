package calendar;

import java.time.LocalDateTime;

public enum Limite {
    Iteraciones(LocalDateTime.now(), 5){
        @Override
        public void setFechaLimite(LocalDateTime fecha) {}
        @Override
        public void setIteraciones(Integer iteraciones) {this.iteraciones = iteraciones;}
        @Override
        public boolean verificarProximasIteraciones(LocalDateTime fecha) {
            return iteraciones > 0;
        }
        @Override
        public void ajustarIteracion() {
            iteraciones -= 1;
        }
    },
    FechaMax(LocalDateTime.now(), 5){
        @Override
        public void setFechaLimite(LocalDateTime fecha) {this.fechaLimite = fecha;}
        @Override
        public void setIteraciones(Integer iteraciones) {}

        @Override
        public boolean verificarProximasIteraciones(LocalDateTime fecha) {
            return fecha.isBefore(fechaLimite);
        }
        @Override
        public void ajustarIteracion() {}
    },
   SinLimite(LocalDateTime.MAX, 5){
        @Override
        public void setFechaLimite(LocalDateTime fecha) {this.fechaLimite = LocalDateTime.MAX;}
        @Override
        public void setIteraciones(Integer iteraciones) {}
       @Override
       public boolean verificarProximasIteraciones(LocalDateTime fecha) {
           return true;
       }
       @Override
       public void ajustarIteracion() {}

   };

    protected LocalDateTime fechaLimite;
    protected Integer iteraciones;

    Limite(LocalDateTime fecha, Integer iteraciones){
        this.fechaLimite = fecha;
        this.iteraciones = iteraciones;
    }
    public abstract void setFechaLimite(LocalDateTime fecha);
    public abstract void setIteraciones(Integer iteraciones);
    public abstract boolean verificarProximasIteraciones(LocalDateTime fecha);
    public abstract void ajustarIteracion();

}
