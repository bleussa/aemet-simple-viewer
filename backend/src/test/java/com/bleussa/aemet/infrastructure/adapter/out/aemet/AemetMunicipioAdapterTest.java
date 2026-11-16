package com.bleussa.aemet.infrastructure.adapter.out.aemet;

import com.bleussa.aemet.domain.model.Municipio;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.json.JsonMapper;

import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class AemetMunicipioAdapterTest {

    private static final Charset ISO_8859_15 = Charset.forName("ISO-8859-15");

    private final RestClient.Builder builder = RestClient.builder()
            .baseUrl("https://aemet.test/api")
            .defaultHeader("api_key", "clave");
    private final MockRestServiceServer servidor = MockRestServiceServer.bindTo(builder).build();
    private final AemetMunicipioAdapter adapter = new AemetMunicipioAdapter(
            new AemetClient(builder.build(), JsonMapper.builder().build()));

    @Test
    void obtieneMunicipiosConCodigoDeCincoDigitosYTildes() {
        servidor.expect(requestTo("https://aemet.test/api/maestro/municipios"))
                .andExpect(header("api_key", "clave"))
                .andRespond(withSuccess("""
                        {"descripcion":"exito","estado":200,"datos":"https://aemet.test/datos/1","metadatos":"https://aemet.test/meta/1"}
                        """, MediaType.APPLICATION_JSON));
        servidor.expect(requestTo("https://aemet.test/datos/1"))
                .andRespond(withSuccess("""
                        [{"id":"id44013","nombre":"Alcañiz","capital":"Alcañiz"},{"id":"id05019","nombre":"Ávila"}]
                        """.getBytes(ISO_8859_15), new MediaType("text", "plain", ISO_8859_15)));

        assertThat(adapter.obtenerTodos()).containsExactly(
                new Municipio("44013", "Alcañiz"),
                new Municipio("05019", "Ávila"));
        servidor.verify();
    }
}
