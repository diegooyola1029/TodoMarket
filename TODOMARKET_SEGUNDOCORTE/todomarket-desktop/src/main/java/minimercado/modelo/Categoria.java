package minimercado.modelo;

/** Categoría de un producto. Encapsula id y nombre para evitar cadenas sueltas. */
public class Categoria {
    private final int idCategoria;
    private final String nombre;

    public Categoria(int idCategoria, String nombre) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
    }

    public int getIdCategoria() { return idCategoria; }
    public String getNombre()   { return nombre; }

    @Override public String toString() { return nombre; }
}
