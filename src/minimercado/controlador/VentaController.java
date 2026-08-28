package minimercado.controlador;

import java.util.List;
import minimercado.modelo.Cliente;
import minimercado.modelo.Factura;
import minimercado.modelo.MetodoDePago;
import minimercado.servicio.VentaService;

/** Controlador MVC que expone las operaciones necesarias para la pantalla de ventas. */
public class VentaController {
    private final VentaService service;
    public VentaController(VentaService service){this.service=service;}
    public List<Cliente> listarClientes(){return service.listarClientes();}
    public Factura registrar(int producto,int cantidad,Cliente cliente,MetodoDePago pago){return service.registrarVenta(producto,cantidad,cliente,pago);}
    public List<Factura> listar(){return service.listarFacturas();}
}
