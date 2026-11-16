package com.bleussa.aemet.domain.exception;

import java.time.LocalDate;

public class PrediccionNoDisponibleException extends RuntimeException {

    public PrediccionNoDisponibleException(String codigoMunicipio, LocalDate fecha) {
        super("No hay predicción para el municipio %s el día %s".formatted(codigoMunicipio, fecha));
    }
}
