package com.bleussa.aemet.infrastructure.adapter.out.aemet;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;

@Configuration
@EnableConfigurationProperties(AemetProperties.class)
public class AemetConfig {

    @Bean
    RestClient aemetRestClient(AemetProperties properties) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(properties.timeoutConexion())
                .build();
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(properties.timeoutLectura());

        return RestClient.builder()
                .baseUrl(properties.baseUrl())
                .defaultHeader("api_key", properties.apiKey())
                .requestFactory(requestFactory)
                .build();
    }
}
