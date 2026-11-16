package com.bleussa.aemet.application.service;

import com.bleussa.aemet.application.port.in.ObtenerPrediccionUseCase;
import com.bleussa.aemet.application.port.out.PrediccionProvider;
import com.bleussa.aemet.domain.model.PrediccionDiaria;
import com.bleussa.aemet.domain.model.ResumenPrediccion;
import com.bleussa.aemet.domain.model.UnidadTemperatura;

import java.time.Clock;

public class ObtenerPrediccionService implements ObtenerPrediccionUseCase {

    private final PrediccionProvider prediccionProvider;
    private final Clock clock;

    public ObtenerPrediccionService(PrediccionProvider prediccionProvider, Clock clock) {
        this.prediccionProvider = prediccionProvider;
        this.clock = clock;
    }

    @Override
    public ResumenPrediccion obtenerParaManana(String codigoMunicipio, UnidadTemperatura unidad) {
        PrediccionDiaria manana = prediccionProvider.obtenerPorMunicipio(codigoMunicipio).diaSiguiente(clock);
        return ResumenPrediccion.de(manana, unidad);
    }
}
