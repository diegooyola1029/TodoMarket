package minimercado.repositorio;
import java.util.List; import java.util.Optional; import minimercado.modelo.Producto;
/** Contrato de persistencia de productos. Permite cambiar ArrayList por una base de datos sin tocar el servicio. */
public interface ProductoRepository { void guardar(Producto producto); List<Producto> listar(); Optional<Producto> buscarPorId(int id); void actualizar(Producto producto); boolean eliminar(int id); }
