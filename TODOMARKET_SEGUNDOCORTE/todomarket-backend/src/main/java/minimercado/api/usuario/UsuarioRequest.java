package minimercado.api.usuario;

import minimercado.modelo.Rol;
import minimercado.modelo.Usuario;

public record UsuarioRequest(
        int idPersona,
        String nombreUsuario,
        String contrasena,
        String documento,
        String telefono,
        String correo,
        RolRequest rol,
        boolean estado) {

    public Usuario aUsuario() {
        return aUsuario(idPersona);
    }

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

    public record RolRequest(int idRol, String nombreRol, String descripcion) { }
}
