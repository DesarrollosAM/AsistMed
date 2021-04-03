package com.example.asistmed;

public class Tratamiento {

    private int id_tratamiento;
    private String nombre;
    private String duracion;
    private int cantidad_med;
    private int id_usuario;

    public Tratamiento() {
    }

    public Tratamiento(String nombre, String duracion, int cantidad_med, int id_usuario) {
        //this.id_tratamiento = id_tratamiento;
        this.nombre = nombre;
        this.duracion = duracion;
        this.cantidad_med = cantidad_med;
        this.id_usuario = id_usuario;
    }

    public int getId_tratamiento() {
        return id_tratamiento;
    }

    public void setId_tratamiento(int id_tratamiento) {
        this.id_tratamiento = id_tratamiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public int getCantidad_med() {
        return cantidad_med;
    }

    public void setCantidad_med(int cantidad_med) {
        this.cantidad_med = cantidad_med;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    @Override
    public String toString() {
        return "Tratamiento{" +
                "id_tratamiento=" + id_tratamiento +
                ", nombre='" + nombre + '\'' +
                ", duracion=" + duracion +
                ", cantidad_med=" + cantidad_med +
                ", id_usuario=" + id_usuario +
                '}';
    }
}
