package minimercado.servicio;

import java.util.List;
import minimercado.modelo.Usuario;
import minimercado.repositorio.UsuarioRepository;

/**
 * Servicio de negocio para usuarios (SRP).
 * Valida unicidad de ID y nombre de usuario, y delega la persistencia (DIP).
 */
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public void crear(Usuario u) {
        if (repository.buscarPorId(u.getIdPersona()).isPresent())
            throw new IllegalArgumentException("Ya existe un usuario con ese ID.");
        if (nombreExiste(u.getNombreUsuario(), -1))
            throw new IllegalArgumentException("El nombre de usuario \"" + u.getNombreUsuario() + "\" ya está en uso.");
        repository.guardar(u);
    }

    public List<Usuario> listar() { return repository.listar(); }

    public Usuario buscar(int id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
    }

    public void actualizar(Usuario datos) {
        buscar(datos.getIdPersona()); // valida que exista
        if (nombreExiste(datos.getNombreUsuario(), datos.getIdPersona()))
            throw new IllegalArgumentException("El nombre de usuario \"" + datos.getNombreUsuario() + "\" ya está en uso.");
        repository.actualizar(datos); // reemplaza el objeto completo
    }

    public void eliminar(int id) {
        if (!repository.eliminar(id))
            throw new IllegalArgumentException("Usuario no encontrado.");
    }

    /** Devuelve true si el nombre ya existe en otro usuario distinto al de excludeId. */
    private boolean nombreExiste(String nombre, int excludeId) {
        return repository.listar().stream()
                .anyMatch(u -> u.getNombreUsuario().equalsIgnoreCase(nombre)
                        && u.getIdPersona() != excludeId);
    }
}
