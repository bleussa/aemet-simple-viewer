package com.bleussa.aemet.infrastructure.adapter.out.aemet;

import com.bleussa.aemet.domain.exception.ServicioMeteorologicoNoDisponibleException;
import com.bleussa.aemet.infrastructure.adapter.out.aemet.dto.AemetResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.net.URI;
import java.util.Optional;

@Component
public class AemetClient {

    private static final int ESTADO_SIN_DATOS = 404;

    private final RestClient restClient;
    private final JsonMapper jsonMapper;

    public AemetClient(RestClient aemetRestClient, JsonMapper jsonMapper) {
        this.restClient = aemetRestClient;
        this.jsonMapper = jsonMapper;
    }

    public <T> Optional<T> obtenerDatos(TypeReference<T> tipo, String ruta, Object... variables) {
        try {
            AemetResponse respuesta = restClient.get()
                    .uri(ruta, variables)
                    .retrieve()
                    .body(AemetResponse.class);

            if (respuesta != null && respuesta.estado() == ESTADO_SIN_DATOS) {
                return Optional.empty();
            }
            if (respuesta == null || respuesta.datos() == null) {
                throw new ServicioMeteorologicoNoDisponibleException("AEMET no devolvió la URL de datos");
            }

            String json = restClient.get()
                    .uri(URI.create(respuesta.datos()))
                    .retrieve()
                    .body(String.class);

            return Optional.of(jsonMapper.readValue(json, tipo));
        } catch (RestClientException | JacksonException e) {
            throw new ServicioMeteorologicoNoDisponibleException("Error al consultar AEMET", e);
        }
    }
}
