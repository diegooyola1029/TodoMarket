package com.todomarket.inventory.modelo;

import com.todomarket.inventory.producto.Producto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlertaStockObserver implements ProductoObserver {
    private final List<String> alertas = new ArrayList<>();

    @Override
    public synchronized void actualizar(Producto producto) {
        alertas.removeIf(alerta -> alerta.startsWith(producto.getIdProducto() + "|"));
        if (producto.estaAgotado()) {
            alertas.add(producto.getIdProducto() + "|AGOTADO: " + producto.getNombre());
        } else if (producto.tieneStockBajo()) {
            alertas.add(producto.getIdProducto() + "|STOCK BAJO: " + producto.getNombre());
        }
        if (producto.estaProximoAVencer()) {
            alertas.add(producto.getIdProducto() + "|PRÓXIMO A VENCER: " + producto.getNombre());
        }
    }

    public synchronized void eliminarAlertasDeProducto(int idProducto) {
        alertas.removeIf(alerta -> alerta.startsWith(idProducto + "|"));
    }

    public synchronized List<String> getAlertas() {
        return Collections.unmodifiableList(new ArrayList<>(alertas));
    }
}
