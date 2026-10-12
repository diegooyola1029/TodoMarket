package com.todomarket.inventory.api;

import com.todomarket.inventory.producto.Producto;
import java.time.LocalDate;
import java.util.List;

public record ProductoResponse(
        int idProducto,
        String nombre,
        String descripcion,
        double precioCompra,
        double precioVenta,
        int stock,
        int stockMinimo,
        String lote,
        LocalDate fechaVencimiento,
        List<String> categorias,
        boolean estado) {

    public static ProductoResponse desde(Producto producto) {
        return new ProductoResponse(producto.getIdProducto(), producto.getNombre(), producto.getDescripcion(),
                producto.getPrecioCompra(), producto.getPrecioVenta(), producto.getStock(),
                producto.getStockMinimo(), producto.getLote(), producto.getFechaVencimiento(),
                producto.getCategorias(), producto.isEstado());
    }
}
