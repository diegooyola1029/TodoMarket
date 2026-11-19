package minimercado.api.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import minimercado.modelo.Rol;
import minimercado.modelo.Usuario;

@Schema(name = "UsuarioRequest", description = "Datos de entrada para crear o actualizar un usuario.")
public record UsuarioRequest(
        @Schema(description = "ID usado al actualizar; al crear lo asigna el servicio", example = "1") int idPersona,
        @Schema(description = "Nombre de usuario único", example = "nuevo") String nombreUsuario,
        @Schema(description = "Contraseña; para actualización vacía conserva la actual", example = "cambiar-esta-clave", format = "password") String contrasena,
        @Schema(description = "Documento único", example = "1000004") String documento,
        @Schema(description = "Teléfono único", example = "3001111004") String telefono,
        @Schema(description = "Correo único", example = "nuevo@example.com") String correo,
        @Schema(description = "Rol asignado") RolRequest rol,
        @Schema(description = "Indica si el usuario está activo", example = "true") boolean estado) {

    public Usuario aUsuario() { return aUsuario(idPersona); }

    public Usuario aUsuario(int id) {
        Rol rolUsuario = rol == null ? null : new Rol(rol.idRol(), rol.nombreRol(), rol.descripcion());
        return new Usuario(id, nombreUsuario, contrasena, documento, telefono, correo, rolUsuario, estado);
    }

    public static UsuarioRequest desde(Usuario usuario) {
        Rol rolUsuario = usuario.getRol();
        RolRequest rolDto = new RolRequest(rolUsuario.getIdRol(), rolUsuario.getNombreRol(), rolUsuario.getDescripcion());
        return new UsuarioRequest(usuario.getIdPersona(), usuario.getNombreUsuario(), usuario.getContrasena(),
                usuario.getDocumento(), usuario.getTelefono(), usuario.getCorreo(), rolDto, usuario.isEstado());
    }

    @Schema(name = "RolRequest", description = "Datos del rol del usuario.")
    public record RolRequest(
            @Schema(description = "ID del rol", example = "3") int idRol,
            @Schema(description = "Nombre del rol", example = "Cliente") String nombreRol,
            @Schema(description = "Descripción", example = "Cliente del minimercado") String descripcion) { }
}
