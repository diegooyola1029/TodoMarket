package minimercado.api.producto;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/productos")
public class ProductoRestController {
    private final InventarioClient inventario;

    public ProductoRestController(InventarioClient inventario) { this.inventario = inventario; }

    @GetMapping
    public List<ProductoResponse> listar() { return inventario.listar(); }

    @GetMapping("/{id}")
    public ProductoResponse buscar(@PathVariable int id) { return inventario.buscar(id); }

    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@RequestBody ProductoRequest solicitud) {
        ProductoResponse creado = inventario.crear(solicitud);
        return ResponseEntity.created(URI.create("/api/productos/" + creado.idProducto())).body(creado);
    }

    @PutMapping("/{id}")
    public ProductoResponse actualizar(@PathVariable int id, @RequestBody ProductoRequest solicitud) {
        if (id != solicitud.idProducto()) throw new IllegalArgumentException("El ID de la ruta y del producto deben coincidir.");
        return inventario.actualizar(id, solicitud);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        inventario.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stock-bajo")
    public List<ProductoResponse> stockBajo() { return inventario.stockBajo(); }

    @GetMapping("/agotados")
    public List<ProductoResponse> agotados() { return inventario.agotados(); }

    @GetMapping("/vencidos")
    public List<ProductoResponse> vencidos() { return inventario.vencidos(); }

    @GetMapping("/proximos-a-vencer")
    public List<ProductoResponse> proximosAVencer() { return inventario.proximosAVencer(); }

    @GetMapping("/alertas")
    public List<String> alertas() { return inventario.alertas(); }
}
