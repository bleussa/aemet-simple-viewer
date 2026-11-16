package com.bleussa.aemet.infraestructure.adapter.in.rest;

import com.bleussa.aemet.application.port.in.BuscarMunicipiosUseCase;
import com.bleussa.aemet.application.service.BuscarMunicipiosService;
import com.bleussa.aemet.infraestructure.adapter.in.rest.dto.MunicipioResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/municipios")
public class MunicipioController {

    private final BuscarMunicipiosUseCase buscarMunicipiosUseCase;

    public MunicipioController(BuscarMunicipiosUseCase buscarMunicipiosUseCase) {
        this.buscarMunicipiosUseCase = buscarMunicipiosUseCase;
    }

    @GetMapping
    public List<MunicipioResponse> buscar(@RequestParam String nombre) {
        return buscarMunicipiosUseCase.buscarPorNombre(nombre).stream()
                .map(MunicipioResponse::mapearDominio)
                .toList();
    }
}
