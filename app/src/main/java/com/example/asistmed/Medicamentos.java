package com.example.asistmed;

public class Medicamentos {

    private String nombreTratamiento;
    private String usuario;
    private String nombre;
    private int foto;
    private int cantidad;
    private int frecuencia;
    private int duracion;
    private String info;

    public Medicamentos() {
    }

    public Medicamentos(String nombreTratamiento, String usuario, String nombre, int foto, int cantidad, int frecuencia, int duracion, String info) {
        this.nombreTratamiento = nombreTratamiento;
        this.usuario = usuario;
        this.nombre = nombre;
        this.foto = foto;
        this.cantidad = cantidad;
        this.frecuencia = frecuencia;
        this.duracion = duracion;
        this.info = info;
    }

    public String getNombreTratamiento() {
        return nombreTratamiento;
    }

    public void setNombreTratamiento(String nombreTratamiento) {
        this.nombreTratamiento = nombreTratamiento;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(int frecuencia) {
        this.frecuencia = frecuencia;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
