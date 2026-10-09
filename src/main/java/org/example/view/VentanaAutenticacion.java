package org.example.view;

import org.example.controller.*;
import org.example.model.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import java.sql.SQLException;

public class VentanaAutenticacion extends JFrame {
    private final ControladorAutenticacion controlador = new ControladorAutenticacion();
    private JTextField txtCorreo = new JTextField(20);
    private JPasswordField txtContrasena = new JPasswordField(20);
    private final JButton btnIngresar = new JButton("Ingresar");
    private final JButton btnSalir = new JButton("Salir");

    public VentanaAutenticacion() {
        setTitle("Biblioteca escolar - Inicio de sesión");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        contruirInterfaz();
    }

    private void contruirInterfaz() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20,30,20,30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,5,8,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Sistema de Biblioteca escolar", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Correo:"), gbc);
        gbc.gridx = 1;
        panel.add(txtCorreo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        panel.add(txtContrasena, gbc);

        /*JLabel ayuda = new JLabel("<html><center>Estudiantes: use su correo o su RUT como contraseña.</center></html>", SwingConstants.CENTER);
        ayuda.setFont(ayuda.getFont().deriveFont(Font.PLAIN, 11f));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(ayuda, gbc);*/

        JPanel botones = new JPanel();
        botones.add(btnIngresar);
        botones.add(btnSalir);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(botones, gbc);

        setContentPane(panel);
        getRootPane().setDefaultButton(btnIngresar);
        btnIngresar.addActionListener(e -> ingresar());
        btnSalir.addActionListener(e -> System.exit(0));
    }

    private void ingresar() {
        try {
            Usuario usuario = controlador.autenticar(txtCorreo.getText(), new String(txtContrasena.getPassword()));
            if (usuario == null) {
                JOptionPane.showMessageDialog(this, "Correo o contraseña incorrectos.", "Acceso denegado.", JOptionPane.WARNING_MESSAGE);
                txtContrasena.setText("");
                return;
            }
            dispose();
            if(usuario.getRol() == Rol.BIBLIOTECARIO){
                new VentanaMenu(usuario).setVisible(true);
            } else {
                new VentanaPrestamos(usuario).setVisible(true);
            }
        } catch (SQLException e) {
            UtilidadesVista.error(this, e);
        }
    }
}