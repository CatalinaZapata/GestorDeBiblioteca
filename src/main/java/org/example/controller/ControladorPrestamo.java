package org.example.controller;

import org.example.dao.*;
import org.example.model.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ControladorPrestamo {
    public static final int DIAS_PRESTAMO = 7;
    private static final PrestamoDAO prestamoDao = new PrestamoDAO();
    private final LibroDao libroDao = new LibroDao();
    private final EstudianteDao estudianteDao = new EstudianteDao();

    public List<Libro> listarLibros() throws SQLException {
        return libroDao.listar();
    }

    public List<Estudiante> listarEstudiantes() throws SQLException {
        return estudianteDao.listar();
    }

    public Estudiante buscarEstudiantePorCorreo(String correo) throws SQLException {
        for (Estudiante e : estudianteDao.listar()) {
            if (e.getCorreo() != null && e.getCorreo().equalsIgnoreCase(correo)) {
                return e;
            }
        }
        return null;
    }

    public void registrarPrestamo(int idEstudiante, int idLibro, Consumer<String> alTerminar) {
        prestamoDao.registrarPrestamoAsync(idEstudiante, idLibro, DIAS_PRESTAMO, alTerminar);
    }

    public boolean registrarDevolucion(int idPrestamo) throws SQLException {
        return prestamoDao.registrarDevolucion(idPrestamo);
    }

    public List<Object[]> filasPrestamos(Integer idEstudiante) throws SQLException {
        Map<Integer, String> estudiantes = new HashMap<>();
        for (Estudiante e : estudianteDao.listar()) {
            estudiantes.put(e.getId(), e.getNombre());
        }
        Map<Integer, String> libros = new HashMap<>();
        for (Libro l : libroDao.listar()) {
            libros.put(l.getId(), l.getTitulo());
        }

        List<Object[]> filas = new ArrayList<>();
        for (Prestamo p : prestamoDao.listar()) {
            if (idEstudiante != null && p.getIdEstudiante() != idEstudiante) {
                continue;
            }
            filas.add(new Object[]{
                    p.getId(),
                    estudiantes.getOrDefault(p.getIdEstudiante(), "?"),
                    libros.getOrDefault(p.getIdLibro(), "?"),
                    p.getFechaPrestamo(),
                    p.getFechaDevolucion(),
                    p.isDevuelto() ? "Sí" : "No",
                    estado(p)
            });
        }
        filas.sort((a, b) -> Integer.compare((Integer) b[0], (Integer) a[0])); // más recientes primero
        return filas;
    }

    private String estado(Prestamo p) {
        if (p.isDevuelto()) {
            return "Devuelto";
        }
        return p.estaAtrasado() ? "ATRASADO" : "Vigente";
    }

}