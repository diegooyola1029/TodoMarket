package com.todomarket.inventory.producto;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MemoriaProductoRepository implements ProductoRepository {
    private final ConcurrentMap<Integer, Producto> productos = new ConcurrentHashMap<>();

    @Override
    public void guardar(Producto producto) {
        if (productos.putIfAbsent(producto.getIdProducto(), producto) != null) {
            throw new IllegalArgumentException("Ya existe un producto con ese ID.");
        }
    }

    @Override
    public List<Producto> listar() {
        return productos.values().stream()
                .sorted(Comparator.comparingInt(Producto::getIdProducto))
                .toList();
    }

    @Override
    public Optional<Producto> buscarPorId(int id) { return Optional.ofNullable(productos.get(id)); }

    @Override
    public void actualizar(Producto producto) {
        if (!productos.containsKey(producto.getIdProducto())) throw new IllegalArgumentException("Producto no encontrado.");
        productos.put(producto.getIdProducto(), producto);
    }

    @Override
    public boolean eliminar(int id) { return productos.remove(id) != null; }
}
