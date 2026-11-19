package minimercado.api.producto;

import java.time.LocalDate;
import java.util.List;
import minimercado.modelo.Producto;

/** Datos de producto que el escritorio envía al backend REST. */
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

    public static ProductoRequest desde(Producto producto) {
        String categoria = producto.getCategoria() == null
                ? "Otros" : producto.getCategoria().getNombre();
        return new ProductoRequest(producto.getIdProducto(), producto.getNombre(), producto.getDescripcion(),
                producto.getPrecioCompra(), producto.getPrecioVenta(), producto.getStock(),
                producto.getStockMinimo(), producto.getLote(), producto.getFechaVencimiento(),
                List.of(categoria), producto.isEstado());
    }
}
