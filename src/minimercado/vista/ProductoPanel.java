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
 * Todo el contenido vive dentro de un JScrollPane externo.
 * Implementa InventarioObserver (patrón Observer).
 */
public class ProductoPanel extends JPanel implements InventarioObserver {

    private final ProductoController controller;

    // ── Campos ────────────────────────────────────────────────────────────────
    private final JTextField fId          = UIFactory.campoTexto();
    private final JTextField fNombre      = UIFactory.campoTexto();
    private final JTextField fDescripcion = UIFactory.campoTexto();
    private final JTextField fCompra      = UIFactory.campoTexto();
    private final JTextField fVenta       = UIFactory.campoTexto();
    private final JTextField fStock       = UIFactory.campoTexto();
    private final JTextField fLote        = UIFactory.campoTexto();
    private final JTextField fVencimiento = UIFactory.campoFecha(); // ← calendario popup
    private final JComboBox<Categoria> fCategoria = new JComboBox<>(new Categoria[]{
            new Categoria(1, "Granos"),  new Categoria(2, "Lácteos"),
            new Categoria(3, "Bebidas"), new Categoria(4, "Aseo"),
            new Categoria(5, "Otros")
    });

    // ── Stats ─────────────────────────────────────────────────────────────────
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
        estilizarTabla();
        adjuntarValidaciones();
        refrescar();
    }

    @Override
    public void onInventarioCambiado(String evento, Producto producto) {
        SwingUtilities.invokeLater(() -> {
            String msg = switch (evento) {
                case "CREADO"      -> "+ \"" + producto.getNombre() + "\" agregado.";
                case "ACTUALIZADO" -> "~ \"" + producto.getNombre() + "\" actualizado.";
                case "ELIMINADO"   -> "- \"" + producto.getNombre() + "\" eliminado.";
                default            -> "";
            };
            lblAlerta.setText(msg);
            lblAlerta.setForeground(Tema.VERDE);
        });
    }

    private void construir() {
        setLayout(new BorderLayout());
        setBackground(Tema.FONDO);

        JPanel interior = new JPanel();
        interior.setLayout(new BoxLayout(interior, BoxLayout.Y_AXIS));
        interior.setBackground(Tema.FONDO);
        interior.setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));

        interior.add(panelTitulo());
        interior.add(Box.createVerticalStrut(14));
        interior.add(panelStats());
        interior.add(Box.createVerticalStrut(14));
        interior.add(panelFormulario());
        interior.add(Box.createVerticalStrut(14));
        interior.add(panelTabla());

        JScrollPane scroll = new JScrollPane(interior);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getViewport().setBackground(Tema.FONDO);

        add(scroll, BorderLayout.CENTER);
    }

    private JPanel panelTitulo() {
        JPanel p = new JPanel(new GridLayout(2, 1, 0, 4));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        p.add(UIFactory.titulo("Gestión de Productos"));
        p.add(UIFactory.subtitulo("Registra, consulta y controla el inventario del minimercado."));
        return p;
    }

    private JPanel panelStats() {
        JPanel p = new JPanel(new GridLayout(1, 4, 10, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        p.add(UIFactory.tarjetaStat("Total productos",   lblTotal,    Tema.ACENTO));
        p.add(UIFactory.tarjetaStat("Stock bajo",        lblBajos,    Tema.NARANJA));
        p.add(UIFactory.tarjetaStat("Agotados",          lblAgotados, Tema.ROJO));
        p.add(UIFactory.tarjetaStat("Vencidos",          lblVencen,   new Color(120, 40, 40)));
        return p;
    }

    private JPanel panelFormulario() {
        JPanel p = new JPanel(new BorderLayout(0, 14));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.BORDE, 1),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JPanel fHead = new JPanel(new BorderLayout());
        fHead.setBackground(Color.WHITE);
        fHead.add(UIFactory.sectionTitle("Datos del producto"), BorderLayout.WEST);
        lblAlerta.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        fHead.add(lblAlerta, BorderLayout.EAST);
        p.add(fHead, BorderLayout.NORTH);

        fCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPanel campos = new JPanel(new GridBagLayout());
        campos.setBackground(Color.WHITE);
        GridBagConstraints c = new GridBagConstraints();
        c.fill    = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets  = new Insets(4, 0, 10, 14);

        campo(campos, c, 0, 0, "ID",                       fId);
        campo(campos, c, 1, 0, "Nombre",                   fNombre);
        campo(campos, c, 2, 0, "Categoría",                fCategoria);
        campo(campos, c, 0, 1, "Precio Compra ($)",        fCompra);
        campo(campos, c, 1, 1, "Precio Venta ($)",         fVenta);
        campo(campos, c, 2, 1, "Descripción",              fDescripcion);
        campo(campos, c, 0, 2, "Stock",                    fStock);
        campo(campos, c, 1, 2, "Lote",                     fLote);
        campo(campos, c, 2, 2, "Fecha de vencimiento",     fVencimiento);

        p.add(campos, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botones.setBackground(Color.WHITE);
        JButton btnLimpiar    = UIFactory.boton("Limpiar",    Tema.BTN_LIMPIAR);
        JButton btnEliminar   = UIFactory.boton("Eliminar",   Tema.BTN_ELIMINAR);
        JButton btnActualizar = UIFactory.boton("Actualizar", Tema.BTN_ACTUALIZAR);
        JButton btnAgregar    = UIFactory.boton("+ Agregar",  Tema.BTN_AGREGAR);
        botones.add(btnLimpiar);
        botones.add(btnEliminar);
        botones.add(btnActualizar);
        botones.add(btnAgregar);
        p.add(botones, BorderLayout.SOUTH);

        btnLimpiar.addActionListener(e    -> limpiar());
        btnEliminar.addActionListener(e   -> confirmarEliminar());
        btnActualizar.addActionListener(e -> ejecutar(() -> controller.actualizar(leer())));
        btnAgregar.addActionListener(e    -> ejecutar(() -> controller.agregar(leer())));

        return p;
    }

    private JPanel panelTabla() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.BORDE, 1),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)));

        int altura = 36 + (32 * 7) + 36;
        p.setPreferredSize(new Dimension(100, altura));
        p.setMinimumSize(new Dimension(100, altura));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, altura));

        p.add(UIFactory.sectionTitle("Inventario registrado"), BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(Tema.BORDE));
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private void estilizarTabla() {
        tabla.setRowHeight(32);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setForeground(Tema.TEXTO_PRIMARIO);
        tabla.setBackground(Color.WHITE);
        tabla.setGridColor(new Color(235, 240, 248));
        tabla.setShowHorizontalLines(true);
        tabla.setShowVerticalLines(false);
        tabla.setSelectionBackground(new Color(210, 228, 255));
        tabla.setSelectionForeground(Tema.TEXTO_PRIMARIO);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setIntercellSpacing(new Dimension(0, 1));
        tabla.setFillsViewportHeight(true);

        JTableHeader th = tabla.getTableHeader();
        th.setReorderingAllowed(false);
        th.setFont(new Font("Segoe UI", Font.BOLD, 12));
        th.setBackground(new Color(234, 240, 252));
        th.setForeground(new Color(40, 55, 90));
        th.setPreferredSize(new Dimension(0, 36));

        tabla.getColumnModel().getColumn(7).setCellRenderer(new EstadoRenderer());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0)
                cargarDesdeTabla();
        });
    }

    private void campo(JPanel p, GridBagConstraints c, int col, int fila,
                       String label, JComponent campo) {
        c.gridx = col; c.gridy = fila;
        p.add(UIFactory.grupoFormulario(label, campo), c);
    }

    private Producto leer() {
        try {
            return new Producto(
                    Integer.parseInt(fId.getText().trim()),
                    fNombre.getText(),
                    fDescripcion.getText(),
                    Double.parseDouble(fCompra.getText().trim()),
                    Double.parseDouble(fVenta.getText().trim()),
                    Integer.parseInt(fStock.getText().trim()),
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
            JOptionPane.showMessageDialog(this,
                    "Seleccione un producto en la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int op = JOptionPane.showConfirmDialog(this,
                "¿Eliminar este producto?", "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (op == JOptionPane.YES_OPTION)
            ejecutar(() -> controller.eliminar(Integer.parseInt(fId.getText().trim())));
    }

    // ── Validación en tiempo real ─────────────────────────────────────────────
    private void adjuntarValidaciones() {
        UIFactory.validarCampo(fId, v -> {
            if (v.isBlank()) return null;
            try { Integer.parseInt(v); return null; }
            catch (NumberFormatException e) { return "El ID debe ser un número entero."; }
        });
        UIFactory.validarCampo(fNombre, v -> {
            if (v.isBlank()) return null;
            return v.trim().matches("[\\p{L} ]+") ? null : "Solo letras y espacios.";
        });
        UIFactory.validarCampo(fCompra, v -> {
            if (v.isBlank()) return null;
            try { double d = Double.parseDouble(v); return d >= 0 ? null : "Debe ser >= 0."; }
            catch (NumberFormatException e) { return "Debe ser un número (ej: 1500)."; }
        });
        UIFactory.validarCampo(fVenta, v -> {
            if (v.isBlank()) return null;
            try { double d = Double.parseDouble(v); return d >= 0 ? null : "Debe ser >= 0."; }
            catch (NumberFormatException e) { return "Debe ser un número (ej: 2000)."; }
        });
        UIFactory.validarCampo(fStock, v -> {
            if (v.isBlank()) return null;
            try { int i = Integer.parseInt(v); return i >= 0 ? null : "Debe ser >= 0."; }
            catch (NumberFormatException e) { return "Debe ser un número entero (ej: 10)."; }
        });
        UIFactory.validarCampo(fVencimiento, v -> {
            if (v.isBlank()) return null;
            return v.matches("\\d{4}-\\d{2}-\\d{2}") ? null : "Formato: AAAA-MM-DD (ej: 2026-12-31).";
        });
    }

    private void limpiar() {
        for (JTextField f : new JTextField[]{fId, fNombre, fDescripcion, fCompra,
                fVenta, fStock, fLote, fVencimiento}) {
            f.setText("");
            UIFactory.resetCampo(f);
        }
        fCategoria.setSelectedIndex(0);
        tabla.clearSelection();
        lblAlerta.setText(" ");
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
                    p.getFechaVencimiento() == null ? "—" : p.getFechaVencimiento().toString(),
                    p.getCategoria(),
                    estadoTexto(p)
            });
        }
        lblTotal.setText("" + controller.listar().size());
        lblBajos.setText("" + controller.stockBajo().size());
        lblAgotados.setText("" + controller.agotados().size());
        lblVencen.setText("" + controller.vencidos().size());
    }

    private String estadoTexto(Producto p) {
        if (p.estaAgotado())        return "AGOTADO";
        if (p.estaVencido())        return "VENCIDO";
        if (p.tieneStockBajo())     return "STOCK BAJO";
        if (p.estaProximoAVencer()) return "POR VENCER";
        return "DISPONIBLE";
    }

    private static class EstadoRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable t, Object v, boolean sel, boolean foc, int row, int col) {
            JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, sel, foc, row, col);
            l.setHorizontalAlignment(SwingConstants.CENTER);
            l.setFont(new Font("Segoe UI", Font.BOLD, 11));
            if (!sel) {
                switch (String.valueOf(v)) {
                    case "DISPONIBLE" -> { l.setForeground(Tema.VERDE);   l.setBackground(Tema.VERDE_FONDO); }
                    case "AGOTADO"    -> { l.setForeground(Tema.ROJO);    l.setBackground(Tema.ROJO_FONDO); }
                    case "VENCIDO"    -> { l.setForeground(new Color(120, 40, 40)); l.setBackground(new Color(255, 210, 210)); }
                    case "STOCK BAJO" -> { l.setForeground(Tema.NARANJA); l.setBackground(Tema.NARANJA_FONDO); }
                    case "POR VENCER" -> { l.setForeground(Tema.MORADO);  l.setBackground(new Color(238, 230, 255)); }
                    default           -> { l.setForeground(Tema.TEXTO_PRIMARIO); l.setBackground(Color.WHITE); }
                }
            }
            return l;
        }
    }
}
