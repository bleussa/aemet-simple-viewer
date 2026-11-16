package com.bleussa.aemet.infraestructure.adapter.out.aemet.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AemetMunicipio(
        String id,
        String nombre
) {}
