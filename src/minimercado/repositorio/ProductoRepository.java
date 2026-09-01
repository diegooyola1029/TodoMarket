package minimercado.repositorio;

import java.util.List;
import java.util.Optional;
import minimercado.modelo.Producto;

/**
 * Contrato de persistencia de productos (DIP – Dependency Inversion Principle).
 * El servicio depende de esta abstracción, no de la implementación concreta.
 */
public interface ProductoRepository {
    void             guardar(Producto producto);
    List<Producto>   listar();
    Optional<Producto> buscarPorId(int id);
    void             actualizar(Producto producto);
    boolean          eliminar(int id);
}
