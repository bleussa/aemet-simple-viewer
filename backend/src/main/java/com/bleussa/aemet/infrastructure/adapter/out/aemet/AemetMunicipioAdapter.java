package com.bleussa.aemet.infrastructure.adapter.out.aemet;

import com.bleussa.aemet.application.port.out.MunicipioProvider;
import com.bleussa.aemet.domain.exception.ServicioMeteorologicoNoDisponibleException;
import com.bleussa.aemet.domain.model.Municipio;
import com.bleussa.aemet.infrastructure.adapter.out.aemet.dto.AemetMunicipio;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;

import java.util.List;

@Component
public class AemetMunicipioAdapter implements MunicipioProvider {

    private final AemetClient aemetClient;

    public AemetMunicipioAdapter(AemetClient aemetClient) {
        this.aemetClient = aemetClient;
    }

    @Override
    public List<Municipio> obtenerTodos() {
        return aemetClient.obtenerDatos(new TypeReference<List<AemetMunicipio>>() {}, "/maestro/municipios")
                .orElseThrow(() -> new ServicioMeteorologicoNoDisponibleException("AEMET no devolvió municipios"))
                .stream()
                .map(AemetMunicipioAdapter::mapearDominio)
                .toList();
    }

    private static Municipio mapearDominio(AemetMunicipio aemetMunicipio) {
        String codigo = aemetMunicipio.id().substring(2);
        return new Municipio(codigo, aemetMunicipio.nombre());
    }
}
