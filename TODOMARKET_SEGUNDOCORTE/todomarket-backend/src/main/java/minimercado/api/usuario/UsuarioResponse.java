package minimercado.api.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import minimercado.modelo.Usuario;

@Schema(name = "UsuarioResponse", description = "Usuario devuelto por la API; no incluye la contraseña.")
public record UsuarioResponse(
        @Schema(description = "Identificador asignado", example = "1") int idPersona,
        @Schema(description = "Nombre de usuario", example = "admin") String nombreUsuario,
        @Schema(description = "Documento", example = "1000001") String documento,
        @Schema(description = "Teléfono", example = "3001111001") String telefono,
        @Schema(description = "Correo", example = "admin@example.com") String correo,
        @Schema(description = "Rol") RolResponse rol,
        @Schema(description = "Estado activo", example = "true") boolean estado,
        @Schema(description = "Fecha de creación", example = "2026-11-19") LocalDate fechaCreacion) {

    public static UsuarioResponse desde(Usuario usuario) {
        var rol = usuario.getRol();
        return new UsuarioResponse(usuario.getIdPersona(), usuario.getNombreUsuario(), usuario.getDocumento(),
                usuario.getTelefono(), usuario.getCorreo(),
                new RolResponse(rol.getIdRol(), rol.getNombreRol(), rol.getDescripcion()),
                usuario.isEstado(), usuario.getFechaCreacion());
    }

    public Usuario aUsuario() {
        var rolUsuario = new minimercado.modelo.Rol(rol.idRol(), rol.nombreRol(), rol.descripcion());
        return new Usuario(idPersona, nombreUsuario, "", documento, telefono, correo, rolUsuario, estado);
    }

    @Schema(name = "RolResponse", description = "Rol incluido en la respuesta del usuario.")
    public record RolResponse(
            @Schema(description = "ID del rol", example = "1") int idRol,
            @Schema(description = "Nombre", example = "Administrador") String nombreRol,
            @Schema(description = "Descripción", example = "Gerente") String descripcion) { }
}
