package org.example.view;

import org.example.controller.*;
import org.example.model.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.sql.SQLException;

public class VentanaReportes extends JFrame {
    private final ControladorReporte controller = new ControladorReporte();
    private final DefaultTableModel mdlMasPrestados = modelo("Libro", "Autor", "Veces prestado");
    private final DefaultTableModel mdlHistorial = modelo("Libro", "Fecha préstamo", "Vence", "Estado");
    private final DefaultTableModel mdlEnPrestamo = modelo("Libro", "Estudiante", "Fecha préstamo", "Vence", "Estado");
    private final JComboBox<Estudiante> cmbEstudiante = new JComboBox<>();

    public VentanaReportes() {
        setTitle("Reportes");
        setSize(900, 560);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        construirInterfaz();
        cargarTodo();
    }

    private static DefaultTableModel modelo(String... columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
    }

    private void construirInterfaz() {
        cmbEstudiante.setRenderer(UtilidadesVista.<Estudiante>textoDe(e -> e.getNombre() + " (" + e.getRut() + ")"));

        JPanel pestana1 = new JPanel(new BorderLayout());
        pestana1.add(new JScrollPane(new JTable(mdlMasPrestados)), BorderLayout.CENTER);

        JTable tablaHistorial = new JTable(mdlHistorial);
        UtilidadesVista.resaltarAtrasados(tablaHistorial, 3);
        JPanel selector = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selector.add(new JLabel("Estudiante:"));
        selector.add(cmbEstudiante);
        JPanel pestana2 = new JPanel(new BorderLayout());
        pestana2.add(selector, BorderLayout.NORTH);
        pestana2.add(new JScrollPane(tablaHistorial), BorderLayout.CENTER);

        JTable tablaEnPrestamo = new JTable(mdlEnPrestamo);
        UtilidadesVista.resaltarAtrasados(tablaEnPrestamo, 4);
        JPanel pestana3 = new JPanel(new BorderLayout());
        pestana3.add(new JScrollPane(tablaEnPrestamo), BorderLayout.CENTER);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Libros más prestados", pestana1);
        pestanas.addTab("Historial por estudiante", pestana2);
        pestanas.addTab("Libros en préstamo", pestana3);

        JButton btnActualizar = new JButton("Actualizar");
        JButton btnCerrar = new JButton("Cerrar");
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botones.add(btnActualizar);
        botones.add(btnCerrar);

        JPanel raiz = new JPanel(new BorderLayout(10, 10));
        raiz.setBorder(new EmptyBorder(10, 10, 10, 10));
        raiz.add(pestanas, BorderLayout.CENTER);
        raiz.add(botones, BorderLayout.SOUTH);
        setContentPane(raiz);

        btnActualizar.addActionListener(e -> cargarTodo());
        btnCerrar.addActionListener(e -> dispose());
    }

    private void cargarTodo() {
        try {
            llenar(mdlMasPrestados, controller.librosMasPrestados());
            llenar(mdlEnPrestamo, controller.librosEnPrestamo());

            var listeners = cmbEstudiante.getActionListeners();
            for (var l : listeners) {
                cmbEstudiante.removeActionListener(l);
            }
            cmbEstudiante.removeAllItems();
            for (Estudiante e : controller.listarEstudiantes()) {
                cmbEstudiante.addItem(e);
            }
            cmbEstudiante.addActionListener(e -> cargarHistorial());
            cargarHistorial();
        } catch (SQLException ex) {
            UtilidadesVista.error(this, ex);
        }
    }

    private void cargarHistorial() {
        mdlHistorial.setRowCount(0);
        Estudiante e = (Estudiante) cmbEstudiante.getSelectedItem();
        if (e == null) {
            return;
        }
        try {
            llenar(mdlHistorial, controller.historialPorEstudiante(e.getId()));
        } catch (SQLException ex) {
            UtilidadesVista.error(this, ex);
        }
    }

    private void llenar(DefaultTableModel modelo, List<Object[]> filas) {
        modelo.setRowCount(0);
        for (Object[] fila : filas) {
            modelo.addRow(fila);
        }
    }
}
