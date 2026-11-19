package minimercado.controlador;

import java.util.List;
import minimercado.api.cliente.ApiClientSupport;
import minimercado.api.cliente.RestCrudClient;
import minimercado.api.producto.ProductoRequest;
import minimercado.api.producto.ProductoResponse;
import minimercado.modelo.Inventario;
import minimercado.modelo.Producto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

public class ProductoController {
    private final RestClient cliente;
    private final RestCrudClient<ProductoRequest, ProductoResponse> productos;
    private final Inventario inventario;

    public ProductoController(RestClient cliente, Inventario inventario) {
        this.cliente = cliente;
        productos = new RestCrudClient<>(cliente, "/api/productos", ProductoResponse.class,
                new ParameterizedTypeReference<List<ProductoResponse>>() { });
        this.inventario = inventario;
    }

    public void agregar(Producto producto) {
        productos.crear(ProductoRequest.desde(producto));
        inventario.notificar("CREADO", producto);
    }

    public List<Producto> listar() { return convertir(productos.listar()); }

    public Producto buscar(int id) {
        return productos.buscar(id).aProducto();
    }

    public void actualizar(Producto producto) {
        productos.actualizar(producto.getIdProducto(), ProductoRequest.desde(producto));
        inventario.notificar("ACTUALIZADO", producto);
    }

    public void eliminar(int id) {
        Producto producto = buscar(id);
        productos.eliminar(id);
        inventario.notificar("ELIMINADO", producto);
    }

    public List<Producto> stockBajo() { return listar("/api/productos/stock-bajo"); }
    public List<Producto> agotados() { return listar("/api/productos/agotados"); }
    public List<Producto> vencidos() { return listar("/api/productos/vencidos"); }
    public List<Producto> proximosAVencer() { return listar("/api/productos/proximos-a-vencer"); }

    public List<String> alertas() {
        return ApiClientSupport.ejecutar(() -> cliente.get().uri("/api/productos/alertas")
                .retrieve().body(new ParameterizedTypeReference<>() { }));
    }

    private List<Producto> listar(String ruta) {
        return convertir(productos.listar(ruta));
    }

    private List<Producto> convertir(List<ProductoResponse> respuestas) {
        return respuestas.stream().map(ProductoResponse::aProducto).toList();
    }
}
