package minimercado.modelo;

/**
 * Interfaz Observer (patrón Observer).
 * Cualquier componente que quiera reaccionar a cambios en el inventario
 * implementa este contrato.
 */
public interface InventarioObserver {
    void onInventarioCambiado(String evento, Producto producto);
}
