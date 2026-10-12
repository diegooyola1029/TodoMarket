package minimercado.api.producto;

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

}
