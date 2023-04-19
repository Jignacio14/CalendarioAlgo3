package calendar;

import java.time.LocalDateTime;

public enum Limite {
    Iteraciones{
        @Override
        public void setFechaLimite(LocalDateTime fecha) {return;}
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
    FechaMax{
        @Override
        public void setFechaLimite(LocalDateTime fecha) {this.fechaLimite = fecha;}
        @Override
        public void setIteraciones(Integer iteraciones) {return;}

        @Override
        public boolean verificarProximasIteraciones(LocalDateTime fecha) {
            return fecha.isBefore(fechaLimite);
        }
        @Override
        public void ajustarIteracion() {}
    },
   SinLimite{
        @Override
        public void setFechaLimite(LocalDateTime fecha) {this.fechaLimite = LocalDateTime.MAX;}
        @Override
        public void setIteraciones(Integer iteraciones) {return;}
       @Override
       public boolean verificarProximasIteraciones(LocalDateTime fecha) {
           return true;
       }
       @Override
       public void ajustarIteracion() {}

   };



    protected LocalDateTime fechaLimite;
    protected Integer iteraciones;
    public abstract void setFechaLimite(LocalDateTime fecha);
    public abstract void setIteraciones(Integer iteraciones);
    public abstract boolean verificarProximasIteraciones(LocalDateTime fecha);
    public abstract void ajustarIteracion();

}
