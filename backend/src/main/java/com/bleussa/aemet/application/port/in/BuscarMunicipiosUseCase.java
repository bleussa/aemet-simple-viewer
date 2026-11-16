package com.bleussa.aemet.application.port.in;

import com.bleussa.aemet.domain.model.Municipio;

import java.util.List;

public interface BuscarMunicipiosUseCase {
    List<Municipio> buscarPorNombre(String nombre);
}
