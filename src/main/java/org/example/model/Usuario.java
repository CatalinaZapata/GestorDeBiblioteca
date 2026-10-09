package org.example.model;

public class Usuario {
    protected int id;
    protected String nombre;
    protected String rut;
    protected String correo;
    protected String contrasena;
    protected Rol rol;

    public Usuario(int id, String nombre, String rut, String correo, String contraseña, Rol rol) {
        this.id = id;
        this.nombre = nombre;
        this.rut = rut;
        this.correo = correo;
        this.contrasena = contraseña;
        this.rol = rol;
    }

    public int getId() { return id;}
    public String getNombre() { return nombre;}
    public String getRut() { return rut;}
    public String getCorreo() { return correo;}
    public String getContrasena() { return contrasena;}
    public Rol getRol() { return rol;}

    public void setId(int id) { this.id = id;}
    public void setNombre(String nombre) { this.nombre = nombre;}
    public void setRut(String rut) { this.rut = rut;}
    public void setCorreo(String correo) { this.correo = correo;}
    public void setContrasena(String contrasena) { this.contrasena = contrasena;}
    public void setRol(Rol rol) { this.rol = rol;}

    @Override
    public String toString() { return "id: " + id + " | nombre: " + nombre + " | rut: " + rut + " | correo: " + correo + " | contraseña: " + contrasena + " | rol: " + rol;}
}

/**Clase Padre/Super, otorgara herencia a Estudiante*/