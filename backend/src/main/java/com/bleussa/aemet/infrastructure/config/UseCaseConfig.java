package com.bleussa.aemet.infrastructure.config;

import com.bleussa.aemet.application.port.in.BuscarMunicipiosUseCase;
import com.bleussa.aemet.application.port.in.ObtenerPrediccionUseCase;
import com.bleussa.aemet.application.port.out.MunicipioProvider;
import com.bleussa.aemet.application.port.out.PrediccionProvider;
import com.bleussa.aemet.application.service.BuscarMunicipiosService;
import com.bleussa.aemet.application.service.ObtenerPrediccionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class UseCaseConfig {

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    BuscarMunicipiosUseCase buscarMunicipiosUseCase(MunicipioProvider municipioProvider) {
        return new BuscarMunicipiosService(municipioProvider);
    }

    @Bean
    ObtenerPrediccionUseCase obtenerPrediccionUseCase(PrediccionProvider prediccionProvider, Clock clock) {
        return new ObtenerPrediccionService(prediccionProvider, clock);
    }
}
