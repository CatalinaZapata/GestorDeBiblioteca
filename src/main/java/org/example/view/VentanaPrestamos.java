package org.example.view;

import org.example.controller.*;
import org.example.model.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VentanaPrestamos extends JFrame {
    private final ControladorPrestamo controlador = new ControladorPrestamo();
    private final Usuario usuario;
    private final boolean esEstudiante;
    //public VentanaPrestamos(Usuario usuario);
    private Estudiante estudianteActual;
    private final JComboBox<Estudiante> cmbEstudiante = new JComboBox<>();
    private final JComboBox<Libro> cmbLibro  = new JComboBox<>();
    private final JButton btnPrestar = new JButton("Registrar préstamo");
    private final JLabel lblProceso = new JLabel(" ");

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Estudiante", "Libro", "Fecha préstamo", "Vence", "Devuelto", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
    };

    private final JTable tabla = new JTable(modelo);

    public VentanaPrestamos(Usuario usuario){
        this.usuario = usuario;
        this.esEstudiante = usuario.getRol() == Rol.ESTUDIANTE;

        setTitle("Prestamos y devoluviones - " + usuario.getNombre());
        setSize(980, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(esEstudiante ? EXIT_ON_CLOSE : DISPOSE_ON_CLOSE);
        construirInterfaz();
        cargaEstudiantes();
        actualizarDatos();
    }

    private void construirInterfaz() {
        cmbEstudiante.setRenderer(UtilidadesVista.<Estudiante>textoDe(e -> e.getNombre() + " (" + e.getRut() + ")"));
        cmbLibro.setRenderer(UtilidadesVista.<Libro>textoDe(l -> l.getTitulo() + " - " + l.getAutor() + " (stock: " + l.getStock() + ")"));
        LocalDate vence = LocalDate.now().plusDays(ControladorPrestamo.DIAS_PRESTAMO);
        JLabel lblPlazo = new JLabel(ControladorPrestamo.DIAS_PRESTAMO + " días (vence el " + vence.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ")");

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Estudiante:"));
        form.add(cmbEstudiante);
        form.add(new JLabel("Libro:"));
        form.add(cmbLibro);
        form.add(new JLabel("Plazo de préstamo:"));
        form.add(lblPlazo);

        JPanel accionPrestamo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        accionPrestamo.add(btnPrestar);
        accionPrestamo.add(lblProceso);

        JPanel nuevoPrestamo = new JPanel(new BorderLayout(5, 5));
        nuevoPrestamo.setBorder(BorderFactory.createTitledBorder("Nuevo préstamo"));
        nuevoPrestamo.add(form, BorderLayout.CENTER);
        nuevoPrestamo.add(accionPrestamo, BorderLayout.SOUTH);

        JButton btnDevolver = new JButton("Registrar devolución del préstamo seleccionado");
        JButton btnActualizar = new JButton("Actualizar lista");
        JButton btnCerrar = new JButton(esEstudiante ? "Cerrar sesión" : "Volver al menú");
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botones.add(btnDevolver);
        botones.add(btnActualizar);
        botones.add(btnCerrar);

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        UtilidadesVista.resaltarAtrasados(tabla, 6);

        JPanel centro = new JPanel(new BorderLayout(5, 5));
        centro.setBorder(BorderFactory.createTitledBorder(esEstudiante ? "Mis préstamos" : "Préstamos registrados"));
        centro.add(new JScrollPane(tabla), BorderLayout.CENTER);
        centro.add(botones, BorderLayout.SOUTH);

        JPanel raiz = new JPanel(new BorderLayout(10, 10));
        raiz.setBorder(new EmptyBorder(10, 10, 10, 10));
        raiz.add(nuevoPrestamo, BorderLayout.NORTH);
        raiz.add(centro, BorderLayout.CENTER);
        setContentPane(raiz);

        btnPrestar.addActionListener(e -> registrarPrestamo());
        btnDevolver.addActionListener(e -> registrarDevolucion());
        btnActualizar.addActionListener(e -> actualizarDatos());
        btnCerrar.addActionListener(e -> {
            dispose();
            if (esEstudiante) {
                new VentanaAutenticacion().setVisible(true);
            }
        });
    }


    private void cargaEstudiantes(){
        try {
            cmbEstudiante.removeAllItems();
            if (esEstudiante) {
                estudianteActual = controlador.buscarEstudiantePorCorreo(usuario.getCorreo());
                cmbEstudiante.setEnabled(false);
                if (estudianteActual == null) {
                    btnPrestar.setEnabled(false);
                    UtilidadesVista.info(this, "Su usuario no está vinculado a un registro de estudiante "
                            + "(el correo no coincide). Contacte al bibliotecario.");
                } else {
                    cmbEstudiante.addItem(estudianteActual);
                }
            } else {
                for (Estudiante e : controlador.listarEstudiantes()) {
                    cmbEstudiante.addItem(e);
                }
            }
        } catch (SQLException ex) {
            UtilidadesVista.error(this, ex);
        }
    }

    private void actualizarDatos(){
        try {
            Libro elegido = (Libro) cmbLibro.getSelectedItem();
            cmbLibro.removeAllItems();
            for (Libro l : controlador.listarLibros()) {
                cmbLibro.addItem(l);
            }
            if (elegido != null) {
                for (int i = 0; i < cmbLibro.getItemCount(); i++) {
                    if (cmbLibro.getItemAt(i).getId() == elegido.getId()) {
                        cmbLibro.setSelectedIndex(i);
                        break;
                    }
                }
            }

            Integer filtro = null; // bibliotecario: todos
            if (esEstudiante) {
                filtro = estudianteActual != null ? estudianteActual.getId() : -1;
            }
            modelo.setRowCount(0);
            for (Object[] fila : controlador.filasPrestamos(filtro)) {
                modelo.addRow(fila);
            }
        } catch (SQLException ex) {
            UtilidadesVista.error(this, ex);
        }
    }

    private void registrarPrestamo() {
        Estudiante estudiante = (Estudiante) cmbEstudiante.getSelectedItem();
        Libro libro = (Libro) cmbLibro.getSelectedItem();
        if (estudiante == null || libro == null) {
            UtilidadesVista.info(this, "Seleccione un estudiante y un libro.");
            return;
        }

        btnPrestar.setEnabled(false);
        lblProceso.setText("Procesando préstamo...");
        controlador.registrarPrestamo(estudiante.getId(), libro.getId(), mensaje ->
                SwingUtilities.invokeLater(() -> {
                    btnPrestar.setEnabled(true);
                    lblProceso.setText(" ");
                    UtilidadesVista.info(this, mensaje);
                    actualizarDatos();
                }));
    }

    private void registrarDevolucion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            UtilidadesVista.info(this, "Seleccione un préstamo de la tabla.");
            return;
        }
        int idPrestamo = (Integer) modelo.getValueAt(fila, 0);
        try {
            if (controlador.registrarDevolucion(idPrestamo)) {
                UtilidadesVista.info(this, "Devolución registrada correctamente.");
            } else {
                UtilidadesVista.info(this, "Ese préstamo ya estaba devuelto.");
            }
            actualizarDatos();
        } catch (SQLException ex) {
            UtilidadesVista.error(this, ex);
        }
    }

}