package minimercado.servicio;

import java.util.List;
import minimercado.modelo.Usuario;
import minimercado.repositorio.UsuarioRepository;

/**
 * Servicio de negocio para usuarios (SRP).
 * Valida unicidad de ID, nombre de usuario, documento, teléfono y correo.
 * Delega la persistencia al repositorio (DIP).
 */
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public void crear(Usuario u) {
        if (repository.buscarPorId(u.getIdPersona()).isPresent())
            throw new IllegalArgumentException("Ya existe un usuario con ese ID.");
        validarUnicidad(u, -1);
        repository.guardar(u);
    }

    public List<Usuario> listar() { return repository.listar(); }

    public Usuario buscar(int id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
    }

    public void actualizar(Usuario datos) {
        buscar(datos.getIdPersona()); // valida que exista
        validarUnicidad(datos, datos.getIdPersona());
        repository.actualizar(datos);
    }

    public void eliminar(int id) {
        if (!repository.eliminar(id))
            throw new IllegalArgumentException("Usuario no encontrado.");
    }

    /**
     * Verifica que nombre, documento, teléfono y correo no estén en uso
     * por otro usuario distinto a excludeId.
     */
    private void validarUnicidad(Usuario u, int excludeId) {
        for (Usuario existente : repository.listar()) {
            if (existente.getIdPersona() == excludeId) continue;

            if (existente.getNombreUsuario().equalsIgnoreCase(u.getNombreUsuario()))
                throw new IllegalArgumentException(
                        "El nombre de usuario \"" + u.getNombreUsuario() + "\" ya está en uso.");

            if (!u.getDocumento().isBlank()
                    && existente.getDocumento().equals(u.getDocumento()))
                throw new IllegalArgumentException(
                        "El documento \"" + u.getDocumento() + "\" ya está registrado.");

            if (!u.getTelefono().isBlank()
                    && existente.getTelefono().equals(u.getTelefono()))
                throw new IllegalArgumentException(
                        "El teléfono \"" + u.getTelefono() + "\" ya está registrado.");

            if (!u.getCorreo().isBlank()
                    && existente.getCorreo().equalsIgnoreCase(u.getCorreo()))
                throw new IllegalArgumentException(
                        "El correo \"" + u.getCorreo() + "\" ya está registrado.");
        }
    }
}
