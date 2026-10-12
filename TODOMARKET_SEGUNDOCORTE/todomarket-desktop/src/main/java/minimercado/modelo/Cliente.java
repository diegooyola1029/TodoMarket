package minimercado.modelo;

/** Usuario comprador del minimercado. Hereda de Usuario. */
public class Cliente extends Usuario {

    private int idCliente;

    public Cliente(int idPersona, String nombreUsuario, String contrasena,
                   String documento, String telefono, String correo,
                   int idCliente) {
        super(idPersona, nombreUsuario, contrasena, documento, telefono, correo);
        this.idCliente = idCliente;
    }

    public void verHistorialCompras() { /* lógica futura */ }

    public int getIdCliente() { return idCliente; }

    @Override public String toString() { return getNombreUsuario() + " (Cliente)"; }
}
