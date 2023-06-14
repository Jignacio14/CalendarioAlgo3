package Controlador;

import java.time.LocalDateTime;

public enum Avanzador {

    Diario(1){

    },
    Semanal(7){
    },

    Mensual(30){
        @Override
        public LocalDateTime[] avanzar(LocalDateTime desde, LocalDateTime hasta){
            LocalDateTime[] nuevoIncioFin = new LocalDateTime[2];
            int longNuevoMes = hasta.plusDays(1).toLocalDate().lengthOfMonth();
            nuevoIncioFin[0] = desde.plusDays(longNuevoMes);
            nuevoIncioFin[1] = hasta.plusDays(longNuevoMes);
            System.out.println("Desde: " + nuevoIncioFin[0] + " -> " + nuevoIncioFin[1]);
            return nuevoIncioFin;
        }

        @Override
        public LocalDateTime[] regresar(LocalDateTime desde, LocalDateTime hasta){
            LocalDateTime[] nuevoIncioFin = new LocalDateTime[2];
            int longNuevoMes = desde.minusDays(1).toLocalDate().lengthOfMonth();
            nuevoIncioFin[0] = desde.minusDays(longNuevoMes);
            nuevoIncioFin[1] = hasta.minusDays(longNuevoMes);
            System.out.println("Desde: " + nuevoIncioFin[0] + " -> " + nuevoIncioFin[1]);
            return nuevoIncioFin;
        }
    };

    private final Integer porDefecto;
    Avanzador(Integer base){
        porDefecto = base;
    }

    public LocalDateTime[] avanzar(LocalDateTime desde, LocalDateTime hasta){
        LocalDateTime[] nuevoIncioFin = new LocalDateTime[2];
        nuevoIncioFin[0] = desde.plusDays(porDefecto);
        nuevoIncioFin[1] = hasta.plusDays(porDefecto);
        System.out.println("Desde: " + nuevoIncioFin[0] + " -> " + nuevoIncioFin[1]);
        return nuevoIncioFin;
    }

    public LocalDateTime[] regresar(LocalDateTime desde, LocalDateTime hasta){
        LocalDateTime[] nuevoIncioFin = new LocalDateTime[2];
        nuevoIncioFin[0] = desde.minusDays(porDefecto);
        nuevoIncioFin[1] = hasta.minusDays(porDefecto);
        System.out.println("Desde: " + nuevoIncioFin[0] + " -> " + nuevoIncioFin[1]);
        return nuevoIncioFin;
    }

}
