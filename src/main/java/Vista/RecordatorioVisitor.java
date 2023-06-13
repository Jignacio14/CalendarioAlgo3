package Vista;

import Modelo.calendar.Evento;
import Modelo.calendar.Tarea;

public interface RecordatorioVisitor {
    void visitarEvento(Evento e);
    void visitarTarea(Tarea t);
}
