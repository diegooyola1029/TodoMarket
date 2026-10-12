package minimercado.api;

import java.time.LocalDateTime;
import java.util.Locale;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

@RestControllerAdvice
public class ApiExceptionHandler {
    private final ObjectMapper json = new ObjectMapper();
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> manejarSolicitudInvalida(IllegalArgumentException excepcion) {
        String mensaje = excepcion.getMessage() == null ? "Solicitud no válida." : excepcion.getMessage();
        String normalizado = mensaje.toLowerCase(Locale.ROOT);
        HttpStatus estado = normalizado.contains("no encontrado")
                ? HttpStatus.NOT_FOUND
                : normalizado.contains("ya existe") ? HttpStatus.CONFLICT : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(estado).body(new ApiError(mensaje, LocalDateTime.now()));
    }

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<ApiError> manejarRespuestaDeInventario(RestClientResponseException excepcion) {
        String mensaje = mensajeDeInventario(excepcion);
        return ResponseEntity.status(excepcion.getStatusCode())
                .body(new ApiError(mensaje, LocalDateTime.now()));
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ApiError> manejarInventarioNoDisponible(ResourceAccessException excepcion) {
        String mensaje = "El microservicio de inventario no está disponible. Inicia inventory-service en el puerto 8183.";
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ApiError(mensaje, LocalDateTime.now()));
    }

    private String mensajeDeInventario(RestClientResponseException excepcion) {
        try {
            JsonNode respuesta = json.readTree(excepcion.getResponseBodyAsString());
            if (respuesta.hasNonNull("message")) return respuesta.get("message").asText();
        } catch (Exception ignorada) {
            // Se conserva un mensaje genérico si el servicio no devolvió el formato de error esperado.
        }
        return "El microservicio rechazó la solicitud (HTTP " + excepcion.getStatusCode().value() + ").";
    }
}
