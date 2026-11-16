package com.bleussa.aemet.domain.model;

import java.util.List;

public record ResumenPrediccion(
        double mediaTemperatura,
        UnidadTemperatura unidadTemperatura,
        List<ProbabilidadPrecipitacion> probPrecipitacion
) {

    public static ResumenPrediccion de(PrediccionDiaria dia, UnidadTemperatura unidad) {
        return new ResumenPrediccion(dia.mediaTemperatura(unidad), unidad, dia.franjasDeSeisHoras());
    }
}
