package minimercado.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "minimercado")
public class TodoMarketBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(TodoMarketBackendApplication.class, args);
    }
}
