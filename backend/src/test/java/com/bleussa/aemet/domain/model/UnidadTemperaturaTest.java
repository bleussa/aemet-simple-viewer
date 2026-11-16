package com.bleussa.aemet.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UnidadTemperaturaTest {

    @Test
    void celsiusNoConvierte() {
        assertThat(UnidadTemperatura.G_CEL.desdeCelsius(19.5)).isEqualTo(19.5);
    }

    @Test
    void convierteAFahrenheit() {
        assertThat(UnidadTemperatura.G_FAH.desdeCelsius(0)).isEqualTo(32);
        assertThat(UnidadTemperatura.G_FAH.desdeCelsius(100)).isEqualTo(212);
        assertThat(UnidadTemperatura.G_FAH.desdeCelsius(-40)).isEqualTo(-40);
    }
}
