package com.todomarket.inventory.modelo;

import com.todomarket.inventory.producto.Producto;

public interface ProductoObserver {
    void actualizar(Producto producto);
}
