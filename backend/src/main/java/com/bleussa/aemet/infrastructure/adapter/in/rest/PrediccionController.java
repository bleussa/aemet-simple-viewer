package com.bleussa.aemet.infrastructure.adapter.in.rest;

import com.bleussa.aemet.application.port.in.ObtenerPrediccionUseCase;
import com.bleussa.aemet.domain.model.UnidadTemperatura;
import com.bleussa.aemet.infrastructure.adapter.in.rest.dto.PrediccionResponse;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prediccion")
public class PrediccionController {

    private final ObtenerPrediccionUseCase obtenerPrediccionUseCase;

    public PrediccionController(ObtenerPrediccionUseCase obtenerPrediccionUseCase) {
        this.obtenerPrediccionUseCase = obtenerPrediccionUseCase;
    }

    @GetMapping("/{codigoMunicipio}")
    public PrediccionResponse obtener(
            @PathVariable @Pattern(regexp = "\\d{5}", message = "debe tener 5 dígitos") String codigoMunicipio,
            @RequestParam(defaultValue = "G_CEL") UnidadTemperatura unidad) {
        return PrediccionResponse.mapearDominio(obtenerPrediccionUseCase.obtenerParaManana(codigoMunicipio, unidad));
    }
}
