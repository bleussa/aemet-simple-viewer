package com.bleussa.aemet.infrastructure.adapter.out.aemet.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AemetResponse(
        String descripcion,
        int estado,
        String datos,
        String metadatos
) {}
