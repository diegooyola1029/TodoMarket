package minimercado.modelo;

/** Usuario que representa al personal operativo del minimercado. Hereda de Usuario. */
public class Empleado extends Usuario {

    private int    idEmpleado;
    private String cargo;

    public Empleado(int idPersona, String nombreUsuario, String contrasena,
                    String documento, String telefono, String correo,
                    int idEmpleado, String cargo) {
        super(idPersona, nombreUsuario, contrasena, documento, telefono, correo);
        this.idEmpleado = idEmpleado;
        this.cargo      = cargo;
    }

    public void registrarVenta()      { /* lógica futura */ }
    public void consultarInventario() { /* lógica futura */ }

    public int    getIdEmpleado() { return idEmpleado; }
    public String getCargo()      { return cargo; }
    public void   setCargo(String v) { this.cargo = v; }

    @Override public String toString() { return getNombreUsuario() + " (Empleado)"; }
}
