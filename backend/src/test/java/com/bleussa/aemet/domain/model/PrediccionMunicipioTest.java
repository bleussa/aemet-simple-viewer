package com.bleussa.aemet.domain.model;

import com.bleussa.aemet.domain.exception.PrediccionNoDisponibleException;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PrediccionMunicipioTest {

    private static final PrediccionMunicipio PREDICCION = new PrediccionMunicipio("44001", List.of(
            dia(LocalDate.of(2026, 11, 16)),
            dia(LocalDate.of(2026, 11, 17)),
            dia(LocalDate.of(2026, 11, 18))));

    private static PrediccionDiaria dia(LocalDate fecha) {
        return new PrediccionDiaria(fecha, 20, 10, List.of());
    }

    private static Clock relojEn(String instante) {
        return Clock.fixed(Instant.parse(instante), ZoneOffset.UTC);
    }

    @Test
    void seleccionaElDiaSiguiente() {
        assertThat(PREDICCION.diaSiguiente(relojEn("2026-11-16T10:00:00Z")).fecha())
                .isEqualTo(LocalDate.of(2026, 11, 17));
    }

    @Test
    void calculaElDiaSiguienteConLaHoraDeEspana() {
        assertThat(PREDICCION.diaSiguiente(relojEn("2026-11-16T23:30:00Z")).fecha())
                .isEqualTo(LocalDate.of(2026, 11, 18));
    }

    @Test
    void lanzaExcepcionSiNoHayDatosParaManana() {
        assertThatThrownBy(() -> PREDICCION.diaSiguiente(relojEn("2026-11-18T10:00:00Z")))
                .isInstanceOf(PrediccionNoDisponibleException.class);
    }
}
