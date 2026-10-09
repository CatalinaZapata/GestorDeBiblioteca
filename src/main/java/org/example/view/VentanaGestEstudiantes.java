package org.example.view;

import org.example.controller.*;
import org.example.model.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

public class VentanaGestEstudiantes extends JFrame {
    private final ControladorEstudiante controlador = new ControladorEstudiante();
    private final JTextField txtNombre = new JTextField(22);
    private final JTextField txtRut = new JTextField(22);
    private final JTextField txtCurso = new JTextField(22);
    private final JTextField txtCorreo = new JTextField(22);
    private final JPasswordField txtContrasena = new JPasswordField(22);

    private final DefaultTableModel modelo = new DefaultTableModel( new String[]{"ID", "Nombre", "RUT", "Curso", "Correo"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);
    private List<Estudiante> estudiantes = new ArrayList<>();
    private int idSeleccionado = -1;

    public VentanaGestEstudiantes() {
        setTitle("Gestión de estudiantes");
        setSize(900, 580);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        construirInterfaz();
        try {
            cargarTabla();
        } catch (SQLException ex) {
            UtilidadesVista.error(this, ex);
        }
    }

    private void construirInterfaz() {
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Nombre:"));
        form.add(txtNombre);
        form.add(new JLabel("RUT:"));
        form.add(txtRut);
        form.add(new JLabel("Curso:"));
        form.add(txtCurso);
        form.add(new JLabel("Correo:"));
        form.add(txtCorreo);
        form.add(new JLabel("Contraseña:"));
        form.add(txtContrasena);

        JPanel contenedorForm = new JPanel(new FlowLayout(FlowLayout.LEFT));
        contenedorForm.setBorder(BorderFactory.createTitledBorder("Datos del estudiante (al modificar, deje la contraseña vacía para conservarla)"));
        contenedorForm.add(form);

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnCerrar = new JButton("Cerrar");
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botones.add(btnRegistrar);
        botones.add(btnModificar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);
        botones.add(btnCerrar);

        JPanel norte = new JPanel(new BorderLayout());
        norte.add(contenedorForm, BorderLayout.CENTER);
        norte.add(botones, BorderLayout.SOUTH);

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (!e.getValueIsAdjusting() && fila >= 0 && fila < estudiantes.size()) {
                cargarFormulario(estudiantes.get(fila));
            }
        });

        JPanel raiz = new JPanel(new BorderLayout(10, 10));
        raiz.setBorder(new EmptyBorder(10, 10, 10, 10));
        raiz.add(norte, BorderLayout.NORTH);
        raiz.add(new JScrollPane(tabla), BorderLayout.CENTER);
        setContentPane(raiz);

        btnRegistrar.addActionListener(e -> registrar());
        btnModificar.addActionListener(e -> modificar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnCerrar.addActionListener(e -> dispose());
    }

    private void cargarTabla() throws SQLException {
        estudiantes = controlador.listar();
        modelo.setRowCount(0);
        for (Estudiante e : estudiantes) {
            modelo.addRow(new Object[]{e.getId(), e.getNombre(), e.getRut(), e.getCurso(), e.getCorreo()});
        }
    }

    private void cargarFormulario(Estudiante e) {
        idSeleccionado = e.getId();
        txtNombre.setText(e.getNombre());
        txtRut.setText(e.getRut());
        txtCurso.setText(e.getCurso());
        txtCorreo.setText(e.getCorreo());
        txtContrasena.setText("");
    }

    private void limpiarFormulario() {
        idSeleccionado = -1;
        txtNombre.setText("");
        txtRut.setText("");
        txtCurso.setText("");
        txtCorreo.setText("");
        txtContrasena.setText("");
        tabla.clearSelection();
    }

    private void registrar() {
        try {
            controlador.registrar(txtNombre.getText(), txtRut.getText(), txtCurso.getText(),
                    txtCorreo.getText(), new String(txtContrasena.getPassword()));
            UtilidadesVista.info(this, "Estudiante registrado correctamente.");
            limpiarFormulario();
            cargarTabla();
        } catch (IllegalArgumentException | SQLException ex) {
            UtilidadesVista.error(this, ex);
        }
    }

    private void modificar() {
        if (idSeleccionado < 0) {
            UtilidadesVista.info(this, "Seleccione un estudiante de la tabla para modificarlo.");
            return;
        }
        try {
            controlador.modificar(idSeleccionado, txtNombre.getText(), txtRut.getText(),
                    txtCurso.getText(), txtCorreo.getText(), new String(txtContrasena.getPassword()));
            UtilidadesVista.info(this, "Estudiante modificado correctamente.");
            limpiarFormulario();
            cargarTabla();
        } catch (IllegalArgumentException | SQLException ex) {
            UtilidadesVista.error(this, ex);
        }
    }

    private void eliminar() {
        if (idSeleccionado < 0) {
            UtilidadesVista.info(this, "Seleccione un estudiante de la tabla para eliminarlo.");
            return;
        }
        if (!UtilidadesVista.confirmar(this, "¿Eliminar al estudiante seleccionado y su usuario de acceso?")) {
            return;
        }
        try {
            controlador.eliminar(idSeleccionado);
            UtilidadesVista.info(this, "Estudiante eliminado correctamente.");
            limpiarFormulario();
            cargarTabla();
        } catch (SQLException ex) {
            UtilidadesVista.error(this, ex);
        }
    }
}
