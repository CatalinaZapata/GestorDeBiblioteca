package org.example.dao;

import org.example.model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao implements CrudDAO<Usuario>{
    @Override
    public boolean crear(Usuario u)  throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, rut, correo, contraseña, rol)  VALUES (?, ?, ?, ?, ?)";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getRut());
            ps.setString(3, u.getCorreo());
            ps.setString(4, u.getContrasena());
            ps.setString(5, u.getRol().name().toLowerCase());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Usuario leer(int id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
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
    public List<Usuario> listar() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
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
    public boolean actualizar(Usuario u) throws SQLException {
        String sql = "UPDATE usuarios SET nombre = ?, rut = ?, correo = ?, contraseña = ?, rol = ? WHERE id = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getRut());
            ps.setString(3, u.getCorreo());
            ps.setString(4, u.getContrasena());
            ps.setString(5, u.getRol().name().toLowerCase());
            ps.setInt(6, u.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public Usuario autenticar(String correo, String contrasena) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE correo = ? AND BINARY contraseña = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setString(2, contrasena);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("rut"),
                rs.getString("correo"),
                rs.getString("contraseña"),
                Rol.valueOf(rs.getString("rol").toUpperCase())
        );
    }
}