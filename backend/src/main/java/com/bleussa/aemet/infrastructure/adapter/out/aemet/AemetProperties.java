package com.bleussa.aemet.infrastructure.adapter.out.aemet;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "aemet")
public record AemetProperties(
        String baseUrl,
        String apiKey,
        Duration timeoutConexion,
        Duration timeoutLectura,
        Cache cache
) {

    public record Cache(Duration municipiosTtl, Duration prediccionTtl) {}
}
