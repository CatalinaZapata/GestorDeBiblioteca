package org.example.dao;

import org.example.model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibroDao implements CrudDAO<Libro>{
    @Override
    public boolean crear(Libro l) throws SQLException {
        String sql = "INSERT INTO libros (titulo, autor, isbn, editorial, stock, id_categoria) VALUES (?, ?, ?, ?, ?, ?)";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, l.getTitulo());
            ps.setString(2, l.getAutor());
            ps.setString(3, l.getIsbn());
            ps.setString(4, l.getEditorial());
            ps.setInt(5, l.getStock());
            ps.setInt(6, l.getCategoria().getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Libro leer(int id) throws SQLException {
        String sql = "SELECT * FROM libros WHERE id = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Libro> listar() throws SQLException {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT * FROM libros";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    @Override
    public boolean actualizar(Libro l) throws SQLException {
        String sql = "UPDATE libros SET titulo = ?, autor  = ?, isbn  = ?, editorial = ?, stock = ?, id_categoria = ? WHERE id = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, l.getTitulo());
            ps.setString(2, l.getAutor());
            ps.setString(3, l.getIsbn());
            ps.setString(4, l.getEditorial());
            ps.setInt(5, l.getStock());
            ps.setInt(6, l.getCategoria().getId());
            ps.setInt(7, l.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM libros WHERE id = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Libro mapear(ResultSet rs) throws SQLException {
        Categoria categoria = new CategoriaDAO().leer(rs.getInt("id_categoria"));
        return new Libro(
                rs.getInt("id"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getString("isbn"),
                rs.getString("editorial"),
                rs.getInt("stock"),
                categoria
        );
    }
}