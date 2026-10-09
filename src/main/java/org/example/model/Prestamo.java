package org.example.model;

import java.time.LocalDate;

public class Prestamo {
    protected int id;
    protected int idEstudiante;
    protected int idLibro;
    protected LocalDate fechaPrestamo;
    protected LocalDate fechaDevolucion;
    protected boolean devuelto;

    public Prestamo(int id, int id_estudiante, int id_libro, LocalDate fecha_prestamo, LocalDate fecha_devolucion, boolean devuelto) {
        this.id = id;
        this.idEstudiante = id_estudiante;
        this.idLibro = id_libro;
        this.fechaPrestamo = fecha_prestamo;
        this.fechaDevolucion = fecha_devolucion;
        this.devuelto = devuelto;
    }

    public int getId() { return id;}
    public int getIdEstudiante() { return idEstudiante;}
    public int getIdLibro() { return idLibro;}
    public LocalDate getFechaPrestamo() { return fechaPrestamo;}
    public LocalDate getFechaDevolucion() { return fechaDevolucion;}
    public boolean isDevuelto() { return devuelto;}

    public void setId(int id) { this.id = id;}
    public void setIdEstudiante(int idEstudiante) { this.idEstudiante = idEstudiante;}
    public void setIdLibro(int idLibro) { this.idLibro = idLibro;}
    public void setFechaPrestamo(LocalDate fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }
    public void setFechaDevolucion(LocalDate fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }
    public void setDevuelto(boolean devuelto) { this.devuelto = devuelto; }

    @Override
    public String toString() { return "id: " + id + " | idEstudiante: " + idEstudiante + " | idLibro: " + idLibro + " | fechaPrestamo: " + fechaPrestamo + " | fechaDevolucion: " + fechaDevolucion + " | devuelto: " + devuelto; }

    public boolean estaAtrasado() {
        return !devuelto && LocalDate.now().isAfter(fechaDevolucion);
    }
}
/**Clase independiente que tiene dependencia mediante FK de Libro y estudiante. Registra un préstamo; referencia a Estudiante y Libro mediante sus ids.*/