package com.todomarket.inventory.config;

import com.todomarket.inventory.modelo.AlertaStockObserver;
import com.todomarket.inventory.producto.MemoriaProductoRepository;
import com.todomarket.inventory.producto.Producto;
import com.todomarket.inventory.producto.ProductoRepository;
import com.todomarket.inventory.producto.ProductoService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InventoryConfiguration {
    @Bean
    ProductoRepository productoRepository() { return new MemoriaProductoRepository(); }

    @Bean
    AlertaStockObserver alertaStockObserver() { return new AlertaStockObserver(); }

    @Bean
    ProductoService productoService(ProductoRepository repository, AlertaStockObserver observer) {
        return new ProductoService(repository, observer);
    }

    @Bean
    CommandLineRunner cargarProductos(ProductoService productos) {
        return args -> {
            if (!productos.listar().isEmpty()) return;
            productos.crear(new Producto(1, "Arroz", "Arroz blanco 500g", 2800, 4000, 20, 5,
                    "L001", LocalDate.now().plusMonths(8), List.of("Granos")));
            productos.crear(new Producto(2, "Leche", "Leche entera 1L", 2300, 3500, 15, 5,
                    "L002", LocalDate.now().plusDays(20), List.of("Lácteos")));
            productos.crear(new Producto(3, "Gaseosa", "Gaseosa 2L", 2800, 4500, 8, 5,
                    "L003", LocalDate.now().plusMonths(5), List.of("Bebidas")));
            productos.crear(new Producto(4, "Jabon", "Jabon de tocador", 1500, 2500, 30, 5,
                    "L004", LocalDate.now().plusMonths(12), List.of("Aseo")));
            productos.crear(new Producto(5, "Frijoles", "Frijoles rojos 1kg", 3200, 4800, 3, 5,
                    "L005", LocalDate.now().plusMonths(6), List.of("Granos")));
        };
    }
}
