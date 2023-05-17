package Persistencia;

import calendar.AlarmaEfectos;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

public abstract class Deserealizador {

    public abstract Enum<?>[] listar();



}
