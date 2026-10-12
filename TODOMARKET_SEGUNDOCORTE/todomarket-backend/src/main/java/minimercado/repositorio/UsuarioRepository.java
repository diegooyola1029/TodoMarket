package minimercado.repositorio;

import java.util.List;
import java.util.Optional;
import minimercado.modelo.Usuario;

/** Contrato de persistencia para el CRUD de usuarios. */
public interface UsuarioRepository {
    void guardar(Usuario usuario);
    List<Usuario> listar();
    Optional<Usuario> buscarPorId(int id);
    Optional<Usuario> buscarPorNombreUsuario(String nombreUsuario);
    void actualizar(Usuario usuario);
    boolean eliminar(int id);
}
