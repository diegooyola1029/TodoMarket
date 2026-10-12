package minimercado.api.usuario;

import java.time.LocalDate;
import minimercado.modelo.Usuario;

public record UsuarioResponse(
        int idPersona,
        String nombreUsuario,
        String documento,
        String telefono,
        String correo,
        RolResponse rol,
        boolean estado,
        LocalDate fechaCreacion) {

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

    public record RolResponse(int idRol, String nombreRol, String descripcion) { }
}
