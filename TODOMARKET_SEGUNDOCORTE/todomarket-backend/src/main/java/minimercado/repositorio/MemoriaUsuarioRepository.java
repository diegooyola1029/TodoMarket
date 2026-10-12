package minimercado.repositorio;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import minimercado.modelo.Usuario;

/** Implementación en memoria para la ejecución académica local. */
public class MemoriaUsuarioRepository implements UsuarioRepository {
    private final ConcurrentMap<Integer, Usuario> usuarios = new ConcurrentHashMap<>();

    public void guardar(Usuario usuario) {
        if (usuarios.putIfAbsent(usuario.getIdPersona(), usuario) != null) {
            throw new IllegalArgumentException("Ya existe un usuario con ese ID.");
        }
    }

    public List<Usuario> listar() {
        return usuarios.values().stream().sorted(Comparator.comparingInt(Usuario::getIdPersona)).toList();
    }

    public Optional<Usuario> buscarPorId(int id) { return Optional.ofNullable(usuarios.get(id)); }

    public Optional<Usuario> buscarPorNombreUsuario(String nombreUsuario) {
        return usuarios.values().stream()
                .filter(usuario -> usuario.getNombreUsuario().equalsIgnoreCase(nombreUsuario))
                .findFirst();
    }

    public void actualizar(Usuario usuario) {
        if (!usuarios.containsKey(usuario.getIdPersona())) throw new IllegalArgumentException("Usuario no encontrado.");
        usuarios.put(usuario.getIdPersona(), usuario);
    }

    public boolean eliminar(int id) { return usuarios.remove(id) != null; }
}
