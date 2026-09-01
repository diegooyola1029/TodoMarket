package minimercado.vista;

import java.awt.*;
import javax.swing.*;
import minimercado.controlador.ProductoController;
import minimercado.controlador.UsuarioController;
import minimercado.modelo.Inventario;

/**
 * Ventana principal del sistema.
 * MVC: orquesta los paneles y les pasa sus controladores.
 * Registra ProductoPanel como Observer del Inventario.
 */
public class VentanaPrincipal extends JFrame {

    private final ProductoController productos;
    private final UsuarioController  usuarios;
    private final Inventario         inventario;

    public VentanaPrincipal(ProductoController productos,
                            UsuarioController usuarios,
                            Inventario inventario) {
        this.productos  = productos;
        this.usuarios   = usuarios;
        this.inventario = inventario;
        configurar();
        construir();
    }

    private void configurar() {
        setTitle("TodoMarket — Sistema de Gestión");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 720));
        setSize(1200, 780);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Tema.FONDO);
    }

    private void construir() {
        setLayout(new BorderLayout());

        // Paneles de cada módulo
        ProductoPanel productosPanel = new ProductoPanel(productos);
        UsuarioPanel  usuariosPanel  = new UsuarioPanel(usuarios);

        // Registrar vista de productos como Observer (patrón Observer)
        inventario.agregarObservador(productosPanel);

        // Contenido central con CardLayout
        JPanel contenido = new JPanel(new CardLayout());
        contenido.setBackground(Tema.FONDO);
        contenido.add(usuariosPanel,  "usuarios");
        contenido.add(productosPanel, "productos");

        CardLayout cl = (CardLayout) contenido.getLayout();

        // Sidebar con navegación
        JPanel sidebar = crearSidebar();

        // Vincular botones del sidebar a las cartas
        for (Component c : sidebar.getComponents()) {
            if (c instanceof JButton btn) {
                String key = (String) btn.getClientProperty("card");
                if (key != null) {
                    btn.addActionListener(e -> {
                        cl.show(contenido, key);
                        if ("productos".equals(key)) productosPanel.refrescar();
                        if ("usuarios".equals(key))  usuariosPanel.refrescar();
                        marcarActivo(btn, sidebar);
                    });
                }
            }
        }

        // Activar primer botón por defecto
        activarPrimero(sidebar);

        add(sidebar,   BorderLayout.WEST);
        add(contenido, BorderLayout.CENTER);
    }

    // ── Sidebar ───────────────────────────────────────────────────────────────
    private JPanel crearSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Tema.SIDEBAR);
        sidebar.setPreferredSize(new Dimension(210, 0));

        // Encabezado
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Tema.SIDEBAR_HEADER);
        header.setBorder(BorderFactory.createEmptyBorder(22, 20, 22, 20));
        header.setMaximumSize(new Dimension(210, 88));

        JLabel appName  = new JLabel("TodoMarket");
        appName.setFont(new Font("Segoe UI", Font.BOLD, 20));
        appName.setForeground(Color.WHITE);

        JLabel tagline  = new JLabel("Sistema de Gestión");
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tagline.setForeground(new Color(170, 195, 230));

        JPanel titles = new JPanel(new GridLayout(2, 1, 0, 2));
        titles.setOpaque(false);
        titles.add(appName);
        titles.add(tagline);
        header.add(titles, BorderLayout.CENTER);
        sidebar.add(header);

        sidebar.add(Box.createVerticalStrut(14));
        sidebar.add(seccionLabel("MÓDULOS"));

        sidebar.add(navBtn("Usuarios",  "usuarios"));
        sidebar.add(navBtn("Productos", "productos"));

        sidebar.add(Box.createVerticalGlue());

        // Footer
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Tema.SIDEBAR);
        footer.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        footer.setMaximumSize(new Dimension(210, 54));
        JLabel ver = new JLabel("v1.0  •  Corte 1");
        ver.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        ver.setForeground(new Color(120, 145, 185));
        footer.add(ver);
        sidebar.add(footer);

        return sidebar;
    }

    private JButton navBtn(String texto, String card) {
        JButton btn = new JButton(texto);
        btn.putClientProperty("card", card);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(new Color(195, 215, 245));
        btn.setBackground(Tema.SIDEBAR);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 12));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(210, 48));
        btn.setHorizontalAlignment(SwingConstants.LEFT);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!Boolean.TRUE.equals(btn.getClientProperty("activo")))
                    btn.setBackground(Tema.SIDEBAR_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (!Boolean.TRUE.equals(btn.getClientProperty("activo")))
                    btn.setBackground(Tema.SIDEBAR);
            }
        });
        return btn;
    }

    private JLabel seccionLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(new Color(110, 140, 180));
        lbl.setBorder(BorderFactory.createEmptyBorder(6, 24, 4, 12));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void marcarActivo(JButton activo, JPanel sidebar) {
        for (Component c : sidebar.getComponents()) {
            if (c instanceof JButton btn) {
                btn.putClientProperty("activo", false);
                btn.setBackground(Tema.SIDEBAR);
                btn.setForeground(new Color(195, 215, 245));
            }
        }
        activo.putClientProperty("activo", true);
        activo.setBackground(Tema.ACENTO);
        activo.setForeground(Color.WHITE);
    }

    private void activarPrimero(JPanel sidebar) {
        for (Component c : sidebar.getComponents()) {
            if (c instanceof JButton btn && btn.getClientProperty("card") != null) {
                marcarActivo(btn, sidebar);
                break;
            }
        }
    }
}
