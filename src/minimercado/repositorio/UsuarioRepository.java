package minimercado.repositorio;

import java.util.List;
import java.util.Optional;
import minimercado.modelo.Usuario;

/**
 * Contrato de persistencia de usuarios (DIP).
 * Permite intercambiar la implementación sin afectar la capa de servicio.
 */
public interface UsuarioRepository {
    void           guardar(Usuario usuario);
    List<Usuario>  listar();
    Optional<Usuario> buscarPorId(int id);
    void           actualizar(Usuario usuario);
    boolean        eliminar(int id);
}
