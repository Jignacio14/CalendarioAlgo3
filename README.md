# CalendarioAlgo3

## Participantes

Lucía Agha Zadeh Dehdeh - Padrón: 106905

José Ignacio Castro Martinez - Padrón: 106957

## Informe 
----

### Diseño general 
  
  El trabajo practico consta de cuatro modulos, tres correspondientes al diseño por el patròn MVC (Model, view,  controler) y otro modulo correspondiente a la logica de la persistencia del modelo. 
  
  #### Modelo
    
  En el modelo esta concentrada toda la logica de negocio utilizada para hacer funcionar el calendario correctamente además de la persistencia. Consiste en un conjunto de clases que aplican patrones de diseño para resolver efectivamente el problema encarado, estas clases son:
      
  1. Calendario: el cual corresponde al patron fachada, conteniendo a las clases: Alarma, AlarmaEfectos, Evento, Frecuencia, Limite, Organizador, Recordatorio, Repetidor y tarea, además de las clases asociadas a la persistencia. Todo con el proposito de proporcionar una logica centralizada y completa
  2. Alarma: la cual contiene las fechas de lanzado de las alarmas, sus repeticiones, el comportamiento de la misma mediante sus efectos: AlarmaEfecto; esta clase aplica el patrón estrategy para definir diversos comportamientos según sea requerido
  3. AlarmaEfectos: Logica de los efectos de las alarmas
  4. Recordatorio: Clase abstracta que engloba el comportamiento general de tarea y evento, define un comportamiento estandar de las clases mencionadas y permite agrupar tanto eventos como tareas.
  5. Eventos: Representa la logica de un funcionamiento semejantes a los eventos definidos en google Calendar, a su vez es una clase que contiene "un repetidor" el cual le permite generara repeticiones.
  6. Tarea: Representa la logica de un funcionamiento semejante a las tareas de google Calendar
  7. Frecuencia: Enum que aplica el patron strategy para generar repeticiones de diferentes tipos, entre las cuales esta: diaria, semanal, mensual y anual
  6. Limite: Enum que aplica el patron strategy para validar correctamente la generacion de repeticiones
  7. Organizado: Clase que aplica el patron observer, genera y ordena las fechas como tambien sirve para realizar consultas por rango 
  8. Repetidor: Clase dedicada a la generacion de repeticiones, aplica el patrón state

 Por otro lado listamos los patrones de diseño utilizados en el desarrollo de la logica de negocio:
  - Strategy
  - Abstrac method 
  - State
  - Façade 
  - MVC (aunque su aplicacion no es perfecta)
  - Composite 
  - Memento 
  - Observer
  
  #### Persistencia
  
  Mediante el uso de GSON se agrega la creacion de archivos JSON para persistir el model.
  
  Un dato importante a destacar de este modelo es el hecho que genera las consultas por demanda, solo las instancias de Recordatorio son almacenadas y el resto de la informacion se genera por demanda
  
  
