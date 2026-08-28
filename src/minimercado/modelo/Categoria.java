package minimercado.modelo;

import java.util.Objects;

/** Clasifica los productos y permite agruparlos en la interfaz. */
public class Categoria {
    private int idCategoria;
    private String nombre;
    public Categoria(int idCategoria, String nombre) { this.idCategoria = idCategoria; this.nombre = nombre; }
    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    @Override public String toString() { return nombre; }
    @Override public boolean equals(Object otro) { return otro instanceof Categoria && idCategoria == ((Categoria) otro).idCategoria; }
    @Override public int hashCode() { return Objects.hash(idCategoria); }
}
