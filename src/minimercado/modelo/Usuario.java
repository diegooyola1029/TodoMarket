package minimercado.modelo;

import java.time.LocalDate;

/** Clase padre de los tipos de usuario; concentra los datos que todos comparten. */
public class Usuario {
    private int idPersona; private String nombreUsuario, contrasena, documento, telefono, correo;
    private LocalDate fechaCreacion;
    public Usuario(int idPersona, String nombreUsuario, String contrasena, String documento, String telefono, String correo) {
        this.idPersona=idPersona; this.nombreUsuario=nombreUsuario; this.contrasena=contrasena; this.documento=documento; this.telefono=telefono; this.correo=correo; this.fechaCreacion=LocalDate.now();
    }
    public void actualizarDatos(String telefono, String correo) { this.telefono=telefono; this.correo=correo; }
    public boolean iniciarSesion(String usuario, String clave) { return nombreUsuario.equals(usuario) && contrasena.equals(clave); }
    public void cerrarSesion() { }
    public void cambiarContrasena(String nueva) { if(nueva != null && !nueva.isBlank()) contrasena=nueva; }
    public int getIdPersona(){return idPersona;} public String getNombreUsuario(){return nombreUsuario;} public String getDocumento(){return documento;}
    public String getTelefono(){return telefono;} public String getCorreo(){return correo;} public LocalDate getFechaCreacion(){return fechaCreacion;}
}
