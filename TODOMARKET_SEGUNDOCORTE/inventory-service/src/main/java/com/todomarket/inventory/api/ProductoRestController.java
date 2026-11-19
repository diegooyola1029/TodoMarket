package com.todomarket.inventory.api;

import com.todomarket.inventory.producto.Producto;
import com.todomarket.inventory.producto.ProductoService;
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
@Tag(name = "Productos e inventario", description = "Gestión de productos, existencias y alertas.")
public class ProductoRestController {
    private final ProductoService service;

    public ProductoRestController(ProductoService service) { this.service = service; }

    @Operation(summary = "Listar productos", description = "Devuelve todos los productos registrados, ordenados por ID.")
    @ApiResponse(responseCode = "200", description = "Lista de productos", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductoResponse.class))))
    @GetMapping
    public List<ProductoResponse> listar() { return respuestasDe(service.listar()); }

    @Operation(summary = "Consultar producto", description = "Busca un producto por su identificador.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto encontrado", content = @Content(schema = @Schema(implementation = ProductoResponse.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{id}")
    public ProductoResponse buscar(@Parameter(name = "id", in = ParameterIn.PATH, description = "ID del producto", example = "1", required = true) @PathVariable int id) { return ProductoResponse.desde(service.buscar(id)); }

    @Operation(summary = "Crear producto", description = "Crea un producto. El ID se asigna automáticamente; el valor de idProducto del cuerpo se ignora al crear.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Producto creado", content = @Content(schema = @Schema(implementation = ProductoResponse.class), examples = @ExampleObject(value = "{\"idProducto\":6,\"nombre\":\"Avena\",\"descripcion\":\"Avena en hojuelas\",\"precioCompra\":2500,\"precioVenta\":3500,\"stock\":20,\"stockMinimo\":5,\"lote\":\"L006\",\"fechaVencimiento\":\"2027-12-31\",\"categorias\":[\"Granos\"],\"estado\":true}"))),
        @ApiResponse(responseCode = "400", description = "Datos no válidos", content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "409", description = "Ya existe un producto con ese nombre", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Datos del producto", content = @Content(schema = @Schema(implementation = ProductoRequest.class), examples = @ExampleObject(value = "{\"idProducto\":0,\"nombre\":\"Avena\",\"descripcion\":\"Avena en hojuelas\",\"precioCompra\":2500,\"precioVenta\":3500,\"stock\":20,\"stockMinimo\":5,\"lote\":\"L006\",\"fechaVencimiento\":\"2027-12-31\",\"categorias\":[\"Granos\"],\"estado\":true}")))
    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@RequestBody ProductoRequest solicitud) {
        Producto creado = service.crear(solicitud.aProducto(1));
        return ResponseEntity.created(URI.create("/api/productos/" + creado.getIdProducto()))
                .body(ProductoResponse.desde(creado));
    }

    @Operation(summary = "Actualizar producto", description = "Actualiza todos los datos del producto indicado. El ID del cuerpo debe coincidir con el de la ruta.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto actualizado", content = @Content(schema = @Schema(implementation = ProductoResponse.class))),
        @ApiResponse(responseCode = "400", description = "ID de ruta y cuerpo no coinciden", content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "409", description = "Nombre de producto duplicado", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Representación completa del producto; idProducto debe coincidir con la ruta", content = @Content(schema = @Schema(implementation = ProductoRequest.class), examples = @ExampleObject(value = "{\"idProducto\":1,\"nombre\":\"Arroz\",\"descripcion\":\"Arroz blanco 500g\",\"precioCompra\":2800,\"precioVenta\":4000,\"stock\":20,\"stockMinimo\":5,\"lote\":\"L001\",\"fechaVencimiento\":\"2027-12-31\",\"categorias\":[\"Granos\"],\"estado\":true}")))
    @PutMapping("/{id}")
    public ProductoResponse actualizar(@Parameter(name = "id", in = ParameterIn.PATH, description = "ID del producto", example = "1", required = true) @PathVariable int id, @RequestBody ProductoRequest solicitud) {
        if (id != solicitud.idProducto()) throw new IllegalArgumentException("El ID de la ruta y del producto deben coincidir.");
        return ProductoResponse.desde(service.actualizar(id, solicitud.aProducto()));
    }

    @Operation(summary = "Eliminar producto", description = "Elimina el producto y sus alertas asociadas.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Producto eliminado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(name = "id", in = ParameterIn.PATH, description = "ID del producto", example = "1", required = true) @PathVariable int id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Consultar productos con stock bajo", description = "Lista productos con existencias mayores que cero e iguales o inferiores al mínimo.")
    @ApiResponse(responseCode = "200", description = "Productos con stock bajo", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductoResponse.class))))
    @GetMapping("/stock-bajo")
    public List<ProductoResponse> stockBajo() { return respuestasDe(service.stockBajo()); }

    @Operation(summary = "Consultar productos agotados")
    @ApiResponse(responseCode = "200", description = "Productos agotados", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductoResponse.class))))
    @GetMapping("/agotados")
    public List<ProductoResponse> agotados() { return respuestasDe(service.agotados()); }

    @Operation(summary = "Consultar productos vencidos")
    @ApiResponse(responseCode = "200", description = "Productos vencidos", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductoResponse.class))))
    @GetMapping("/vencidos")
    public List<ProductoResponse> vencidos() { return respuestasDe(service.vencidos()); }

    @Operation(summary = "Consultar productos próximos a vencer")
    @ApiResponse(responseCode = "200", description = "Productos próximos a vencer", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductoResponse.class))))
    @GetMapping("/proximos-a-vencer")
    public List<ProductoResponse> proximosAVencer() { return respuestasDe(service.proximosAVencer()); }

    @Operation(summary = "Consultar alertas de inventario", description = "Devuelve los mensajes de alerta generados por el observador de stock.")
    @ApiResponse(responseCode = "200", description = "Mensajes de alerta", content = @Content(array = @ArraySchema(schema = @Schema(type = "string"))))
    @GetMapping("/alertas")
    public List<String> alertas() { return service.alertas(); }

    private List<ProductoResponse> respuestasDe(List<Producto> productos) {
        return productos.stream().map(ProductoResponse::desde).toList();
    }
}
