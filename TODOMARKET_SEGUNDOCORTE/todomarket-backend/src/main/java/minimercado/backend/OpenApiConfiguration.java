package minimercado.backend;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI todoMarketBackendOpenApi() {
        return new OpenAPI()
                .addServersItem(new Server().url("/").description("Dominio actual del servicio"))
                .info(new Info()
                        .title("TodoMarket - Backend")
                        .description("API REST para la gestión de usuarios y para el proxy de productos gestionado por TodoMarket.")
                        .version("1.0.0"));
    }
}
