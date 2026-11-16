package com.bleussa.aemet.infrastructure.adapter.in.rest.dto;

import com.bleussa.aemet.domain.model.ResumenPrediccion;
import com.bleussa.aemet.domain.model.UnidadTemperatura;

import java.util.List;

public record PrediccionResponse(
        double mediaTemperatura,
        UnidadTemperatura unidadTemperatura,
        List<ProbabilidadPrecipitacionResponse> probPrecipitacion
) {

    public record ProbabilidadPrecipitacionResponse(int probabilidad, String periodo) {}

    public static PrediccionResponse mapearDominio(ResumenPrediccion resumen) {
        return new PrediccionResponse(
                resumen.mediaTemperatura(),
                resumen.unidadTemperatura(),
                resumen.probPrecipitacion().stream()
                        .map(probabilidad -> new ProbabilidadPrecipitacionResponse(probabilidad.probabilidad(), probabilidad.periodo()))
                        .toList());
    }
}
