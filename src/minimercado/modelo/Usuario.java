package minimercado.modelo;

import java.time.LocalDate;

/**
 * Clase base (herencia) para todos los tipos de usuario del sistema.
 * Concentra los atributos comunes: identificación, credenciales y contacto.
 */
public class Usuario {

    private int       idPersona;
    private String    nombreUsuario;
    private String    contrasena;
    private String    documento;
    private String    telefono;
    private String    correo;
    private final LocalDate fechaCreacion;

    public Usuario(int idPersona, String nombreUsuario, String contrasena,
                   String documento, String telefono, String correo) {
        this.idPersona     = idPersona;
        this.nombreUsuario = nombreUsuario;
        this.contrasena    = contrasena;
        this.documento     = documento;
        this.telefono      = telefono;
        this.correo        = correo;
        this.fechaCreacion = LocalDate.now();
    }

    public void actualizarDatos(String telefono, String correo) {
        this.telefono = telefono;
        this.correo   = correo;
    }

    public boolean iniciarSesion(String usuario, String clave) {
        return nombreUsuario.equals(usuario) && contrasena.equals(clave);
    }

    public void cerrarSesion() { /* lógica de sesión */ }

    public void cambiarContrasena(String nueva) {
        if (nueva != null && !nueva.isBlank()) contrasena = nueva;
    }

    // ── Getters / Setters ──────────────────────────────────────────────────────
    public int       getIdPersona()     { return idPersona; }
    public String    getNombreUsuario() { return nombreUsuario; }
    public String    getDocumento()     { return documento; }
    public String    getTelefono()      { return telefono; }
    public String    getCorreo()        { return correo; }
    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public String    getContrasena()    { return contrasena; }

    public void setNombreUsuario(String v) { this.nombreUsuario = v; }
    public void setDocumento(String v)     { this.documento = v; }
    public void setTelefono(String v)      { this.telefono = v; }
    public void setCorreo(String v)        { this.correo = v; }

    @Override public String toString() { return nombreUsuario + " [" + documento + "]"; }
}
