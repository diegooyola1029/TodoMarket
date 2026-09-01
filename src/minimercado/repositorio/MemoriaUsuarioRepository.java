package minimercado.repositorio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import minimercado.modelo.Usuario;

/**
 * Implementación en memoria del repositorio de usuarios.
 *
 * Patrón Singleton: misma razón que el repositorio de productos —
 * una sola fuente de verdad durante toda la ejecución.
 */
public class MemoriaUsuarioRepository implements UsuarioRepository {

    // ── Singleton ──────────────────────────────────────────────────────────────
    private static MemoriaUsuarioRepository instancia;

    private MemoriaUsuarioRepository() {}

    public static MemoriaUsuarioRepository getInstancia() {
        if (instancia == null) {
            instancia = new MemoriaUsuarioRepository();
        }
        return instancia;
    }
    // ──────────────────────────────────────────────────────────────────────────

    private final ArrayList<Usuario> usuarios = new ArrayList<>();

    @Override public void guardar(Usuario u) { usuarios.add(u); }

    @Override public List<Usuario> listar() { return new ArrayList<>(usuarios); }

    @Override
    public Optional<Usuario> buscarPorId(int id) {
        return usuarios.stream().filter(u -> u.getIdPersona() == id).findFirst();
    }

    @Override
    public void actualizar(Usuario u) {
        if (buscarPorId(u.getIdPersona()).isEmpty())
            throw new IllegalArgumentException("Usuario no encontrado.");
    }

    @Override
    public boolean eliminar(int id) {
        return usuarios.removeIf(u -> u.getIdPersona() == id);
    }
}
