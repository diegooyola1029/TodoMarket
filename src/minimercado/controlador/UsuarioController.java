package minimercado.controlador;

import java.util.List;
import minimercado.modelo.Usuario;
import minimercado.servicio.UsuarioService;

/**
 * Controlador MVC de usuarios.
 * Expone las operaciones CRUD a la vista sin exponer la lógica de negocio.
 */
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    public void          agregar(Usuario u)   { service.crear(u); }
    public List<Usuario> listar()             { return service.listar(); }
    public Usuario       buscar(int id)       { return service.buscar(id); }
    public void          actualizar(Usuario u){ service.actualizar(u); }
    public void          eliminar(int id)     { service.eliminar(id); }
}
