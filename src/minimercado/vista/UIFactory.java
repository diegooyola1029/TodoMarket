package minimercado.vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * Fábrica de componentes Swing reutilizables.
 * Aplica OCP: agregar un nuevo tipo de componente no modifica los existentes.
 */
public final class UIFactory {

    private UIFactory() {}

    public static JTextField campoTexto() {
        JTextField t = new JTextField();
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setForeground(Tema.TEXTO_PRIMARIO);
        t.setBackground(Color.WHITE);
        t.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.BORDE),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        t.setPreferredSize(new Dimension(0, 34));
        return t;
    }

    public static JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(Tema.TEXTO_LABEL);
        return l;
    }

    public static JLabel titulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", Font.BOLD, 22));
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
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setForeground(Tema.ACENTO);
        return l;
    }

    public static JButton boton(String texto, Color fondo) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setForeground(Color.WHITE);
        b.setBackground(fondo);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(9, 18, 9, 18));
        b.setOpaque(true);

        Color hover = fondo.darker();
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(hover); }
            public void mouseExited(java.awt.event.MouseEvent e)  { b.setBackground(fondo); }
        });
        return b;
    }

    /** Tarjeta blanca con sombra simulada mediante borde y fondo. */
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

    /** Pequeña insignia de estado con color de fondo y texto. */
    public static JLabel badge(String texto, Color fondo, Color foreground) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setForeground(foreground);
        l.setBackground(fondo);
        l.setOpaque(true);
        l.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        l.setHorizontalAlignment(SwingConstants.CENTER);
        return l;
    }

    /** Tarjeta de estadística con número grande y etiqueta descriptiva. */
    public static JPanel tarjetaStat(String etiqueta, JLabel numero, Color colorNum) {
        JPanel p = tarjeta();
        p.setLayout(new GridLayout(2, 1, 0, 4));
        JLabel lbl = etiqueta(etiqueta);
        lbl.setForeground(Tema.TEXTO_SECUNDARIO);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        numero.setFont(new Font("Segoe UI", Font.BOLD, 26));
        numero.setForeground(colorNum);
        p.add(lbl);
        p.add(numero);
        return p;
    }

    /** Agrupa etiqueta + campo en un panel vertical. */
    public static JPanel grupoFormulario(String etiqueta, JComponent campo) {
        JPanel g = new JPanel(new BorderLayout(0, 5));
        g.setOpaque(false);
        g.add(etiqueta(etiqueta), BorderLayout.NORTH);
        g.add(campo, BorderLayout.CENTER);
        return g;
    }
}
