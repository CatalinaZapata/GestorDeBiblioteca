package org.example.dao;

import org.example.model.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PrestamoDAO implements CrudDAO<Prestamo>{
    @Override
    public boolean crear(Prestamo p) throws SQLException {
        String sql = "INSERT INTO prestamos (id_estudiante, id_libro, fecha_prestamo, fecha_devolucion, devuelto) VALUES (?, ?, ?, ?, ?)";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getIdEstudiante());
            ps.setInt(2, p.getIdLibro());
            ps.setDate(3, Date.valueOf(p.getFechaPrestamo()));
            ps.setDate(4, Date.valueOf(p.getFechaDevolucion()));
            ps.setBoolean(5, p.isDevuelto());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Prestamo leer(int id) throws SQLException {
        String sql = "SELECT * FROM prestamos WHERE id = ?";
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
    public List<Prestamo> listar() throws SQLException {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT * FROM prestamos";
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
    public boolean actualizar(Prestamo p) throws SQLException {
        String sql = "UPDATE prestamos SET id_estudiante = ?, id_libro = ?, fecha_prestamo = ?, fecha_devolucion = ?, devuelto = ? WHERE id = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getIdEstudiante());
            ps.setInt(2, p.getIdLibro());
            ps.setDate(3, Date.valueOf(p.getFechaPrestamo()));
            ps.setDate(4, Date.valueOf(p.getFechaDevolucion()));
            ps.setBoolean(5, p.isDevuelto());
            ps.setInt(6, p.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM prestamos WHERE id = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Prestamo mapear(ResultSet rs) throws SQLException {
        return new Prestamo(
                rs.getInt("id"),
                rs.getInt("id_estudiante"),
                rs.getInt("id_libro"),
                rs.getDate("fecha_prestamo").toLocalDate(),
                rs.getDate("fecha_devolucion").toLocalDate(),
                rs.getBoolean("devuelto")
        );
    }

    public synchronized boolean registrarPrestamo(int idEstudiante, int idLibro, int dias) throws SQLException, ClassNotFoundException {
        Connection con = DatabaseConnection.getInstance().getConnection();
        String sqlStock = "UPDATE libros SET stock = stock - 1 WHERE id = ? And stock > 0";
        try (PreparedStatement ps = con.prepareStatement(sqlStock)) {
            ps.setInt(1, idLibro);
            if(ps.executeUpdate() == 0){
                return false;
            }
        }
        Prestamo p = new Prestamo(0, idEstudiante, idLibro, LocalDate.now(), LocalDate.now().plusDays(dias), false);
        try {
            return crear(p);
        } catch (SQLException e) {
            try (PreparedStatement ps = con.prepareStatement("UPDATE libros SET stock = stock + 1 WHERE id = ?")) {
                ps.setInt(1, idLibro);
                ps.executeUpdate();
            }
            throw e;
        }
    }

    public Thread registrarPrestamoAsync(int idEstudiante, int idLibro, int dias, Consumer<String> alTerminar) {
        Thread hilo = new Thread(() -> {
            try {
                boolean ok = registrarPrestamo(idEstudiante, idLibro, dias);
                alTerminar.accept(ok ? "Préstamo registrado" : "Sin stock disponible");
            } catch (SQLException e) {
                alTerminar.accept("Error: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        hilo.start();
        return hilo;
    }

    public synchronized boolean registrarDevolucion(int idPrestamo) throws SQLException {
        Prestamo p = leer(idPrestamo);
        if (p == null || p.isDevuelto()){
            return false;
        }
        Connection con = DatabaseConnection.getInstance().getConnection();
        String sqlPrestamo = "UPDATE prestamos SET devuelto = TRUE WHERE id = ? AND devuelto = FALSE";
        try (PreparedStatement ps = con.prepareStatement(sqlPrestamo)) {
            ps.setInt(1, idPrestamo);
            if (ps.executeUpdate() == 0) {
                return false;
            }
        }

        String sqlStock = "UPDATE libros SET stock = stock + 1 WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sqlStock)) {
            ps.setInt(1, p.getIdLibro());
            ps.executeUpdate();
        }
        return true;
    }
}