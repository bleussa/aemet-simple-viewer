package com.bleussa.aemet.infrastructure.adapter.out.aemet;

import com.bleussa.aemet.application.port.out.PrediccionProvider;
import com.bleussa.aemet.domain.exception.MunicipioNoEncontradoException;
import com.bleussa.aemet.domain.model.PrediccionDiaria;
import com.bleussa.aemet.domain.model.PrediccionMunicipio;
import com.bleussa.aemet.domain.model.ProbabilidadPrecipitacion;
import com.bleussa.aemet.infrastructure.adapter.out.aemet.dto.AemetPrediccion;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;

import java.util.List;

@Component
public class AemetPrediccionAdapter implements PrediccionProvider {

    private final AemetClient aemetClient;

    public AemetPrediccionAdapter(AemetClient aemetClient) {
        this.aemetClient = aemetClient;
    }

    @Override
    @Cacheable(AemetCacheConfig.PREDICCION)
    public PrediccionMunicipio obtenerPorMunicipio(String codigoMunicipio) {
        List<AemetPrediccion> respuesta = aemetClient.obtenerDatos(
                new TypeReference<List<AemetPrediccion>>() {},
                "/prediccion/especifica/municipio/diaria/{codigo}",
                codigoMunicipio)
                .orElseThrow(() -> new MunicipioNoEncontradoException(codigoMunicipio));

        List<PrediccionDiaria> dias = respuesta.stream()
                .findFirst()
                .map(prediccion -> prediccion.prediccion().dia().stream()
                        .map(AemetPrediccionAdapter::mapearDominio)
                        .toList())
                .orElse(List.of());

        return new PrediccionMunicipio(codigoMunicipio, dias);
    }

    private static PrediccionDiaria mapearDominio(AemetPrediccion.Dia dia) {
        List<ProbabilidadPrecipitacion> probabilidades = dia.probPrecipitacion().stream()
                .filter(probabilidad -> probabilidad.value() != null)
                .map(probabilidad -> new ProbabilidadPrecipitacion(probabilidad.value(), probabilidad.periodo()))
                .toList();

        return new PrediccionDiaria(
                dia.fecha().toLocalDate(),
                dia.temperatura().maxima(),
                dia.temperatura().minima(),
                probabilidades);
    }
}
