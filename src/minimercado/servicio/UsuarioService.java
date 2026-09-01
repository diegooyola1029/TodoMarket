package minimercado.servicio;

import java.util.List;
import minimercado.modelo.Usuario;
import minimercado.repositorio.UsuarioRepository;

/**
 * Servicio de negocio para usuarios (SRP).
 * Valida unicidad de ID y delega la persistencia al repositorio (DIP).
 */
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public void crear(Usuario u) {
        if (repository.buscarPorId(u.getIdPersona()).isPresent())
            throw new IllegalArgumentException("Ya existe un usuario con ese ID.");
        repository.guardar(u);
    }

    public List<Usuario> listar() { return repository.listar(); }

    public Usuario buscar(int id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
    }

    public void actualizar(Usuario datos) {
        Usuario u = buscar(datos.getIdPersona());
        u.setNombreUsuario(datos.getNombreUsuario());
        u.setDocumento(datos.getDocumento());
        u.setTelefono(datos.getTelefono());
        u.setCorreo(datos.getCorreo());
        repository.actualizar(u);
    }

    public void eliminar(int id) {
        if (!repository.eliminar(id))
            throw new IllegalArgumentException("Usuario no encontrado.");
    }
}
