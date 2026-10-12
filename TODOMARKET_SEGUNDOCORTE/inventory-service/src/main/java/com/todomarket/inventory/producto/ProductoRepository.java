package com.todomarket.inventory.producto;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository {
    void guardar(Producto producto);
    List<Producto> listar();
    Optional<Producto> buscarPorId(int id);
    void actualizar(Producto producto);
    boolean eliminar(int id);
}
