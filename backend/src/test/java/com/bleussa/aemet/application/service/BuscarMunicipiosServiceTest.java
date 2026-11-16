package com.bleussa.aemet.application.service;

import com.bleussa.aemet.application.port.out.MunicipioProvider;
import com.bleussa.aemet.domain.model.Municipio;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BuscarMunicipiosServiceTest {

    private final MunicipioProvider providerFalso = () -> List.of(
            new Municipio("12345", "Elche"),
            new Municipio("54321", "Alicante"),
            new Municipio("11223", "Vigo")
    );

    private final BuscarMunicipiosService service = new BuscarMunicipiosService(providerFalso);

    @Test
    void encuentraMunicipiosQueContienenElTexto() {
        List<Municipio> resultado = service.buscarPorNombre("elc");

        assertThat(resultado)
                .extracting(Municipio::nombre)
                .containsExactly("Elche");
    }

    @Test
    void ignoraMayusculas() {
        assertThat(service.buscarPorNombre("VIGO")).hasSize(1);
    }

    @Test
    void devuelveListaVaciaSiElTextoEstaVacio() {
        assertThat(service.buscarPorNombre("  ")).isEmpty();
    }

}
