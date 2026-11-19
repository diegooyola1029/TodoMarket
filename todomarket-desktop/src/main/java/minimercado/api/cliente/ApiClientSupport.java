package minimercado.api.cliente;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.function.Supplier;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

public final class ApiClientSupport {
    private static final ObjectMapper JSON = new ObjectMapper();

    private ApiClientSupport() { }

    public static <T> T ejecutar(Supplier<T> llamada) {
        try {
            return llamada.get();
        } catch (RestClientResponseException excepcion) {
            throw new IllegalArgumentException(mensajeDe(excepcion), excepcion);
        } catch (ResourceAccessException excepcion) {
            throw new IllegalArgumentException(
                    "No fue posible conectarse con el backend. Verifica que los servicios estén iniciados.", excepcion);
        }
    }

    public static String mensajeDe(RestClientResponseException excepcion) {
        try {
            JsonNode respuesta = JSON.readTree(excepcion.getResponseBodyAsString());
            if (respuesta.hasNonNull("message")) return respuesta.get("message").asText();
            if (respuesta.hasNonNull("error")) return respuesta.get("error").asText();
        } catch (Exception ignorada) {
            // Si el servidor no devolvió JSON, se usa un mensaje HTTP genérico.
        }
        return "La solicitud no se pudo completar (HTTP " + excepcion.getStatusCode().value() + ").";
    }
}
