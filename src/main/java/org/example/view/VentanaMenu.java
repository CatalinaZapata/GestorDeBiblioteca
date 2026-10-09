package org.example.view;

import org.example.model.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VentanaMenu extends JFrame {
    public  VentanaMenu(Usuario usuario) {
        setTitle("Biblioteca escolar - Menú principal");
        setSize(420, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel bienvenida = new JLabel("Bienvenido/a, " + usuario.getNombre(), SwingConstants.CENTER);
        bienvenida.setFont(bienvenida.getFont().deriveFont(Font.BOLD, 16f));

        JButton btnLibros = new JButton("Gestión de libros");
        JButton btnEtudiantes = new JButton("Gestión de estudiantes");
        JButton btnPrestamos = new JButton("Registro de préstamos y devoluciones");
        JButton btnReportes = new JButton("Visualización de reportes");
        JButton btnSalir = new JButton("Salir");

        btnLibros.addActionListener(e -> new VentanaGestLibros().setVisible(true));
        btnEtudiantes.addActionListener(e -> new VentanaGestEstudiantes().setVisible(true));
        btnPrestamos.addActionListener(e -> new VentanaPrestamos(usuario).setVisible(true));
        btnReportes.addActionListener(e -> new VentanaReportes().setVisible(true));
        btnSalir.addActionListener(e -> {
            if(UtilidadesVista.confirmar(this, "¿Desea salir del sistema?")){
                System.exit(0);
            }
        });

        JPanel botones = new JPanel(new GridLayout(5,1,10,10));
        botones.add(btnLibros);
        botones.add(btnEtudiantes);
        botones.add(btnPrestamos);
        botones.add(btnReportes);
        botones.add(btnSalir);

        JPanel raiz = new JPanel(new BorderLayout(10,20));
        raiz.setBorder(new EmptyBorder(20,40,20,40));
        raiz.add(bienvenida,BorderLayout.NORTH);
        raiz.add(botones,BorderLayout.CENTER);
        setContentPane(raiz);
    }
}