package org.example.model;

public class Estudiante extends Usuario{
    protected String curso;

    public Estudiante(int id, String nombre, String rut, String correo, String contraseña, Rol rol, String curso) {
        super(id, nombre, rut, correo, contraseña, rol);
        this.curso = curso;
    }

    public String getCurso() { return curso;}
    public void setCurso(String curso) { this.curso = curso;}

    @Override
    public String toString() { return super.toString() + " | curso: " + curso;}
}
/**Clase hija de Usuario, hereda herencia*/