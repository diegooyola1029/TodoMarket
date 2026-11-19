package minimercado;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import minimercado.controlador.ProductoController;
import minimercado.controlador.UsuarioController;
import minimercado.modelo.Inventario;
import minimercado.vista.VentanaPrincipal;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

/** Inicia la interfaz original y la conecta con el backend REST. */
public class Main {
    private static final String URL_BACKEND =
            System.getProperty("todomarket.backend.url", "http://localhost:8184");

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::abrirAplicacion);
    }

    private static void abrirAplicacion() {
        try {
            usarNimbus();

            RestClient cliente = crearCliente(URL_BACKEND);
            comprobarServicios(cliente);

            Inventario inventarioLocal = new Inventario();
            ProductoController productos = new ProductoController(cliente, inventarioLocal);
            UsuarioController usuarios = new UsuarioController(cliente);
            new VentanaPrincipal(productos, usuarios, inventarioLocal).setVisible(true);
        } catch (RuntimeException excepcion) {
            JOptionPane.showMessageDialog(null,
                    "No fue posible abrir TodoMarket.\n\n"
                            + excepcion.getMessage()
                            + "\n\nInicia inventory-service (puerto 8183) y todomarket-backend "
                            + "(puerto 8184). Revisa las configuraciones de IntelliJ.",
                    "Servicios no disponibles", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void usarNimbus() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 13));
                    UIManager.put("nimbusBase", new Color(59, 130, 246));
                    UIManager.put("nimbusBlueGrey", new Color(236, 240, 248));
                    UIManager.put("control", new Color(248, 250, 252));
                    UIManager.put("textForeground", new Color(17, 24, 39));
                    return;
                }
            }
        } catch (Exception ignorada) {
            // Si Nimbus no está disponible, se conserva el estilo predeterminado.
        }
    }

    private static RestClient crearCliente(String urlBase) {
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
        return RestClient.builder()
                .baseUrl(urlBase)
                .messageConverters(conversores -> {
                    conversores.removeIf(MappingJackson2HttpMessageConverter.class::isInstance);
                    conversores.add(0, new MappingJackson2HttpMessageConverter(mapper));
                })
                .build();
    }

    private static void comprobarServicios(RestClient cliente) {
        try {
            cliente.get().uri("/api/usuarios").retrieve().body(List.class);
        } catch (RuntimeException excepcion) {
            throw new IllegalArgumentException(
                    "No responde todomarket-backend en " + URL_BACKEND + ". "
                            + "Inicia TodoMarketBackendApplication y revisa el puerto 8184.", excepcion);
        }

        try {
            cliente.get().uri("/api/productos").retrieve().body(List.class);
        } catch (RuntimeException excepcion) {
            throw new IllegalArgumentException(
                    "El backend responde, pero no puede consultar el inventario. "
                            + "Inicia InventoryApplication y revisa el puerto 8183.", excepcion);
        }
    }
}
