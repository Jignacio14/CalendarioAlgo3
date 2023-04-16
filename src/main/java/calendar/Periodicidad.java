package calendar;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Periodicidad {

    /*private boolean dia;
    private boolean semana;
    private boolean mes;
    private boolean anual;*/

    //private boolean noSeRepite;
    //private enum Terminar{NUNCA, FECHA, OCURRENCIAS}

    //enum tipos {DIA, SEMANA, MES, ANUAL} -> ver si en vez de recibir el tipo de repeticion por string que sea un enum

    /* una forma de hacer el constructor pero como el profe dijo que "En esta etapa pueden asumir que todos los datos de entrada son válidos" no va a ser necesario ver el tipo de repeticion por separado
    public Repetir(boolean dia, boolean semana, boolean mes, boolean anual, int cantidad, String condicionDeFin) {
        this.dia = dia;
        this.semana = semana;
        this.mes = mes;
        this.anual = anual;
        //this.noSeRepite = noSeRepite;
        this.cantidad = verificarCantidad(cantidad);
        this.condicionDeFin = condicionDeFin;
    }*/

    private int cantidad;
    private String condicionDeFin;
    private String tipoRepeticion;

    public Periodicidad(String tipoRepeticion, int cantidad, String condicionDeFin) {
        this.tipoRepeticion = tipoRepeticion;
        //this.noSeRepite = noSeRepite;
        this.cantidad = verificarCantidad(cantidad);
        this.condicionDeFin = condicionDeFin;
    }

    public int verificarCantidad(int cantidad) {
        return (cantidad > 0 ? cantidad : -1); // el -1 en forma de error ya que si el usuario elijio que se repita la cantidad debe ser mayor a 1 sino hay que lanzar un error
    }

    public void modificarDatos(String tipoRepeticion, int cantidad, String condicionDeFin){
        this.tipoRepeticion = tipoRepeticion;
        this.cantidad = verificarCantidad(cantidad);
        this.condicionDeFin = condicionDeFin;
    }

    public ArrayList<LocalDateTime> calcularDiasRepetidos(){
        // aca se va a usar el atributo condicionDeFin:
        // -> NUNCA: se hace metodo por demanda
        // -> DIA ESPECIFICO: se calcula la cantidad de dias que van a ser
        // -> OCURRENCIAS: "x" cantidad de repeticiones

        // se van a usar los atributos tipoRepeticion y cantidad:
        // -> por dia (cada "cantidad" dias)
        // -> por semana (cada "cantidad" semanas) --> aca hay que recibir tambien que dias de la semana
        // -> por mes (cada "cantidad" mes)
        // -> por año (cada "cantidad" años)

        return null;
    }

}
