package com.todomarket.inventory.api;

import com.todomarket.inventory.producto.Producto;
import java.time.LocalDate;
import java.util.List;

public record ProductoRequest(
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

    public Producto aProducto() {
        return aProducto(idProducto);
    }

    public Producto aProducto(int id) {
        Producto producto = new Producto(id, nombre, descripcion, precioCompra, precioVenta,
                stock, stockMinimo, lote, fechaVencimiento, categorias);
        producto.setEstado(estado);
        return producto;
    }
}
