package org.example.controller;

import org.example.dao.*;
import org.example.model.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class ControladorReporte {
    private final PrestamoDAO prestamoDao = new PrestamoDAO();
    private final LibroDao libroDao = new LibroDao();
    private final EstudianteDao estudianteDao = new EstudianteDao();

    public List<Estudiante> listarEstudiantes() throws SQLException {
        return estudianteDao.listar();
    }

    public List<Object[]> librosMasPrestados() throws SQLException {
        Map<Integer, Long> conteo = prestamoDao.listar().stream() .collect(Collectors.groupingBy(Prestamo::getIdLibro, Collectors.counting()));
        List<Object[]> filas = new ArrayList<>();
        for (Libro l : libroDao.listar()) {
            long veces = conteo.getOrDefault(l.getId(), 0L);
            if (veces > 0) {
                filas.add(new Object[]{l.getTitulo(), l.getAutor(), veces});
            }
        }
        filas.sort((a, b) -> Long.compare((Long) b[2], (Long) a[2]));
        return filas;
    }

    public List<Object[]> historialPorEstudiante(int idEstudiante) throws SQLException {
        Map<Integer, String> libros = titulosPorId();
        List<Object[]> filas = new ArrayList<>();
        for (Prestamo p : prestamoDao.listar()) {
            if (p.getIdEstudiante() == idEstudiante) {
                filas.add(new Object[]{
                        libros.getOrDefault(p.getIdLibro(), "?"),
                        p.getFechaPrestamo(), p.getFechaDevolucion(), estado(p)});
            }
        }
        return filas;
    }

    public List<Object[]> librosEnPrestamo() throws SQLException {
        Map<Integer, String> libros = titulosPorId();
        Map<Integer, String> estudiantes = new HashMap<>();
        for (Estudiante e : estudianteDao.listar()) {
            estudiantes.put(e.getId(), e.getNombre());
        }
        List<Object[]> filas = new ArrayList<>();
        for (Prestamo p : prestamoDao.listar()) {
            if (!p.isDevuelto()) {
                filas.add(new Object[]{
                        libros.getOrDefault(p.getIdLibro(), "?"),
                        estudiantes.getOrDefault(p.getIdEstudiante(), "?"),
                        p.getFechaPrestamo(), p.getFechaDevolucion(), estado(p)});
            }
        }
        return filas;
    }

    private Map<Integer, String> titulosPorId() throws SQLException {
        Map<Integer, String> libros = new HashMap<>();
        for (Libro l : libroDao.listar()) {
            libros.put(l.getId(), l.getTitulo());
        }
        return libros;
    }

    private String estado(Prestamo p) {
        if (p.isDevuelto()) {
            return "Devuelto";
        }
        return p.estaAtrasado() ? "ATRASADO" : "Vigente";
    }
}
