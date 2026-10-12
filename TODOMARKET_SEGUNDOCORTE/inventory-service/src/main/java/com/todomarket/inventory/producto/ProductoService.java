package com.todomarket.inventory.producto;

import com.todomarket.inventory.modelo.AlertaStockObserver;
import java.util.List;

public class ProductoService {
    private final ProductoRepository repository;
    private final AlertaStockObserver alertaObserver;

    public ProductoService(ProductoRepository repository, AlertaStockObserver alertaObserver) {
        this.repository = repository;
        this.alertaObserver = alertaObserver;
    }

    public Producto crear(Producto producto) {
        if (listar().stream().anyMatch(existente ->
                existente.getNombre().equalsIgnoreCase(producto.getNombre()))) {
            throw new IllegalArgumentException("Ya existe un producto con ese nombre.");
        }
        producto.agregarObservador(alertaObserver);
        repository.guardar(producto);
        producto.notificarObservadores();
        return producto;
    }

    public List<Producto> listar() { return repository.listar(); }
    public Producto buscar(int id) {
        return repository.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado."));
    }

    public Producto actualizar(int id, Producto datos) {
        Producto actual = buscar(id);
        if (listar().stream().anyMatch(existente -> existente.getIdProducto() != id
                && existente.getNombre().equalsIgnoreCase(datos.getNombre()))) {
            throw new IllegalArgumentException("Ya existe otro producto con ese nombre.");
        }
        actual.setNombre(datos.getNombre());
        actual.setDescripcion(datos.getDescripcion());
        actual.setPrecioCompra(datos.getPrecioCompra());
        actual.setPrecioVenta(datos.getPrecioVenta());
        actual.setStock(datos.getStock());
        actual.setStockMinimo(datos.getStockMinimo());
        actual.setLote(datos.getLote());
        actual.setFechaVencimiento(datos.getFechaVencimiento());
        actual.setCategorias(datos.getCategorias());
        actual.setEstado(datos.isEstado());
        repository.actualizar(actual);
        return actual;
    }

    public void eliminar(int id) {
        if (!repository.eliminar(id)) throw new IllegalArgumentException("Producto no encontrado.");
        alertaObserver.eliminarAlertasDeProducto(id);
    }

    public List<Producto> stockBajo() {
        return listar().stream().filter(producto -> producto.getStock() > 0 && producto.tieneStockBajo()).toList();
    }
    public List<Producto> agotados() { return listar().stream().filter(Producto::estaAgotado).toList(); }
    public List<Producto> vencidos() { return listar().stream().filter(Producto::estaVencido).toList(); }
    public List<Producto> proximosAVencer() { return listar().stream().filter(Producto::estaProximoAVencer).toList(); }
    public List<String> alertas() { return alertaObserver.getAlertas(); }
}
