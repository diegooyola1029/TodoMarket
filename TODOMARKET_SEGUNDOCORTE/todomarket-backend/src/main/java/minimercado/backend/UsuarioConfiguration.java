package minimercado.backend;

import java.time.LocalDate;
import minimercado.modelo.Rol;
import minimercado.modelo.Usuario;
import minimercado.repositorio.MemoriaUsuarioRepository;
import minimercado.repositorio.UsuarioRepository;
import minimercado.servicio.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UsuarioConfiguration {
    @Bean
    UsuarioRepository usuarioRepository() { return new MemoriaUsuarioRepository(); }

    @Bean
    UsuarioService usuarioService(UsuarioRepository repository) { return new UsuarioService(repository); }

    @Bean
    CommandLineRunner cargarUsuarios(UsuarioService usuarios) {
        return args -> {
            usuarios.crear(new Usuario(1, "admin", "admin123", "1000001", "3001111001",
                    "admin@gmail.com", new Rol(1, "Administrador", "Gerente"), true));
            usuarios.crear(new Usuario(2, "cajero", "caj123", "1000002", "3001111002",
                    "cajero@gmail.com", new Rol(2, "Empleado", "Cajero"), true));
            usuarios.crear(new Usuario(3, "cliente", "cli123", "1000003", "3001111003",
                    "cliente@hotmail.com", new Rol(3, "Cliente", "Cliente del minimercado"), true));
        };
    }
}
