package com.bleussa.aemet.domain.model;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public record PrediccionDiaria(
        LocalDate fecha,
        int temperaturaMaxima,
        int temperaturaMinima,
        List<ProbabilidadPrecipitacion> probPrecipitacion
) {

    private static final Set<String> FRANJAS_SEIS_HORAS = Set.of("00-06", "06-12", "12-18", "18-24");

    public double mediaTemperatura(UnidadTemperatura unidad) {
        double mediaCelsius = (temperaturaMaxima + temperaturaMinima) / 2.0;
        return Math.round(unidad.desdeCelsius(mediaCelsius) * 10) / 10.0;
    }

    public List<ProbabilidadPrecipitacion> franjasDeSeisHoras() {
        return probPrecipitacion.stream()
                .filter(probabilidad -> FRANJAS_SEIS_HORAS.contains(probabilidad.periodo()))
                .sorted(Comparator.comparing(ProbabilidadPrecipitacion::periodo))
                .toList();
    }
}
