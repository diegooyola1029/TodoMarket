package com.todomarket.inventory.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI inventoryOpenApi() {
        return new OpenAPI().info(new Info()
                .title("TodoMarket - Inventory Service")
                .description("API REST para la gestión de productos, existencias, consultas de inventario y alertas de TodoMarket.")
                .version("1.0.0"));
    }
}
