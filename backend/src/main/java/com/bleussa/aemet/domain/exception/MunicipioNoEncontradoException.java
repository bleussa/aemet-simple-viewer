package com.bleussa.aemet.domain.exception;

public class MunicipioNoEncontradoException extends RuntimeException {

    public MunicipioNoEncontradoException(String codigoMunicipio) {
        super("No existe el municipio %s".formatted(codigoMunicipio));
    }
}
