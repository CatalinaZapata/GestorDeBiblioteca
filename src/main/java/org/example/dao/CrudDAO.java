package org.example.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface CrudDAO<T> {
    boolean crear(T objeto) throws SQLException;
    T leer(int id) throws SQLException;
    List<T> listar() throws SQLException;
    boolean actualizar(T objeto) throws SQLException;
    boolean eliminar(int id) throws SQLException;
}
