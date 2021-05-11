package com.example.asistmed;

public class Tratamiento {

    private String nombre;
    private String duracion;
    private String usuario;
    private int foto;

    public Tratamiento() {
    }

    public Tratamiento(String nombre, String duracion, String usuario) {
        this.nombre = nombre;
        this.duracion = duracion;
        this.usuario = usuario;
    }

    public Tratamiento(String nombre, String duracion, int foto){
        this.nombre = nombre;
        this.duracion = duracion;
        this.foto = foto;
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
