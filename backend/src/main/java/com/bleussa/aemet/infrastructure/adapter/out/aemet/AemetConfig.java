package com.bleussa.aemet.infrastructure.adapter.out.aemet;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(AemetProperties.class)
public class AemetConfig {

    @Bean
    RestClient aemetRestClient(AemetProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.baseUrl())
                .defaultHeader("api_key", properties.apiKey())
                .build();
    }
}
