package com.bleussa.aemet.application.service;

import com.bleussa.aemet.application.port.out.PrediccionProvider;
import com.bleussa.aemet.domain.exception.PrediccionNoDisponibleException;
import com.bleussa.aemet.domain.model.PrediccionDiaria;
import com.bleussa.aemet.domain.model.PrediccionMunicipio;
import com.bleussa.aemet.domain.model.ProbabilidadPrecipitacion;
import com.bleussa.aemet.domain.model.ResumenPrediccion;
import com.bleussa.aemet.domain.model.UnidadTemperatura;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ObtenerPrediccionServiceTest {

    private static final Clock RELOJ = Clock.fixed(Instant.parse("2026-11-16T10:00:00Z"), ZoneId.of("Europe/Madrid"));

    private final PrediccionProvider providerFalso = codigo -> new PrediccionMunicipio(codigo, List.of(
            new PrediccionDiaria(LocalDate.of(2026, 11, 16), 25, 15, List.of()),
            new PrediccionDiaria(LocalDate.of(2026, 11, 17), 22, 17, List.of(
                    new ProbabilidadPrecipitacion(10, "00-24"),
                    new ProbabilidadPrecipitacion(10, "12-18"),
                    new ProbabilidadPrecipitacion(5, "00-06"),
                    new ProbabilidadPrecipitacion(10, "06-12"),
                    new ProbabilidadPrecipitacion(5, "18-24")))));

    private final ObtenerPrediccionService service = new ObtenerPrediccionService(providerFalso, RELOJ);

    @Test
    void resumeLaPrediccionDeMananaEnLaUnidadPedida() {
        ResumenPrediccion resumen = service.obtenerParaManana("44001", UnidadTemperatura.G_FAH);

        assertThat(resumen).isEqualTo(new ResumenPrediccion(67.1, UnidadTemperatura.G_FAH, List.of(
                new ProbabilidadPrecipitacion(5, "00-06"),
                new ProbabilidadPrecipitacion(10, "06-12"),
                new ProbabilidadPrecipitacion(10, "12-18"),
                new ProbabilidadPrecipitacion(5, "18-24"))));
    }

    @Test
    void fallaSiNoHayPrediccionParaManana() {
        PrediccionProvider sinDatos = codigo -> new PrediccionMunicipio(codigo, List.of());

        assertThatThrownBy(() -> new ObtenerPrediccionService(sinDatos, RELOJ).obtenerParaManana("44001", UnidadTemperatura.G_CEL))
                .isInstanceOf(PrediccionNoDisponibleException.class);
    }
}
