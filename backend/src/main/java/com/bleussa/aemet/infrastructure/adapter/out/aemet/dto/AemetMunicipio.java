package com.bleussa.aemet.infrastructure.adapter.out.aemet.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AemetMunicipio(
        String id,
        String nombre
) {}
