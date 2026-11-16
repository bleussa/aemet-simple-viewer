package com.bleussa.aemet.infraestructure.adapter.out.aemet.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AemetResponse(
        String descripcion,
        int estado,
        String datos,
        String metadatos
) {}
