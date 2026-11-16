package com.bleussa.aemet.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PrediccionDiariaTest {

    private static PrediccionDiaria dia(int maxima, int minima, List<ProbabilidadPrecipitacion> probabilidades) {
        return new PrediccionDiaria(LocalDate.of(2026, 11, 17), maxima, minima, probabilidades);
    }

    @Test
    void calculaLaMediaEnCelsius() {
        assertThat(dia(20, 9, List.of()).mediaTemperatura(UnidadTemperatura.G_CEL)).isEqualTo(14.5);
    }

    @Test
    void calculaLaMediaEnFahrenheitRedondeadaAUnDecimal() {
        assertThat(dia(22, 17, List.of()).mediaTemperatura(UnidadTemperatura.G_FAH)).isEqualTo(67.1);
    }

    @Test
    void calculaLaMediaConTemperaturasNegativas() {
        assertThat(dia(-1, -6, List.of()).mediaTemperatura(UnidadTemperatura.G_CEL)).isEqualTo(-3.5);
    }

    @Test
    void devuelveSoloLasFranjasDeSeisHorasOrdenadas() {
        PrediccionDiaria dia = dia(20, 10, List.of(
                new ProbabilidadPrecipitacion(10, "00-24"),
                new ProbabilidadPrecipitacion(5, "18-24"),
                new ProbabilidadPrecipitacion(10, "00-12"),
                new ProbabilidadPrecipitacion(10, "12-24"),
                new ProbabilidadPrecipitacion(0, "00-06"),
                new ProbabilidadPrecipitacion(15, "12-18"),
                new ProbabilidadPrecipitacion(5, "06-12")));

        assertThat(dia.franjasDeSeisHoras()).containsExactly(
                new ProbabilidadPrecipitacion(0, "00-06"),
                new ProbabilidadPrecipitacion(5, "06-12"),
                new ProbabilidadPrecipitacion(15, "12-18"),
                new ProbabilidadPrecipitacion(5, "18-24"));
    }
}
