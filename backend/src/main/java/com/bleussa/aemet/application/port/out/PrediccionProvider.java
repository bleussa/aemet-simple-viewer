package com.bleussa.aemet.application.port.out;

import com.bleussa.aemet.domain.model.PrediccionMunicipio;

public interface PrediccionProvider {
    PrediccionMunicipio obtenerPorMunicipio(String codigoMunicipio);
}
