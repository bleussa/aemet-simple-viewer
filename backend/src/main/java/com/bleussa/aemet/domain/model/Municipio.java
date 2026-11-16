package com.bleussa.aemet.domain.model;

public record Municipio(String codigo, String nombre) {

    public boolean nombreContiene(String texto){
        return nombre.toLowerCase().contains(texto.toLowerCase());
    }

}
