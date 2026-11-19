package minimercado.api.usuario;

import java.net.URI;
import java.util.List;
import minimercado.modelo.Usuario;
import minimercado.servicio.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Usuarios", description = "Operaciones CRUD para la gestión de usuarios.")
public class UsuarioRestController {
    private final UsuarioService service;

    public UsuarioRestController(UsuarioService service) { this.service = service; }

    @Operation(summary = "Listar usuarios", description = "Devuelve todos los usuarios registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UsuarioResponse.class))))
    @GetMapping
    public List<UsuarioResponse> listar() { return service.listar().stream().map(UsuarioResponse::desde).toList(); }

    @Operation(summary = "Consultar usuario", description = "Busca un usuario por su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario encontrado", content = @Content(schema = @Schema(implementation = UsuarioResponse.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(schema = @Schema(implementation = minimercado.api.ApiError.class)))
    })
    @GetMapping("/{id}")
    public UsuarioResponse buscar(@Parameter(name = "id", in = ParameterIn.PATH, description = "ID del usuario", example = "1", required = true) @PathVariable int id) { return UsuarioResponse.desde(service.buscar(id)); }

    @Operation(summary = "Crear usuario", description = "Crea un usuario; el servicio asigna el ID automáticamente. La contraseña se utiliza en la solicitud y no se devuelve en la respuesta.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario creado", content = @Content(schema = @Schema(implementation = UsuarioResponse.class), examples = @ExampleObject(value = "{\"idPersona\":4,\"nombreUsuario\":\"nuevo\",\"documento\":\"1000004\",\"telefono\":\"3001111004\",\"correo\":\"nuevo@example.com\",\"rol\":{\"idRol\":3,\"nombreRol\":\"Cliente\",\"descripcion\":\"Cliente del minimercado\"},\"estado\":true,\"fechaCreacion\":\"2026-11-19\"}"))),
        @ApiResponse(responseCode = "400", description = "Solicitud no válida", content = @Content(schema = @Schema(implementation = minimercado.api.ApiError.class))),
        @ApiResponse(responseCode = "409", description = "Nombre, documento, teléfono o correo duplicado", content = @Content(schema = @Schema(implementation = minimercado.api.ApiError.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Datos del usuario y su rol", content = @Content(schema = @Schema(implementation = UsuarioRequest.class), examples = @ExampleObject(value = "{\"idPersona\":0,\"nombreUsuario\":\"nuevo\",\"contrasena\":\"cambiar-esta-clave\",\"documento\":\"1000004\",\"telefono\":\"3001111004\",\"correo\":\"nuevo@example.com\",\"rol\":{\"idRol\":3,\"nombreRol\":\"Cliente\",\"descripcion\":\"Cliente del minimercado\"},\"estado\":true}")))
    @PostMapping
    public ResponseEntity<UsuarioResponse> crear(@RequestBody UsuarioRequest solicitud) {
        Usuario creado = service.crear(solicitud.aUsuario(1));
        return ResponseEntity.created(URI.create("/api/usuarios/" + creado.getIdPersona()))
                .body(UsuarioResponse.desde(creado));
    }

    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos del usuario existente. El ID de ruta debe coincidir con idPersona en el cuerpo. Una contraseña vacía deja la contraseña actual sin cambios.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario actualizado", content = @Content(schema = @Schema(implementation = UsuarioResponse.class))),
        @ApiResponse(responseCode = "400", description = "ID no coincide o datos no válidos", content = @Content(schema = @Schema(implementation = minimercado.api.ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(schema = @Schema(implementation = minimercado.api.ApiError.class))),
        @ApiResponse(responseCode = "409", description = "Dato único ya registrado", content = @Content(schema = @Schema(implementation = minimercado.api.ApiError.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Datos actualizados del usuario; idPersona debe coincidir con la ruta", content = @Content(schema = @Schema(implementation = UsuarioRequest.class), examples = @ExampleObject(value = "{\"idPersona\":1,\"nombreUsuario\":\"admin\",\"contrasena\":\"\",\"documento\":\"1000001\",\"telefono\":\"3001111001\",\"correo\":\"admin@example.com\",\"rol\":{\"idRol\":1,\"nombreRol\":\"Administrador\",\"descripcion\":\"Gerente\"},\"estado\":true}")))
    @PutMapping("/{id}")
    public UsuarioResponse actualizar(@Parameter(name = "id", in = ParameterIn.PATH, description = "ID del usuario", example = "1", required = true) @PathVariable int id, @RequestBody UsuarioRequest solicitud) {
        if (id != solicitud.idPersona()) throw new IllegalArgumentException("El ID de la ruta y del usuario deben coincidir.");
        Usuario usuario = solicitud.aUsuario();
        service.actualizar(usuario);
        return UsuarioResponse.desde(service.buscar(id));
    }

    @Operation(summary = "Eliminar usuario", description = "Elimina el usuario identificado.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Usuario eliminado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(schema = @Schema(implementation = minimercado.api.ApiError.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(name = "id", in = ParameterIn.PATH, description = "ID del usuario", example = "1", required = true) @PathVariable int id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
