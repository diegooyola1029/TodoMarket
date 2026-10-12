package minimercado.modelo;

import java.time.LocalDate;

/** Cuenta y datos básicos de una persona que utiliza el sistema. */
public class Usuario {
    private final int idPersona;
    private String nombreUsuario;
    private String contrasena;
    private String documento;
    private String telefono;
    private String correo;
    private final LocalDate fechaCreacion;
    private boolean estado;
    private Rol rol;

    public Usuario(int idPersona, String nombreUsuario, String contrasena, String documento, String telefono, String correo) {
        this(idPersona, nombreUsuario, contrasena, documento, telefono, correo, new Rol(1, "Cliente", "Usuario general"), true);
    }

    public Usuario(int idPersona, String nombreUsuario, String contrasena, String documento, String telefono, String correo, Rol rol, boolean estado) {
        if (idPersona <= 0) throw new IllegalArgumentException("El ID del usuario debe ser mayor que cero.");
        validar(nombreUsuario, documento, correo, rol);
        this.idPersona = idPersona;
        this.nombreUsuario = nombreUsuario.trim();
        this.contrasena = contrasena == null ? "" : contrasena;
        this.documento = documento.trim();
        this.telefono = telefono == null ? "" : telefono.trim();
        this.correo = correo == null ? "" : correo.trim();
        this.fechaCreacion = LocalDate.now();
        this.rol = rol;
        this.estado = estado;
    }

    private static void validar(String nombreUsuario, String documento, String correo, Rol rol) {
        if (nombreUsuario == null || nombreUsuario.isBlank()) throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
        if (documento == null || documento.isBlank()) throw new IllegalArgumentException("El documento es obligatorio.");
        if (correo != null && !correo.isBlank() && !correo.contains("@")) throw new IllegalArgumentException("El correo no tiene un formato válido.");
        if (rol == null) throw new IllegalArgumentException("Debe seleccionar un rol.");
    }

    public void actualizarDatos(String telefono, String correo) {
        if (correo != null && !correo.isBlank() && !correo.contains("@")) throw new IllegalArgumentException("El correo no tiene un formato válido.");
        this.telefono = telefono == null ? "" : telefono.trim();
        this.correo = correo == null ? "" : correo.trim();
    }

    public boolean iniciarSesion(String usuario, String clave) { return estado && nombreUsuario.equals(usuario) && contrasena.equals(clave); }
    public void cerrarSesion() { }
    public void cambiarContrasena(String nueva) { if (nueva != null && !nueva.isBlank()) contrasena = nueva; }
    public void setNombreUsuario(String v) { if (v == null || v.isBlank()) throw new IllegalArgumentException("El nombre de usuario es obligatorio."); nombreUsuario = v.trim(); }
    public void setDocumento(String v) { if (v == null || v.isBlank()) throw new IllegalArgumentException("El documento es obligatorio."); documento = v.trim(); }
    public void setRol(Rol rol) { if (rol == null) throw new IllegalArgumentException("Debe seleccionar un rol."); this.rol = rol; }
    public void setEstado(boolean estado) { this.estado = estado; }
    public int getIdPersona(){ return idPersona; }
    public String getNombreUsuario(){ return nombreUsuario; }
    public String getContrasena(){ return contrasena; }
    public String getDocumento(){ return documento; }
    public String getTelefono(){ return telefono; }
    public String getCorreo(){ return correo; }
    public LocalDate getFechaCreacion(){ return fechaCreacion; }
    public boolean isEstado(){ return estado; }
    public Rol getRol(){ return rol; }
    @Override public String toString(){ return nombreUsuario + " · " + rol; }
}
