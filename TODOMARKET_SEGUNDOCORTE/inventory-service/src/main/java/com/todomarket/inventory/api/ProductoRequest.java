package com.todomarket.inventory.api;

import com.todomarket.inventory.producto.Producto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;

@Schema(name = "ProductoRequest", description = "Datos de entrada para crear o actualizar un producto.")
public record ProductoRequest(
        @Schema(description = "ID usado en actualización; al crear lo asigna el servicio", example = "1") int idProducto,
        @Schema(description = "Nombre único del producto", example = "Arroz") String nombre,
        @Schema(description = "Descripción del producto", example = "Arroz blanco 500g") String descripcion,
        @Schema(description = "Precio de compra", example = "2800") double precioCompra,
        @Schema(description = "Precio de venta", example = "4000") double precioVenta,
        @Schema(description = "Unidades en existencia", example = "20") int stock,
        @Schema(description = "Umbral mínimo de stock", example = "5") int stockMinimo,
        @Schema(description = "Código de lote", example = "L001") String lote,
        @Schema(description = "Fecha de vencimiento ISO-8601", example = "2027-12-31", nullable = true) LocalDate fechaVencimiento,
        @Schema(description = "Categorías del producto", example = "[\"Granos\"]") List<String> categorias,
        @Schema(description = "Indica si el producto está activo", example = "true") boolean estado) {

    public Producto aProducto() { return aProducto(idProducto); }

    public Producto aProducto(int id) {
        Producto producto = new Producto(id, nombre, descripcion, precioCompra, precioVenta,
                stock, stockMinimo, lote, fechaVencimiento, categorias);
        producto.setEstado(estado);
        return producto;
    }
}
