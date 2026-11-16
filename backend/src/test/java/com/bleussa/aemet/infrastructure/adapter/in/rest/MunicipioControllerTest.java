package com.bleussa.aemet.infrastructure.adapter.in.rest;

import com.bleussa.aemet.application.port.in.BuscarMunicipiosUseCase;
import com.bleussa.aemet.domain.exception.ServicioMeteorologicoNoDisponibleException;
import com.bleussa.aemet.domain.model.Municipio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@WebMvcTest(MunicipioController.class)
class MunicipioControllerTest {

    @Autowired
    private MockMvcTester mvc;

    @MockitoBean
    private BuscarMunicipiosUseCase buscarMunicipiosUseCase;

    @Test
    void devuelveLosMunicipiosEncontrados() {
        given(buscarMunicipiosUseCase.buscarPorNombre("alcañ"))
                .willReturn(List.of(new Municipio("44013", "Alcañiz")));

        assertThat(mvc.get().uri("/api/municipios").param("nombre", "alcañ"))
                .hasStatusOk()
                .bodyJson()
                .isStrictlyEqualTo("""
                        [{ "codigo": "44013", "nombre": "Alcañiz" }]
                        """);
    }

    @Test
    void devuelve400SinNombre() {
        assertThat(mvc.get().uri("/api/municipios")).hasStatus(HttpStatus.BAD_REQUEST);
    }

    @Test
    void devuelve502SiAemetFalla() {
        given(buscarMunicipiosUseCase.buscarPorNombre("vigo"))
                .willThrow(new ServicioMeteorologicoNoDisponibleException("Error al consultar AEMET"));

        assertThat(mvc.get().uri("/api/municipios").param("nombre", "vigo"))
                .hasStatus(HttpStatus.BAD_GATEWAY);
    }
}
