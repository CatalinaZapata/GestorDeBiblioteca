package org.example.dao;

import org.example.model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO implements CrudDAO<Categoria>{
    @Override
    public boolean crear(Categoria c) throws SQLException {
        String sql = "INSERT INTO categorias (nombre) VALUES (?)";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Categoria leer(int id) throws SQLException {
        String sql = "SELECT * FROM categorias WHERE id = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Categoria> listar() throws SQLException {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM categorias";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             while (rs.next()) {
                 lista.add(mapear(rs));
             }
        }
        return lista;
    }

    @Override
    public boolean actualizar(Categoria c) throws SQLException {
        String sql = "UPDATE categorias SET nombre = ? WHERE id = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setInt(2, c.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM categorias WHERE id = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Categoria mapear(ResultSet rs) throws SQLException {
        return new Categoria(
                rs.getInt("id"),
                rs.getString("nombre")
        );
    }
}