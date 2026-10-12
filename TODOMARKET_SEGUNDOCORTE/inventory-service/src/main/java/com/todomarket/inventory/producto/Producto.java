package com.todomarket.inventory.producto;

import com.todomarket.inventory.modelo.ProductoObserver;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Producto {
    private final int idProducto;
    private String nombre;
    private String descripcion;
    private double precioCompra;
    private double precioVenta;
    private int stock;
    private int stockMinimo;
    private String lote;
    private LocalDate fechaVencimiento;
    private final List<String> categorias = new ArrayList<>();
    private boolean estado = true;
    private final List<ProductoObserver> observadores = new ArrayList<>();

    public Producto(int idProducto, String nombre, String descripcion, double precioCompra,
                    double precioVenta, int stock, int stockMinimo, String lote,
                    LocalDate fechaVencimiento, List<String> categorias) {
        if (idProducto <= 0) throw new IllegalArgumentException("El ID del producto debe ser mayor que cero.");
        this.idProducto = idProducto;
        setNombre(nombre);
        setDescripcion(descripcion);
        setPrecioCompra(precioCompra);
        setPrecioVenta(precioVenta);
        setStock(stock);
        setStockMinimo(stockMinimo);
        setLote(lote);
        this.fechaVencimiento = fechaVencimiento;
        setCategorias(categorias);
    }

    public void agregarObservador(ProductoObserver observador) {
        if (observador != null && !observadores.contains(observador)) observadores.add(observador);
    }

    public void notificarObservadores() {
        for (ProductoObserver observador : List.copyOf(observadores)) observador.actualizar(this);
    }

    public void actualizarStock(int cantidad) {
        if (stock + cantidad < 0) throw new IllegalArgumentException("No hay stock suficiente.");
        stock += cantidad;
        notificarObservadores();
    }

    public boolean estaAgotado() { return stock == 0; }
    public boolean tieneStockBajo() { return stock <= stockMinimo; }
    public boolean estaVencido() { return fechaVencimiento != null && fechaVencimiento.isBefore(LocalDate.now()); }
    public boolean estaProximoAVencer() {
        return fechaVencimiento != null
                && !fechaVencimiento.isBefore(LocalDate.now())
                && !fechaVencimiento.isAfter(LocalDate.now().plusDays(30));
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        this.nombre = nombre.trim();
    }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion == null ? "" : descripcion.trim(); }
    public void setPrecioCompra(double precioCompra) {
        if (!Double.isFinite(precioCompra) || precioCompra < 0) throw new IllegalArgumentException("El precio de compra debe ser un número válido no negativo.");
        this.precioCompra = precioCompra;
    }
    public void setPrecioVenta(double precioVenta) {
        if (!Double.isFinite(precioVenta) || precioVenta < 0) throw new IllegalArgumentException("El precio de venta debe ser un número válido no negativo.");
        this.precioVenta = precioVenta;
    }
    public void setStock(int stock) {
        if (stock < 0) throw new IllegalArgumentException("El stock no puede ser negativo.");
        this.stock = stock;
        notificarObservadores();
    }
    public void setStockMinimo(int stockMinimo) {
        if (stockMinimo < 0) throw new IllegalArgumentException("El stock mínimo no puede ser negativo.");
        this.stockMinimo = stockMinimo;
    }
    public void setLote(String lote) {
        if (lote == null || lote.isBlank()) throw new IllegalArgumentException("El lote es obligatorio.");
        this.lote = lote.trim();
    }
    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
        notificarObservadores();
    }
    public void setCategorias(List<String> valores) {
        categorias.clear();
        if (valores != null) {
            for (String categoria : valores) {
                if (categoria != null && !categoria.isBlank() && !categorias.contains(categoria.trim())) {
                    categorias.add(categoria.trim());
                }
            }
        }
        if (categorias.isEmpty()) categorias.add("Otros");
    }
    public void setEstado(boolean estado) { this.estado = estado; }

    public int getIdProducto() { return idProducto; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public double getPrecioCompra() { return precioCompra; }
    public double getPrecioVenta() { return precioVenta; }
    public int getStock() { return stock; }
    public int getStockMinimo() { return stockMinimo; }
    public String getLote() { return lote; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public List<String> getCategorias() { return Collections.unmodifiableList(new ArrayList<>(categorias)); }
    public boolean isEstado() { return estado; }
    public String getCategoriaPrincipal() { return categorias.get(0); }
}
