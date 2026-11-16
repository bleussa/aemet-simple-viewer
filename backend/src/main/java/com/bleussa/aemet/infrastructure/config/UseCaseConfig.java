package com.bleussa.aemet.infrastructure.config;

import com.bleussa.aemet.application.port.in.BuscarMunicipiosUseCase;
import com.bleussa.aemet.application.port.out.MunicipioProvider;
import com.bleussa.aemet.application.service.BuscarMunicipiosService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    BuscarMunicipiosUseCase buscarMunicipiosUseCase(MunicipioProvider municipioProvider) {
        return new BuscarMunicipiosService(municipioProvider);
    }
}
