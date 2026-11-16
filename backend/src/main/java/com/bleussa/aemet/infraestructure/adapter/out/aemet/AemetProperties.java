package com.bleussa.aemet.infraestructure.adapter.out.aemet;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aemet")
public record AemetProperties(String baseUrl, String apiKey) {}
