package com.bleussa.aemet.infraestructure.adapter.out.aemet;

import com.bleussa.aemet.application.port.out.MunicipioProvider;
import com.bleussa.aemet.domain.model.Municipio;
import com.bleussa.aemet.infraestructure.adapter.out.aemet.dto.AemetMunicipio;
import com.bleussa.aemet.infraestructure.adapter.out.aemet.dto.AemetResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.net.URI;
import java.util.List;

@Component
public class AemetMunicipioAdapter implements MunicipioProvider {

    private final RestClient restClient;
    private final JsonMapper jsonMapper;

    public AemetMunicipioAdapter(RestClient restClient, JsonMapper jsonMapper) {
        this.restClient = restClient;
        this.jsonMapper = jsonMapper;
    }

    @Override
    public List<Municipio> obtenerTodos() {
        AemetResponse respuesta = restClient.get()
                .uri("/maestro/municipios")
                .retrieve()
                .body(AemetResponse.class);

        if (respuesta == null || respuesta.datos() == null) {
            throw new IllegalStateException("AEMET no devolvió la URL de datos");
        }

        String json = restClient.get()
                .uri(URI.create(respuesta.datos()))
                .retrieve()
                .body(String.class);

        List<AemetMunicipio> dtos = jsonMapper.readValue(json, new TypeReference<List<AemetMunicipio>>() {});

        return dtos.stream()
                .map(AemetMunicipioAdapter::mapearDominio)
                .toList();
    }

    private static Municipio mapearDominio(AemetMunicipio aemetMunicipio) {
        String codigo = aemetMunicipio.id().substring(2);
        return new Municipio(codigo, aemetMunicipio.nombre());
    }
}
