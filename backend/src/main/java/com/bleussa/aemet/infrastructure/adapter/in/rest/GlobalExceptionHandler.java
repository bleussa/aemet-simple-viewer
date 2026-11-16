package com.bleussa.aemet.infrastructure.adapter.in.rest;

import com.bleussa.aemet.domain.exception.MunicipioNoEncontradoException;
import com.bleussa.aemet.domain.exception.PrediccionNoDisponibleException;
import com.bleussa.aemet.domain.exception.ServicioMeteorologicoNoDisponibleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({MunicipioNoEncontradoException.class, PrediccionNoDisponibleException.class})
    ProblemDetail noEncontrado(RuntimeException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(ServicioMeteorologicoNoDisponibleException.class)
    ProblemDetail servicioNoDisponible(ServicioMeteorologicoNoDisponibleException e) {
        log.warn("Fallo al consultar AEMET", e);
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY, e.getMessage());
    }
}
