package minimercado.servicio;

import java.util.List;
import minimercado.modelo.Inventario;
import minimercado.modelo.Producto;
import minimercado.repositorio.ProductoRepository;

/**
 * Servicio de negocio para productos (SRP – Single Responsibility).
 * Delega la persistencia al repositorio (DIP) y notifica cambios
 * al Inventario observable (patrón Observer).
 */
public class ProductoService {

    private final ProductoRepository repository;
    private final Inventario         inventario;

    public ProductoService(ProductoRepository repository, Inventario inventario) {
        this.repository = repository;
        this.inventario = inventario;
    }

    public void crear(Producto p) {
        if (repository.buscarPorId(p.getIdProducto()).isPresent())
            throw new IllegalArgumentException("Ya existe un producto con ese ID.");
        if (nombreProductoExiste(p.getNombre(), -1))
            throw new IllegalArgumentException(
                    "Ya existe un producto llamado \"" + p.getNombre() + "\". Usa Actualizar para modificarlo.");
        repository.guardar(p);
        inventario.notificar("CREADO", p);
    }

    public List<Producto> listar() { return repository.listar(); }

    public Producto buscar(int id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado."));
    }

    public void actualizar(Producto datos) {
        buscar(datos.getIdProducto()); // valida que exista
        if (nombreProductoExiste(datos.getNombre(), datos.getIdProducto()))
            throw new IllegalArgumentException(
                    "Ya existe otro producto llamado \"" + datos.getNombre() + "\".");
        repository.actualizar(datos);
        inventario.notificar("ACTUALIZADO", datos);
    }

    private boolean nombreProductoExiste(String nombre, int excludeId) {
        return repository.listar().stream()
                .anyMatch(p -> p.getNombre().equalsIgnoreCase(nombre)
                        && p.getIdProducto() != excludeId);
    }

    public void eliminar(int id) {
        Producto p = buscar(id);
        if (!repository.eliminar(id))
            throw new IllegalArgumentException("Producto no encontrado.");
        inventario.notificar("ELIMINADO", p);
    }

    public List<Producto> stockBajo()        { return listar().stream().filter(Producto::tieneStockBajo).toList(); }
    public List<Producto> agotados()         { return listar().stream().filter(Producto::estaAgotado).toList(); }
    public List<Producto> vencidos()         { return listar().stream().filter(Producto::estaVencido).toList(); }
    public List<Producto> proximosAVencer()  { return listar().stream().filter(Producto::estaProximoAVencer).toList(); }
}
