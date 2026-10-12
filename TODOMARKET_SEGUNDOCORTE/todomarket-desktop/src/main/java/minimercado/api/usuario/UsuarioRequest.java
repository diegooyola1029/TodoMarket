package minimercado.api.usuario;

import minimercado.modelo.Administrador;
import minimercado.modelo.Empleado;
import minimercado.modelo.Usuario;

/** Datos que envía el escritorio al backend para crear o actualizar usuarios. */
public record UsuarioRequest(
        int idPersona,
        String nombreUsuario,
        String contrasena,
        String documento,
        String telefono,
        String correo,
        RolRequest rol,
        boolean estado) {

    public static UsuarioRequest desde(Usuario usuario) {
        RolRequest rol;
        if (usuario instanceof Administrador administrador) {
            rol = new RolRequest(1, "Administrador", administrador.getCargo());
        } else if (usuario instanceof Empleado empleado) {
            rol = new RolRequest(2, "Empleado", empleado.getCargo());
        } else {
            rol = new RolRequest(3, "Cliente", "Cliente del minimercado");
        }

        return new UsuarioRequest(usuario.getIdPersona(), usuario.getNombreUsuario(), usuario.getContrasena(),
                usuario.getDocumento(), usuario.getTelefono(), usuario.getCorreo(), rol, true);
    }

    public record RolRequest(int idRol, String nombreRol, String descripcion) { }
}
