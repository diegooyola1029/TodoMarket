package minimercado.modelo;

/** Rol funcional asociado a una cuenta de usuario. */
public class Rol {
    private final int idRol;
    private String nombreRol;
    private String descripcion;

    public Rol(int idRol, String nombreRol, String descripcion) {
        if (nombreRol == null || nombreRol.isBlank()) {
            throw new IllegalArgumentException("El nombre del rol es obligatorio.");
        }
        this.idRol = idRol;
        this.nombreRol = nombreRol.trim();
        this.descripcion = descripcion == null ? "" : descripcion.trim();
    }

    public int getIdRol() { return idRol; }
    public String getNombreRol() { return nombreRol; }
    public String getDescripcion() { return descripcion; }
    public void setNombreRol(String nombreRol) {
        if (nombreRol == null || nombreRol.isBlank()) throw new IllegalArgumentException("El nombre del rol es obligatorio.");
        this.nombreRol = nombreRol.trim();
    }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion == null ? "" : descripcion.trim(); }
    @Override public String toString() { return nombreRol; }
}
