package minimercado.servicio;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import minimercado.modelo.Rol;
import minimercado.modelo.Usuario;
import minimercado.repositorio.UsuarioRepository;

/** Reglas de negocio del CRUD de usuarios. Depende de una abstracción (DIP). */
public class UsuarioService {
    private final UsuarioRepository repository;
    private final AtomicInteger secuenciaIds = new AtomicInteger();

    public UsuarioService(UsuarioRepository repository){ this.repository = repository; }

    public synchronized Usuario crear(Usuario usuario){
        if (repository.buscarPorNombreUsuario(usuario.getNombreUsuario()).isPresent()) throw new IllegalArgumentException("Ya existe ese nombre de usuario.");
        validarDatosUnicos(usuario, -1);
        int id = secuenciaIds.updateAndGet(actual ->
                Math.max(actual, repository.listar().stream().mapToInt(Usuario::getIdPersona).max().orElse(0)) + 1);
        Usuario creado = new Usuario(id, usuario.getNombreUsuario(), usuario.getContrasena(),
                usuario.getDocumento(), usuario.getTelefono(), usuario.getCorreo(), usuario.getRol(),
                usuario.isEstado());
        repository.guardar(creado);
        return creado;
    }

    public List<Usuario> listar(){ return repository.listar(); }
    public Usuario buscar(int id){ return repository.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado.")); }

    public void actualizar(Usuario datos){
        Usuario actual = buscar(datos.getIdPersona());
        repository.buscarPorNombreUsuario(datos.getNombreUsuario()).ifPresent(encontrado -> {
            if (encontrado.getIdPersona() != actual.getIdPersona()) throw new IllegalArgumentException("Ya existe ese nombre de usuario.");
        });
        validarDatosUnicos(datos, actual.getIdPersona());
        actual.setNombreUsuario(datos.getNombreUsuario());
        actual.setDocumento(datos.getDocumento());
        actual.actualizarDatos(datos.getTelefono(), datos.getCorreo());
        actual.setRol(datos.getRol());
        actual.setEstado(datos.isEstado());
        if (datos.getContrasena() != null && !datos.getContrasena().isBlank()) actual.cambiarContrasena(datos.getContrasena());
        repository.actualizar(actual);
    }

    public void eliminar(int id){ if(!repository.eliminar(id)) throw new IllegalArgumentException("Usuario no encontrado."); }
    public Rol crearRol(int id, String nombre, String descripcion){ return new Rol(id, nombre, descripcion); }

    private void validarDatosUnicos(Usuario usuario, int excluirId) {
        for (Usuario existente : repository.listar()) {
            if (existente.getIdPersona() == excluirId) continue;
            if (!usuario.getDocumento().isBlank() && existente.getDocumento().equals(usuario.getDocumento())) {
                throw new IllegalArgumentException("El documento ya está registrado.");
            }
            if (!usuario.getTelefono().isBlank() && existente.getTelefono().equals(usuario.getTelefono())) {
                throw new IllegalArgumentException("El teléfono ya está registrado.");
            }
            if (!usuario.getCorreo().isBlank() && existente.getCorreo().equalsIgnoreCase(usuario.getCorreo())) {
                throw new IllegalArgumentException("El correo ya está registrado.");
            }
        }
    }
}
