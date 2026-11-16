package com.bleussa.aemet.infrastructure.adapter.out.aemet;

import com.bleussa.aemet.domain.exception.MunicipioNoEncontradoException;
import com.bleussa.aemet.domain.exception.ServicioMeteorologicoNoDisponibleException;
import com.bleussa.aemet.domain.model.PrediccionDiaria;
import com.bleussa.aemet.domain.model.PrediccionMunicipio;
import com.bleussa.aemet.domain.model.ProbabilidadPrecipitacion;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.json.JsonMapper;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class AemetPrediccionAdapterTest {

    private static final Charset ISO_8859_15 = Charset.forName("ISO-8859-15");

    private final RestClient.Builder builder = RestClient.builder().baseUrl("https://aemet.test/api");
    private final MockRestServiceServer servidor = MockRestServiceServer.bindTo(builder).build();
    private final AemetPrediccionAdapter adapter = new AemetPrediccionAdapter(
            new AemetClient(builder.build(), JsonMapper.builder().build()));

    @Test
    void obtieneLosDiasDeLaPrediccion() {
        servidor.expect(requestTo("https://aemet.test/api/prediccion/especifica/municipio/diaria/44013"))
                .andRespond(withSuccess("""
                        {"descripcion":"exito","estado":200,"datos":"https://aemet.test/datos/2","metadatos":"https://aemet.test/meta/2"}
                        """, MediaType.APPLICATION_JSON));
        servidor.expect(requestTo("https://aemet.test/datos/2"))
                .andRespond(withSuccess("""
                        [{"nombre":"Alcañiz","provincia":"Teruel","prediccion":{"dia":[
                          {"fecha":"2026-11-17T00:00:00",
                           "probPrecipitacion":[{"value":10,"periodo":"00-24"},{"value":5,"periodo":"00-06"},{"value":"","periodo":"06-12"}],
                           "temperatura":{"maxima":22,"minima":17,"dato":[]}},
                          {"fecha":"2026-11-18T00:00:00",
                           "probPrecipitacion":[{"value":40}],
                           "temperatura":{"maxima":15,"minima":8}}
                        ]}}]
                        """.getBytes(ISO_8859_15), new MediaType("text", "plain", ISO_8859_15)));

        PrediccionMunicipio prediccion = adapter.obtenerPorMunicipio("44013");

        assertThat(prediccion).isEqualTo(new PrediccionMunicipio("44013", List.of(
                new PrediccionDiaria(LocalDate.of(2026, 11, 17), 22, 17, List.of(
                        new ProbabilidadPrecipitacion(10, "00-24"),
                        new ProbabilidadPrecipitacion(5, "00-06"))),
                new PrediccionDiaria(LocalDate.of(2026, 11, 18), 15, 8, List.of(
                        new ProbabilidadPrecipitacion(40, null))))));
        servidor.verify();
    }

    @Test
    void lanzaMunicipioNoEncontradoSiAemetResponde404EnElSobre() {
        servidor.expect(requestTo("https://aemet.test/api/prediccion/especifica/municipio/diaria/99999"))
                .andRespond(withSuccess("""
                        {"descripcion":"Error al obtener los datos","estado":404}
                        """, MediaType.APPLICATION_JSON));

        assertThatThrownBy(() -> adapter.obtenerPorMunicipio("99999"))
                .isInstanceOf(MunicipioNoEncontradoException.class);
    }

    @Test
    void lanzaServicioNoDisponibleSiAemetFalla() {
        servidor.expect(requestTo("https://aemet.test/api/prediccion/especifica/municipio/diaria/44013"))
                .andRespond(withServerError());

        assertThatThrownBy(() -> adapter.obtenerPorMunicipio("44013"))
                .isInstanceOf(ServicioMeteorologicoNoDisponibleException.class);
    }
}
