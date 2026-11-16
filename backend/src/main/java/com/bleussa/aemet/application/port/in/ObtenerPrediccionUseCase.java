package com.bleussa.aemet.application.port.in;

import com.bleussa.aemet.domain.model.ResumenPrediccion;
import com.bleussa.aemet.domain.model.UnidadTemperatura;

public interface ObtenerPrediccionUseCase {
    ResumenPrediccion obtenerParaManana(String codigoMunicipio, UnidadTemperatura unidad);
}
