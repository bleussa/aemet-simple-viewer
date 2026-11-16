package com.bleussa.aemet.infrastructure.adapter.out.aemet;

import com.bleussa.aemet.application.port.out.MunicipioProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringJUnitConfig
class AemetCacheTest {

    @Configuration
    @Import({AemetCacheConfig.class, AemetMunicipioAdapter.class})
    static class Config {

        private final RestClient.Builder builder = RestClient.builder().baseUrl("https://aemet.test/api");

        @Bean
        MockRestServiceServer servidor() {
            return MockRestServiceServer.bindTo(builder).build();
        }

        @Bean
        AemetClient aemetClient(MockRestServiceServer servidor) {
            return new AemetClient(builder.build(), JsonMapper.builder().build());
        }

        @Bean
        AemetProperties aemetProperties() {
            return new AemetProperties(null, null, null, null,
                    new AemetProperties.Cache(Duration.ofHours(24), Duration.ofHours(1)));
        }
    }

    @Autowired
    private MunicipioProvider municipioProvider;

    @Autowired
    private MockRestServiceServer servidor;

    @Test
    void consultaAemetUnaSolaVezParaElListadoDeMunicipios() {
        servidor.expect(requestTo("https://aemet.test/api/maestro/municipios"))
                .andRespond(withSuccess("""
                        {"estado":200,"datos":"https://aemet.test/datos/1"}
                        """, MediaType.APPLICATION_JSON));
        servidor.expect(requestTo("https://aemet.test/datos/1"))
                .andRespond(withSuccess("""
                        [{"id":"id44013","nombre":"Alcañiz"}]
                        """, MediaType.APPLICATION_JSON));

        municipioProvider.obtenerTodos();
        assertThat(municipioProvider.obtenerTodos()).hasSize(1);

        servidor.verify();
    }
}
