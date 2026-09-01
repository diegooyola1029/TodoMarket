package minimercado.vista;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * Fábrica de componentes Swing reutilizables.
 * OCP: agregar componentes nuevos no modifica los existentes.
 *
 * IMPORTANTE: usa un JPanel pintado a mano para los botones en lugar de
 * JButton nativo, para evitar que el L&F de Windows/Nimbus sobreescriba
 * los colores de fondo y texto.
 */
public final class UIFactory {

    private UIFactory() {}

    // ── Campos de texto ───────────────────────────────────────────────────────
    public static JTextField campoTexto() {
        JTextField t = new JTextField();
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setForeground(Tema.TEXTO_PRIMARIO);
        t.setBackground(Color.WHITE);
        t.setCaretColor(Tema.ACENTO);
        t.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 200, 220), 1),
                BorderFactory.createEmptyBorder(7, 11, 7, 11)));
        t.setPreferredSize(new Dimension(0, 36));
        return t;
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

    // ── Botones (pintado propio para ignorar L&F) ─────────────────────────────
    public static JButton boton(String texto, Color fondo) {
        JButton b = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getBackground();
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                // Dibujar texto encima
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
        b.setOpaque(false);          // false porque pintamos nosotros
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        Color hover = fondo.darker();
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(hover);  b.repaint(); }
            public void mouseExited (MouseEvent e) { b.setBackground(fondo);  b.repaint(); }
            public void mousePressed(MouseEvent e) { b.setBackground(hover.darker()); b.repaint(); }
            public void mouseReleased(MouseEvent e){ b.setBackground(hover);  b.repaint(); }
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

    /** Tarjeta de estadística: número grande + etiqueta. */
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

    /** Agrupa etiqueta + campo en un panel vertical. */
    public static JPanel grupoFormulario(String etiqueta, JComponent campo) {
        JPanel g = new JPanel(new BorderLayout(0, 5));
        g.setOpaque(false);
        g.add(etiqueta(etiqueta), BorderLayout.NORTH);
        g.add(campo,              BorderLayout.CENTER);
        return g;
    }
}
