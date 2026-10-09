package org.example.controller;

import org.example.dao.*;
import org.example.model.*;
import java.util.List;
import java.sql.SQLException;
import java.util.Objects;

public class ControladorEstudiante {
    private final EstudianteDao estudianteDao = new EstudianteDao();
    private final UsuarioDao usuarioDao = new UsuarioDao();

    public List<Estudiante> listar() throws SQLException {
        return estudianteDao.listar();
    }

    public void registrar(String nombre, String rut, String curso, String correo, String contrasena) throws SQLException {
        validar(nombre, rut, curso, correo);
        if (contrasena == null || contrasena.isBlank()) {
            throw new IllegalArgumentException("Ingrese una contraseña para el estudiante.");
        }
        for (Usuario u : usuarioDao.listar()) {
            if (rut.trim().equalsIgnoreCase(u.getRut()) || correo.trim().equalsIgnoreCase(u.getCorreo())) {
                throw new IllegalArgumentException("Ya existe un usuario con ese RUT o correo.");
            }
        }
        estudianteDao.crear(new Estudiante(0, nombre.trim(), rut.trim(), correo.trim(), null, Rol.ESTUDIANTE, curso.trim()));
        usuarioDao.crear(new Usuario(0, nombre.trim(), rut.trim(), correo.trim(), contrasena, Rol.ESTUDIANTE));
    }

    public void modificar(int id, String nombre, String rut, String curso, String correo, String contrasena) throws SQLException {
        validar(nombre, rut, curso, correo);
        Estudiante anterior = estudianteDao.leer(id);
        estudianteDao.actualizar(new Estudiante(id, nombre.trim(), rut.trim(), correo.trim(), null, Rol.ESTUDIANTE, curso.trim()));

        if (anterior != null) {
            for (Usuario u : usuarioDao.listar()) {
                if (u.getRol() == Rol.ESTUDIANTE && Objects.equals(u.getRut(), anterior.getRut())) {
                    u.setNombre(nombre.trim());
                    u.setRut(rut.trim());
                    u.setCorreo(correo.trim());
                    if (contrasena != null && !contrasena.isBlank()) {
                        u.setContrasena(contrasena);
                    }
                    usuarioDao.actualizar(u);
                    break;
                }
            }
        }
    }

    public void eliminar(int id) throws SQLException {
        Estudiante e = estudianteDao.leer(id);
        estudianteDao.eliminar(id);
        if (e != null) {
            for (Usuario u : usuarioDao.listar()) {
                if (u.getRol() == Rol.ESTUDIANTE && Objects.equals(u.getRut(), e.getRut())) {
                    usuarioDao.eliminar(u.getId());
                    break;
                }
            }
        }
    }

    private void validar(String nombre, String rut, String curso, String correo) {
        if (nombre == null || nombre.isBlank() || rut == null || rut.isBlank() || curso == null || curso.isBlank() || correo == null || correo.isBlank()) {
            throw new IllegalArgumentException("Complete todos los campos del estudiante.");
        }
        if (!correo.contains("@")) {
            throw new IllegalArgumentException("El correo no es válido.");
        }
    }
}
