package org.example.model;

public class Libro {
    protected int id;
    protected String titulo;
    protected String autor;
    protected String isbn;
    protected String editorial;
    protected int stock;
    protected Categoria categoria;

    public Libro(int id, String titulo, String autor, String isbn, String editorial, int stock, Categoria categoria) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.editorial = editorial;
        this.stock = stock;
        this.categoria = categoria;
    }

    public int getId() { return id;}
    public String getTitulo() { return titulo;}
    public String getAutor() { return autor;}
    public String getIsbn() { return isbn;}
    public String getEditorial() { return editorial;}
    public int getStock() { return stock;}
    public Categoria getCategoria() { return categoria;}

    public void setId(int id) { this.id = id;}
    public void setTitulo(String titulo) { this.titulo = titulo;}
    public void setAutor(String autor) { this.autor = autor;}
    public void setIsbn(String isbn) { this.isbn = isbn;}
    public void setEditorial(String editorial) { this.editorial = editorial;}
    public void setStock(int stock) { this.stock = stock;}
    public void setCategoria(Categoria categoria) { this.categoria = categoria;}

    @Override
    public String toString() {return "id: " + id + " | titulo: " + titulo + " | autor: " + autor + " | isbn: " + isbn + " | editorial: " + editorial + " | stock: " + stock + " | categoria: " + categoria;}
}
/**Clase componente de Categoria, hereda la composición*/