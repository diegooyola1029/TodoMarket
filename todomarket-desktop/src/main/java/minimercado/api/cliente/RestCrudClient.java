package minimercado.api.cliente;

import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

/** Reúne las llamadas CRUD que comparten los recursos REST del escritorio. */
public final class RestCrudClient<Solicitud, Respuesta> {
    private final RestClient cliente;
    private final String ruta;
    private final Class<Respuesta> tipoRespuesta;
    private final ParameterizedTypeReference<List<Respuesta>> tipoLista;

    public RestCrudClient(RestClient cliente, String ruta, Class<Respuesta> tipoRespuesta,
                          ParameterizedTypeReference<List<Respuesta>> tipoLista) {
        this.cliente = cliente;
        this.ruta = ruta;
        this.tipoRespuesta = tipoRespuesta;
        this.tipoLista = tipoLista;
    }

    public List<Respuesta> listar() {
        return listar(ruta);
    }

    public List<Respuesta> listar(String rutaConsulta) {
        return ApiClientSupport.ejecutar(() -> cliente.get().uri(rutaConsulta)
                .retrieve().body(tipoLista));
    }

    public Respuesta buscar(int id) {
        return ApiClientSupport.ejecutar(() -> cliente.get().uri(ruta + "/{id}", id)
                .retrieve().body(tipoRespuesta));
    }

    public void crear(Solicitud solicitud) {
        ApiClientSupport.ejecutar(() -> cliente.post().uri(ruta).body(solicitud)
                .retrieve().toBodilessEntity());
    }

    public void actualizar(int id, Solicitud solicitud) {
        ApiClientSupport.ejecutar(() -> cliente.put().uri(ruta + "/{id}", id).body(solicitud)
                .retrieve().toBodilessEntity());
    }

    public void eliminar(int id) {
        ApiClientSupport.ejecutar(() -> cliente.delete().uri(ruta + "/{id}", id)
                .retrieve().toBodilessEntity());
    }
}
