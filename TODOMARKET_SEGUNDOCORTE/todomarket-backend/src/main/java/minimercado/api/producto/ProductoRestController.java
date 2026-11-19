package minimercado.api.producto;

import java.net.URI;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Proxy de productos", description = "Operaciones de producto delegadas al microservicio inventory-service.")
public class ProductoRestController {
    private final InventarioClient inventario;

    public ProductoRestController(InventarioClient inventario) { this.inventario = inventario; }

    @Operation(summary = "Listar productos (proxy)", description = "Obtiene los productos desde inventory-service.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de productos", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductoResponse.class)))),
        @ApiResponse(responseCode = "503", description = "Servicio de inventario no disponible", content = @Content(schema = @Schema(implementation = minimercado.api.ApiError.class)))
    })
    @GetMapping
    public List<ProductoResponse> listar() { return inventario.listar(); }

    @Operation(summary = "Consultar producto (proxy)", description = "Busca un producto en inventory-service por ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto encontrado", content = @Content(schema = @Schema(implementation = ProductoResponse.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content(schema = @Schema(implementation = minimercado.api.ApiError.class))),
        @ApiResponse(responseCode = "503", description = "Servicio de inventario no disponible", content = @Content(schema = @Schema(implementation = minimercado.api.ApiError.class)))
    })
    @GetMapping("/{id}")
    public ProductoResponse buscar(@Parameter(name = "id", in = ParameterIn.PATH, description = "ID del producto", example = "1", required = true) @PathVariable int id) { return inventario.buscar(id); }

    @Operation(summary = "Crear producto (proxy)", description = "Reenvía la solicitud de creación a inventory-service.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Producto creado", content = @Content(schema = @Schema(implementation = ProductoResponse.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud no válida", content = @Content(schema = @Schema(implementation = minimercado.api.ApiError.class))),
        @ApiResponse(responseCode = "409", description = "Producto duplicado", content = @Content(schema = @Schema(implementation = minimercado.api.ApiError.class))),
        @ApiResponse(responseCode = "503", description = "Servicio de inventario no disponible", content = @Content(schema = @Schema(implementation = minimercado.api.ApiError.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Datos del producto, con la forma del DTO del servicio de inventario", content = @Content(schema = @Schema(implementation = ProductoRequest.class), examples = @ExampleObject(value = "{\"idProducto\":0,\"nombre\":\"Avena\",\"descripcion\":\"Avena en hojuelas\",\"precioCompra\":2500,\"precioVenta\":3500,\"stock\":20,\"stockMinimo\":5,\"lote\":\"L006\",\"fechaVencimiento\":\"2027-12-31\",\"categorias\":[\"Granos\"],\"estado\":true}")))
    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@RequestBody ProductoRequest solicitud) {
        ProductoResponse creado = inventario.crear(solicitud);
        return ResponseEntity.created(URI.create("/api/productos/" + creado.idProducto())).body(creado);
    }

    @Operation(summary = "Actualizar producto (proxy)", description = "Reenvía la actualización a inventory-service. El ID del cuerpo debe coincidir con el de la ruta.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto actualizado", content = @Content(schema = @Schema(implementation = ProductoResponse.class))),
        @ApiResponse(responseCode = "400", description = "ID de ruta y cuerpo no coinciden", content = @Content(schema = @Schema(implementation = minimercado.api.ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content(schema = @Schema(implementation = minimercado.api.ApiError.class))),
        @ApiResponse(responseCode = "503", description = "Servicio de inventario no disponible", content = @Content(schema = @Schema(implementation = minimercado.api.ApiError.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Representación completa del producto; idProducto debe coincidir con la ruta", content = @Content(schema = @Schema(implementation = ProductoRequest.class), examples = @ExampleObject(value = "{\"idProducto\":1,\"nombre\":\"Arroz\",\"descripcion\":\"Arroz blanco 500g\",\"precioCompra\":2800,\"precioVenta\":4000,\"stock\":20,\"stockMinimo\":5,\"lote\":\"L001\",\"fechaVencimiento\":\"2027-12-31\",\"categorias\":[\"Granos\"],\"estado\":true}")))
    @PutMapping("/{id}")
    public ProductoResponse actualizar(@Parameter(name = "id", in = ParameterIn.PATH, description = "ID del producto", example = "1", required = true) @PathVariable int id, @RequestBody ProductoRequest solicitud) {
        if (id != solicitud.idProducto()) throw new IllegalArgumentException("El ID de la ruta y del producto deben coincidir.");
        return inventario.actualizar(id, solicitud);
    }

    @Operation(summary = "Eliminar producto (proxy)", description = "Solicita a inventory-service eliminar el producto.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Producto eliminado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content(schema = @Schema(implementation = minimercado.api.ApiError.class))),
        @ApiResponse(responseCode = "503", description = "Servicio de inventario no disponible", content = @Content(schema = @Schema(implementation = minimercado.api.ApiError.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(name = "id", in = ParameterIn.PATH, description = "ID del producto", example = "1", required = true) @PathVariable int id) {
        inventario.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Consultar productos con stock bajo (proxy)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Productos con stock bajo", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductoResponse.class)))),
        @ApiResponse(responseCode = "503", description = "Servicio de inventario no disponible", content = @Content(schema = @Schema(implementation = minimercado.api.ApiError.class)))
    })
    @GetMapping("/stock-bajo")
    public List<ProductoResponse> stockBajo() { return inventario.stockBajo(); }

    @Operation(summary = "Consultar productos agotados (proxy)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Productos agotados", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductoResponse.class)))),
        @ApiResponse(responseCode = "503", description = "Servicio de inventario no disponible", content = @Content(schema = @Schema(implementation = minimercado.api.ApiError.class)))
    })
    @GetMapping("/agotados")
    public List<ProductoResponse> agotados() { return inventario.agotados(); }

    @Operation(summary = "Consultar productos vencidos (proxy)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Productos vencidos", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductoResponse.class)))),
        @ApiResponse(responseCode = "503", description = "Servicio de inventario no disponible", content = @Content(schema = @Schema(implementation = minimercado.api.ApiError.class)))
    })
    @GetMapping("/vencidos")
    public List<ProductoResponse> vencidos() { return inventario.vencidos(); }

    @Operation(summary = "Consultar productos próximos a vencer (proxy)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Productos próximos a vencer", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductoResponse.class)))),
        @ApiResponse(responseCode = "503", description = "Servicio de inventario no disponible", content = @Content(schema = @Schema(implementation = minimercado.api.ApiError.class)))
    })
    @GetMapping("/proximos-a-vencer")
    public List<ProductoResponse> proximosAVencer() { return inventario.proximosAVencer(); }

    @Operation(summary = "Consultar alertas de inventario (proxy)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Alertas de inventario", content = @Content(array = @ArraySchema(schema = @Schema(type = "string")))),
        @ApiResponse(responseCode = "503", description = "Servicio de inventario no disponible", content = @Content(schema = @Schema(implementation = minimercado.api.ApiError.class)))
    })
    @GetMapping("/alertas")
    public List<String> alertas() { return inventario.alertas(); }
}
