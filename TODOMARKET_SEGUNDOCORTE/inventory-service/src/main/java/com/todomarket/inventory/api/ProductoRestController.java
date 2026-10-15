package com.todomarket.inventory.api;

import com.todomarket.inventory.producto.Producto;
import com.todomarket.inventory.producto.ProductoService;
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
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductoRestController {
    private final ProductoService service;

    public ProductoRestController(ProductoService service) { this.service = service; }

    @GetMapping
    public List<ProductoResponse> listar() { return respuestasDe(service.listar()); }

    @GetMapping("/{id}")
    public ProductoResponse buscar(@PathVariable int id) { return ProductoResponse.desde(service.buscar(id)); }

    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@RequestBody ProductoRequest solicitud) {
        Producto creado = service.crear(solicitud.aProducto(1));
        return ResponseEntity.created(URI.create("/api/productos/" + creado.getIdProducto()))
                .body(ProductoResponse.desde(creado));
    }

    @PutMapping("/{id}")
    public ProductoResponse actualizar(@PathVariable int id, @RequestBody ProductoRequest solicitud) {
        if (id != solicitud.idProducto()) throw new IllegalArgumentException("El ID de la ruta y del producto deben coincidir.");
        return ProductoResponse.desde(service.actualizar(id, solicitud.aProducto()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stock-bajo")
    public List<ProductoResponse> stockBajo() { return respuestasDe(service.stockBajo()); }

    @GetMapping("/agotados")
    public List<ProductoResponse> agotados() { return respuestasDe(service.agotados()); }

    @GetMapping("/vencidos")
    public List<ProductoResponse> vencidos() { return respuestasDe(service.vencidos()); }

    @GetMapping("/proximos-a-vencer")
    public List<ProductoResponse> proximosAVencer() { return respuestasDe(service.proximosAVencer()); }

    @GetMapping("/alertas")
    public List<String> alertas() { return service.alertas(); }

    private List<ProductoResponse> respuestasDe(List<Producto> productos) {
        return productos.stream().map(ProductoResponse::desde).toList();
    }
}
