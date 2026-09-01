package minimercado.vista;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.*;
import javax.swing.table.*;
import minimercado.controlador.ProductoController;
import minimercado.modelo.Categoria;
import minimercado.modelo.InventarioObserver;
import minimercado.modelo.Producto;

/**
 * Vista CRUD de productos.
 * Implementa InventarioObserver (patrón Observer) para recibir alertas
 * automáticas cuando cambia el inventario.
 */
public class ProductoPanel extends JPanel implements InventarioObserver {

    private final ProductoController controller;

    // ── Campos del formulario ─────────────────────────────────────────────────
    private final JTextField fId          = UIFactory.campoTexto();
    private final JTextField fNombre      = UIFactory.campoTexto();
    private final JTextField fDescripcion = UIFactory.campoTexto();
    private final JTextField fCompra      = UIFactory.campoTexto();
    private final JTextField fVenta       = UIFactory.campoTexto();
    private final JTextField fStock       = UIFactory.campoTexto();
    private final JTextField fMinimo      = UIFactory.campoTexto();
    private final JTextField fLote        = UIFactory.campoTexto();
    private final JTextField fVencimiento = UIFactory.campoTexto();

    private final JComboBox<Categoria> fCategoria = new JComboBox<>(new Categoria[]{
            new Categoria(1, "Granos"),  new Categoria(2, "Lácteos"),
            new Categoria(3, "Bebidas"), new Categoria(4, "Aseo"),
            new Categoria(5, "Otros")
    });

    // ── Tarjetas de estadísticas ──────────────────────────────────────────────
    private final JLabel lblTotal    = new JLabel("0");
    private final JLabel lblBajos    = new JLabel("0");
    private final JLabel lblAgotados = new JLabel("0");
    private final JLabel lblVencen   = new JLabel("0");
    private final JLabel lblAlerta   = new JLabel(" ");

    // ── Tabla ─────────────────────────────────────────────────────────────────
    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new String[]{"ID", "Nombre", "Precio Venta", "Stock", "Lote", "Vencimiento", "Categoría", "Estado"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabla = new JTable(modeloTabla);

    public ProductoPanel(ProductoController controller) {
        this.controller = controller;
        construir();
        aplicarEstiloTabla();
        refrescar();
    }

    // ── Observer ──────────────────────────────────────────────────────────────
    @Override
    public void onInventarioCambiado(String evento, Producto producto) {
        // Se actualiza la alerta en el hilo de Swing
        SwingUtilities.invokeLater(() -> {
            String msg = switch (evento) {
                case "CREADO"      -> "✔  Producto \"" + producto.getNombre() + "\" agregado al inventario.";
                case "ACTUALIZADO" -> "✏  Producto \"" + producto.getNombre() + "\" actualizado.";
                case "ELIMINADO"   -> "🗑  Producto \"" + producto.getNombre() + "\" eliminado.";
                default            -> "";
            };
            lblAlerta.setText(msg);
            lblAlerta.setForeground(Tema.VERDE);
        });
    }

    // ── Construcción de la UI ─────────────────────────────────────────────────
    private void construir() {
        setLayout(new BorderLayout(0, 16));
        setBackground(Tema.FONDO);
        setBorder(BorderFactory.createEmptyBorder(24, 26, 24, 26));

        add(panelEncabezado(), BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(0, 16));
        centro.setOpaque(false);
        centro.add(panelFormulario(), BorderLayout.NORTH);
        centro.add(panelInventario(), BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);
    }

    private JPanel panelEncabezado() {
        JPanel p = new JPanel(new BorderLayout(0, 14));
        p.setOpaque(false);

        // Título
        JPanel titulos = new JPanel(new GridLayout(2, 1, 0, 3));
        titulos.setOpaque(false);
        titulos.add(UIFactory.titulo("Gestión de Productos"));
        titulos.add(UIFactory.subtitulo("Registra, consulta y controla el inventario del minimercado."));
        p.add(titulos, BorderLayout.NORTH);

        // Tarjetas de estadísticas
        JPanel stats = new JPanel(new GridLayout(1, 4, 12, 0));
        stats.setOpaque(false);
        stats.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        stats.add(UIFactory.tarjetaStat("Total productos",    lblTotal,    Tema.ACENTO));
        stats.add(UIFactory.tarjetaStat("Stock bajo",        lblBajos,    Tema.NARANJA));
        stats.add(UIFactory.tarjetaStat("Agotados",          lblAgotados, Tema.ROJO));
        stats.add(UIFactory.tarjetaStat("Próximos a vencer", lblVencen,   Tema.MORADO));
        p.add(stats, BorderLayout.CENTER);

        return p;
    }

    private JPanel panelFormulario() {
        JPanel p = UIFactory.tarjeta();
        p.setLayout(new BorderLayout(0, 14));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(UIFactory.sectionTitle("Datos del producto"), BorderLayout.WEST);
        header.add(lblAlerta, BorderLayout.EAST);
        lblAlerta.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        p.add(header, BorderLayout.NORTH);

        // Campos
        fCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        fCategoria.setBackground(Color.WHITE);

        JPanel campos = new JPanel(new GridBagLayout());
        campos.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets  = new Insets(4, 0, 10, 14);

        agregarCampo(campos, gbc, 0, 0, "ID",             fId);
        agregarCampo(campos, gbc, 1, 0, "Nombre",         fNombre);
        agregarCampo(campos, gbc, 2, 0, "Categoría",      fCategoria);
        agregarCampo(campos, gbc, 0, 1, "Precio Compra",  fCompra);
        agregarCampo(campos, gbc, 1, 1, "Precio Venta",   fVenta);
        agregarCampo(campos, gbc, 2, 1, "Descripción",    fDescripcion);
        agregarCampo(campos, gbc, 0, 2, "Stock",          fStock);
        agregarCampo(campos, gbc, 1, 2, "Stock Mínimo",   fMinimo);
        agregarCampo(campos, gbc, 2, 2, "Lote",           fLote);
        agregarCampo(campos, gbc, 0, 3, "Vencimiento (AAAA-MM-DD)", fVencimiento);

        p.add(campos, BorderLayout.CENTER);

        // Botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botones.setOpaque(false);
        JButton btnLimpiar    = UIFactory.boton("Limpiar",     Tema.BTN_LIMPIAR);
        JButton btnEliminar   = UIFactory.boton("Eliminar",    Tema.BTN_ELIMINAR);
        JButton btnActualizar = UIFactory.boton("Actualizar",  Tema.BTN_ACTUALIZAR);
        JButton btnAgregar    = UIFactory.boton("+ Agregar",   Tema.BTN_AGREGAR);
        botones.add(btnLimpiar);
        botones.add(btnEliminar);
        botones.add(btnActualizar);
        botones.add(btnAgregar);
        p.add(botones, BorderLayout.SOUTH);

        // Acciones
        btnAgregar.addActionListener(e    -> ejecutar(() -> controller.agregar(leerProducto())));
        btnActualizar.addActionListener(e -> ejecutar(() -> controller.actualizar(leerProducto())));
        btnEliminar.addActionListener(e   -> confirmarEliminar());
        btnLimpiar.addActionListener(e    -> limpiar());

        return p;
    }

    private JPanel panelInventario() {
        JPanel p = UIFactory.tarjeta();
        p.setLayout(new BorderLayout(0, 12));
        p.add(UIFactory.sectionTitle("Inventario registrado"), BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(Tema.BORDE));
        scroll.getViewport().setBackground(Color.WHITE);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private void aplicarEstiloTabla() {
        tabla.setRowHeight(30);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setForeground(Tema.TEXTO_PRIMARIO);
        tabla.setBackground(Color.WHITE);
        tabla.setGridColor(new Color(235, 238, 245));
        tabla.setShowHorizontalLines(true);
        tabla.setShowVerticalLines(false);
        tabla.setSelectionBackground(new Color(219, 234, 255));
        tabla.setSelectionForeground(Tema.TEXTO_PRIMARIO);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.getTableHeader().setBackground(new Color(235, 240, 250));
        tabla.getTableHeader().setForeground(Tema.TEXTO_PRIMARIO);
        tabla.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Tema.BORDE));

        // Renderizador columna Estado
        tabla.getColumnModel().getColumn(7).setCellRenderer(new EstadoRenderer());

        // Clic en fila → cargar en formulario
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) cargarDesdeTabla();
        });
    }

    // ── Helpers de formulario ─────────────────────────────────────────────────
    private void agregarCampo(JPanel p, GridBagConstraints gbc,
                               int col, int fila, String label, JComponent campo) {
        gbc.gridx = col;
        gbc.gridy = fila;
        p.add(UIFactory.grupoFormulario(label, campo), gbc);
    }

    private Producto leerProducto() {
        try {
            return new Producto(
                    Integer.parseInt(fId.getText().trim()),
                    fNombre.getText(),
                    fDescripcion.getText(),
                    Double.parseDouble(fCompra.getText().trim()),
                    Double.parseDouble(fVenta.getText().trim()),
                    Integer.parseInt(fStock.getText().trim()),
                    Integer.parseInt(fMinimo.getText().trim()),
                    fLote.getText(),
                    fVencimiento.getText().isBlank() ? null : LocalDate.parse(fVencimiento.getText().trim()),
                    (Categoria) fCategoria.getSelectedItem());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("ID, precios y stock deben ser números válidos.");
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("La fecha debe tener formato AAAA-MM-DD.");
        }
    }

    private void cargarDesdeTabla() {
        int id = (int) modeloTabla.getValueAt(tabla.getSelectedRow(), 0);
        Producto p = controller.buscar(id);
        fId.setText("" + p.getIdProducto());
        fNombre.setText(p.getNombre());
        fDescripcion.setText(p.getDescripcion());
        fCompra.setText("" + p.getPrecioCompra());
        fVenta.setText("" + p.getPrecioVenta());
        fStock.setText("" + p.getStock());
        fMinimo.setText("" + p.getStockMinimo());
        fLote.setText(p.getLote());
        fVencimiento.setText(p.getFechaVencimiento() == null ? "" : p.getFechaVencimiento().toString());
        fCategoria.setSelectedItem(p.getCategoria());
    }

    private void ejecutar(Runnable accion) {
        try {
            accion.run();
            refrescar();
            limpiar();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void confirmarEliminar() {
        if (tabla.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto en la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar este producto del inventario?", "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            ejecutar(() -> controller.eliminar(Integer.parseInt(fId.getText().trim())));
        }
    }

    private void limpiar() {
        for (JTextField f : new JTextField[]{fId, fNombre, fDescripcion, fCompra,
                fVenta, fStock, fMinimo, fLote, fVencimiento})
            f.setText("");
        fCategoria.setSelectedIndex(0);
        tabla.clearSelection();
        fNombre.requestFocus();
    }

    public void refrescar() {
        modeloTabla.setRowCount(0);
        for (Producto p : controller.listar()) {
            modeloTabla.addRow(new Object[]{
                    p.getIdProducto(),
                    p.getNombre(),
                    "$ " + String.format("%,.0f", p.getPrecioVenta()),
                    p.getStock(),
                    p.getLote(),
                    p.getFechaVencimiento(),
                    p.getCategoria(),
                    estadoTexto(p)
            });
        }
        lblTotal.setText("" + controller.listar().size());
        lblBajos.setText("" + controller.stockBajo().size());
        lblAgotados.setText("" + controller.agotados().size());
        lblVencen.setText("" + controller.proximosAVencer().size());
    }

    private String estadoTexto(Producto p) {
        if (p.estaAgotado())          return "AGOTADO";
        if (p.tieneStockBajo())       return "STOCK BAJO";
        if (p.estaProximoAVencer())   return "PRÓXIMO A VENCER";
        return "DISPONIBLE";
    }

    // ── Renderer columna Estado ───────────────────────────────────────────────
    private static class EstadoRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable t, Object value, boolean sel, boolean foc, int row, int col) {
            super.getTableCellRendererComponent(t, value, sel, foc, row, col);
            setHorizontalAlignment(SwingConstants.CENTER);
            String txt = String.valueOf(value);
            if (!sel) {
                switch (txt) {
                    case "DISPONIBLE"      -> { setForeground(Tema.VERDE);   setBackground(Tema.VERDE_FONDO); }
                    case "AGOTADO"         -> { setForeground(Tema.ROJO);    setBackground(Tema.ROJO_FONDO); }
                    case "STOCK BAJO"      -> { setForeground(Tema.NARANJA); setBackground(Tema.NARANJA_FONDO); }
                    case "PRÓXIMO A VENCER"-> { setForeground(Tema.MORADO);  setBackground(new Color(238, 230, 255)); }
                    default                -> { setForeground(Tema.TEXTO_PRIMARIO); setBackground(Color.WHITE); }
                }
            }
            setFont(new Font("Segoe UI", Font.BOLD, 11));
            setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            return this;
        }
    }
}
