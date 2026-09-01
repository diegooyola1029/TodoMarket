package minimercado.repositorio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import minimercado.modelo.Producto;

/**
 * Implementación en memoria del repositorio de productos.
 *
 * Patrón Singleton: garantiza que sólo exista una instancia del almacén
 * en toda la aplicación, evitando estados inconsistentes entre capas.
 */
public class MemoriaProductoRepository implements ProductoRepository {

    // ── Singleton ──────────────────────────────────────────────────────────────
    private static MemoriaProductoRepository instancia;

    private MemoriaProductoRepository() {}

    public static MemoriaProductoRepository getInstancia() {
        if (instancia == null) {
            instancia = new MemoriaProductoRepository();
        }
        return instancia;
    }
    // ──────────────────────────────────────────────────────────────────────────

    private final ArrayList<Producto> productos = new ArrayList<>();

    @Override
    public void guardar(Producto p) {
        productos.add(p);
    }

    @Override
    public List<Producto> listar() {
        return new ArrayList<>(productos);
    }

    @Override
    public Optional<Producto> buscarPorId(int id) {
        return productos.stream().filter(p -> p.getIdProducto() == id).findFirst();
    }

    @Override
    public void actualizar(Producto p) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getIdProducto() == p.getIdProducto()) {
                productos.set(i, p);
                return;
            }
        }
        throw new IllegalArgumentException("Producto no encontrado.");
    }

    @Override
    public boolean eliminar(int id) {
        return productos.removeIf(p -> p.getIdProducto() == id);
    }
}
