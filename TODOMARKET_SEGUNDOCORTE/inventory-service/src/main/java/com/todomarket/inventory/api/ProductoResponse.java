package com.todomarket.inventory.api;

import com.todomarket.inventory.producto.Producto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;

@Schema(name = "ProductoResponse", description = "Producto devuelto por el servicio de inventario.")
public record ProductoResponse(
        @Schema(description = "Identificador asignado", example = "1") int idProducto,
        @Schema(description = "Nombre", example = "Arroz") String nombre,
        @Schema(description = "Descripción", example = "Arroz blanco 500g") String descripcion,
        @Schema(description = "Precio de compra", example = "2800") double precioCompra,
        @Schema(description = "Precio de venta", example = "4000") double precioVenta,
        @Schema(description = "Existencias", example = "20") int stock,
        @Schema(description = "Stock mínimo", example = "5") int stockMinimo,
        @Schema(description = "Lote", example = "L001") String lote,
        @Schema(description = "Fecha de vencimiento", example = "2027-12-31", nullable = true) LocalDate fechaVencimiento,
        @Schema(description = "Categorías", example = "[\"Granos\"]") List<String> categorias,
        @Schema(description = "Estado activo", example = "true") boolean estado) {

    public static ProductoResponse desde(Producto producto) {
        return new ProductoResponse(producto.getIdProducto(), producto.getNombre(), producto.getDescripcion(),
                producto.getPrecioCompra(), producto.getPrecioVenta(), producto.getStock(),
                producto.getStockMinimo(), producto.getLote(), producto.getFechaVencimiento(),
                producto.getCategorias(), producto.isEstado());
    }
}
