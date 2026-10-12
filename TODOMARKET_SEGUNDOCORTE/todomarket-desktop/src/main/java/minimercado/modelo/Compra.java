package minimercado.modelo;
import java.time.LocalDate; import java.util.*;
/** Compra compuesta por detalles. Al registrarla, incrementa el stock de cada producto. */
public class Compra { private int idCompra; private LocalDate fecha=LocalDate.now(); private double total; private Empleado empleado; private final ArrayList<DetalleCompra> detalles=new ArrayList<>(); public Compra(int id,Empleado e){idCompra=id;empleado=e;} public void agregarDetalle(DetalleCompra d){detalles.add(d);calcularTotal();} public double calcularTotal(){return total=detalles.stream().mapToDouble(DetalleCompra::getSubtotal).sum();} public void registrarCompra(){for(DetalleCompra d:detalles)d.getProducto().actualizarStock(d.getCantidad());} }
