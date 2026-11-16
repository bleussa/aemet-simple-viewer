package com.bleussa.aemet.infrastructure.adapter.out.aemet.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AemetPrediccion(Prediccion prediccion) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Prediccion(List<Dia> dia) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Dia(LocalDateTime fecha, Temperatura temperatura, List<ProbPrecipitacion> probPrecipitacion) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Temperatura(int maxima, int minima) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ProbPrecipitacion(Integer value, String periodo) {}
}
