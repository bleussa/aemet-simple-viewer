package com.bleussa.aemet.application.service;

import com.bleussa.aemet.application.port.in.BuscarMunicipiosUseCase;
import com.bleussa.aemet.application.port.out.MunicipioProvider;
import com.bleussa.aemet.domain.model.Municipio;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class BuscarMunicipiosService implements BuscarMunicipiosUseCase {

    private final MunicipioProvider municipioProvider;

    public BuscarMunicipiosService(MunicipioProvider municipioProvider) {
        this.municipioProvider = municipioProvider;
    }

    @Override
    public List<Municipio> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return List.of();
        }

        return municipioProvider.obtenerTodos().stream()
                .filter(municipio -> municipio.nombreContiene(nombre))
                .sorted(Comparator.comparing(Municipio::nombre))
                .toList();
    }
}
