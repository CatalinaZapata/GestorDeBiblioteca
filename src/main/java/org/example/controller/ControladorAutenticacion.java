package org.example.controller;

import org.example.dao.*;
import org.example.model.*;
import java.sql.SQLException;

public class ControladorAutenticacion {
    private final UsuarioDao usuarioDao = new UsuarioDao();

    public Usuario autenticar(String correo, String contrasena) throws SQLException {
        if(correo == null || correo.isBlank() || contrasena.isBlank()) {
            return null;
        }
        return usuarioDao.autenticar(correo, contrasena);
    }
}
