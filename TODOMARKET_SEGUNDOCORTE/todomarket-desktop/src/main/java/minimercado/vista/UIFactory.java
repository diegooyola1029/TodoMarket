package minimercado.vista;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.function.Function;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Fábrica de componentes Swing reutilizables.
 * OCP: agregar componentes nuevos no modifica los existentes.
 */
public final class UIFactory {

    private UIFactory() {}

    // Bordes para estados de validación
    static final Border BORDE_NORMAL = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(190, 200, 220), 1),
            BorderFactory.createEmptyBorder(7, 11, 7, 11));
    static final Border BORDE_ERROR = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 50, 50), 2),
            BorderFactory.createEmptyBorder(6, 10, 6, 10));

    // ── Campo de texto con cursor al final al hacer foco ──────────────────────
    public static JTextField campoTexto() {
        JTextField t = new JTextField() {
            @Override public void addNotify() {
                super.addNotify();
                addFocusListener(new FocusAdapter() {
                    @Override public void focusGained(FocusEvent e) {
                        SwingUtilities.invokeLater(() ->
                                setCaretPosition(getText().length()));
                    }
                });
            }
        };
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setForeground(Tema.TEXTO_PRIMARIO);
        t.setBackground(Color.WHITE);
        t.setCaretColor(Tema.ACENTO);
        t.setBorder(BORDE_NORMAL);
        t.setPreferredSize(new Dimension(0, 36));
        return t;
    }

    // ── Campo de contraseña con botón ojo mostrar/ocultar ─────────────────────
    /**
     * Devuelve un JPanel que contiene un JPasswordField con botón de ojo.
     * Para leer el valor usa: ((JPasswordField) panel.getComponent(0)).getPassword()
     * Para un acceso más cómodo, usa campoClave() que devuelve el JPasswordField
     * y el panel se obtiene con campoClavePanelConOjo(field).
     */
    public static JPasswordField campoClave() {
        JPasswordField p = new JPasswordField() {
            @Override public void addNotify() {
                super.addNotify();
                addFocusListener(new FocusAdapter() {
                    @Override public void focusGained(FocusEvent e) {
                        SwingUtilities.invokeLater(() ->
                                setCaretPosition(getPassword().length));
                    }
                });
            }
        };
        p.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        p.setForeground(Tema.TEXTO_PRIMARIO);
        p.setBackground(Color.WHITE);
        p.setCaretColor(Tema.ACENTO);
        p.setBorder(BORDE_NORMAL);
        p.setPreferredSize(new Dimension(0, 36));
        p.setEchoChar('•');
        return p;
    }

    /**
     * Envuelve un JPasswordField en un panel con botón "Ver / Ocultar".
     * Pasa el campo creado con campoClave().
     */
    public static JPanel panelClave(JPasswordField campo) {
        JPanel wrapper = new JPanel(new BorderLayout(4, 0));
        wrapper.setOpaque(false);

        JButton ojo = new JButton("Ver") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(90, 110, 150));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.dispose();
                FontMetrics fm = g.getFontMetrics();
                int x = (getWidth()  - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g.setColor(Color.WHITE);
                g.setFont(getFont());
                g.drawString(getText(), x, y);
            }
        };
        ojo.setFont(new Font("Segoe UI", Font.BOLD, 11));
        ojo.setOpaque(false);
        ojo.setContentAreaFilled(false);
        ojo.setBorderPainted(false);
        ojo.setFocusPainted(false);
        ojo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        ojo.setPreferredSize(new Dimension(52, 36));

        final boolean[] visible = {false};
        ojo.addActionListener(e -> {
            visible[0] = !visible[0];
            campo.setEchoChar(visible[0] ? (char) 0 : '•');
            ojo.setText(visible[0] ? "Ocultar" : "Ver");
            ojo.setPreferredSize(new Dimension(visible[0] ? 68 : 52, 36));
            wrapper.revalidate();
        });

        wrapper.add(campo, BorderLayout.CENTER);
        wrapper.add(ojo,   BorderLayout.EAST);
        return wrapper;
    }

    // ── Selector de fecha con calendario popup ────────────────────────────────
    /**
     * Devuelve un JTextField que abre un calendario popup al hacer clic.
     * El valor seleccionado se escribe como "AAAA-MM-DD".
     * Para leer: LocalDate.parse(campo.getText()) — solo si no está en blanco.
     */
    public static JTextField campoFecha() {
        JTextField t = campoTexto();
        t.setEditable(false);
        t.setBackground(Color.WHITE);
        t.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        t.setToolTipText("Clic para abrir el calendario");

        t.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                mostrarCalendario(t);
            }
        });

        // Icono de calendario al final del campo
        t.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 200, 220), 1),
                BorderFactory.createEmptyBorder(7, 11, 7, 30)));

        return t;
    }

    private static void mostrarCalendario(JTextField campo) {
        // Leer fecha actual del campo o usar hoy
        LocalDate fechaBase;
        try {
            fechaBase = campo.getText().isBlank()
                    ? LocalDate.now()
                    : LocalDate.parse(campo.getText());
        } catch (Exception ex) {
            fechaBase = LocalDate.now();
        }

        JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(campo), "Seleccionar fecha",
                java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dlg.setUndecorated(false);
        dlg.setResizable(false);

        final LocalDate[] seleccionada = {fechaBase};
        final int[] mesActual   = {fechaBase.getMonthValue()};
        final int[] anioActual  = {fechaBase.getYear()};

        JPanel root = new JPanel(new BorderLayout(0, 6));
        root.setBackground(Color.WHITE);
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ── Navegación mes/año ────────────────────────────────────────────────
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(Tema.ACENTO);
        nav.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        JLabel lblMesAnio = new JLabel("", SwingConstants.CENTER);
        lblMesAnio.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblMesAnio.setForeground(Color.WHITE);

        JButton prev = btnNavCal("<");
        JButton next = btnNavCal(">");

        nav.add(prev,       BorderLayout.WEST);
        nav.add(lblMesAnio, BorderLayout.CENTER);
        nav.add(next,       BorderLayout.EAST);

        // ── Grilla de días ────────────────────────────────────────────────────
        JPanel grilla = new JPanel(new GridLayout(0, 7, 4, 4));
        grilla.setBackground(Color.WHITE);

        // Cabecera días de semana
        String[] dias = {"Do", "Lu", "Ma", "Mi", "Ju", "Vi", "Sa"};
        for (String d : dias) {
            JLabel l = new JLabel(d, SwingConstants.CENTER);
            l.setFont(new Font("Segoe UI", Font.BOLD, 11));
            l.setForeground(Tema.TEXTO_SECUNDARIO);
            grilla.add(l);
        }

        Runnable[] refrescarGrilla = {null};

        refrescarGrilla[0] = () -> {
            // quitar celdas anteriores (menos los 7 encabezados)
            while (grilla.getComponentCount() > 7) grilla.remove(7);

            String[] MESES = {"Enero","Febrero","Marzo","Abril","Mayo","Junio",
                    "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
            lblMesAnio.setText(MESES[mesActual[0] - 1] + " " + anioActual[0]);

            Calendar cal = Calendar.getInstance();
            cal.set(anioActual[0], mesActual[0] - 1, 1);
            int primerDia = cal.get(Calendar.DAY_OF_WEEK) - 1; // 0=Dom
            int totalDias = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

            for (int i = 0; i < primerDia; i++) grilla.add(new JLabel(""));

            for (int d = 1; d <= totalDias; d++) {
                final int dia = d;
                LocalDate fecha = LocalDate.of(anioActual[0], mesActual[0], dia);
                JButton btn = new JButton(String.valueOf(d));
                btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                btn.setFocusPainted(false);
                btn.setBorderPainted(false);
                btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                if (fecha.equals(seleccionada[0])) {
                    btn.setBackground(Tema.ACENTO);
                    btn.setForeground(Color.WHITE);
                    btn.setOpaque(true);
                } else if (fecha.equals(LocalDate.now())) {
                    btn.setBackground(new Color(230, 240, 255));
                    btn.setForeground(Tema.ACENTO);
                    btn.setOpaque(true);
                } else {
                    btn.setBackground(Color.WHITE);
                    btn.setForeground(Tema.TEXTO_PRIMARIO);
                    btn.setOpaque(true);
                }

                btn.addActionListener(e -> {
                    seleccionada[0] = fecha;
                    campo.setText(fecha.toString());
                    dlg.dispose();
                });
                grilla.add(btn);
            }
            grilla.revalidate();
            grilla.repaint();
        };

        prev.addActionListener(e -> {
            mesActual[0]--;
            if (mesActual[0] < 1) { mesActual[0] = 12; anioActual[0]--; }
            refrescarGrilla[0].run();
        });
        next.addActionListener(e -> {
            mesActual[0]++;
            if (mesActual[0] > 12) { mesActual[0] = 1; anioActual[0]++; }
            refrescarGrilla[0].run();
        });

        // Botón limpiar
        JButton btnLimpiar = new JButton("Sin fecha");
        btnLimpiar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.addActionListener(e -> { campo.setText(""); dlg.dispose(); });

        root.add(nav,       BorderLayout.NORTH);
        root.add(grilla,    BorderLayout.CENTER);
        root.add(btnLimpiar,BorderLayout.SOUTH);

        refrescarGrilla[0].run();

        dlg.setContentPane(root);
        dlg.pack();
        dlg.setLocationRelativeTo(campo);
        dlg.setVisible(true);
    }

    private static JButton btnNavCal(String txt) {
        JButton b = new JButton(txt);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setForeground(Color.WHITE);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    // ── Validación en tiempo real ─────────────────────────────────────────────
    public static void validarCampo(JTextField campo, Function<String, String> validator) {
        Runnable check = () -> {
            String error = validator.apply(campo.getText());
            if (error != null) {
                campo.setBorder(BORDE_ERROR);
                campo.setToolTipText(error);
                campo.setBackground(new Color(255, 245, 245));
            } else {
                campo.setBorder(BORDE_NORMAL);
                campo.setToolTipText(null);
                campo.setBackground(Color.WHITE);
            }
        };
        campo.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { check.run(); }
            public void removeUpdate(DocumentEvent e)  { check.run(); }
            public void changedUpdate(DocumentEvent e) { check.run(); }
        });
    }

    public static void resetCampo(JTextField campo) {
        campo.setBorder(BORDE_NORMAL);
        campo.setBackground(Color.WHITE);
        campo.setToolTipText(null);
    }

    // ── Etiquetas ─────────────────────────────────────────────────────────────
    public static JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(Tema.TEXTO_LABEL);
        return l;
    }

    public static JLabel titulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", Font.BOLD, 24));
        l.setForeground(Tema.TEXTO_PRIMARIO);
        return l;
    }

    public static JLabel subtitulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        l.setForeground(Tema.TEXTO_SECUNDARIO);
        return l;
    }

    public static JLabel sectionTitle(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", Font.BOLD, 15));
        l.setForeground(Tema.ACENTO);
        return l;
    }

    // ── Botones ───────────────────────────────────────────────────────────────
    public static JButton boton(String texto, Color fondo) {
        JButton b = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                FontMetrics fm = g.getFontMetrics();
                int x = (getWidth()  - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g.setColor(Color.WHITE);
                g.setFont(getFont());
                g.drawString(getText(), x, y);
            }
        };
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setForeground(Color.WHITE);
        b.setBackground(fondo);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        Color hover = fondo.darker();
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e)  { b.setBackground(hover);         b.repaint(); }
            public void mouseExited (MouseEvent e)  { b.setBackground(fondo);         b.repaint(); }
            public void mousePressed(MouseEvent e)  { b.setBackground(hover.darker()); b.repaint(); }
            public void mouseReleased(MouseEvent e) { b.setBackground(hover);         b.repaint(); }
        });
        return b;
    }

    // ── Tarjetas ──────────────────────────────────────────────────────────────
    public static JPanel tarjeta() {
        JPanel p = new JPanel();
        p.setBackground(Tema.TARJETA);
        p.setBorder(tarjetaBorde());
        return p;
    }

    public static Border tarjetaBorde() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.BORDE, 1),
                BorderFactory.createEmptyBorder(16, 18, 16, 18));
    }

    public static JPanel tarjetaStat(String etiqueta, JLabel numero, Color colorNum) {
        JPanel p = tarjeta();
        p.setLayout(new BorderLayout(0, 6));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.BORDE, 1),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)));
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(Tema.TEXTO_SECUNDARIO);
        numero.setFont(new Font("Segoe UI", Font.BOLD, 28));
        numero.setForeground(colorNum);
        p.add(numero, BorderLayout.NORTH);
        p.add(lbl,    BorderLayout.CENTER);
        return p;
    }

    public static JPanel grupoFormulario(String etiqueta, JComponent campo) {
        JPanel g = new JPanel(new BorderLayout(0, 5));
        g.setOpaque(false);
        g.add(etiqueta(etiqueta), BorderLayout.NORTH);
        g.add(campo,              BorderLayout.CENTER);
        return g;
    }
}
