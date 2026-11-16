package com.bleussa.aemet.domain.exception;

public class ServicioMeteorologicoNoDisponibleException extends RuntimeException {

    public ServicioMeteorologicoNoDisponibleException(String mensaje) {
        super(mensaje);
    }

    public ServicioMeteorologicoNoDisponibleException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
