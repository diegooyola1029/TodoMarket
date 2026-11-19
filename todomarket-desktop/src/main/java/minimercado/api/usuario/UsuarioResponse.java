package minimercado.api.usuario;

import java.time.LocalDate;
import minimercado.modelo.Administrador;
import minimercado.modelo.Cliente;
import minimercado.modelo.Empleado;
import minimercado.modelo.Usuario;

/** Representación de usuario recibida desde la API; la contraseña nunca se devuelve. */
public record UsuarioResponse(
        int idPersona,
        String nombreUsuario,
        String documento,
        String telefono,
        String correo,
        RolResponse rol,
        boolean estado,
        LocalDate fechaCreacion) {

    public Usuario aUsuario() {
        String nombreRol = rol == null ? "Cliente" : rol.nombreRol();
        String descripcion = rol == null ? "Cliente del minimercado" : rol.descripcion();

        return switch (nombreRol) {
            case "Administrador" -> new Administrador(idPersona, nombreUsuario, "", documento,
                    telefono, correo, idPersona, descripcion);
            case "Empleado" -> new Empleado(idPersona, nombreUsuario, "", documento,
                    telefono, correo, idPersona, descripcion);
            default -> new Cliente(idPersona, nombreUsuario, "", documento,
                    telefono, correo, idPersona);
        };
    }

    public record RolResponse(int idRol, String nombreRol, String descripcion) { }
}
