package com.bleussa.aemet.application.port.out;

import com.bleussa.aemet.domain.model.Municipio;

import java.util.List;

public interface MunicipioProvider {
    List<Municipio> obtenerTodos();
}
