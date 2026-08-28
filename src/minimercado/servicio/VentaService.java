package minimercado.servicio;

import java.util.ArrayList;
import java.util.List;
import minimercado.modelo.Cliente;
import minimercado.modelo.DetalleFactura;
import minimercado.modelo.Factura;
import minimercado.modelo.MetodoDePago;
import minimercado.modelo.Pago;
import minimercado.modelo.Producto;

/**
 * Coordina una venta completa: valida stock, crea pago y factura, y guarda la
 * factura en memoria. El descuento ocurre dentro de Factura tras aprobarse el pago.
 */
public class VentaService {
    private final ProductoService productos;
    private final ArrayList<Cliente> clientes = new ArrayList<>();
    private final ArrayList<Factura> facturas = new ArrayList<>();
    private int siguienteFactura = 1, siguientePago = 1, siguienteDetalle = 1;
    public VentaService(ProductoService productos) { this.productos=productos; cargarClientes(); }
    private void cargarClientes() { clientes.add(new Cliente(1,"Ana Torres","","1001","3000000001","ana@correo.com",1)); clientes.add(new Cliente(2,"Carlos Pérez","","1002","3000000002","carlos@correo.com",2)); clientes.add(new Cliente(3,"Cliente general","","0","","",3)); }
    public List<Cliente> listarClientes(){return List.copyOf(clientes);}
    /** Registra una venta de un producto y devuelve la factura aprobada. */
    public Factura registrarVenta(int idProducto, int cantidad, Cliente cliente, MetodoDePago metodo) {
        if (cliente == null) throw new IllegalArgumentException("Seleccione un cliente.");
        if (cantidad <= 0) throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        Producto producto=productos.buscar(idProducto);
        if (cantidad > producto.getStock()) throw new IllegalArgumentException("Stock insuficiente. Disponibles: " + producto.getStock());
        double subtotal=producto.getPrecioVenta()*cantidad;
        Pago pago=new Pago(siguientePago++, subtotal*1.19, "PAGO-"+siguienteFactura, metodo);
        Factura factura=new Factura(siguienteFactura, String.format("FAC-%04d",siguienteFactura++), cliente, null, pago);
        factura.agregarDetalle(new DetalleFactura(siguienteDetalle++,producto,cantidad,producto.getPrecioVenta(),0));
        if(!factura.generarFactura()) throw new IllegalArgumentException("No fue posible procesar el pago.");
        facturas.add(factura);
        return factura;
    }
    public List<Factura> listarFacturas(){return List.copyOf(facturas);}
}
