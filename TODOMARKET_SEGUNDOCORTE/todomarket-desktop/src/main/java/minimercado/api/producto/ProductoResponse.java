package minimercado.api.producto;

import java.time.LocalDate;
import java.util.List;
import minimercado.modelo.Categoria;
import minimercado.modelo.Producto;

/** Datos de producto recibidos desde la API y convertidos al modelo original. */
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

    public Producto aProducto() {
        String nombreCategoria = categorias == null || categorias.isEmpty()
                ? "Otros" : categorias.get(0);
        Categoria categoria = new Categoria(idCategoria(nombreCategoria), nombreCategoria);

        Producto producto = new Producto(idProducto, nombre, descripcion, precioCompra, precioVenta,
                stock, lote, fechaVencimiento, categoria);
        producto.setEstado(estado);
        return producto;
    }

    private static int idCategoria(String nombre) {
        return switch (nombre.toLowerCase()) {
            case "granos" -> 1;
            case "lácteos", "lacteos" -> 2;
            case "bebidas" -> 3;
            case "aseo" -> 4;
            default -> 5;
        };
    }
}
