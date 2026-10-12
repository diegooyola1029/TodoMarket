package minimercado.controlador;

import java.util.List;
import minimercado.api.cliente.RestCrudClient;
import minimercado.api.usuario.UsuarioRequest;
import minimercado.api.usuario.UsuarioResponse;
import minimercado.modelo.Usuario;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

public class UsuarioController {
    private final RestCrudClient<UsuarioRequest, UsuarioResponse> usuarios;

    public UsuarioController(RestClient cliente) {
        usuarios = new RestCrudClient<>(cliente, "/api/usuarios", UsuarioResponse.class,
                new ParameterizedTypeReference<List<UsuarioResponse>>() { });
    }

    public void agregar(Usuario usuario) {
        usuarios.crear(UsuarioRequest.desde(usuario));
    }

    public List<Usuario> listar() {
        return usuarios.listar().stream().map(UsuarioResponse::aUsuario).toList();
    }

    public Usuario buscar(int id) {
        return usuarios.buscar(id).aUsuario();
    }

    public void actualizar(Usuario usuario) {
        usuarios.actualizar(usuario.getIdPersona(), UsuarioRequest.desde(usuario));
    }

    public void eliminar(int id) {
        usuarios.eliminar(id);
    }
}
