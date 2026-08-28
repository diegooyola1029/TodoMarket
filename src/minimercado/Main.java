package minimercado;

import java.time.LocalDate;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import minimercado.controlador.ProductoController;
import minimercado.controlador.VentaController;
import minimercado.modelo.Categoria;
import minimercado.modelo.Producto;
import minimercado.repositorio.MemoriaProductoRepository;
import minimercado.servicio.ProductoService;
import minimercado.servicio.VentaService;
import minimercado.vista.VentanaPrincipal;

/** Inicia la aplicación, configura el aspecto visual y carga datos de demostración. */
public class Main {
    public static void main(String[] args){try{UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}catch(Exception ignored){}ProductoService servicioProductos=new ProductoService(new MemoriaProductoRepository());cargarDatos(servicioProductos);ProductoController productos=new ProductoController(servicioProductos);VentaController ventas=new VentaController(new VentaService(servicioProductos));SwingUtilities.invokeLater(()->new VentanaPrincipal(productos,ventas).setVisible(true));}
    /** Agrega productos iniciales para que el CRUD y las alertas se puedan demostrar al abrir el programa. */
    private static void cargarDatos(ProductoService s){Categoria granos=new Categoria(1,"Granos"),lacteos=new Categoria(2,"Lácteos"),bebidas=new Categoria(3,"Bebidas");s.crear(new Producto(1,"Arroz","Arroz blanco",3000,4000,20,5,"L001",LocalDate.now().plusMonths(8),granos));s.crear(new Producto(2,"Leche","Leche entera",2500,3500,15,5,"L002",LocalDate.now().plusDays(20),lacteos));s.crear(new Producto(3,"Gaseosa","Bebida gaseosa",3000,4500,8,10,"L003",LocalDate.now().plusMonths(5),bebidas));}
}
