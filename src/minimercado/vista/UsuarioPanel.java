package minimercado.vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import minimercado.controlador.UsuarioController;
import minimercado.modelo.Administrador;
import minimercado.modelo.Cliente;
import minimercado.modelo.Empleado;
import minimercado.modelo.Usuario;

/**
 * Vista CRUD de usuarios.
 * Permite registrar Administradores, Empleados y Clientes (herencia de Usuario).
 */
public class UsuarioPanel extends JPanel {

    private final UsuarioController controller;

    // ── Campos del formulario ─────────────────────────────────────────────────
    private final JTextField fId       = UIFactory.campoTexto();
    private final JTextField fNombre   = UIFactory.campoTexto();
    private final JTextField fContrasena = UIFactory.campoTexto();
    private final JTextField fDocumento = UIFactory.campoTexto();
    private final JTextField fTelefono  = UIFactory.campoTexto();
    private final JTextField fCorreo    = UIFactory.campoTexto();
    private final JTextField fCargo     = UIFactory.campoTexto();

    private final JComboBox<String> fRol = new JComboBox<>(new String[]{
            "Administrador", "Empleado", "Cliente"
    });

    // ── Contadores ────────────────────────────────────────────────────────────
    private final JLabel lblTotal   = new JLabel("0");
    private final JLabel lblAdmins  = new JLabel("0");
    private final JLabel lblEmps    = new JLabel("0");
    private final JLabel lblClients = new JLabel("0");

    // ── Tabla ─────────────────────────────────────────────────────────────────
    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new String[]{"ID", "Nombre de usuario", "Documento", "Teléfono", "Correo", "Rol", "Creación"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabla = new JTable(modeloTabla);

    public UsuarioPanel(UsuarioController controller) {
        this.controller = controller;
        construir();
        aplicarEstiloTabla();
        refrescar();
    }

    private void construir() {
        setLayout(new BorderLayout(0, 16));
        setBackground(Tema.FONDO);
        setBorder(BorderFactory.createEmptyBorder(24, 26, 24, 26));

        add(panelEncabezado(), BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(0, 16));
        centro.setOpaque(false);
        centro.add(panelFormulario(), BorderLayout.NORTH);
        centro.add(panelListado(),    BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);
    }

    private JPanel panelEncabezado() {
        JPanel p = new JPanel(new BorderLayout(0, 14));
        p.setOpaque(false);

        JPanel titulos = new JPanel(new GridLayout(2, 1, 0, 3));
        titulos.setOpaque(false);
        titulos.add(UIFactory.titulo("Gestión de Usuarios"));
        titulos.add(UIFactory.subtitulo("Registra y administra los usuarios del sistema."));
        p.add(titulos, BorderLayout.NORTH);

        JPanel stats = new JPanel(new GridLayout(1, 4, 12, 0));
        stats.setOpaque(false);
        stats.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        stats.add(UIFactory.tarjetaStat("Total usuarios",   lblTotal,   Tema.ACENTO));
        stats.add(UIFactory.tarjetaStat("Administradores",  lblAdmins,  new Color(105, 60, 170)));
        stats.add(UIFactory.tarjetaStat("Empleados",        lblEmps,    Tema.VERDE));
        stats.add(UIFactory.tarjetaStat("Clientes",         lblClients, Tema.NARANJA));
        p.add(stats, BorderLayout.CENTER);

        return p;
    }

    private JPanel panelFormulario() {
        JPanel p = UIFactory.tarjeta();
        p.setLayout(new BorderLayout(0, 14));
        p.add(UIFactory.sectionTitle("Datos del usuario"), BorderLayout.NORTH);

        fRol.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        fRol.setBackground(Color.WHITE);

        JPanel campos = new JPanel(new GridBagLayout());
        campos.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets  = new Insets(4, 0, 10, 14);

        agregarCampo(campos, gbc, 0, 0, "ID",              fId);
        agregarCampo(campos, gbc, 1, 0, "Nombre de usuario", fNombre);
        agregarCampo(campos, gbc, 2, 0, "Rol",             fRol);
        agregarCampo(campos, gbc, 0, 1, "Contraseña",      fContrasena);
        agregarCampo(campos, gbc, 1, 1, "Documento",       fDocumento);
        agregarCampo(campos, gbc, 2, 1, "Teléfono",        fTelefono);
        agregarCampo(campos, gbc, 0, 2, "Correo",          fCorreo);
        agregarCampo(campos, gbc, 1, 2, "Cargo (Admin/Empleado)", fCargo);

        p.add(campos, BorderLayout.CENTER);

        // Botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botones.setOpaque(false);
        JButton btnLimpiar    = UIFactory.boton("Limpiar",    Tema.BTN_LIMPIAR);
        JButton btnEliminar   = UIFactory.boton("Eliminar",   Tema.BTN_ELIMINAR);
        JButton btnActualizar = UIFactory.boton("Actualizar", Tema.BTN_ACTUALIZAR);
        JButton btnAgregar    = UIFactory.boton("+ Agregar",  Tema.BTN_AGREGAR);
        botones.add(btnLimpiar);
        botones.add(btnEliminar);
        botones.add(btnActualizar);
        botones.add(btnAgregar);
        p.add(botones, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e    -> ejecutar(() -> controller.agregar(leerUsuario())));
        btnActualizar.addActionListener(e -> ejecutar(() -> controller.actualizar(leerUsuario())));
        btnEliminar.addActionListener(e   -> confirmarEliminar());
        btnLimpiar.addActionListener(e    -> limpiar());

        return p;
    }

    private JPanel panelListado() {
        JPanel p = UIFactory.tarjeta();
        p.setLayout(new BorderLayout(0, 12));
        p.add(UIFactory.sectionTitle("Usuarios registrados"), BorderLayout.NORTH);
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

        tabla.getColumnModel().getColumn(5).setCellRenderer(new RolRenderer());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) cargarDesdeTabla();
        });
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private void agregarCampo(JPanel p, GridBagConstraints gbc,
                               int col, int fila, String label, JComponent campo) {
        gbc.gridx = col;
        gbc.gridy = fila;
        p.add(UIFactory.grupoFormulario(label, campo), gbc);
    }

    private Usuario leerUsuario() {
        try {
            int    id       = Integer.parseInt(fId.getText().trim());
            String nombre   = fNombre.getText().trim();
            String clave    = fContrasena.getText().trim();
            String doc      = fDocumento.getText().trim();
            String tel      = fTelefono.getText().trim();
            String correo   = fCorreo.getText().trim();
            String cargo    = fCargo.getText().trim();
            String rol      = (String) fRol.getSelectedItem();

            if (nombre.isBlank()) throw new IllegalArgumentException("El nombre de usuario es obligatorio.");

            return switch (rol) {
                case "Administrador" -> new Administrador(id, nombre, clave, doc, tel, correo, id, cargo.isBlank() ? "Administrador" : cargo);
                case "Empleado"      -> new Empleado(id, nombre, clave, doc, tel, correo, id, cargo.isBlank() ? "Empleado" : cargo);
                default              -> new Cliente(id, nombre, clave, doc, tel, correo, id);
            };
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("El ID debe ser un número válido.");
        }
    }

    private void cargarDesdeTabla() {
        int id = (int) modeloTabla.getValueAt(tabla.getSelectedRow(), 0);
        Usuario u = controller.buscar(id);
        fId.setText("" + u.getIdPersona());
        fNombre.setText(u.getNombreUsuario());
        fContrasena.setText(u.getContrasena());
        fDocumento.setText(u.getDocumento());
        fTelefono.setText(u.getTelefono());
        fCorreo.setText(u.getCorreo());

        if (u instanceof Administrador a) {
            fRol.setSelectedItem("Administrador");
            fCargo.setText(a.getCargo());
        } else if (u instanceof Empleado e) {
            fRol.setSelectedItem("Empleado");
            fCargo.setText(e.getCargo());
        } else {
            fRol.setSelectedItem("Cliente");
            fCargo.setText("");
        }
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
            JOptionPane.showMessageDialog(this, "Seleccione un usuario en la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar este usuario?", "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            ejecutar(() -> controller.eliminar(Integer.parseInt(fId.getText().trim())));
        }
    }

    private void limpiar() {
        for (JTextField f : new JTextField[]{fId, fNombre, fContrasena, fDocumento, fTelefono, fCorreo, fCargo})
            f.setText("");
        fRol.setSelectedIndex(0);
        tabla.clearSelection();
        fNombre.requestFocus();
    }

    public void refrescar() {
        modeloTabla.setRowCount(0);
        int admins = 0, emps = 0, clients = 0;
        for (Usuario u : controller.listar()) {
            String rol;
            if (u instanceof Administrador) { rol = "Administrador"; admins++; }
            else if (u instanceof Empleado) { rol = "Empleado";      emps++; }
            else                            { rol = "Cliente";        clients++; }

            modeloTabla.addRow(new Object[]{
                    u.getIdPersona(),
                    u.getNombreUsuario(),
                    u.getDocumento(),
                    u.getTelefono(),
                    u.getCorreo(),
                    rol,
                    u.getFechaCreacion()
            });
        }
        lblTotal.setText("" + controller.listar().size());
        lblAdmins.setText("" + admins);
        lblEmps.setText("" + emps);
        lblClients.setText("" + clients);
    }

    // ── Renderer columna Rol ──────────────────────────────────────────────────
    private static class RolRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable t, Object value, boolean sel, boolean foc, int row, int col) {
            super.getTableCellRendererComponent(t, value, sel, foc, row, col);
            setHorizontalAlignment(SwingConstants.CENTER);
            String txt = String.valueOf(value);
            if (!sel) {
                switch (txt) {
                    case "Administrador" -> { setForeground(new Color(105, 60, 170)); setBackground(new Color(238, 230, 255)); }
                    case "Empleado"      -> { setForeground(Tema.VERDE);              setBackground(Tema.VERDE_FONDO); }
                    case "Cliente"       -> { setForeground(Tema.NARANJA);            setBackground(Tema.NARANJA_FONDO); }
                    default              -> { setForeground(Tema.TEXTO_PRIMARIO);     setBackground(Color.WHITE); }
                }
            }
            setFont(new Font("Segoe UI", Font.BOLD, 11));
            setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            return this;
        }
    }
}
