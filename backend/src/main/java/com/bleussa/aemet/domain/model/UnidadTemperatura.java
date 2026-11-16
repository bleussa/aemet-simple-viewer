package com.bleussa.aemet.domain.model;

public enum UnidadTemperatura {
    G_CEL,
    G_FAH;

    public double desdeCelsius(double celsius) {
        return switch (this) {
            case G_CEL -> celsius;
            case G_FAH -> celsius * 9 / 5 + 32;
        };
    }
}
