package com.bleussa.aemet.infrastructure.adapter.in.rest;

import com.bleussa.aemet.application.port.in.ObtenerPrediccionUseCase;
import com.bleussa.aemet.domain.exception.MunicipioNoEncontradoException;
import com.bleussa.aemet.domain.exception.PrediccionNoDisponibleException;
import com.bleussa.aemet.domain.exception.ServicioMeteorologicoNoDisponibleException;
import com.bleussa.aemet.domain.model.ProbabilidadPrecipitacion;
import com.bleussa.aemet.domain.model.ResumenPrediccion;
import com.bleussa.aemet.domain.model.UnidadTemperatura;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyNoInteractions;

@WebMvcTest(PrediccionController.class)
class PrediccionControllerTest {

    @Autowired
    private MockMvcTester mvc;

    @MockitoBean
    private ObtenerPrediccionUseCase obtenerPrediccionUseCase;

    @Test
    void devuelveLaPrediccionEnLaUnidadPedida() {
        given(obtenerPrediccionUseCase.obtenerParaManana("44001", UnidadTemperatura.G_FAH))
                .willReturn(new ResumenPrediccion(66.7, UnidadTemperatura.G_FAH, List.of(
                        new ProbabilidadPrecipitacion(5, "00-06"),
                        new ProbabilidadPrecipitacion(10, "06-12"),
                        new ProbabilidadPrecipitacion(10, "12-18"),
                        new ProbabilidadPrecipitacion(5, "18-24"))));

        assertThat(mvc.get().uri("/api/prediccion/44001?unidad=G_FAH"))
                .hasStatusOk()
                .bodyJson()
                .isStrictlyEqualTo("""
                        {
                          "mediaTemperatura": 66.7,
                          "unidadTemperatura": "G_FAH",
                          "probPrecipitacion": [
                            { "probabilidad": 5, "periodo": "00-06" },
                            { "probabilidad": 10, "periodo": "06-12" },
                            { "probabilidad": 10, "periodo": "12-18" },
                            { "probabilidad": 5, "periodo": "18-24" }
                          ]
                        }
                        """);
    }

    @Test
    void usaCelsiusPorDefecto() {
        given(obtenerPrediccionUseCase.obtenerParaManana("44001", UnidadTemperatura.G_CEL))
                .willReturn(new ResumenPrediccion(19.0, UnidadTemperatura.G_CEL, List.of()));

        assertThat(mvc.get().uri("/api/prediccion/44001"))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.unidadTemperatura").isEqualTo("G_CEL");
    }

    @ParameterizedTest
    @ValueSource(strings = {"4400", "440011", "4400a"})
    void rechazaCodigosDeMunicipioInvalidos(String codigo) {
        assertThat(mvc.get().uri("/api/prediccion/{codigo}", codigo))
                .hasStatus(HttpStatus.BAD_REQUEST)
                .bodyJson()
                .extractingPath("$.status").isEqualTo(400);
        verifyNoInteractions(obtenerPrediccionUseCase);
    }

    @Test
    void rechazaUnidadesInvalidas() {
        assertThat(mvc.get().uri("/api/prediccion/44001?unidad=KELVIN"))
                .hasStatus(HttpStatus.BAD_REQUEST);
        verifyNoInteractions(obtenerPrediccionUseCase);
    }

    @Test
    void devuelve404SiElMunicipioNoExiste() {
        given(obtenerPrediccionUseCase.obtenerParaManana(anyString(), any()))
                .willThrow(new MunicipioNoEncontradoException("99999"));

        assertThat(mvc.get().uri("/api/prediccion/99999"))
                .hasStatus(HttpStatus.NOT_FOUND)
                .bodyJson()
                .extractingPath("$.detail").isEqualTo("No existe el municipio 99999");
    }

    @Test
    void devuelve404SiNoHayPrediccionParaManana() {
        given(obtenerPrediccionUseCase.obtenerParaManana(anyString(), any()))
                .willThrow(new PrediccionNoDisponibleException("44001", LocalDate.of(2026, 11, 17)));

        assertThat(mvc.get().uri("/api/prediccion/44001")).hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void devuelve502SiAemetFalla() {
        given(obtenerPrediccionUseCase.obtenerParaManana(anyString(), any()))
                .willThrow(new ServicioMeteorologicoNoDisponibleException("Error al consultar AEMET"));

        assertThat(mvc.get().uri("/api/prediccion/44001")).hasStatus(HttpStatus.BAD_GATEWAY);
    }
}
