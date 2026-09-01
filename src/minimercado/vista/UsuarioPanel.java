package minimercado.vista;

import java.awt.*;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.table.*;
import minimercado.controlador.UsuarioController;
import minimercado.modelo.Administrador;
import minimercado.modelo.Cliente;
import minimercado.modelo.Empleado;
import minimercado.modelo.Usuario;

/**
 * Vista CRUD de usuarios.
 * Todo el contenido vive dentro de un JScrollPane externo para que
 * formulario y tabla sean siempre accesibles.
 */
public class UsuarioPanel extends JPanel {

    private static final Pattern SOLO_NUMEROS = Pattern.compile("^\\d+$");
    private static final Pattern CORREO_VALIDO = Pattern.compile(
            "^[\\w.+-]+@(gmail\\.com|hotmail\\.com)$", Pattern.CASE_INSENSITIVE);
    private static final Pattern SOLO_LETRAS = Pattern.compile("^[\\p{L} ]+$");

    private final UsuarioController controller;

    // ── Campos ────────────────────────────────────────────────────────────────
    private final JTextField fId        = UIFactory.campoTexto();
    private final JTextField fNombre    = UIFactory.campoTexto();
    private final JTextField fClave     = UIFactory.campoTexto();
    private final JTextField fDocumento = UIFactory.campoTexto();
    private final JTextField fTelefono  = UIFactory.campoTexto();
    private final JTextField fCorreo    = UIFactory.campoTexto();
    private final JTextField fCargo     = UIFactory.campoTexto();
    private final JComboBox<String> fRol = new JComboBox<>(
            new String[]{"Administrador", "Empleado", "Cliente"});

    // ── Stats ─────────────────────────────────────────────────────────────────
    private final JLabel lblTotal   = new JLabel("0");
    private final JLabel lblAdmins  = new JLabel("0");
    private final JLabel lblEmps    = new JLabel("0");
    private final JLabel lblClients = new JLabel("0");

    // ── Tabla ─────────────────────────────────────────────────────────────────
    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new String[]{"ID", "Usuario", "Documento", "Teléfono", "Correo", "Rol", "Creación"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabla = new JTable(modeloTabla);

    public UsuarioPanel(UsuarioController controller) {
        this.controller = controller;
        construir();
        estilizarTabla();
        refrescar();
    }

    // ── Layout ────────────────────────────────────────────────────────────────
    private void construir() {
        setLayout(new BorderLayout());
        setBackground(Tema.FONDO);

        // Panel interno que contiene todo el contenido
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

        // Scroll externo que lo envuelve todo
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
        p.add(UIFactory.titulo("Gestión de Usuarios"));
        p.add(UIFactory.subtitulo("Registra y administra los usuarios del sistema."));
        return p;
    }

    private JPanel panelStats() {
        JPanel p = new JPanel(new GridLayout(1, 4, 10, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        p.add(UIFactory.tarjetaStat("Total usuarios",  lblTotal,   Tema.ACENTO));
        p.add(UIFactory.tarjetaStat("Administradores", lblAdmins,  Tema.MORADO));
        p.add(UIFactory.tarjetaStat("Empleados",       lblEmps,    Tema.VERDE));
        p.add(UIFactory.tarjetaStat("Clientes",        lblClients, Tema.NARANJA));
        return p;
    }

    // ── Formulario ────────────────────────────────────────────────────────────
    private JPanel panelFormulario() {
        JPanel p = new JPanel(new BorderLayout(0, 14));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.BORDE, 1),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        p.add(UIFactory.sectionTitle("Datos del usuario"), BorderLayout.NORTH);

        fRol.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPanel campos = new JPanel(new GridBagLayout());
        campos.setBackground(Color.WHITE);
        GridBagConstraints c = new GridBagConstraints();
        c.fill    = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets  = new Insets(4, 0, 10, 14);

        campo(campos, c, 0, 0, "ID",                         fId);
        campo(campos, c, 1, 0, "Nombre de usuario",          fNombre);
        campo(campos, c, 2, 0, "Rol",                        fRol);
        campo(campos, c, 0, 1, "Contraseña",                 fClave);
        campo(campos, c, 1, 1, "Documento (solo números)",   fDocumento);
        campo(campos, c, 2, 1, "Teléfono (solo números)",    fTelefono);
        campo(campos, c, 0, 2, "Correo (@gmail / @hotmail)", fCorreo);
        campo(campos, c, 1, 2, "Cargo (Admin / Empleado)",   fCargo);

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
        btnActualizar.addActionListener(e -> ejecutar(() -> controller.actualizar(leerUsuario())));
        btnAgregar.addActionListener(e    -> ejecutar(() -> controller.agregar(leerUsuario())));

        return p;
    }

    // ── Tabla ─────────────────────────────────────────────────────────────────
    private JPanel panelTabla() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.BORDE, 1),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)));

        // Altura fija: encabezado + 7 filas
        int altura = 36 + (32 * 7) + 36;
        p.setPreferredSize(new Dimension(100, altura));
        p.setMinimumSize(new Dimension(100, altura));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, altura));

        p.add(UIFactory.sectionTitle("Usuarios registrados"), BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(Tema.BORDE));
        scroll.getViewport().setBackground(Color.WHITE);
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

        tabla.getColumnModel().getColumn(5).setCellRenderer(new RolRenderer());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0)
                cargarDesdeTabla();
        });
    }

    // ── Validaciones ──────────────────────────────────────────────────────────
    private void validar(String nombre, String doc, String tel, String correo) {
        if (nombre.isBlank())
            throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
        if (!SOLO_LETRAS.matcher(nombre).matches())
            throw new IllegalArgumentException("El nombre solo puede contener letras y espacios.");
        if (!doc.isBlank() && !SOLO_NUMEROS.matcher(doc).matches())
            throw new IllegalArgumentException("El documento solo puede contener números.");
        if (!tel.isBlank() && !SOLO_NUMEROS.matcher(tel).matches())
            throw new IllegalArgumentException("El teléfono solo puede contener números.");
        if (!correo.isBlank() && !CORREO_VALIDO.matcher(correo).matches())
            throw new IllegalArgumentException("El correo debe ser @gmail.com o @hotmail.com.");
    }

    private void campo(JPanel p, GridBagConstraints c, int col, int fila,
                       String lbl, JComponent campo) {
        c.gridx = col; c.gridy = fila;
        p.add(UIFactory.grupoFormulario(lbl, campo), c);
    }

    private Usuario leerUsuario() {
        try {
            int    id    = Integer.parseInt(fId.getText().trim());
            String nom   = fNombre.getText().trim();
            String clave = fClave.getText().trim();
            String doc   = fDocumento.getText().trim();
            String tel   = fTelefono.getText().trim();
            String mail  = fCorreo.getText().trim();
            String cargo = fCargo.getText().trim();

            validar(nom, doc, tel, mail);

            return switch ((String) fRol.getSelectedItem()) {
                case "Administrador" -> new Administrador(id, nom, clave, doc, tel, mail,
                        id, cargo.isBlank() ? "Administrador" : cargo);
                case "Empleado"      -> new Empleado(id, nom, clave, doc, tel, mail,
                        id, cargo.isBlank() ? "Empleado" : cargo);
                default              -> new Cliente(id, nom, clave, doc, tel, mail, id);
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
        fClave.setText(u.getContrasena());
        fDocumento.setText(u.getDocumento());
        fTelefono.setText(u.getTelefono());
        fCorreo.setText(u.getCorreo());
        if      (u instanceof Administrador a) { fRol.setSelectedItem("Administrador"); fCargo.setText(a.getCargo()); }
        else if (u instanceof Empleado e)      { fRol.setSelectedItem("Empleado");      fCargo.setText(e.getCargo()); }
        else                                   { fRol.setSelectedItem("Cliente");        fCargo.setText(""); }
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
                    "Seleccione un usuario en la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int op = JOptionPane.showConfirmDialog(this, "¿Eliminar este usuario?",
                "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (op == JOptionPane.YES_OPTION)
            ejecutar(() -> controller.eliminar(Integer.parseInt(fId.getText().trim())));
    }

    private void limpiar() {
        for (JTextField f : new JTextField[]{fId, fNombre, fClave, fDocumento, fTelefono, fCorreo, fCargo})
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
            if      (u instanceof Administrador) { rol = "Administrador"; admins++; }
            else if (u instanceof Empleado)      { rol = "Empleado";      emps++;   }
            else                                 { rol = "Cliente";        clients++; }
            modeloTabla.addRow(new Object[]{
                    u.getIdPersona(), u.getNombreUsuario(), u.getDocumento(),
                    u.getTelefono(),  u.getCorreo(),        rol,
                    u.getFechaCreacion()
            });
        }
        lblTotal.setText("" + controller.listar().size());
        lblAdmins.setText("" + admins);
        lblEmps.setText("" + emps);
        lblClients.setText("" + clients);
    }

    private static class RolRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable t, Object v, boolean sel, boolean foc, int row, int col) {
            JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, sel, foc, row, col);
            l.setHorizontalAlignment(SwingConstants.CENTER);
            l.setFont(new Font("Segoe UI", Font.BOLD, 11));
            if (!sel) {
                switch (String.valueOf(v)) {
                    case "Administrador" -> { l.setForeground(Tema.MORADO);  l.setBackground(new Color(238, 230, 255)); }
                    case "Empleado"      -> { l.setForeground(Tema.VERDE);   l.setBackground(Tema.VERDE_FONDO); }
                    case "Cliente"       -> { l.setForeground(Tema.NARANJA); l.setBackground(Tema.NARANJA_FONDO); }
                    default              -> { l.setForeground(Tema.TEXTO_PRIMARIO); l.setBackground(Color.WHITE); }
                }
            }
            return l;
        }
    }
}
