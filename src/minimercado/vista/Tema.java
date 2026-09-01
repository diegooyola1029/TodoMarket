package minimercado.vista;

import java.awt.*;

/**
 * Paleta de colores y estilos compartidos por todas las vistas.
 * Centralizar aquí permite cambiar el diseño sin tocar cada panel.
 */
public final class Tema {

    private Tema() {}

    // Sidebar
    public static final Color SIDEBAR        = new Color(22, 33, 55);
    public static final Color SIDEBAR_HEADER = new Color(16, 24, 42);
    public static final Color SIDEBAR_HOVER  = new Color(35, 52, 85);

    // Acento principal (azul medio)
    public static final Color ACENTO         = new Color(37, 99, 200);
    public static final Color ACENTO_OSCURO  = new Color(27, 75, 155);

    // Fondos
    public static final Color FONDO          = new Color(242, 245, 250);
    public static final Color TARJETA        = Color.WHITE;

    // Textos
    public static final Color TEXTO_PRIMARIO   = new Color(22, 32, 50);
    public static final Color TEXTO_SECUNDARIO = new Color(90, 105, 130);
    public static final Color TEXTO_LABEL      = new Color(50, 65, 90);

    // Estados
    public static final Color VERDE            = new Color(22, 115, 70);
    public static final Color VERDE_FONDO      = new Color(220, 248, 235);
    public static final Color ROJO             = new Color(185, 40, 40);
    public static final Color ROJO_FONDO       = new Color(255, 228, 228);
    public static final Color NARANJA          = new Color(175, 100, 0);
    public static final Color NARANJA_FONDO    = new Color(255, 240, 210);
    public static final Color MORADO           = new Color(105, 60, 170);

    // Botones de acción
    public static final Color BTN_AGREGAR      = new Color(37, 99, 200);
    public static final Color BTN_ACTUALIZAR   = new Color(25, 120, 80);
    public static final Color BTN_ELIMINAR     = new Color(185, 40, 40);
    public static final Color BTN_LIMPIAR      = new Color(95, 105, 125);

    // Borde sutil
    public static final Color BORDE            = new Color(215, 222, 233);
}
