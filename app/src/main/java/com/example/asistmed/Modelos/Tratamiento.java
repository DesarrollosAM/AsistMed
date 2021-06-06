package com.example.asistmed.Modelos;

public class Tratamiento {

    //Declaramos las variables necesarias.
    private String nombre;
    private String duracion;
    private String usuario;
    private int foto;

    //Constructor vacío.
    public Tratamiento() {
    }

    //Constructor con parámetros
    public Tratamiento(String nombre, String duracion, String usuario) {
        this.nombre = nombre;
        this.duracion = duracion;
        this.usuario = usuario;
    }

    //2º constructor con parametros
    public Tratamiento(String nombre, String duracion, int foto){
        this.nombre = nombre;
        this.duracion = duracion;
        this.foto = foto;
    }

    /*
    Getter´s & Setter´s
     */
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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }
}
