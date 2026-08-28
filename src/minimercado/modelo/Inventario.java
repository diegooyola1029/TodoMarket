package minimercado.modelo;
import java.util.*;
/** Colección de productos para representar un inventario único sin persistencia externa. */
public class Inventario {
    private int idInventario; private final ArrayList<Producto> productos=new ArrayList<>();
    public Inventario(int id){idInventario=id;} public void agregarProducto(Producto p){productos.add(p);} public List<Producto> listarProductos(){return new ArrayList<>(productos);} public Producto buscarProductoPorId(int id){return productos.stream().filter(p->p.getIdProducto()==id).findFirst().orElse(null);} public boolean actualizarProducto(Producto p){return buscarProductoPorId(p.getIdProducto())!=null;} public boolean eliminarProducto(int id){return productos.removeIf(p->p.getIdProducto()==id);} public List<Producto> obtenerProductosStockBajo(){return productos.stream().filter(Producto::tieneStockBajo).toList();} public List<Producto> obtenerProductosProximosAVencer(){return productos.stream().filter(Producto::estaProximoAVencer).toList();}
}
