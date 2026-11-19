package minimercado.api.producto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;

@Schema(name = "ProductoRequest", description = "Datos de entrada para el proxy de productos; se reenvían a inventory-service.")
public record ProductoRequest(
        @Schema(description = "ID usado al actualizar", example = "1") int idProducto,
        @Schema(description = "Nombre único", example = "Arroz") String nombre,
        @Schema(description = "Descripción", example = "Arroz blanco 500g") String descripcion,
        @Schema(description = "Precio de compra", example = "2800") double precioCompra,
        @Schema(description = "Precio de venta", example = "4000") double precioVenta,
        @Schema(description = "Existencias", example = "20") int stock,
        @Schema(description = "Stock mínimo", example = "5") int stockMinimo,
        @Schema(description = "Lote", example = "L001") String lote,
        @Schema(description = "Fecha de vencimiento ISO-8601", example = "2027-12-31", nullable = true) LocalDate fechaVencimiento,
        @Schema(description = "Categorías", example = "[\"Granos\"]") List<String> categorias,
        @Schema(description = "Estado activo", example = "true") boolean estado) { }
