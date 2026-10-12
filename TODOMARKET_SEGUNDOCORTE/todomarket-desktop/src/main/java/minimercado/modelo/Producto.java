package minimercado.modelo;

import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * Producto vendible del inventario.
 * El stockMinimo es interno (5 unidades) — no lo define el usuario.
 * El nombre solo acepta letras y espacios.
 */
public class Producto {

    private static final int     STOCK_MINIMO_DEFAULT = 5;
    private static final Pattern SOLO_LETRAS = Pattern.compile("^[\\p{L} ]+$");

    private int       idProducto;
    private int       stock;
    private final int stockMinimo = STOCK_MINIMO_DEFAULT;
    private String    nombre;
    private String    descripcion;
    private String    lote;
    private double    precioCompra;
    private double    precioVenta;
    private LocalDate fechaVencimiento;
    private Categoria categoria;
    private boolean   estado;

    /** Constructor sin stockMinimo — el umbral es fijo internamente. */
    public Producto(int id, String nombre, String descripcion,
                    double compra, double venta, int stock,
                    String lote, LocalDate vencimiento, Categoria categoria) {
        validar(nombre, compra, venta, stock, lote);
        this.idProducto      = id;
        this.nombre          = nombre.trim();
        this.descripcion     = descripcion;
        this.precioCompra    = compra;
        this.precioVenta     = venta;
        this.stock           = stock;
        this.lote            = lote.trim();
        this.fechaVencimiento = vencimiento;
        this.categoria       = categoria;
        this.estado          = true;
    }

    private void validar(String n, double pc, double pv, int s, String l) {
        if (n == null || n.isBlank())
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        if (!SOLO_LETRAS.matcher(n.trim()).matches())
            throw new IllegalArgumentException("El nombre solo puede contener letras y espacios.");
        if (l == null || l.isBlank())
            throw new IllegalArgumentException("El lote es obligatorio.");
        if (pc < 0 || pv < 0)
            throw new IllegalArgumentException("Los precios no pueden ser negativos.");
        if (s < 0)
            throw new IllegalArgumentException("El stock no puede ser negativo.");
    }

    public void actualizarStock(int cantidad) {
        if (stock + cantidad < 0) throw new IllegalArgumentException("No hay stock suficiente.");
        stock += cantidad;
    }

    /** Stock bajo: <= 5 unidades pero mayor que 0. */
    public boolean tieneStockBajo()    { return stock > 0 && stock <= stockMinimo; }
    public boolean estaAgotado()       { return stock == 0; }
    public boolean estaVencido() {
        return fechaVencimiento != null && fechaVencimiento.isBefore(LocalDate.now());
    }
    public boolean estaProximoAVencer() {
        return fechaVencimiento != null
                && !fechaVencimiento.isBefore(LocalDate.now())
                && !fechaVencimiento.isAfter(LocalDate.now().plusDays(30));
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public int       getIdProducto()      { return idProducto; }
    public String    getNombre()          { return nombre; }
    public String    getDescripcion()     { return descripcion; }
    public double    getPrecioCompra()    { return precioCompra; }
    public double    getPrecioVenta()     { return precioVenta; }
    public int       getStock()           { return stock; }
    public int       getStockMinimo()     { return stockMinimo; }
    public String    getLote()            { return lote; }
    public LocalDate getFechaVencimiento(){ return fechaVencimiento; }
    public Categoria getCategoria()       { return categoria; }
    public boolean   isEstado()           { return estado; }

    // ── Setters ───────────────────────────────────────────────────────────────
    public void setNombre(String v)           { validar(v, precioCompra, precioVenta, stock, lote); nombre = v.trim(); }
    public void setDescripcion(String v)      { descripcion = v; }
    public void setPrecioCompra(double v)     { validar(nombre, v, precioVenta, stock, lote); precioCompra = v; }
    public void setPrecioVenta(double v)      { validar(nombre, precioCompra, v, stock, lote); precioVenta = v; }
    public void setStock(int v)               { validar(nombre, precioCompra, precioVenta, v, lote); stock = v; }
    public void setLote(String v)             { validar(nombre, precioCompra, precioVenta, stock, v); lote = v.trim(); }
    public void setFechaVencimiento(LocalDate v){ fechaVencimiento = v; }
    public void setCategoria(Categoria v)     { categoria = v; }
    public void setEstado(boolean v)          { estado = v; }

    @Override public String toString() { return idProducto + " - " + nombre; }
}
