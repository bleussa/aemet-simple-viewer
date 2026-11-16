package com.bleussa.aemet.domain.model;

import com.bleussa.aemet.domain.exception.PrediccionNoDisponibleException;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public record PrediccionMunicipio(String codigoMunicipio, List<PrediccionDiaria> dias) {

    private static final ZoneId ZONA_ESPANA = ZoneId.of("Europe/Madrid");

    public PrediccionDiaria diaSiguiente(Clock clock) {
        LocalDate manana = LocalDate.now(clock.withZone(ZONA_ESPANA)).plusDays(1);
        return dias.stream()
                .filter(dia -> dia.fecha().equals(manana))
                .findFirst()
                .orElseThrow(() -> new PrediccionNoDisponibleException(codigoMunicipio, manana));
    }
}
