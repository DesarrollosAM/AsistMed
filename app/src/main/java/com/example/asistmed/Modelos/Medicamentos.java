package com.example.asistmed.Modelos;

public class Medicamentos {

    //Declaramos las variables necesarias.
    private String nombreTratamiento;
    private String usuario;
    private String nombre;
    private int fotoInicial;
    private int cantidad;
    private int frecuencia;
    private int duracion;
    private String info;
    private String addFoto;

    //Constructor vacío.
    public Medicamentos() {
    }

    //Constructor con parámetros.
    public Medicamentos(String nombreTratamiento, String usuario, String nombre, int fotoInicial, int cantidad, int frecuencia, int duracion, String info, String addFoto) {
        this.nombreTratamiento = nombreTratamiento;
        this.usuario = usuario;
        this.nombre = nombre;
        this.fotoInicial = fotoInicial;
        this.cantidad = cantidad;
        this.frecuencia = frecuencia;
        this.duracion = duracion;
        this.info = info;
        this.addFoto = addFoto;
    }

    /*
    Getter´s & Setter´s
     */
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

    public int getFotoInicial() {
        return fotoInicial;
    }

    public void setFotoInicial(int fotoInicial) {
        this.fotoInicial = fotoInicial;
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

    public String getAddFoto() {
        return addFoto;
    }

    public void setAddFoto(String addFoto) {
        this.addFoto = addFoto;
    }
}
