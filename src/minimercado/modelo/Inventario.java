package minimercado.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Sujeto Observable (patrón Observer).
 * Gestiona la lista de observadores y les notifica cuando el inventario cambia.
 * La vista se registra aquí para recibir alertas automáticas.
 */
public class Inventario {

    private final List<InventarioObserver> observadores = new ArrayList<>();

    public void agregarObservador(InventarioObserver o) {
        observadores.add(o);
    }

    public void removerObservador(InventarioObserver o) {
        observadores.remove(o);
    }

    /** Notifica a todos los observadores registrados sobre un cambio. */
    public void notificar(String evento, Producto producto) {
        for (InventarioObserver o : observadores) {
            o.onInventarioCambiado(evento, producto);
        }
    }
}
