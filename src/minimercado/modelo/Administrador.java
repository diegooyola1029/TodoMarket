package minimercado.modelo;

/** Usuario con rol de administración del sistema. Hereda de Usuario. */
public class Administrador extends Usuario {

    private int    idAdmin;
    private String cargo;

    public Administrador(int idPersona, String nombreUsuario, String contrasena,
                         String documento, String telefono, String correo,
                         int idAdmin, String cargo) {
        super(idPersona, nombreUsuario, contrasena, documento, telefono, correo);
        this.idAdmin = idAdmin;
        this.cargo   = cargo;
    }

    public void gestionarSistema() { /* lógica futura */ }

    public int    getIdAdmin() { return idAdmin; }
    public String getCargo()   { return cargo; }
    public void   setCargo(String v) { this.cargo = v; }

    @Override public String toString() { return getNombreUsuario() + " (Admin)"; }
}
