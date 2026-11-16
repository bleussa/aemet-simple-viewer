package com.bleussa.aemet.infrastructure.adapter.out.aemet;

import com.bleussa.aemet.infrastructure.adapter.out.aemet.dto.AemetResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.net.URI;

@Component
public class AemetClient {

    private final RestClient restClient;
    private final JsonMapper jsonMapper;

    public AemetClient(RestClient aemetRestClient, JsonMapper jsonMapper) {
        this.restClient = aemetRestClient;
        this.jsonMapper = jsonMapper;
    }

    public <T> T obtenerDatos(TypeReference<T> tipo, String ruta, Object... variables) {
        AemetResponse respuesta = restClient.get()
                .uri(ruta, variables)
                .retrieve()
                .body(AemetResponse.class);

        if (respuesta == null || respuesta.datos() == null) {
            throw new IllegalStateException("AEMET no devolvió la URL de datos");
        }

        String json = restClient.get()
                .uri(URI.create(respuesta.datos()))
                .retrieve()
                .body(String.class);

        return jsonMapper.readValue(json, tipo);
    }
}
