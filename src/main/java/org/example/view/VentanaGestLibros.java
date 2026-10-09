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

public class VentanaGestLibros extends JFrame {
    private final ControladorLibro controlador = new ControladorLibro();
    private final JTextField txtTitulo = new JTextField(22);
    private final JTextField txtAutor = new JTextField(22);
    private final JTextField txtIsbn = new JTextField(22);
    private final JTextField txtEditorial = new JTextField(22);
    private final JSpinner spStock = new JSpinner(new SpinnerNumberModel(1, 0, 10000, 1));
    private final JComboBox<Categoria> cmbCategoria = new JComboBox<>();

    private final DefaultTableModel modelo = new DefaultTableModel(
        new String[]{"ID", "Título", "Autor", "ISBN", "Editorial", "Stock", "Categoría"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    private final JTable tabla = new JTable(modelo);
    private List<Libro> libros = new ArrayList<>();
    private int idSeleccionado = -1;

    public VentanaGestLibros() {
        setTitle("Gestión de libros");
        setSize(950, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        construirInterfaz();
        try {
            cargarCategorias();
            cargarTabla();
        } catch (SQLException ex) {
            UtilidadesVista.error(this, ex);
        }
    }

    private void construirInterfaz() {
        cmbCategoria.setRenderer(UtilidadesVista.<Categoria>textoDe(Categoria::getNombre));

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Título:"));
        form.add(txtTitulo);
        form.add(new JLabel("Autor:"));
        form.add(txtAutor);
        form.add(new JLabel("ISBN:"));
        form.add(txtIsbn);
        form.add(new JLabel("Editorial:"));
        form.add(txtEditorial);
        form.add(new JLabel("Stock:"));
        form.add(spStock);
        form.add(new JLabel("Categoría:"));
        form.add(cmbCategoria);

        JPanel contenedorForm = new JPanel(new FlowLayout(FlowLayout.LEFT));
        contenedorForm.setBorder(BorderFactory.createTitledBorder("Datos del libro"));
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
            if (!e.getValueIsAdjusting() && fila >= 0 && fila < libros.size()) {
                cargarFormulario(libros.get(fila));
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

    private void cargarCategorias() throws SQLException {
        cmbCategoria.removeAllItems();
        for (Categoria c : controlador.listarCategorias()) {
            cmbCategoria.addItem(c);
        }
    }

    private void cargarTabla() throws SQLException {
        libros = controlador.listar();
        modelo.setRowCount(0);
        for (Libro l : libros) {
            modelo.addRow(new Object[]{l.getId(), l.getTitulo(), l.getAutor(), l.getIsbn(),
                    l.getEditorial(), l.getStock(),
                    l.getCategoria() != null ? l.getCategoria().getNombre() : ""});
        }
    }

    private void cargarFormulario(Libro l) {
        idSeleccionado = l.getId();
        txtTitulo.setText(l.getTitulo());
        txtAutor.setText(l.getAutor());
        txtIsbn.setText(l.getIsbn());
        txtEditorial.setText(l.getEditorial());
        spStock.setValue(l.getStock());
        if (l.getCategoria() != null) {
            for (int i = 0; i < cmbCategoria.getItemCount(); i++) {
                if (cmbCategoria.getItemAt(i).getId() == l.getCategoria().getId()) {
                    cmbCategoria.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void limpiarFormulario() {
        idSeleccionado = -1;
        txtTitulo.setText("");
        txtAutor.setText("");
        txtIsbn.setText("");
        txtEditorial.setText("");
        spStock.setValue(1);
        tabla.clearSelection();
    }

    private void registrar() {
        try {
            controlador.registrar(txtTitulo.getText(), txtAutor.getText(), txtIsbn.getText(),
                    txtEditorial.getText(), (Integer) spStock.getValue(),
                    (Categoria) cmbCategoria.getSelectedItem());
            UtilidadesVista.info(this, "Libro registrado correctamente.");
            limpiarFormulario();
            cargarTabla();
        } catch (IllegalArgumentException | SQLException ex) {
            UtilidadesVista.error(this, ex);
        }
    }

    private void modificar() {
        if (idSeleccionado < 0) {
            UtilidadesVista.info(this, "Seleccione un libro de la tabla para modificarlo.");
            return;
        }
        try {
            controlador.modificar(idSeleccionado, txtTitulo.getText(), txtAutor.getText(),
                    txtIsbn.getText(), txtEditorial.getText(), (Integer) spStock.getValue(),
                    (Categoria) cmbCategoria.getSelectedItem());
            UtilidadesVista.info(this, "Libro modificado correctamente.");
            limpiarFormulario();
            cargarTabla();
        } catch (IllegalArgumentException | SQLException ex) {
            UtilidadesVista.error(this, ex);
        }
    }

    private void eliminar() {
        if (idSeleccionado < 0) {
            UtilidadesVista.info(this, "Seleccione un libro de la tabla para eliminarlo.");
            return;
        }
        if (!UtilidadesVista.confirmar(this, "¿Eliminar el libro seleccionado?")) {
            return;
        }
        try {
            controlador.eliminar(idSeleccionado);
            UtilidadesVista.info(this, "Libro eliminado correctamente.");
            limpiarFormulario();
            cargarTabla();
        } catch (SQLException ex) {
            UtilidadesVista.error(this, ex);
        }
    }

}