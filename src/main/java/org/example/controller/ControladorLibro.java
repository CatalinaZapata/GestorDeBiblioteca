package org.example.controller;

import org.example.dao.*;
import org.example.model.*;
import java.sql.SQLException;
import java.util.List;

public class ControladorLibro {
    private final LibroDao libroDao = new LibroDao();
    private final CategoriaDAO categoriaDao = new CategoriaDAO();

    public List<Libro> listar() throws SQLException {
        return libroDao.listar();
    }

    public List<Categoria> listarCategorias() throws SQLException {
        return categoriaDao.listar();
    }

    public void registrar(String titulo, String autor, String isbn, String editorial, int stock, Categoria categoria) throws SQLException {
        validar(titulo, autor, isbn, editorial, stock, categoria);
        libroDao.crear(new Libro(0, titulo.trim(), autor.trim(), isbn.trim(), editorial.trim(), stock, categoria));
    }

    public void modificar(int id, String titulo, String autor, String isbn, String editorial, int stock, Categoria categoria) throws SQLException {
        validar(titulo, autor, isbn, editorial, stock, categoria);
        libroDao.actualizar(new Libro(id, titulo.trim(), autor.trim(), isbn.trim(), editorial.trim(), stock, categoria));
    }

    public void eliminar(int id) throws SQLException {
        libroDao.eliminar(id);
    }

    private void validar(String titulo, String autor, String isbn, String editorial, int stock, Categoria categoria) {
        if (titulo == null || titulo.isBlank() || autor == null || autor.isBlank() || isbn == null || isbn.isBlank() || editorial == null || editorial.isBlank()) {
            throw new IllegalArgumentException("Complete todos los campos del libro.");
        }
        if (isbn.trim().length() > 20) {
            throw new IllegalArgumentException("El ISBN no puede superar los 20 caracteres.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        if (categoria == null) {
            throw new IllegalArgumentException("Seleccione una categoría.");
        }
    }
}
