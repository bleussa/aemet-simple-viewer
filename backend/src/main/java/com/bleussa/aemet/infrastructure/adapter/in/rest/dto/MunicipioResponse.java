package com.bleussa.aemet.infrastructure.adapter.in.rest.dto;

import com.bleussa.aemet.domain.model.Municipio;

public record MunicipioResponse (
        String codigo,
        String nombre
){
    public static MunicipioResponse mapearDominio(Municipio municipio) {
        return new MunicipioResponse(municipio.codigo(), municipio.nombre());
    }
}
