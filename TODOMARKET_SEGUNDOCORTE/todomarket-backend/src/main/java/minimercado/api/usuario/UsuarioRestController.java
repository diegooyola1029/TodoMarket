package minimercado.api.usuario;

import java.net.URI;
import java.util.List;
import minimercado.modelo.Usuario;
import minimercado.servicio.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioRestController {
    private final UsuarioService service;

    public UsuarioRestController(UsuarioService service) { this.service = service; }

    @GetMapping
    public List<UsuarioResponse> listar() { return service.listar().stream().map(UsuarioResponse::desde).toList(); }

    @GetMapping("/{id}")
    public UsuarioResponse buscar(@PathVariable int id) { return UsuarioResponse.desde(service.buscar(id)); }

    @PostMapping
    public ResponseEntity<UsuarioResponse> crear(@RequestBody UsuarioRequest solicitud) {
        Usuario usuario = solicitud.aUsuario();
        service.crear(usuario);
        return ResponseEntity.created(URI.create("/api/usuarios/" + usuario.getIdPersona()))
                .body(UsuarioResponse.desde(usuario));
    }

    @PutMapping("/{id}")
    public UsuarioResponse actualizar(@PathVariable int id, @RequestBody UsuarioRequest solicitud) {
        if (id != solicitud.idPersona()) throw new IllegalArgumentException("El ID de la ruta y del usuario deben coincidir.");
        Usuario usuario = solicitud.aUsuario();
        service.actualizar(usuario);
        return UsuarioResponse.desde(service.buscar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
