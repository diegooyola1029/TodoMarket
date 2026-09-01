package minimercado;

import java.time.LocalDate;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import minimercado.controlador.ProductoController;
import minimercado.controlador.UsuarioController;
import minimercado.modelo.Administrador;
import minimercado.modelo.Categoria;
import minimercado.modelo.Cliente;
import minimercado.modelo.Empleado;
import minimercado.modelo.Inventario;
import minimercado.modelo.Producto;
import minimercado.repositorio.MemoriaProductoRepository;
import minimercado.repositorio.MemoriaUsuarioRepository;
import minimercado.servicio.ProductoService;
import minimercado.servicio.UsuarioService;
import minimercado.vista.VentanaPrincipal;

/**
 * Punto de entrada de la aplicación.
 * Ensambla todas las capas: repositorio → servicio → controlador → vista.
 */
public class Main {

    public static void main(String[] args) {
        // Nimbus respeta colores personalizados; el L&F nativo de Windows los ignora
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        // Inventario observable — patrón Observer
        Inventario inventario = new Inventario();

        // Repositorios Singleton
        MemoriaProductoRepository repoProductos = MemoriaProductoRepository.getInstancia();
        MemoriaUsuarioRepository  repoUsuarios  = MemoriaUsuarioRepository.getInstancia();

        // Servicios
        ProductoService servicioProductos = new ProductoService(repoProductos, inventario);
        UsuarioService  servicioUsuarios  = new UsuarioService(repoUsuarios);

        // Datos de demostración
        cargarProductos(servicioProductos);
        cargarUsuarios(servicioUsuarios);

        // Controladores
        ProductoController ctrlProductos = new ProductoController(servicioProductos);
        UsuarioController  ctrlUsuarios  = new UsuarioController(servicioUsuarios);

        // La ventana recibe el inventario para registrar la vista como Observer
        SwingUtilities.invokeLater(() ->
                new VentanaPrincipal(ctrlProductos, ctrlUsuarios, inventario).setVisible(true));
    }

    private static void cargarProductos(ProductoService s) {
        Categoria granos  = new Categoria(1, "Granos");
        Categoria lacteos = new Categoria(2, "Lácteos");
        Categoria bebidas = new Categoria(3, "Bebidas");
        Categoria aseo    = new Categoria(4, "Aseo");

        s.crear(new Producto(1, "Arroz",    "Arroz blanco 500g",  2800, 4000, 20, "L001", LocalDate.now().plusMonths(8),  granos));
        s.crear(new Producto(2, "Leche",    "Leche entera 1L",    2300, 3500, 15, "L002", LocalDate.now().plusDays(20),   lacteos));
        s.crear(new Producto(3, "Gaseosa",  "Gaseosa 2L",         2800, 4500,  8, "L003", LocalDate.now().plusMonths(5),  bebidas));
        s.crear(new Producto(4, "Jabon",    "Jabon de tocador",   1500, 2500, 30, "L004", LocalDate.now().plusMonths(12), aseo));
        s.crear(new Producto(5, "Frijoles", "Frijoles rojos 1kg", 3200, 4800,  3, "L005", LocalDate.now().plusMonths(6),  granos));
    }

    private static void cargarUsuarios(UsuarioService s) {
        s.crear(new Administrador(1, "admin",   "admin123", "1000001", "3001111001", "admin@gmail.com",   1, "Gerente"));
        s.crear(new Empleado    (2, "cajero",   "caj123",   "1000002", "3001111002", "cajero@gmail.com",  2, "Cajero"));
        s.crear(new Cliente     (3, "cliente",  "cli123",   "1000003", "3001111003", "cliente@hotmail.com", 3));
    }
}
